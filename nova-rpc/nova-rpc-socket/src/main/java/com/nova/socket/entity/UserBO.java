package com.nova.socket.entity;

import com.nova.common.core.model.pojo.BaseBO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/19 19:49
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO extends BaseBO {

    /**
     * 客户端和服务端共有的
     */
    private Integer id;

    private String userName;

    private Boolean sex;
}
