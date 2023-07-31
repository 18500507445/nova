package com.nova.tools.demo.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description (1)ForkJoinPool 默认线程池大小=CPU核心数 并非设置越大越好，IO密集型任务 受限于磁盘IO
 * (2)fork-join和ThreadPool，各自有各自的应用场景，二者是并存互补的关系
 * (3)递归任务，很适合用fork-join。如分治任务：分而治之，父任务，依赖于子任务的完成
 * @date: 2023/07/24 16:49
 */
public class ForkJoinDemo {

    /**
     * 任务1包含（2，3，4，5，6）
     * 任务2包含（2）
     * 任务3包含（8）
     * 任务4包含（9）
     * 如果用原始的线程池方式会造成死锁，切换成工作线程
     */
    @Test
    public void demoA() {
        ThreadPoolExecutor pool1 = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());

        ForkJoinPool pool2 = new ForkJoinPool(4);

        Task task1 = new Task("task1", 0, pool1);
        Task task2 = new Task("task2", 1, pool1);
        Task task3 = new Task("task3", 1, pool1);
        Task task4 = new Task("task4", 1, pool1);
        Task task5 = new Task("task5", 10, pool1);
        Task task6 = new Task("task6", 10, pool1);
        Task task7 = new Task("task7", 1, pool1);
        Task task8 = new Task("task8", 1, pool1);
        Task task9 = new Task("task9", 1, pool1);


        task1.dependentTasks.add(task2);
        task1.dependentTasks.add(task3);
        task1.dependentTasks.add(task4);
        task1.dependentTasks.add(task5);
        task1.dependentTasks.add(task6);
        task2.dependentTasks.add(task7);
        task3.dependentTasks.add(task8);
        task4.dependentTasks.add(task9);

        System.err.println("start time: " + DateUtil.now());
//        pool1.submit(task1);
        pool2.invoke(task1);

        new ArrayList<Integer>().parallelStream().reduce((a, b) -> a + b);
    }

    public static class Task extends RecursiveTask<String> implements Callable<String> {
        String name;
        ThreadPoolExecutor pool;
        long execTime;
        List<Task> dependentTasks = new ArrayList<>();

        public Task(String name, long execTime, ThreadPoolExecutor pool) {
            this.name = name;
            this.execTime = execTime;
            this.pool = pool;
        }

        @Override
        @SneakyThrows
        public String call() {
            List<Future<String>> futures = dependentTasks.stream()
                    .map(task -> pool.submit(task))
                    .collect(Collectors.toList());

            for (Future<String> future : futures) {
                future.get();
            }
            Thread.sleep(execTime * 1000);
            System.err.println("time: " + DateUtil.now() + ", taskName:" + name + ", threadName:" + Thread.currentThread().getName());
            return "time" + DateUtil.now() + ", taskName:" + name;
        }

        @SneakyThrows
        @Override
        protected String compute() {
            for (Task dependentTask : dependentTasks) {
                dependentTask.fork();
            }
            for (Task dependentTask : dependentTasks) {
                dependentTask.join();
            }
            Thread.sleep(execTime * 1000);
            System.err.println("time: " + DateUtil.now() + ", taskName:" + name + ", threadName:" + Thread.currentThread().getName());
            return "xxx";
        }
    }

    /**
     * 任务分而治之，递归拆分10组进行求和
     */
    @Test
    public void demoD() {
        TimeInterval timer = DateUtil.timer();
        ForkJoinPool pool = new ForkJoinPool();
        TaskA task = new TaskA(0L, 10000000000L);
        Long invoke = pool.invoke(task);
        System.err.println(invoke);
        System.err.println("花费时间:" + timer.interval() + "ms");
    }

    public static class TaskA extends RecursiveTask<Long> {
        private final long start;
        private final long end;
        private static final long THURS_HOLD = 10000000L;

        public TaskA(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long length = end - start;
            if (length <= THURS_HOLD) {//小于临界值
                long sum = 0L;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;

            } else {
                long mid = (start + end) / 2;
                TaskA left = new TaskA(start, mid);
                TaskA right = new TaskA(mid + 1, end);
                invokeAll(left, right);
                // 把两个小任务累加的结果合并起来
                return left.join() + right.join();
            }
        }
    }
}
