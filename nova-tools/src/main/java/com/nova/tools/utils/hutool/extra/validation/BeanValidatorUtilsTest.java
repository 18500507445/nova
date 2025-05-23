package com.nova.tools.utils.hutool.extra.validation;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.validation.BeanValidationResult;
import cn.hutool.extra.validation.ValidationUtil;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;

/**
 * java bean 校验工具类单元测试
 *
 * @author: chengqiang
 */
public class BeanValidatorUtilsTest {

    public static class TestClass {

        @NotBlank(message = "姓名不能为空")
        private String name;

        @NotBlank(message = "地址不能为空")
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    @Test
    public void beanValidatorTest() {
        BeanValidationResult result = ValidationUtil.warpValidate(new TestClass());
        Assert.isFalse(result.isSuccess());
        Assert.equals(2, result.getErrorMessages().size());
    }

    @Test
    public void propertyValidatorTest() {
        BeanValidationResult result = ValidationUtil.warpValidateProperty(new TestClass(), "name");
        Assert.isFalse(result.isSuccess());
        Assert.equals(1, result.getErrorMessages().size());
    }
}
