package com.nova.socket.service;


import com.nova.rpc.manual.entity.BlogBO;

public interface BlogService {

    BlogBO getBlogById(Integer id);
}
