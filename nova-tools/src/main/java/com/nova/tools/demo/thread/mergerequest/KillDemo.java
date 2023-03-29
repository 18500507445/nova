package com.nova.tools.demo.thread.mergerequest;

import com.google.common.collect.Lists;
import com.nova.common.utils.thread.Threads;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: jihai
 * @date: 2022/10/11 10:35
 */
public class KillDemo {

    /**
     * 模拟数据库操作日志表
     * order_id_operate_type uk
     */
    private final List<OperateChangeLog> operateChangeLogList = new ArrayList<>();

    /**
     * 模拟数据库行
     */
    private Integer stock = 6;

    private final BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

    /**
     * 启动10个用户线程
     * 库存6个
     * 生成一个合并队列，3个用户一批次
     * 每个用户能拿到自己的请求响应
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        KillDemo killDemo = new KillDemo();
        killDemo.mergeJob();
        Threads.sleep(2000);

        CountDownLatch countDownLatch = new CountDownLatch(10);

        System.out.println("-------- 库存 --------");
        System.out.println("库存初始数量 :" + killDemo.stock);

        Map<UserRequest, Future<Result>> requestFutureMap = new HashMap<>(16);
        for (int i = 0; i < 10; i++) {
            final Long orderId = i + 100L;
            final Long userId = (long) i;
            UserRequest userRequest = new UserRequest(orderId, userId, 1);
            Future<Result> future = executorService.submit(() -> {
                countDownLatch.countDown();
                countDownLatch.await(1, TimeUnit.SECONDS);
                return killDemo.operate(userRequest);
            });

            requestFutureMap.put(userRequest, future);
        }

        System.out.println("------- 客户端响应 -------");
        TimeUnit.SECONDS.sleep(1);
        requestFutureMap.entrySet().forEach(entry -> {
            try {
                Result result = entry.getValue().get(300, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + ":客户端请求响应:" + result);

                if (!result.getSuccess() && result.getMsg().equals("等待超时")) {
                    // 超时，发送请求回滚
                    System.out.println(entry.getKey() + " 发起回滚操作");
                    killDemo.rollback(entry.getKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("------- 库存操作日志 -------");
        System.out.println("扣减成功条数: " + killDemo.operateChangeLogList.stream().filter(e -> e.getOperateType().equals(1)).count());
        killDemo.operateChangeLogList.forEach(e -> {
            if (e.getOperateType().equals(1)) {
                System.out.println(e);
            }
        });

        System.out.println("扣减回滚条数: " + killDemo.operateChangeLogList.stream().filter(e -> e.getOperateType().equals(2)).count());
        killDemo.operateChangeLogList.forEach(e -> {
            if (e.getOperateType().equals(2)) {
                System.out.println(e);
            }
        });

        System.out.println("-------- 库存 --------");
        System.out.println("库存初始数量 :" + killDemo.stock);

    }

    private void rollback(UserRequest userRequest) {
        if (operateChangeLogList.stream().anyMatch(operateChangeLog -> operateChangeLog.getOrderId().equals(userRequest.getOrderId()))) {
            // 回滚
            boolean hasRollback = operateChangeLogList.stream().anyMatch(operateChangeLog -> operateChangeLog.getOrderId().equals(userRequest.getOrderId()) && operateChangeLog.getOperateType().equals(2));
            if (hasRollback) {
                return;
            }
            System.out.println("最终回滚");
            stock += userRequest.getCount();
            saveChangeLog(Lists.newArrayList(userRequest), 2);
        }
        // 忽略
    }

    /**
     * 用户库存扣减
     *
     * @param userRequest
     * @return
     */
    public Result operate(UserRequest userRequest) throws InterruptedException {
        // TODO 阈值判断
        // TODO 队列的创建
        RequestPromise requestPromise = new RequestPromise(userRequest);
        synchronized (requestPromise) {
            boolean enqueueSuccess = queue.offer(requestPromise, 100, TimeUnit.MILLISECONDS);
            if (!enqueueSuccess) {
                return new Result(false, "系统繁忙");
            }
            try {
                requestPromise.wait(200);
                if (requestPromise.getResult() == null) {
                    return new Result(false, "等待超时");
                }
            } catch (InterruptedException e) {
                return new Result(false, "被中断");
            }
        }
        return requestPromise.getResult();
    }

    public void mergeJob() {
        new Thread(() -> {
            List<RequestPromise> list = new ArrayList<>();
            while (true) {
                if (queue.isEmpty()) {
                    Threads.sleep(10);
                    continue;
                }

                int batchSize = 3;
                for (int i = 0; i < batchSize; i++) {
                    try {
                        list.add(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 用户ID=5的批次和之后的批次，请求都会超时
                if (list.stream().anyMatch(e -> e.getUserRequest().getUserId().equals(5L))) {
                    Threads.sleep(200);
                }

                System.out.println(Thread.currentThread().getName() + ":合并扣减库存:" + list);

                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
                // 两种情况
                if (sum <= stock) {
                    // 开始事务
                    stock -= sum;
                    saveChangeLog(list.stream().map(RequestPromise::getUserRequest).collect(Collectors.toList()), 1);
                    // 关闭事务
                    // notify user
                    list.forEach(requestPromise -> {
                        requestPromise.setResult(new Result(true, "ok"));
                        synchronized (requestPromise) {
                            requestPromise.notify();
                        }
                    });
                    list.clear();
                    continue;
                }
                for (RequestPromise requestPromise : list) {
                    int count = requestPromise.getUserRequest().getCount();
                    if (count <= stock) {
                        // 开启事务
                        stock -= count;
                        saveChangeLog(Lists.newArrayList(requestPromise.getUserRequest()), 1);
                        // 关闭事务
                        requestPromise.setResult(new Result(true, "ok"));
                    } else {
                        requestPromise.setResult(new Result(false, "库存不足"));
                    }
                    synchronized (requestPromise) {
                        requestPromise.notify();
                    }
                }
                list.clear();
            }
        }, "mergeThread").start();
    }


    /**
     * 写库存流水
     *
     * @param list
     * @param operateType
     */
    private void saveChangeLog(List<UserRequest> list, int operateType) {
        List<OperateChangeLog> collect = list.stream().map(userRequest -> new OperateChangeLog(userRequest.getOrderId(),
                userRequest.getCount(), operateType)).collect(Collectors.toList());
        operateChangeLogList.addAll(collect);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class OperateChangeLog {
        private Long orderId;
        private Integer count;

        /**
         * 1-扣减，2-回滚
         */
        private Integer operateType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RequestPromise {
        private UserRequest userRequest;
        private Result result;

        public RequestPromise(UserRequest userRequest) {
            this.userRequest = userRequest;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result {
        private Boolean success;
        private String msg;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UserRequest {
        private Long orderId;
        private Long userId;
        private Integer count;

    }
}

