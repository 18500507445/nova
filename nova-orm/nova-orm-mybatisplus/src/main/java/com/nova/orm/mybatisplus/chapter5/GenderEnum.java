package com.nova.orm.mybatisplus.chapter5;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wzh
 * @description: 性别枚举
 * @date: 2023/06/21 10:42
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    MAN(1, "男"), WOMAN(0, "女");

    @EnumValue
    private final Integer gender;

    private final String genderName;

}
