package com.nova.common.core.model.business;

import com.nova.common.core.model.pojo.BaseReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * @description: Validator请求对象
 * @author: wzh
 * @date: 2022/12/19 20:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ValidatorReqDTO extends BaseReqDTO {

    /**
     * 主键
     */
    @Pattern(regexp = "\\d+$", message = "id只能是数字")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 年龄
     */
    @DecimalMin(value = "18", message = "年龄不能小于18岁")
    @DecimalMax(value = "60", message = "年龄不能大于60岁")
    private Integer age;

    @Email(message = "邮件格式不正确")
    private String email;

}