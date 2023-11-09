package com.nova.orm.mybatis.service;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/1 21:54
 */
public interface TransactionService {

    /**
     * 测试插入时候事务
     */
    void testTransactional();

    void testDefault();

    void testSupports();

    void testMandatory();

    void testNested();

    void testNever();

    void testNew();

    void a();

    void a1();

    void aPro();
}
