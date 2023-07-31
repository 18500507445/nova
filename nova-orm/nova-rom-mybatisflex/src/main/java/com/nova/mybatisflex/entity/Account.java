package com.nova.mybatisflex.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: wzh
 * @description 用户数据库对象
 * @date: 2023/06/15 19:35
 */
@Data
@Accessors(chain = true)
@Table("tb_account")
public class Account {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String userName;

    private Integer age;

    private Date birthday;

    private Integer gender;

}