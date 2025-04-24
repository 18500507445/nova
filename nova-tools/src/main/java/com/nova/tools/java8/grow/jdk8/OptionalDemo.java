package com.nova.tools.java8.grow.jdk8;

import java.util.Optional;

/**
 * Optional
 *
 * @author:wzh
 * @date:2018/2/8
 */
class OptionalDemo {

    public static void main(String[] args) {
        Optional<String> fullName = Optional.ofNullable(null);
        System.err.println("Full Name is set? " + fullName.isPresent());
        System.err.println("Full Name: " + fullName.orElse("[none]"));
        System.err.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

        Optional<String> firstName = Optional.of("Tom");
        System.err.println("First Name is set? " + firstName.isPresent());
        System.err.println("First Name: " + firstName.orElseGet(() -> "[none]"));
        System.err.println(firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
        System.err.println();

    }
}
