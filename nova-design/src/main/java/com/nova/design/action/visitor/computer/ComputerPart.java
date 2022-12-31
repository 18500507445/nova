package com.nova.design.action.visitor.computer;


/**
 * @description: 定义接受操作
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface ComputerPart {

    void accept(ComputerPartVisitor computerPartVisitor);

}
