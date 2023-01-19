package com.nova.rpc.manual.entity;

import com.nova.common.core.model.pojo.BaseBO;
import lombok.*;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/19 19:49
 */
@EqualsAndHashCode(callSuper = true)
@Builder
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
