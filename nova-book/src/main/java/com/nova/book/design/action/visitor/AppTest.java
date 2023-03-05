package com.nova.book.design.action.visitor;


import com.nova.book.design.action.visitor.computer.ComputerPart;
import com.nova.book.design.action.visitor.computer.ComputerPartDisplayVisitor;
import com.nova.book.design.action.visitor.computer.impl.Computer;
import com.nova.book.design.action.visitor.login.Login;
import com.nova.book.design.action.visitor.login.Visitor;
import com.nova.book.design.action.visitor.login.impl.IqyVisitor;
import com.nova.book.design.action.visitor.login.impl.QQLogin;
import com.nova.book.design.action.visitor.login.impl.WeiboLogin;
import com.nova.book.design.action.visitor.login.impl.YkVisitor;
import org.junit.jupiter.api.Test;

/**
 * @description: 访问者测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void testComputer() {
        ComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }

    @Test
    public void testLogin() {
        //优酷访问者
        Visitor visitor = new YkVisitor();
        //微博登录
        Login login = new WeiboLogin();

        //优酷访问者 -> 微博登录
        visitor.visit(login);

        login = new QQLogin();
        visitor = new IqyVisitor();
        visitor.visit(login);
    }
}