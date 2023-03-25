package com.nova.tools.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ThreadExample {


    @Test
    public void demoA() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Test
    public void demoB() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Test
    public void demoC() {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        runnable.run();

        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("Done!");
    }
}