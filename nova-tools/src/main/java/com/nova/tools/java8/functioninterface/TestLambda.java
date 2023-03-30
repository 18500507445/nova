package com.nova.tools.java8.functioninterface;

/**
 * @description: 推导lambda表达式
 * 函数式接口定义：任何接口，如果只包含"唯一一个"抽象方法，那么它就是一个函数式接口
 * 对于函数式接口，我们可以通过Lambda表达式来创建该接口的对象
 * @author: wzh
 * @date: 2021/4/20 20:58
 */

class TestLambda {

    //3.静态内部类
    static class Like2 implements ILike {
        @Override
        public void lambda() {
            System.out.println("I Like Lambda2");
        }
    }


    public static void main(String[] args) {
        ILike like = new Like();
        like.lambda();

        like = new Like2();
        like.lambda();

        //4.局部内部类
        class Like3 implements ILike {
            @Override
            public void lambda() {
                System.out.println("I Like Lambda3");
            }
        }

        like = new Like3();
        like.lambda();

        //5.匿名内部类,没有类的名称,必须借助接口或者父类
        like = new ILike() {
            @Override
            public void lambda() {
                System.out.println("I Like Lambda5");
            }
        };

        like.lambda();

        //6.用Lambda简化
        like = () -> {
            System.out.println("I Like Lambda6");
        };

        like.lambda();
    }

}


//1.定义一个函数式接口
interface ILike {

    public abstract void lambda();

}

//2.实现类
class Like implements ILike {

    @Override
    public void lambda() {
        System.out.println("I Like Lambda1");
    }

}


