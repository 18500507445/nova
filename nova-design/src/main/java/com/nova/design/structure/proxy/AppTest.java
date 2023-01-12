package com.nova.design.structure.proxy;


import com.nova.design.structure.proxy.cglib.ProxyFactory;
import com.nova.design.structure.proxy.cglib.UserDao;
import com.nova.design.structure.proxy.service.JdbcService;
import org.junit.jupiter.api.Test;

/**
 * @description: 代理模式测试类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class AppTest {

    @Test
    public void demoA() {
        JdbcService jdbc = new JdbcProxy();
        jdbc.selectOne();
        System.out.println("-------------");
        jdbc.selectOne();
    }

    @Test
    public void demoB(){
        //目标对象
        UserDao target = new UserDao();
        //代理对象
        UserDao proxy = (UserDao) new ProxyFactory(target).getProxyInstance();
        //执行代理对象的方法
        proxy.save();
    }

    @Test
    public void demoC(){
        System.out.println("--------自己来--------");
        You you = new You();
        you.happyMarry();

        System.out.println("--------找婚庆--------");
        new WeddingCompany(new You()).happyMarry();
    }

}