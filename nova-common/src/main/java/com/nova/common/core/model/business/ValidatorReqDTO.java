package com.nova.common.core.model.business;

import com.nova.common.core.model.pojo.BaseReqDTO;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @description: Validator请求对象
 * @author: wzh
 * @date: 2022/12/19 20:58
 */
@EqualsAndHashCode(callSuper = true)
@lombok.Data
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
    @Range(min = 18, max = 60, message = "年龄只能在18-60之间")
    private Integer age;

    @Email(message = "邮件格式不正确")
    private String email;

    private String name;

    /**
     * 分组one 校验
     */
    public interface GroupOne {

    }

    /**
     * 分组Two 校验
     */
    public interface GroupTwo {

    }

    //嵌套校验
    @Valid
    private Data data;

    @lombok.Data
    private static class Data {

        //匹配分组2
        @NotBlank(message = "data内部id不能为空", groups = GroupTwo.class)
        private String id;
    }


}
