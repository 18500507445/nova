package com.nova.ebook.effectivejava.chapter5.section5;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/23 19:24
 */
enum OperationEnum implements Operation {

    ADD() {
        @Override
        public double calculation(double x, double y) {
            return x + y;
        }
    },

    SUB() {
        @Override
        public double calculation(double x, double y) {
            return x - y;
        }
    },

    MUL() {
        @Override
        public double calculation(double x, double y) {
            return x * y;
        }
    },

    DIV() {
        @Override
        public double calculation(double x, double y) {
            return x / y;
        }
    };

    public static double calculation(Operation operation, double x, double y) {
        return operation.calculation(x, y);
    }

}
