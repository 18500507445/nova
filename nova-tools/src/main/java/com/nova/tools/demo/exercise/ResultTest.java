package com.nova.tools.demo.exercise;

import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.core.model.result.ResultUtil;
import com.nova.common.enums.ResultCode;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/12 14:07
 */
public class ResultTest {

    public static final String RESULT = "测试";

    @Test
    public void demoA() {
        RespResult<String> success = RespResult.success(RESULT);
        System.out.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoB() {
        AjaxResult success = AjaxResult.success(RESULT);
        System.out.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoC() {
        String success = ResultUtil.makeResult(ResultCode.SUCCESS, RESULT);
        System.out.println(success);
    }

}
