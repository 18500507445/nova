package com.nova.tools.demo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: wzh
 * @description: JPA写法，继承MongoRepository，指定实体和主键的类型作为泛型
 * @date: 2023/07/31 14:31
 */
@Repository
public interface UserMapper extends MongoRepository<Topic, String> {

}
