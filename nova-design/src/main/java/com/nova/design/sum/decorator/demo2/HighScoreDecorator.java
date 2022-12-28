package com.nova.design.sum.decorator.demo2;

/**
 * 最高成绩修饰
 *
 * @author landyl
 * @create 4:49 PM 07/16/2018
 */
public class HighScoreDecorator extends Decorator {

    public HighScoreDecorator(SchoolReport sr) {
        super(sr);
    }

    private void reportHighScore() {
        System.out.println("最高成绩是：98");
    }

    @Override
    public void report() {
        this.reportHighScore();
        super.report();
    }

}
