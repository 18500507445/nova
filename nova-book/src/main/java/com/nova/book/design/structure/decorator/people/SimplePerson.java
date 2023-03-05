package com.nova.book.design.structure.decorator.people;


/**
 * 实现一个普通人
 * @description:  SimplePerson是最核心,最原始,最基本的接口或抽象类的实现,要装饰的就是它
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class SimplePerson implements Person {

    @Override
    public void run() {
        System.err.println("2只脚脚走小碎步...");
    }
}