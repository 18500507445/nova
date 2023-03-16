package com.nova.book.algorithm.base;

import cn.hutool.core.date.StopWatch;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @description: 动态数组
 * 数组中的元素是[连续存储]的，数组中元素的地址，可以通过索引计算
 * @author: wzh
 * @date: 2023/3/5 21:47
 */
class DynamicArray implements Iterable<Integer> {

    /**
     * 逻辑大小
     */
    private int size = 0;

    /**
     * 容量
     */
    private int capacity = 8;

    private int[] array = new int[capacity];

    /**
     * 数组最后位置添加一个元素
     * <p>
     * 等价于
     * array[size] = element;
     * size++;
     *
     * @param element
     */
    public void addLast(int element) {
        add(size, element);
    }

    /**
     * 下标插入元素
     * <p>
     * index后的下标移动一个位置
     * <p>
     * 数组内拷贝复制
     * System.arraycopy(拷贝数组, 起始下标, 目标数组, 起始下标, 拷贝长度)
     */
    public void add(int index, int element) {
        checkAndGrow();
        if (index >= 0 && index < size) {
            //数组从index开始往后移动一个位置，留出index的空间复制给element
            System.arraycopy(array, index, array, index + 1, size - index);
        }
        array[index] = element;
        size++;
    }

    /**
     * 考虑扩容 1.5倍
     */
    private void checkAndGrow() {
        if (size == capacity) {
            capacity += capacity >> 1;
            int[] newArray = new int[capacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }


    public int get(int index) {
        return array[index];
    }

    public void forEach() {
        for (int i = 0; i < size; i++) {
            System.out.println(array[i]);
        }
    }

    /**
     * 函数是接口，由调用者提供方法
     *
     * @param consumer
     */
    public void forEachPro(Consumer<Integer> consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(array[i]);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            /**
             * 先返回值，然后i++
             * @return
             */
            @Override
            public Integer next() {
                return array[i++];
            }
        };
    }

    public IntStream stream() {
        return IntStream.of(Arrays.copyOfRange(array, 0, size));
    }

    public int remove(int index) {
        int removed = array[index];
        //小于最后一个下标不复制
        if (index < size - 1) {
            //数组从index+1开始往前移动一个位置
            System.arraycopy(array, index + 1, array, index, size - index - 1);
        }
        size--;
        return removed;
    }

    public static void ij(int[][] a, int rows, int columns) {
        long sum = 0L;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sum += a[i][j];
            }
        }
        System.out.println(sum);
    }

    public static void ji(int[][] a, int rows, int columns) {
        long sum = 0L;
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                sum += a[i][j];
            }
        }
        System.out.println(sum);
    }

}

/**
 * 动态数组测试类
 */
class DynamicArrayTest {

    /**
     * 下标对应0，1，2，3
     * <p>
     * 如果下标0的内存地址为baseAddress，那么任何下标的地址为baseAddress+index*size(占用字节数)
     */
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
    }


    /**
     * 测试 添加
     */
    @Test
    public void demoA() {
        DynamicArray list = new DynamicArray();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        list.add(3, 4);

        for (int i = 0; i < 4; i++) {
            System.out.println(list.get(i));
        }
    }

    /**
     * 遍历
     */
    @Test
    public void demoB() {
        DynamicArray list = new DynamicArray();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        System.out.println("手动foreach开始");
        list.forEachPro(integer -> System.out.println(integer));

        System.out.println("迭代器遍历开始");
        for (Integer element : list) {
            System.out.println(element);
        }

        System.out.println("java8 steam遍历");
        list.stream().forEach(System.out::println);
    }

    /**
     * 测试删除
     */
    @Test
    public void demoC() {
        DynamicArray list = new DynamicArray();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        int remove = list.remove(2);
        System.out.println("remove = " + remove);
        list.stream().forEach(System.out::println);
    }

    /**
     * 测试扩容
     */
    @Test
    public void demoD() {
        DynamicArray list = new DynamicArray();
        for (int i = 0; i < 9; i++) {
            list.addLast(i);
        }
        list.stream().forEach(System.out::println);
    }

    /**
     * 性能比较
     * <p>
     * get() 时间复杂度O(1)
     * addLast() 时间复杂度O(1)
     * add() 时间复杂度 O(n)
     */
    @Test
    public void demoE() {

    }

    /**
     * CPU做运算(皮秒)，从内存(纳秒)中读取数据，运算完成再写入内存
     * 内存的读取速度跟不上CPU的运算速度，所以中间加入一个缓存，每次计算都从缓存先去找，没有再去内存中找
     * 缓存一次放入64个字节，也会把临近的数据存放至满。64个字节叫缓存行(cache line)
     * <p>
     * 这也就解释了为啥"外大里小"速度快，因为外大缓存数据多，里层可以充分利用缓存
     */
    @Test
    public void demoF() {
        int rows = 1000000;
        int columns = 14;
        int[][] a = new int[rows][columns];

        StopWatch sw = new StopWatch();

        sw.start("ij");
        DynamicArray.ij(a, rows, columns);
        sw.stop();

        sw.start("ji");
        DynamicArray.ji(a, rows, columns);
        sw.stop();
        System.out.println(sw.prettyPrint());
    }


}

