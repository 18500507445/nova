package com.nova.tools.demo.mongo;

import com.starter.mongo.service.impl.EasyMongoServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: wzh
 * @description
 * @date: 2023/07/31 14:32
 */
@Service
public class UserServiceImpl extends EasyMongoServiceImpl<User> implements UserService {

}