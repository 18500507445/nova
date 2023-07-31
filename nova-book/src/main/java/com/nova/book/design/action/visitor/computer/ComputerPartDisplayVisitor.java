package com.nova.book.design.action.visitor.computer;

import com.nova.book.design.action.visitor.computer.impl.Computer;
import com.nova.book.design.action.visitor.computer.impl.Keyboard;
import com.nova.book.design.action.visitor.computer.impl.Mouse;

/**
 * @description: 定义访问者实现类
 * @author: wzh
 * @date: 2022/12/31 11:46
 */
public class ComputerPartDisplayVisitor implements ComputerPartVisitor {

    @Override
    public void visit(Mouse mouse) {
        System.err.println("Displaying Mouse.");
    }

    @Override
    public void visit(Keyboard keyboard) {
        System.err.println("Displaying Keyboard.");
    }

    @Override
    public void visit(Computer computer) {
        System.err.println("Displaying Computer.");
    }
}
