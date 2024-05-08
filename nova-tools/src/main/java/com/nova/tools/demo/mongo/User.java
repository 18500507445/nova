package com.nova.tools.demo.mongo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@Accessors(chain = true)
public class User {

    @Id
    private Long id;

    private String loginName;

    private String passWord;

    private Integer age;

    private String createTime;

    private String updateTime;

    //屏蔽字段
    @Transient
    private String startTime;

    @Transient
    private String endTime;

    @Transient
    private Integer pageNo;

    @Transient
    private Integer pageSize;
}

