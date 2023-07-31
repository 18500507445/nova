package com.nova.book.design.action.state.student;

/**
 * @description:
 * @author: wzh
 * @date: 2022/12/31 10:30
 */
public class Student {

    /**
     * 使用一个成员来存储状态
     */
    private StudentState state;

    public void setState(StudentState state) {
        this.state = state;
    }

    /**
     * 根据不同的状态，学习方法会有不同的结果
     */
    public void study() {
        switch (state) {
            case LAZY:
                System.err.println("只要我不努力，老板就别想过上想要的生活，开摆！");
                break;
            case NORMAL:
                System.err.println("拼搏百天，我要上清华大学！");
                break;
        }
    }
}
