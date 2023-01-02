package com.nova.common.core.model.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Object
 * @description: 与数据库表结构一一对应，通过DAO层向上传输数据源对象。
 * @author: wzh
 * @date: 2023/1/2 16:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDO {

    /**
     * 隐藏字段
     * 解决lombok部分注解(ex:构造器@NoArgsConstructor、@AllArgsConstructor)无法使用问题
     */
    @JSONField(serialize = false)
    private String xxx;

}
