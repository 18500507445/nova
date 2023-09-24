package com.nova.elasticsearch.service;

import com.nova.elasticsearch.entity.User;

import java.util.Optional;

/**
 * @author: wzh
 * @description 接口
 * @date: 2023/07/13 22:58
 */
public interface UserService {

    User save(User user);

    void delete(User user);

    Iterable<User> getAll();

    Optional<User> findById(String id);
}
