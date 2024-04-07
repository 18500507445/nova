package com.nova.shopping.order;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nova.shopping.order.repository.mapper.OrderDao;
import com.nova.shopping.order.repository.entity.Order;
import com.nova.shopping.order.repository.entity.User;
import com.nova.shopping.order.service.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/15 23:00
 */
@SpringBootTest
public class OrderTest {

    @Resource
    private UserService userService;

    @Resource
    private OrderDao orderDao;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private static final TimeInterval TIMER = DateUtil.timer();

    @Test
    public void testUser() {
        final User user = userService.queryById(1L);
        System.out.println("myUser = " + JSONObject.toJSONString(user));
    }

    /**
     * PageHelper分页
     */
    @Test
    public void testUserList() {
        Page<Order> orders = PageHelper.startPage(1, 10).doSelectPage(() -> orderDao.queryList(null));
        System.out.println("myUser = " + JSONObject.toJSONString(orders));
    }

    public List<Order> getList() {
        List<Order> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Order order = new Order();
            order.setUserId(Convert.toLong(i));
            order.setGoodsId(0L);
            order.setPrice(new BigDecimal("0"));
            order.setStatus(0);
            order.setPayStatus(0);
            order.setExpirationTime(new Date());
            order.setRemark("");
            order.setOperator("");
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            list.add(order);
        }
        return list;
    }

    /**
     * 普通批量插入，耗时：900823 ms
     */
    @Test
    public void normalInsert() {
        List<Order> list = getList();
        list.forEach(order -> orderDao.insert(order));
        System.out.println("插入" + list.size() + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }


    /**
     * foreach批量插入，耗时：8479 ms
     */
    @Test
    public void testBatch() {
        int i = orderDao.insertBatch(getList());
        System.out.println("插入" + i + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }

    /**
     * 开启SqlSession批量插入，耗时：7776 ms
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        OrderDao mapper = sqlSession.getMapper(OrderDao.class);
        List<Order> list = getList();
        list.forEach(mapper::insert);
        System.out.println("插入" + list.size() + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }


}
