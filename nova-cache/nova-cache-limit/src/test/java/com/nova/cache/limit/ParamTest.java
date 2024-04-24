package com.nova.cache.limit;


import com.google.common.base.Preconditions;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.exception.ParamException;
import com.nova.common.utils.common.ValidatorUtils;
import com.nova.common.utils.copybean.ParamsUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: wzh
 * @description 参数校验测试类
 * @date: 2023/09/07 15:36
 */
@Slf4j(topic = "ParamTest")
public class ParamTest {

    /**
     * 谷歌guava、请求参数验证
     */
    @Test
    public void preconditionsTest() {
        try {
            ValidatorReqDTO reqDTO = new ValidatorReqDTO();
            reqDTO.setId("1");
            reqDTO.setAge(20);
            Preconditions.checkNotNull(reqDTO, "reqDTO不能为null");
            Preconditions.checkNotNull(reqDTO.getId(), "id 不能为null");
            Preconditions.checkNotNull(reqDTO.getEmail(), "email不能为null");
        } catch (Exception e) {
            String message = e.getMessage();
            System.err.println("message = " + message);
        }
    }

    /**
     * 工具类测试
     */
    @Test
    public void paramUtilTest() {
        ValidatorReqDTO reqDTO = new ValidatorReqDTO();
        try {
            ParamsUtils.checkNotNull(reqDTO);
        } catch (Exception e) {
            String message = e.getMessage();
            System.err.println("message = " + message);
        }
    }

    /**
     * 自定义Validator测试类
     */
    @Test
    public void customValidatorTest() {
        ValidatorReqDTO reqDTO = new ValidatorReqDTO();
        reqDTO.setId("a");
        try {
            ValidatorUtils.validate(reqDTO);
        } catch (ParamException e) {
            String message = e.getMessage();
            System.err.println("message = " + message);
        }
    }


}
