package com.nova.tools.demo.collection;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @description: 队列练习
 * @author: wzh
 * @date: 2023/4/4 22:48
 */
class QueueExercise {

    /**
     * 数组实现的有界阻塞队列。先进先出
     */
    @Test
    public void arrayBlockingQueue() {
        ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.add(1);
        blockingQueue.add(2);
        blockingQueue.add(3);

        final Integer peek = blockingQueue.peek();
        System.out.println("peek = " + peek);
    }

    /**
     * 链表实现的有界阻塞队列。默认和最大长度为Integer.MAX_VALUE，先进先出
     */
    @Test
    public void linkedBlockingQueue() {
        final LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(10);
        linkedBlockingQueue.add(1);
        linkedBlockingQueue.add(2);
        linkedBlockingQueue.add(3);

        final Integer peek = linkedBlockingQueue.peek();
        System.out.println("peek = " + peek);
    }

    /**
     * 支持优先级的无界阻塞队列，默认情况下元素采取自然顺序升序排列。
     * 也可以自定义类实现compareTo()方法来指定元素排序规则
     */
    @Test
    public void priorityBlockingQueue() {
        final PriorityBlockingQueue<Integer> blockingQueue = new PriorityBlockingQueue<>(10);
        blockingQueue.add(2);
        blockingQueue.add(3);
        blockingQueue.add(1);

        final Integer peek = blockingQueue.peek();
        System.out.println("peek = " + peek);
    }

    /**
     * 延时队列
     * PriorityBlockingQueue实现
     * 队列中的元素必须实现Delayed接口，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。
     */
    @Test
    public void delayQueue() {
        final DelayQueue<Delayed> delayQueue = new DelayQueue<>();
    }


}
