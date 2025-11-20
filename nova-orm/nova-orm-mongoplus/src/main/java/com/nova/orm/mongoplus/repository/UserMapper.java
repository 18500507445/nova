package com.nova.orm.mongoplus.repository;

import com.mongoplus.annotation.mapper.Mongo;
import com.mongoplus.mapper.MongoMapper;

// mapper接口需要继承`MongoMapper`接口，并传入所属实体泛型
// solon需要在此处标注 @Mongo注解
@Mongo
public interface UserMapper extends MongoMapper<User> {

}