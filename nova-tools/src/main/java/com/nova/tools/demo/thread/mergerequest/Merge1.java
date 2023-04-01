package com.nova.tools.demo.thread.mergerequest;

import com.nova.common.utils.thread.Threads;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description: 极海初版
 * @author: wzh
 * @date: 2023/4/1 13:03
 */
@Slf4j(topic = "Merge1")
class Merge1 {

    /**
     * 库存
     */
    private Integer stock = 6;

    /**
     * 阻塞队列
     */
    private final BlockingQueue<RequestPromise> queue = new LinkedBlockingQueue<>(10);

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        Merge1 killDemo = new Merge1();
        killDemo.mergeJob();
        log.debug("等待mergeJob启动");
        Threads.sleep(1500);

        List<Future<Result>> list = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            final Long orderId = i + 100L;
            final Long userId = (long) i;
            Future<Result> future = pool.submit(() -> {
                latch.countDown();
                return killDemo.operate(new UserRequest(orderId, userId, 1));
            });
            list.add(future);
        }

        //遍历每一个响应结果
        list.forEach(resultFuture -> {
            try {
                final Result result = resultFuture.get(300, TimeUnit.MILLISECONDS);
                log.debug("客户端请求响应:{}", result);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        Threads.stop(pool);
    }

    /**
     * 用户库存扣减
     *
     * @param userRequest
     * @return
     */
    private Result operate(UserRequest userRequest) {
        //todo 阈值判断

        //todo 队列的创建
        RequestPromise requestPromise = new RequestPromise(userRequest);
        try {
            boolean enqueueSuccess = queue.offer(requestPromise, 100, TimeUnit.MILLISECONDS);
            if (!enqueueSuccess) {
                return new Result(false, "系统繁忙");
            }
            synchronized (requestPromise) {
                requestPromise.wait(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return requestPromise.getResult();
    }

    /**
     * 合并job
     */
    public void mergeJob() {
        new Thread(() -> {
            ArrayList<RequestPromise> list = new ArrayList<>();
            while (true) {
                if (queue.isEmpty()) {
                    Threads.sleep(10);
                    continue;
                }

                //判断队列不为空，继续取出放list
                while (null != queue.peek()) {
                    list.add(queue.poll());
                }

                log.debug("合并扣减库存数：{}", list.size());

                int sum = list.stream().mapToInt(e -> e.getUserRequest().getCount()).sum();

                //两种情况 (1)库存充足
                if (sum <= stock) {
                    stock -= sum;
                    // notify user
                    list.forEach(requestPromise -> {
                        //放入结果，通知其它线程
                        requestPromise.setResult(new Result(true, "ok"));
                        synchronized (requestPromise) {
                            requestPromise.notify();
                        }

                    });
                    continue;
                }

                //(2)库存不足，退化成循环
                for (RequestPromise requestPromise : list) {
                    Integer count = requestPromise.getUserRequest().getCount();
                    if (count <= stock) {
                        stock -= count;
                        requestPromise.setResult(new Result(true, "ok"));
                        synchronized (requestPromise) {
                            requestPromise.notify();
                        }
                    } else {
                        requestPromise.setResult(new Result(false, "库存不足"));
                    }
                }

                //批次运行完，清空
                list.clear();
            }
        }, "mergeJob").start();
    }

}
