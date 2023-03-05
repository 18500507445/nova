package com.nova.book.design.action.visitor.computer.impl;


import com.nova.book.design.action.visitor.computer.ComputerPartVisitor;
import com.nova.book.design.action.visitor.computer.ComputerPart;

public class Keyboard implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}