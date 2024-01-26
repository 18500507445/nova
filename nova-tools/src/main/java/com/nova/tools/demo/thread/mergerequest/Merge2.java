package com.nova.tools.demo.thread.mergerequest;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @see <a href="https://github.com/ArminZheng/order">修复版</a>
 * 1 countDown等待
 * 2 await被中断后才会抛异常(无法区分notify和timeout)
 * 3 synchronized 指向堆对象
 * 4 库存不够未通知(wait到底)
 * 5 消费速度<生产速度->OOM(while->for固定批次or获取队列长度)
 * 6 【merge轮训】before【提交消费】i.e.notify先于wait(入队列在获取锁之后)
 * 7 feature可以换成completedFuture 做到先返回先打印
 * @author: wzh
 * @date: 2023/4/1 16:43
 */
@Slf4j(topic = "Merge2")
class Merge2 {

    /**
     * 模拟数据库行
     */
    private volatile Integer stock = 6;

    /**
     * 合并队列
     */
    private final BlockingDeque<RequestPromise> QUEUE = new LinkedBlockingDeque<>(10);

    private static final ExecutorService POLL = Executors.newCachedThreadPool();

    /**
     * 启动10个线程
     * 库存6个
     * 生成一个合并队列
     * 每个用户都能拿到自己的请求响应
     *
     * @param args
     */
    public static void main(String[] args) {
        Merge2 killDemo = new Merge2();
        killDemo.mergeJob();
        log.info("等待mergeJob启动");
        ThreadUtil.sleep(1500);

        List<Future<Result>> futureList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            Long orderId = i + 100L;
            Long userId = (long) i;
            Future<Result> future = POLL.submit(() -> {
                countDownLatch.countDown();
                // 没有做等待
                countDownLatch.await(1, TimeUnit.SECONDS);
                return killDemo.operate(new UserRequest(orderId, userId, 1));
            });
            futureList.add(future);
        }

        futureList.forEach(future -> {
            try {
                // 每个用户最多等待 300ms
                Result result = future.get(300, TimeUnit.MILLISECONDS);
                log.info("客户端请求响应:{}", result);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        POLL.shutdown();
    }


    /**
     * 用户库存扣减
     *
     * <pre>
     * TODO 阈值判断
     * TODO 队列创建</pre>
     * <p>
     * synchronized 指向堆对象
     *
     * <p>当对象为局部变量（非原始类型）时，只有引用是局部的，而不是实际的对象本身。实际上在堆上其可以被许多其他线程访问。
     *
     * <p>因此，需要对对象进行同步，以便单个线程一次只能访问该对象。
     *
     * @see <a
     * href="https://stackoverflow.com/questions/43134998/is-it-reasonable-to-synchronize-on-a-local-variable">Is
     * it reasonable to synchronize on a local variable?</a>
     */
    public Result operate(UserRequest userRequest) throws InterruptedException {
        RequestPromise requestPromise = new RequestPromise(userRequest);
        synchronized (requestPromise) {
            boolean enqueueSuccess = QUEUE.offer(requestPromise, 100, TimeUnit.MILLISECONDS);
            if (!enqueueSuccess) {
                return new Result(false, "系统繁忙");
            }
            try {
                // 进队列成功后阻塞 200ms
                requestPromise.wait(200);
                // 等待超时不会抛出异常。结束时间之后，直接往下执行，返回null
                if (requestPromise.getResult() == null) {
                    return new Result(false, "等待超时");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return requestPromise.getResult();
    }

    public void mergeJob() {
        new Thread(() -> {
            ArrayList<RequestPromise> list = new ArrayList<>();
            while (true) {
                if (QUEUE.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //放弃之前的写法，如果生产端比消费端快，就会造成死循环
                int batchSize = QUEUE.size();
                for (int i = 0; i < batchSize; i++) {
                    // poll 自动移除
                    list.add(QUEUE.poll());
                }
                log.info("合并扣减库存数：{}", list.size());

                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();
                // 两种情况 ：1/2 库存足够
                if (sum <= stock) {
                    stock -= sum;
                    // notify user
                    list.forEach(
                            requestPromise -> {
                                requestPromise.setResult(new Result(true, "ok"));
                                synchronized (requestPromise) {
                                    requestPromise.notify();
                                }
                            });
                    continue;
                }
                // 2/2 库存不足
                for (RequestPromise requestPromise : list) {
                    int count = requestPromise.getUserRequest().getCount();
                    // 库存: 1, 下单: {user1: 2, user2: 1}
                    if (count <= stock) {
                        stock -= count;
                        requestPromise.setResult(new Result(true, "ok"));
                    } else {
                        requestPromise.setResult(new Result(false, "库存不足"));
                    }

                    // 库存不足没有进行通知
                    synchronized (requestPromise) {
                        requestPromise.notify();
                    }
                }
                list.clear();
            }
        }, "mergeJob").start();
    }

}
