package com.nova.design.action.visitor.computer.impl;


import com.nova.design.action.visitor.computer.ComputerPartVisitor;
import com.nova.design.action.visitor.computer.ComputerPart;

public class Mouse implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}