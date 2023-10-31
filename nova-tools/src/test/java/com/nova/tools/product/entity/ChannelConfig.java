package com.nova.tools.product.entity;

import com.nova.tools.product.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * @author: wzh
 * @description 渠道配置
 * @date: 2023/10/26 17:46
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChannelConfig {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称{@link ChannelEnum#getName()}
     */
    private String name;

    /**
     * 环境 默认1 dev，2 pro
     */
    private Integer env;

    /**
     * 秘钥key
     */
    private String key;

    /**
     * 秘钥
     */
    private String secret;

    /**
     * 接口域名
     */
    private String url;

    /**
     * 删除标识  默认0 ，删除1
     */
    private Integer delFlag;

    /**
     * 上下线状态 1 上线 2 下线
     */
    private Integer status;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
