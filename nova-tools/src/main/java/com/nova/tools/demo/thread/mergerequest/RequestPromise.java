package com.nova.tools.demo.thread.mergerequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @description: wait和notify作用这个对象上，包含了Result和UserRequest信息
 * @author: wzh
 * @date: 2023/4/1 13:04
 */
@Data
@AllArgsConstructor
@Slf4j(topic = "RequestPromise")
public class RequestPromise implements Callable<RequestPromise> {
    private UserRequest userRequest;
    private Result result;

    private Future<RequestPromise> future;

    public RequestPromise(UserRequest userRequest) {
        this.userRequest = userRequest;
    }


    @Override
    public RequestPromise call() {
        return this;
    }
}

/**
 * 结果类
 */
@Data
@AllArgsConstructor
@Slf4j(topic = "Result")
class Result {
    private Boolean success;
    private String msg;
}

/**
 * 用户请求类
 */
@Data
@AllArgsConstructor
@Slf4j(topic = "UserRequest")
class UserRequest {
    private Long orderId;
    private Long userId;
    private Integer count;
}

/**
 * 模拟数据库操作日志表
 */
@Data
@AllArgsConstructor
@Slf4j(topic = "OperateChangeLog")
class OperateChangeLog {
    private Long orderId;
    private Integer count;
    /**
     * 1-扣减，2-回滚
     */
    private Integer operateType;
}
