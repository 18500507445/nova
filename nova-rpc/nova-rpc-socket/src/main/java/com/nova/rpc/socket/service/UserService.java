package com.nova.rpc.socket.service;


import com.nova.rpc.socket.entity.UserBO;

/**
 * @description:
 * @author: wzh
 * @date: 2023/1/19 19:48
 */
public interface UserService {

    /**
     * 客户端通过这个接口调用服务端的实现类
     *
     * @param id
     * @return
     */
    UserBO getUserByUserId(Integer id);

    Integer insertUserId(UserBO user);

    String hello();

}
