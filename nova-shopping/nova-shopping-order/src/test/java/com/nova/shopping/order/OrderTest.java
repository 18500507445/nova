package com.nova.shopping.order;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nova.shopping.order.dao.MyOrderDao;
import com.nova.shopping.order.entity.MyOrder;
import com.nova.shopping.order.entity.MyUser;
import com.nova.shopping.order.service.MyUserService;
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
    private MyUserService myUserService;

    @Resource
    private MyOrderDao myOrderDao;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    private static final TimeInterval TIMER = DateUtil.timer();

    @Test
    public void testUser() {
        final MyUser myUser = myUserService.queryById(1L);
        System.out.println("myUser = " + JSONObject.toJSONString(myUser));
    }

    /**
     * PageHelper分页
     */
    @Test
    public void testUserList() {
        Page<MyOrder> myOrders = PageHelper.startPage(1, 10).doSelectPage(() -> myOrderDao.queryList(null));
        System.out.println("myUser = " + JSONObject.toJSONString(myOrders));
    }

    public List<MyOrder> getList() {
        List<MyOrder> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            MyOrder myOrder = new MyOrder();
            myOrder.setUserId(Convert.toLong(i));
            myOrder.setGoodsId(0L);
            myOrder.setPrice(new BigDecimal("0"));
            myOrder.setStatus(0);
            myOrder.setPayStatus(0);
            myOrder.setExpirationTime(new Date());
            myOrder.setRemark("");
            myOrder.setOperator("");
            myOrder.setCreateTime(new Date());
            myOrder.setUpdateTime(new Date());
            list.add(myOrder);
        }
        return list;
    }

    /**
     * 普通批量插入，耗时：900823 ms
     */
    @Test
    public void normalInsert() {
        List<MyOrder> list = getList();
        list.forEach(myOrder -> myOrderDao.insert(myOrder));
        System.out.println("插入" + list.size() + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }


    /**
     * foreach批量插入，耗时：8479 ms
     */
    @Test
    public void testBatch() {
        int i = myOrderDao.insertBatch(getList());
        System.out.println("插入" + i + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }

    /**
     * 开启SqlSession批量插入，耗时：7776 ms
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        MyOrderDao mapper = sqlSession.getMapper(MyOrderDao.class);
        List<MyOrder> list = getList();
        list.forEach(mapper::insert);
        System.out.println("插入" + list.size() + "条数据，耗时 ： " + TIMER.interval() + " ms");
    }


}
