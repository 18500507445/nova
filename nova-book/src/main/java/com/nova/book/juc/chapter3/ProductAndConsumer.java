package com.nova.book.juc.chapter3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @description: 生产者、消费者
 * @author: wzh
 * @date: 2023/3/27 10:08
 */
public class ProductAndConsumer {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);

        for (int i = 1; i <= 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = queue.take();
            }
        }, "消费者").start();
    }
}

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
final class Message {
    private int id;
    private Object value;
}

@Slf4j(topic = "queue")
class MessageQueue {

    /**
     * 消息的队列集合
     */
    private final LinkedList<Message> list = new LinkedList<>();
    /**
     * 队列容量
     */
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 获取消息
     *
     * @return
     */
    public Message take() {
        // 检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空, 消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 从队列头部获取消息并返回
            Message message = list.removeFirst();
            log.debug("已消费消息 {}", message);
            list.notifyAll();
            return message;
        }
    }

    /**
     * 存入消息
     *
     * @param message
     */
    public void put(Message message) {
        synchronized (list) {
            // 检查对象是否已满
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满, 生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将消息加入队列尾部
            list.addLast(message);
            log.debug("已生产消息 {}", message);
            list.notifyAll();
        }
    }
}