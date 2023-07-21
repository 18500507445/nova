package com.nova.tools.java8.grow.jdk8;

import java.util.function.Supplier;

/**
 *
 */
public interface defaultLableFactory {
    // Interfaces now allow static methods
    static Integer create(Supplier<Integer> supplier) {
        return supplier.get();
    }
}