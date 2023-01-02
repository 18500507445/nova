package com.nova.common.core.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 视图对象（Value Object）用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来，一般由后端传输给前端。
 * @description: Controller就用VO返回，犯懒可以和DO(DAO)、BO(Service)用一个
 * @author: wzh
 * @date: 2023/1/2 16:35
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;


}
