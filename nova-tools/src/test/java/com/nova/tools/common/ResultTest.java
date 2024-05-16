package com.nova.tools.common;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.core.model.result.ResResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 14:07
 */
@Slf4j(topic = "commonTest")
class ResultTest {

    public static final Object RESULT = "测试";

    @Test
    public void demoA() {
        ResResult<Object> success = ResResult.success(IResultCode.ResultCode.SUCCESS);
        success.setData(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoB() {
        AjaxResult success = AjaxResult.success(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }
}
