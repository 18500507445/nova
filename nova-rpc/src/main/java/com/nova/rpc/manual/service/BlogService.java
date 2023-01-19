package com.nova.rpc.manual.service;


import com.nova.rpc.manual.entity.BlogBO;

public interface BlogService {

    BlogBO getBlogById(Integer id);
}
