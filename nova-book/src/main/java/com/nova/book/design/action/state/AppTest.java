package com.nova.book.design.action.state;


import com.nova.book.design.action.state.student.Student;
import com.nova.book.design.action.state.student.StudentState;
import org.junit.jupiter.api.Test;

/**
 * @description: 状态模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void demoA() {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        System.out.println(context.getState());

        StopState stopState = new StopState();
        stopState.doAction(context);

        System.out.println(context.getState());
    }

    @Test
    public void demoB() {
        Student student = new Student();
        //先正常模式
        student.setState(StudentState.NORMAL);
        student.study();
        //开启摆烂模式
        student.setState(StudentState.LAZY);
        student.study();
    }

}