package com.nova.book.algorithm.list;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @description: 链表是数据元素的线性集合，其每个元素都指向下一个元素，元素存储上并不连续
 * 链表类型：单项链表（每个元素只知道下一个元素），双向链表（每个元素知道上一个元素和下一个元素），循环链表
 * 性能：查找index时间复杂度O(n)，
 * @author: wzh
 * @date: 2023/3/6 18:04
 */
class SinglyLinkedList implements Iterable<Integer> {

    /**
     * 头指针
     */
    private Node head = null;

    /**
     * 节点类，因为链表和节点是组合关系，所以当做内部类，对外隐藏实现细节
     */
    private static class Node {
        //值
        int value;

        //下一个节点指针
        Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * 头插法
     *
     * @param value
     */
    public void addFirst(int value) {
        //1.链表为空 头部节点指向第一个节点
        //head = new Node(value, null);

        //2.链表非空 头部节点指向新节点，新节点指向第一个节点，无线套娃
        head = new Node(value, head);
    }

    /**
     * 遍历链表
     */
    public void forEach1(Consumer<Integer> consumer) {
        Node p = head;
        while (null != p) {
            consumer.accept(p.value);
            p = p.next;
        }
    }

    public void forEach2(Consumer<Integer> consumer) {
        for (Node p = head; null != p; p = p.next) {
            consumer.accept(p.value);
        }
    }

    /**
     * 递归遍历
     */
    public void forEach3() {
        recursion(head);
    }

    /**
     * 针对某个节点进行的操作
     */
    private void recursion(Node curr) {
        if (null == curr) {
            return;
        }
        System.err.println("before:" + curr.value);
        recursion(curr.next);
        System.err.println("after:" + curr.value);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            Node p = head;

            @Override
            public boolean hasNext() {
                return null != p;
            }

            @Override
            public Integer next() {
                int value = p.value;
                p = p.next;
                return value;
            }
        };
    }


    /**
     * 循环遍历找到最后一个节点
     *
     * @param value
     * @return
     */
    private Node findLast(int value) {
        if (null == head) {
            return null;
        }
        Node p = head;
        while (null != p.next) {
            p = p.next;
        }
        return p;
    }

    /**
     * 尾插
     *
     * @param value
     */
    public void addLast(int value) {
        Node last = findLast(value);
        if (null == last) {
            addFirst(value);
            return;
        }
        last.next = new Node(value, null);
    }

    /**
     * 根据下标找节点
     *
     * @param index
     * @return
     */
    private Node findNode(int index) {
        int i = 0;
        for (Node p = head; p != null; p = p.next, i++) {
            if (i == index) {
                return p;
            }
        }
        return null;
    }

    /**
     * 根据下标找value
     *
     * @param index
     * @return
     */
    public int get(int index) {
        Node node = findNode(index);
        if (null == node) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        return node.value;
    }

    /**
     * 下标插入法
     *
     * @param index
     * @param value
     */
    public void insert(int index, int value) {
        if (0 == value) {
            addFirst(value);
            return;
        }
        //找到上一个节点
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        prev.next = new Node(value, prev.next);
    }

    /**
     * 删除首个
     */
    public void removeFirst() {
        if (head == null) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", 0));
        }
        //相当于就是head指向head.next(第二个节点)
        head = head.next;
    }

    /**
     * 索引删除
     * 下标的前一个节点指向删除的对象
     *
     * @param index
     */
    public void remove(int index) {
        if (index == 0) {
            removeFirst();
            return;
        }
        Node prev = findNode(index - 1);
        if (null == prev) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        Node remove = prev.next;
        if (null == remove) {
            throw new IllegalArgumentException(String.format("index [%d] 不合法", index));
        }
        //上一个节点的指针--->被删除的指针，这样删除就断开了，等待gc回收
        prev.next = remove.next;
    }

}

/**
 * 链表测试类
 */
class SinglyLinkedListTest {

    /**
     * 打印结果3、2、1、0，每次插入都是头部插入链表
     */
    @Test
    public void demoA() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 1; i <= 4; i++) {
            list.addFirst(i);
        }
        list.forEach1(System.err::println);
    }

    /**
     * 测试尾插
     */
    @Test
    public void demoB() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.forEach1(System.err::println);
    }

    /**
     * 测试索引查找
     */
    @Test
    public void demoC() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        int i = list.get(0);
        System.err.println("i = " + i);
    }

    /**
     * 测试插入
     */
    @Test
    public void demoD() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.insert(4, 4);
        list.forEach1(System.err::println);
    }

    /**
     * 递归调用
     */
    @Test
    public void demoE() {
        SinglyLinkedList list = new SinglyLinkedList();
        for (int i = 1; i <= 4; i++) {
            list.addLast(i);
        }
        list.forEach3();
    }


}


