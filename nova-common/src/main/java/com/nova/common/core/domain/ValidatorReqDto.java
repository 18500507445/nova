package com.nova.common.core.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: Validator请求对象
 * @Author: wangzehui
 * @Date: 2022/12/19 20:58
 */
@Data
public class ValidatorReqDto {

    /**
     * 主键
     */
    @NotBlank(message = "id不能为空")
    private String id;
}
