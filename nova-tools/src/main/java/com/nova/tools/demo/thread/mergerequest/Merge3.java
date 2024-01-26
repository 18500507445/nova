package com.nova.tools.demo.thread.mergerequest;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @description: 极海升级版
 * <p>
 * 分析：如果105在第二个批次，6个库存那么会扣减3，回滚3个
 * 105在第一个批次，全部回滚
 * 105在最后一个批次，没有回滚
 * @author: wzh
 * @date: 2023/4/1 13:03
 */
@Slf4j(topic = "Merge3")
class Merge3 {

    /**
     * 库存
     */
    private static Integer STOCK = 6;

    /**
     * 每批合并处理数量
     */
    private static final int BATCH_NUMBER = 3;

    /**
     * 计划模拟的请求数
     */
    public static final int REQUEST_COUNT = 10;

    /**
     * 阻塞队列
     */
    private final BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    /**
     * 模拟数据库操作日志表 order_id和operate_type当做一个unionKey
     */
    private final List<OperateChangeLog> operateChangeLogList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Merge3 killDemo = new Merge3();
        killDemo.mergeJob();
        log.info("等待mergeJob启动");
        Thread.sleep(1500);

        CountDownLatch countDownLatch = new CountDownLatch(10);

        log.info("-------- 库存 --------");
        log.info("库存初始数量：{}", STOCK);

        Map<UserRequest, Future<Result>> requestFutureMap = new HashMap<>(16);
        for (int i = 1; i <= REQUEST_COUNT; i++) {
            final Long orderId = i + 100L;
            final Long userId = (long) i;
            UserRequest userRequest = new UserRequest(orderId, userId, 1);
            Future<Result> future = POOL.submit(() -> {
                countDownLatch.countDown();
                countDownLatch.await(1, TimeUnit.SECONDS);
                return killDemo.operate(userRequest);
            });
            requestFutureMap.put(userRequest, future);
        }

        log.info("------- 客户端响应 -------");

        Thread.sleep(1000);
        requestFutureMap.forEach((key, value) -> {
            try {
                Result result = value.get(300, TimeUnit.MILLISECONDS);

                log.info("客户端请求响应：{}", JSONUtil.toJsonStr(result));

                if (!result.getSuccess() && "等待超时".equals(result.getMsg())) {
                    // 超时，发送请求回滚

                    log.info("{}，发起回滚操作", key);
                    killDemo.rollback(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        log.info("------- 库存操作日志 -------");
        log.info("扣减成功条数: {}", killDemo.operateChangeLogList.stream().filter(e -> e.getOperateType().equals(1)).count());
        killDemo.operateChangeLogList.forEach(e -> {
            if (e.getOperateType().equals(1)) {
                System.err.println(e);
            }
        });

        log.info("扣减回滚条数: {}", killDemo.operateChangeLogList.stream().filter(e -> e.getOperateType().equals(2)).count());
        killDemo.operateChangeLogList.forEach(e -> {
            if (e.getOperateType().equals(2)) {
                System.err.println(e);
            }
        });

        log.info("------- 库存 -------");
        log.info("库存最终数量 :{}", STOCK);

        POOL.shutdown();
    }

    /**
     * 用户库存扣减
     *
     * @param userRequest
     * @return
     */
    public Result operate(UserRequest userRequest) throws InterruptedException {
        //todo 阈值判断

        //todo 队列的创建
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
                    ThreadUtil.sleep(10);
                    continue;
                }

                for (int i = 0; i < BATCH_NUMBER; i++) {
                    try {
                        list.add(queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 用户ID=5的批次和之后的批次，请求都会超时
                if (list.stream().anyMatch(e -> e.getUserRequest().getUserId().equals(5L))) {
                    ThreadUtil.sleep(200);
                }

                log.info("合并扣减库存：{}", JSONUtil.toJsonStr(list));

                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
                // 两种情况
                if (sum <= STOCK) {
                    // 开始事务
                    STOCK -= sum;
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
                    if (count <= STOCK) {
                        // 开启事务
                        STOCK -= count;
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

    private void rollback(UserRequest userRequest) {
        if (operateChangeLogList.stream().anyMatch(operateChangeLog -> operateChangeLog.getOrderId().equals(userRequest.getOrderId()))) {
            // 回滚
            boolean hasRollback = operateChangeLogList.stream().anyMatch(operateChangeLog -> operateChangeLog.getOrderId().equals(userRequest.getOrderId()) && operateChangeLog.getOperateType().equals(2));
            if (hasRollback) {
                return;
            }
            log.info("用户id：{}，最终回滚", userRequest.getUserId());
            STOCK += userRequest.getCount();
            saveChangeLog(Lists.newArrayList(userRequest), 2);
        }
        // 忽略
    }

}
