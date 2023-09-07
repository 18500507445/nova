package com.nova.tools;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.core.model.result.ResultUtil;
import com.nova.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 14:07
 */
@Slf4j(topic = "commonTest")
@SpringBootTest
class ResultTest {

    public static final String RESULT = "测试";

    @Test
    public void demoA() {
        RespResult<String> success = RespResult.success(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoB() {
        AjaxResult success = AjaxResult.success(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoC() {
        String success = ResultUtil.makeResult(ResultCode.SUCCESS, RESULT);
        System.err.println(success);
    }

}
