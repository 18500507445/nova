package com.nova.design.sum.delegate.leader;

/**
 * Created by Tom on 2018/3/14.
 */
public class TargetB implements ITarget {

    @Override
    public void doing(String command) {
        System.out.println("我是员工B，我现在开始干" + command + "工作");
    }
}
