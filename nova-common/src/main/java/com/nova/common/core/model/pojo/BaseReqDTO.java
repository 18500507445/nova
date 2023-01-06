package com.nova.common.core.model.pojo;

import com.nova.common.core.model.entity.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 数据传输对象（Data Transfer Object）用于展示层和逻辑层之间的数据传输。
 *
 * @description: 请求DTO
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseReqDTO extends BasePage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * source
     */
    private String source;

    /**
     * 渠道号
     */
    private String sid;
}
