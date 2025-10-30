package com.nova.orm.mongoplus.repository;

import com.mongoplus.annotation.ID;
import com.mongoplus.annotation.collection.CollectionField;
import com.mongoplus.annotation.collection.CollectionName;
import com.mongoplus.annotation.index.MongoIndex;
import lombok.Data;

@Data
@CollectionName("user")
public class User {

    //使用ID注解，标注此字段为MongoDB的_id，或者继承BaseModelID类
    @ID
    private Integer id;

    //唯一索引
    @MongoIndex(unique = true)
    private String name;

    private Long age;

    @CollectionField("address")
    private String address;
}