package com.nova.tools.vc.ka.trade;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description [票补活动属性相关实体类]
 **/
@Data
public class DataList implements Serializable {

    private static final long serialVersionUID = -139493085388416874L;
    /**
     * 票补活动Id
     */
    private Integer activityId;

    /**
     * 票补活动类型(活动类型 1：立减 2：立减到 3：满减)
     */
    private Integer activityType;

    /**
     * 满金额(活动类型为满减时,例：满X元)
     */
    private Long activityLimit;

    /**
     * 减金额(活动类型为满减/立减/立减到时,例：满X元减Y元/立减X元/立减到X元)
     */
    private Long activityLimitValue;

}
