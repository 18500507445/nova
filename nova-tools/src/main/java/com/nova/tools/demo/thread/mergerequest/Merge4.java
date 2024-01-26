package com.nova.tools.demo.thread.mergerequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 网友改版Future+Queue版本
 * https://github.com/ArminZheng/order/blob/hotfix/src/main/java/com/arminzheng/KillDemo.java
 * @author: wzh
 * @date: 2023/4/1 16:30
 */
@Slf4j(topic = "Merge4")
class Merge4 {

    /**
     * 缓存队列
     */
    private static final ArrayBlockingQueue<RequestPromise> BLOCKING_QUEUE = new ArrayBlockingQueue<>(10);

    /**
     * 每批合并处理数量
     */
    private static final int BATCH_NUMBER = 3;

    /**
     * 计划模拟的请求数
     */
    public static final int REQUEST_COUNT = 10;

    private static final ExecutorService POLL = Executors.newCachedThreadPool();

    /**
     * 库存量
     */
    private static final AtomicInteger TOTAL_STOCK = new AtomicInteger(6);

    /**
     * 3人合并一个请求强一起扣，剩余3个库存被其它3个人单独强，最终有4个人没抢到
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch requestCountDown = new CountDownLatch(REQUEST_COUNT);
        POLL.execute(() -> {
            List<RequestPromise> promiseList = new ArrayList<>();
            while (true) {
                //退化为队列单个消费
                //log.info("队列中的任务数:{}",BLOCKING_QUEUE.size());
                if (BLOCKING_QUEUE.size() <= BATCH_NUMBER) {
                    //控制边界再判断当前队列中是否有残留
                    if (!promiseList.isEmpty()) {
                        promiseList.forEach(p -> eachTaskHandler(p, TOTAL_STOCK, POLL, requestCountDown));
                        promiseList.clear();
                        continue;
                    }

                    RequestPromise p = null;
                    try {
                        p = BLOCKING_QUEUE.take();
                    } catch (InterruptedException e) {
                        System.err.println("e.getMessage() = " + e.getMessage());
                    }
                    eachTaskHandler(p, TOTAL_STOCK, POLL, requestCountDown);
                    continue;
                }
                //加入批量列表
                try {
                    promiseList.add(BLOCKING_QUEUE.take());
                } catch (InterruptedException e) {
                    System.err.println("e.getMessage() = " + e.getMessage());
                }

                /**
                 * 批量消费 并控制每批的数量为BATCH_NUMBER
                 * 库存分三种情况
                 * 1.库存大于订单商品量
                 * 2.有库存但小于订单商品量
                 * 3.无库存
                 */
                if (promiseList.size() >= BATCH_NUMBER) {
                    int sum = promiseList.stream().mapToInt(p -> p.request.getWantCount()).sum();
                    //库存充足
                    if (TOTAL_STOCK.get() >= sum) {
                        TOTAL_STOCK.addAndGet(-sum);
                        promiseList.forEach(p -> {
                            Result result = new Result(true, "批量处理，抢单成功");
                            p.setResult(result);
                            final Future<RequestPromise> future = POLL.submit(p);
                            p.setFuture(future);
                            requestCountDown.countDown();
                        });
                        promiseList.clear();
                        continue;
                    }
                    //无库存
                    if (TOTAL_STOCK.get() <= 0) {
                        promiseList.forEach(p -> {
                            Result result = new Result(false, "批量处理，没有库存");
                            p.setResult(result);
                            final Future<RequestPromise> future = POLL.submit(p);
                            p.setFuture(future);
                            requestCountDown.countDown();
                        });
                        promiseList.clear();
                        continue;
                    }

                    //库存紧张，退化为单个消费
                    if (TOTAL_STOCK.get() < sum && TOTAL_STOCK.get() > 0) {
                        promiseList.forEach(p -> {
                            eachTaskHandler(p, TOTAL_STOCK, POLL, requestCountDown);
                        });
                        promiseList.clear();
                    }

                }
            }
        });

        List<RequestPromise> requestList = new ArrayList<>();
        //模拟requestCount个请求
        for (int i = 1; i <= REQUEST_COUNT; i++) {
            UserRequest userRequest = new UserRequest(1, String.valueOf(i), "order" + i);
            RequestPromise requestPromise = new RequestPromise(userRequest, null);
            requestList.add(requestPromise);
            POLL.submit(() -> {
                BLOCKING_QUEUE.offer(requestPromise);
            });
        }

        //等待合并请求处理完成
        requestCountDown.await();

        requestList.forEach(r -> {
            try {
                final RequestPromise callResult = r.getFuture().get(200, TimeUnit.MILLISECONDS);
                log.info("用户id：{} 抢到了吗：{}", r.getRequest().getUserId(), callResult.getResult());
            } catch (Exception e) {
                log.error("发生了异常：{},e:", r, e);
            }
        });


    }

    /**
     * 每个请求任务单独处理
     *
     * @param p          任务
     * @param totalStock 库存数量
     * @param pool
     */
    private static void eachTaskHandler(RequestPromise p, AtomicInteger totalStock, ExecutorService pool, CountDownLatch requestCountDown) {
        if (totalStock.get() >= p.getRequest().getWantCount()) {
            totalStock.addAndGet(-p.getRequest().getWantCount());
            Result result = new Result(true, "单独处理，抢单成功");
            p.setResult(result);
        } else {
            Result result = new Result(false, "单独处理，库存不足");
            p.setResult(result);
        }
        final Future<RequestPromise> future = pool.submit(p);
        p.setFuture(future);
        requestCountDown.countDown();
    }


    @Data
    @ToString
    static class RequestPromise implements Callable<RequestPromise> {

        private UserRequest request;
        private Result result;

        private Future<RequestPromise> future;

        public RequestPromise(UserRequest request, Result result) {
            this.request = request;
            this.result = result;
        }

        @Override
        public RequestPromise call() {
            return this;
        }
    }


    @Data
    @ToString
    @AllArgsConstructor
    static class UserRequest {
        private int wantCount;
        private String userId;
        private String orderId;
    }
}
