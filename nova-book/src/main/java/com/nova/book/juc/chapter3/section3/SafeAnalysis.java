package com.nova.book.juc.chapter3.section3;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 线程安全分析
 * @author: wzh
 * @date: 2023/3/25 16:58
 */
class SafeAnalysis {


}

/**
 * 例子1
 */
class MyServlet extends HttpServlet {
    /**
     * 是否安全？否
     */
    Map<String, Object> map = new HashMap<>();

    /**
     * 是否安全？是
     */
    String S1 = "...";

    /**
     * 是否安全？是
     */
    final String S2 = "...";

    /**
     * 是否安全？否
     */
    Date D1 = new Date();

    /**
     * 是否安全？否（只能保证引用值不变，属性不能保证）
     */
    final Date D2 = new Date();

    /**
     * 是否安全？否，count没有限制
     *
     * @param request
     * @param response
     */
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        //todo 使用上述变量
    }

    interface UserService {

    }

    static class UserServiceImpl implements UserService {
        //记录调用次数
        private int count = 0;

        public void update() {
            //..
            count++;
        }
    }
}


/**
 * 例子2
 */
@Aspect
@Component
class MyAspect {

    /**
     * 是否安全？否，因为spring中没有特殊作用范围默认都是单例的，单例start就要被共享
     */
    private long start = 0L;

    @Before("execution(* *(com.nova.book.juc.chapter3.section3))")
    public void before() {
        start = System.nanoTime();
    }

    @After("execution(* *(com.nova.book.juc.chapter3.section3))")
    public void after() {
        long end = System.nanoTime();
        System.err.println("cost time:" + (end - start));
    }
}


/**
 * 例子3
 */
class MyController {

    /**
     * 是否线程安全？是
     */
    private final UserService userService = new UserServiceImpl();

    public void update() throws SQLException {
        userService.update();
    }

    interface UserService {
        void update() throws SQLException;

        void insert() throws SQLException;
    }

    static class UserServiceImpl implements UserService {

        /**
         * 是否线程安全？是
         */
        private final UserDao userDao = new UserDaoImpl();

        @Override
        public void update() throws SQLException {
            userDao.update();
        }

        /**
         * 是否线程安全？每个线程都new单独的UserDao，调用的insert使用的connection也是私有的，所以是安全的
         *
         * @throws SQLException
         */
        @Override
        public void insert() throws SQLException {
            UserDao userDao = new UserDaoImpl();
            userDao.insert();
        }
    }

    interface UserDao {
        void update() throws SQLException;

        void insert() throws SQLException;
    }

    static class UserDaoImpl implements UserDao {

        @Override
        public void update() {
            String sql = "update ....";
            try {
                /**
                 * 是否线程安全？是，每个线程创建不同的connection
                 */
                Connection connection = DriverManager.getConnection("", "", "");
                //..
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private Connection connection = null;

        /**
         * 是否线程安全，成员变量connection被线程共享，不安全，最好做成线程私有
         *
         * @throws SQLException
         */
        @Override
        public void insert() throws SQLException {
            connection = DriverManager.getConnection("", "", "");
        }

    }
}