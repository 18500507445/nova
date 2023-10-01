package com.nova.orm.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: wzh
 * @description 时间处理器
 * @date: 2023/06/21 11:17
 */
@Component
public class MyMetaHandler implements MetaObjectHandler {

    /**
     * 插入 添加createTime、updateTime
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 修改 更新updateTime
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
