package com.nova.common.core.model.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @description: 后台业务之间数据传递就用BO来接收
 * @author: wzh
 * @date: 2023/1/2 16:34
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 隐藏字段
     * 解决lombok部分注解(ex:构造器@NoArgsConstructor、@AllArgsConstructor)无法使用问题
     */
    @JSONField(serialize = false)
    private String xxx;
}
