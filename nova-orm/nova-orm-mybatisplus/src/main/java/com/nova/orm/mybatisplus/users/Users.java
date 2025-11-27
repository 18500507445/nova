package com.nova.orm.mybatisplus.users;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "users")
public class Users {

    private Long id;

    @TableField(value = "user_name")
    private String userName;

    //密码
    private String password;

    //身份证
    @TableField(value = "id_card")
    private String idCard;

    private String phone;

    //性别，1：男，2女
    private Integer sex;

    //年龄
    private Integer age;

    //地址
    private String address;

    //创建时间
    @TableField(value = "create_time")
    private Date createTime;

    //创建时间
    @TableField(value = "update_time")
    private Date updateTime;
}