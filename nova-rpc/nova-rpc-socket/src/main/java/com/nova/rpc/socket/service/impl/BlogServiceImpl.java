package com.nova.rpc.socket.service.impl;


import com.nova.rpc.socket.entity.BlogBO;
import com.nova.rpc.socket.service.BlogService;

public class BlogServiceImpl implements BlogService {

    @Override
    public BlogBO getBlogById(Integer id) {
        BlogBO blog = BlogBO.builder().id(id).title("我的博客").useId(22).build();
        System.err.println("客户端查询了" + id + "博客");
        return blog;
    }
}
