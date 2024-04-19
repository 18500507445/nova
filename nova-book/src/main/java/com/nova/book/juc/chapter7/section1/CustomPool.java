package com.nova.book.juc.chapter7.section1;

import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 自定义线程池，手写阻塞队列
 * @author: wzh
 * @date: 2023/3/29 20:54
 */
@Slf4j(topic = "CustomPool")
class CustomPool {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MICROSECONDS, 1, (queue, task) -> {
            // 1. 死等
            //queue.put(task);
            // 2) 带超时等待
            queue.offer(task, 1500, TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃任务执行
            //log.debug("放弃{}", task);
            // 4) 让调用者抛出异常
            //throw new RuntimeException("任务执行失败 " + task);
            // 5) 让调用者自己执行任务
            //task.run();
        });
        for (int i = 0; i < 3; i++) {
            int j = i;
            threadPool.execute(() -> {
                ThreadUtil.sleep(1000);
                log.debug("{}", j);
            });
        }
    }
}

/**
 * 拒绝策略
 *
 * @param <T>
 */
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j(topic = "ThreadPool")
class ThreadPool {

    /**
     * 任务队列
     */
    private final BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     */
    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 核心线程数
     */
    private final int coreSize;

    /**
     * 获取任务时的超时时间
     */
    private final long timeout;

    private final TimeUnit timeUnit;

    private final RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(Runnable task) {
        // 当任务数没有超过coreSize时，直接交给worker对象执行
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{}, {}", worker.getClass(), task);
                workers.add(worker);
                worker.start();
            } else {
                //超过了，放入队列暂存
                // taskQueue.put(task);

                /**
                 * 1) 死等 2) 带超时等待 3) 让调用者放弃任务执行 4) 让调用者抛出异常 5) 让调用者自己执行任务
                 */
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            /**
             * 执行任务
             * (1) 当 task 不为空，执行任务
             * (2) 当 task 执行完毕，再接着从任务队列获取任务并执行
             */
            while (task != null || (task = taskQueue.pull(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    log.error("异常：", e);
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker 被移除{}", this);
                workers.remove(this);
            }
        }
    }
}

@Slf4j(topic = "BlockingQueue")
class BlockingQueue<T> {

    /**
     * 1. 任务队列
     */
    private final Deque<T> queue = new ArrayDeque<>();

    /**
     * 2. 锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 3. 生产者条件变量
     */
    private final Condition fullWaitSet = lock.newCondition();

    /**
     * 4. 消费者条件变量
     */
    private final Condition emptyWaitSet = lock.newCondition();

    /**
     * 5. 容量
     */
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 阻塞获取
     *
     * @return
     */
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    log.error("异常：", e);
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时的阻塞获取
     *
     * @param timeout
     * @param unit
     * @return
     */
    public T pull(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将timeout统一转换为纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    //返回值是剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error("异常：", e);
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加
     *
     * @param task
     */
    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列 {} ...", task.toString());
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    log.error("异常：", e);
                }
            }
            log.debug("加入任务队列 {}", task.toString());
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时时间阻塞添加
     *
     * @param task
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    if (nanos <= 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {} ...", task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error("异常：", e);
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {  // 有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}


