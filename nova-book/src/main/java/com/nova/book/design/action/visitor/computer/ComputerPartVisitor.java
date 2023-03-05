package com.nova.book.design.action.visitor.computer;


import com.nova.book.design.action.visitor.computer.impl.Computer;
import com.nova.book.design.action.visitor.computer.impl.Keyboard;
import com.nova.book.design.action.visitor.computer.impl.Mouse;

/**
 * @description: 定义访问者类的操作
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public interface ComputerPartVisitor {

    void visit(Mouse mouse);

    void visit(Keyboard keyboard);

    void visit(Computer computer);
}

