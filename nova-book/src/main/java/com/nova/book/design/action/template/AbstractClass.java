package com.nova.book.design.action.template;

/**
 * @description: 定义算法骨架
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public abstract class AbstractClass {

    /**
     * 为了防止恶意修改，加上final
     * 行为由父类控制，子类实现。
     * ex：造房子，打好地基，第一步干啥，第二步干啥...
     *
     * @author zhengqingya
     */
    final void operation() {
        this.start();
        // 逻辑1...   和水泥，稳固地基
        // 逻辑2...   浇筑地梁
        // 逻辑3...   砌筑墙体
        // 室内装修  北欧风 or 混搭风 or 美美的，布灵布灵的...
        this.decorate();
        this.end();
    }

    private void start() {
        System.out.println("start ...");
    }

    protected abstract void decorate();


    private void end() {
        System.out.println("end ...");
    }
}