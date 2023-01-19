package com.nova.rpc.manual.service.impl;


import com.nova.rpc.manual.entity.BlogBO;
import com.nova.rpc.manual.service.BlogService;

public class BlogServiceImpl implements BlogService {

    @Override
    public BlogBO getBlogById(Integer id) {
        BlogBO blog = BlogBO.builder().id(id).title("我的博客").useId(22).build();
        System.out.println("客户端查询了" + id + "博客");
        return blog;
    }
}
