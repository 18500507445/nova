package com.nova.book.design.action.visitor.computer.impl;


import com.nova.book.design.action.visitor.computer.ComputerPart;
import com.nova.book.design.action.visitor.computer.ComputerPartVisitor;

public class Mouse implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}