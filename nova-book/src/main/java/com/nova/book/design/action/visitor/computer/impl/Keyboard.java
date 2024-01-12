package com.nova.book.design.action.visitor.computer.impl;


import com.nova.book.design.action.visitor.computer.ComputerPart;
import com.nova.book.design.action.visitor.computer.ComputerPartVisitor;

public class Keyboard implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}