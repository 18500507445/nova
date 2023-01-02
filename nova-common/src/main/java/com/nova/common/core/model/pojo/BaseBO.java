package com.nova.common.core.model.pojo;

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

}
