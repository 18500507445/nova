package com.nova.common.core.model.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 响应DTO
 * @author: wzh
 * @date: 2023/1/2 17:00
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer delFlag;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;


}
