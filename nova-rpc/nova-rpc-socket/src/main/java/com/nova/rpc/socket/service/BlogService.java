package com.nova.rpc.socket.service;


import com.nova.rpc.socket.entity.BlogBO;

public interface BlogService {

    BlogBO getBlogById(Integer id);
}
