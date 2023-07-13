package com.nova.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author: wzh
 * @description 实体类
 * @date: 2023/07/13 22:56
 */
@Data
@Document(indexName = "user")
public class User implements Serializable {

    @Id
    private String id;

    private String username;

    private String password;

}
