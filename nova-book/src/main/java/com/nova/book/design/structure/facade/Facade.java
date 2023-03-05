package com.nova.book.design.structure.facade;

/**
 * 外观类
 *
 * @description: 降低访问复杂系统的内部子系统时的复杂度，简化客户端之间的接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class Facade {

    private final System1 system1 = new System1();

    private final System2 system2 = new System2();

    public void handle1() {
        this.system1.handle();
    }

    public void handle2() {
        this.system2.handle();
    }
}
