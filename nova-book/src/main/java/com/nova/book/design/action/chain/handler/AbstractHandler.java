package com.nova.book.design.action.chain.handler;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @description: 责任链 抽象类处理器
 * @author: wzh
 * @date: 2022/12/31 8:20
 */
@Component
public abstract class AbstractHandler {

    /**
     * 当前处理器持有下一个处理器的引用
     */
    private AbstractHandler nextHandler;

    public AbstractHandler appendNext(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    /**
     * 审批
     */
    public abstract boolean approve(Object param);

    /**
     * 链路传递
     */
    protected boolean next(Object param) {
        // 下一个链路没有处理器了，直接返回
        if (Objects.isNull(this.nextHandler)) {
            return true;
        }
        // 执行下一个处理器
        return this.nextHandler.approve(param);
    }
}
