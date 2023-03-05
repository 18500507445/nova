package com.nova.book.design.structure.proxy;

/**
 * @description: 动态代理模式对比Thread, 动态代理模式总结 真是对象和代理对象都实现了同一个接口
 * 好处:代理对象可以做很多真是对象做不了的事情
 * @author: wzh
 * @date: 2021/4/20 20:03
 */

public class StaticProxy {

}

/**
 * 结婚动作
 */
interface Marry {

    void happyMarry();
}

/**
 * 真实角色
 */
class You implements Marry {

    @Override
    public void happyMarry() {
        System.out.println("wzh结婚了！！！！！！！！");
    }
}

/**
 * 代理角色,帮助你结婚
 */
class WeddingCompany implements Marry {

    private final Marry target;

    public WeddingCompany(Marry target) {
        this.target = target;
    }

    @Override
    public void happyMarry() {
        before();
        this.target.happyMarry();
        after();
    }

    private void after() {
        System.out.println("洞房花烛夜");
    }

    private void before() {
        System.out.println("收钱办事");
    }
}
