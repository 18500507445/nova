package com.nova.mybatis.service;

/**
 * @description:
 * @author: wangzehui
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
}
