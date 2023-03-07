### 数据结构与算法
* [《Java版：黑马的数据结构与算法》](https://www.bilibili.com/video/BV1Lv4y1e7HL/?spm_id_from=333.999.0.0&vd_source=04ff874447812687f3346175b839011e)
* [《C语言版：白马的数据结构与算法》](https://www.bilibili.com/video/BV13W4y127Ey?p=1&vd_source=04ff874447812687f3346175b839011e)

## 1.基础数据结构

### 1.1 二分查找法
### 1.2 动态数组
### 1.3 单项链表、哨兵
### 1.4 双向链表、哨兵
### 1.5 环形哨兵链表
### 1.6 递归
说明：
1. 自己调用自己，如果说每个函数对应着一种解决方案，自己调用自己意味着解决方案是一样的（有规律的）
2. 每次调用，函数处理的数据会较上次缩减（子集），而且最后会缩减至无需继续递归
3. 内层函数调用（子集处理）完成，外层函数才能算调用完成

* 深入到最里层叫做**递**，从最里层出来叫做**归**
* 在**递**的过程中，外层函数内的局部变量（以及方法参数）并未消失，**归**的时候还可以用到

**原理**

假设链表中有 3 个节点，value 分别为 1，2，3，以上代码的执行流程就类似于下面的**伪码**
```java
// 1 -> 2 -> 3 -> null  f(1)

void f(Node node = 1) {
    println("before:" + node.value) // 1
    void f(Node node = 2) {
        println("before:" + node.value) // 2
        void f(Node node = 3) {
            println("before:" + node.value) // 3
            void f(Node node = null) {
                if(node == null) {
                    return;
                }
            }
            println("after:" + node.value) // 3
        }
        println("after:" + node.value) // 2
    }
    println("after:" + node.value) // 1
}
```

### 1.7 队列
计算机科学中，queue 是以顺序的方式维护的一组数据集合，在一端添加数据，从另一端移除数据。习惯来说，添加的一端称为尾，移除的一端称为头，就如同生活中的排队买商品

## 2.基础算法篇
### 2.1

## 3.进阶篇

