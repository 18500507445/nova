package com.nova.tools.controller;

import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.RespResult;
import com.nova.common.exception.base.ParamException;
import com.nova.common.trace.Trace;
import com.nova.common.utils.common.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: wzh
 * @description: 通用请求
 * @date: 2024/01/12 17:17
 */
@Slf4j(topic = "AbTestController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommonController extends BaseController {

    /**
     * 如何根据qps，如何评估开启线程数量
     * 200qps，接口单次耗时：0.5秒，那么1秒请求数是 1/0.5 = 2
     * 机器核数为10，那么10 * 2 = 20，也就是10核1秒并行处理20个请求
     * 200(qps)/20(parallelRequest/1s) = 10 ,10个线程
     * 备注：ab test测试返回的Requests per second 约等于 qps数量
     */

    /**
     * apache ab测试
     * 查看版本：apachectl -v
     * 测试连接：ab -n 请求数 -c 并发数 URL
     * ab工具常用参数：
     * -n：总共的请求执行数，缺省是1；
     * -c： 并发数，缺省是1；
     * -t：测试所进行的总时间，秒为单位，缺省50000s
     * -p：POST时的数据文件
     * -w: 以HTML表的格式输出结果
     */
    @GetMapping("/abTest")
    public RespResult<Void> abTest() {
        return RespResult.success();
    }

    /**
     * validator
     */
    @PostMapping("/validator")
    public RespResult<String> validator(@Validated @RequestBody ValidatorReqDTO reqDTO) {
        HttpServletRequest request = getRequest();
        String header = request.getHeader(Trace.TRACE_ID);
        return RespResult.success(header);
    }

    /**
     * 自定义validator
     */
    @PostMapping("/customValidator")
    public RespResult<Void> customValidator(ValidatorReqDTO reqDTO) {
        try {
            ValidatorUtil.validate(reqDTO);
        } catch (ParamException e) {
            return RespResult.error(e.getMessage());
        }
        return RespResult.success();
    }

    @Data
    @AllArgsConstructor
    public static class DateTime{
        private Date time;
    }

    @GetMapping(value = "/time", name = "ResponseBody，测试全局时间戳")
    public RespResult<DateTime> time() {
        return RespResult.success(new DateTime(new Date()));
    }
}
