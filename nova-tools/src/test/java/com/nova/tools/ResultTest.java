package com.nova.tools;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.core.model.result.RespResultCode;
import com.nova.common.core.model.result.avic.ResultCode;
import com.nova.common.core.model.result.avic.ResultVO;
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
        RespResult<Object> success = RespResult.success(RespResultCode.OK);
        success.setData(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoB() {
        AjaxResult success = AjaxResult.success(RESULT);
        System.err.println(JSONUtil.toJsonStr(success));
    }

    @Test
    public void demoC() {
        ResultVO<Object> success = ResultVO.success(ResultCode.SUCCESS, RESULT, "自定义消息");
        System.err.println(JSONUtil.toJsonStr(success));
    }

}
