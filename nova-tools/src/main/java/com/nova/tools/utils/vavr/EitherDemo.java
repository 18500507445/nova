package com.nova.tools.utils.vavr;

import io.vavr.Function1;
import io.vavr.control.Either;

/**
 * @description: Either表示可能有两种不同类型的值，分别称为左值和右值，只能是其中一种情况，
 * Either通常用来表示成功或者失败两种情况，惯例是把成功的值作为右值，失败的值作为左值。
 * 可以在Either后添加应用于左值或又值的计算，当中在实际运行中只会产生左值或右值中的其中一种，也就是说添加的计算也只有一个会生效。
 * @author: wzh
 * @date: 2022/10/13 16:32
 */
class EitherDemo {

    public static void main(String[] args) {
        // 大于0，返回成功（右值），小于等于0，返回失败（左值）
        Function1<Integer, Either<Integer, Integer>> func =
                i -> i > 0 ? Either.right(i)
                        : Either.left(-100);

        // -10000
        Either<Integer, Integer> either = func.apply(0);
        System.err.println(either.mapLeft(l -> l * 100).getLeft());

        // 10000
        Either<Integer, Integer> either1 = func.apply(100);
        System.err.println(either1.map(r -> r * 100).get());

    }
}
