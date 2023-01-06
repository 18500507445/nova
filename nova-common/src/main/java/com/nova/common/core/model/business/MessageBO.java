package com.nova.common.core.model.business;

import com.nova.common.core.model.pojo.BaseBO;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @description: 消息实体类
 * @author: wzh
 * @date: 2023/1/5 12:56
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageBO extends BaseBO {

    private Integer id;

    private String message;
}
