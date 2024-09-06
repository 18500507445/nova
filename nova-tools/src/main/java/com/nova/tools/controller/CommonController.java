package com.nova.tools.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.nova.common.core.controller.BaseController;
import com.nova.common.core.model.business.ValidatorReqDTO;
import com.nova.common.core.model.result.ResResult;
import com.nova.common.core.model.result.ResultCode;
import com.nova.common.exception.BusinessException;
import com.nova.common.exception.ParamException;
import com.nova.common.trace.Trace;
import com.nova.common.utils.common.ValidatorUtils;
import com.nova.starter.sensitive.bean.SensitiveBaseDTO;
import com.nova.tools.demo.springboot.listener.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

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

    private final ApplicationEventPublisher applicationEventPublisher;

    /*
      如何根据qps，如何评估开启线程数量
      200qps，接口单次耗时：0.5秒，那么1秒请求数是 1/0.5 = 2
      机器核数为10，那么10 * 2 = 20，也就是10核1秒并行处理20个请求
      200(qps)/20(parallelRequest/1s) = 10 ,10个线程
      备注：ab test测试返回的Requests per second 约等于 qps数量
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
    public ResResult<Void> abTest() {
        return ResResult.success();
    }

    /**
     * validator，匹配分组1
     */
    @PostMapping("/validator")
    public ResResult<ValidatorReqDTO> validator(@Validated(ValidatorReqDTO.GroupOne.class) @RequestBody ValidatorReqDTO reqDTO) {
        HttpServletRequest request = getRequest();
        String header = request.getHeader(Trace.TRACE_ID);
        System.err.println("header = " + header);
        return ResResult.success(reqDTO);
    }

    /**
     * 自定义validator
     */
    @PostMapping("/customValidator")
    public ResResult<Void> customValidator(ValidatorReqDTO reqDTO) {
        try {
            ValidatorUtils.validate(reqDTO);
        } catch (ParamException e) {
            return ResResult.failure(e.getMessage());
        }
        return ResResult.success();
    }

    @Data
    @AllArgsConstructor
    public static class DateTime {
        //millis：毫秒，unixTime：秒
        @JSONField(format = "millis")
        private Date time;
    }

    @GetMapping(value = "/time", name = "ResponseBody，测试全局时间戳")
    public ResResult<DateTime> time() {
        return ResResult.success(new DateTime(new Date()));
    }

    @GetMapping(value = "/jsonTime", name = "ResponseBody，测试全局时间戳")
    public ResResult<JSONObject> jsonTime() {
        String jsonString = JSONObject.toJSONString(new DateTime(new Date()));
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        return ResResult.success(jsonObject);
    }

    @GetMapping(value = "/pushMsg", name = "测试发送spring事件")
    public ResResult<Void> pushMsg() {
        applicationEventPublisher.publishEvent(new Event<>(1, Arrays.asList("123", "456")));
        applicationEventPublisher.publishEvent(new Event<>(2, "123"));
        return ResResult.success();
    }

    /**
     * 线程阻塞队列
     */
    public static final ThreadPoolExecutor THREAD_POOL = ExecutorBuilder.create()
            .setCorePoolSize(2)
            .setMaxPoolSize(2)
            //同步队列
            .useSynchronousQueue()
            //拒绝策略，BLOCK阻塞等待执行，CALLER_RUNS主线程帮助执行
            .setHandler(RejectPolicy.BLOCK.getValue())
            .build();


    @GetMapping(value = "/rejectPolicy", name = "测试线程池-拒绝策略")
    public ResResult<Void> rejectPolicy() {
        THREAD_POOL.execute(() -> {
            System.err.println("id = " + Thread.currentThread().getId() + " Now：" + DateUtil.now());
            ThreadUtil.sleep(2000);
        });
        return ResResult.success();
    }

    public static final ThreadPoolExecutor POOL = ExecutorBuilder.create()
            .setCorePoolSize(25)
            .setMaxPoolSize(50)
            //同步队列
            .useArrayBlockingQueue(512)
            //拒绝策略
            .setHandler(RejectPolicy.BLOCK.getValue())
            .build();

    /**
     * 主线程1秒后直接返回，子线程再过2秒后拿结果（共耗时3秒）
     */
    @GetMapping(value = "/thenRun", name = "多线程-thenRun")
    public ResResult<Void> thenRun() {
        TimeInterval timer = DateUtil.timer();
        ThreadUtil.sleep(1000);
        CompletableFuture<Void> taskA = CompletableFuture.runAsync(() -> ThreadUtil.sleep(1000), POOL);
        CompletableFuture<Void> taskB = CompletableFuture.runAsync(() -> ThreadUtil.sleep(2000), POOL);
        CompletableFuture.allOf(taskA, taskB).thenRun(() -> log.info("完成，耗时：{} ms", timer.interval()));
        return ResResult.success();
    }

    @GetMapping(value = "/sensitive", name = "脱敏测试")
    public ResResult<SensitiveBaseDTO> sensitive() {
        SensitiveBaseDTO baseDTO = new SensitiveBaseDTO();
        baseDTO.setName("王三四");
        baseDTO.setIdCard("44082199612054343");
        baseDTO.setBankCard("62173300255879654448");
        baseDTO.setFixedPhone("3110026");
        baseDTO.setMobile("18500334455");
        baseDTO.setAddress("北京市朝阳区北苑路北");
        baseDTO.setEmail("123456789@qq.com");

        baseDTO.setRemark("123我是无敌大德鲁伊，你看看我说的是真的吗？哈哈哈哈哈");
        baseDTO.setRemarkA("123我是无敌大德鲁伊，你看看我说的是真的吗？哈哈哈哈哈");
        baseDTO.setRemarkB("123我是无敌大德鲁伊，你看看我说的是真的吗？哈哈哈哈哈");
        return ResResult.success(baseDTO);
    }

    //测试返回类
    @GetMapping(value = "/resResult")
    public ResResult<Date> resResult() {
        ResResult.setInternetIp("ip");
        Date date = new Date();
        ResResult<Date> success = ResResult.success(date);
        success.put("main", "234");

        String json = success.toString();
        System.err.println("string = " + json);

        Class<?> tClass = success.getTClass();
        System.out.println("tClass = " + tClass);
        return success;
    }

    @GetMapping(value = "/a")
    public ResResult<Void> a() {
        if (true) {
            throw new BusinessException(ResultCode.CURRENT_LIMITING);
        }
        return ResResult.success();
    }

}
