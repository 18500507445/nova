package com.nova.tools.vc.ka.trade;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TicketSupplementAmountData
 **/
@Data
public class ActivityData implements Serializable {

    private static final long serialVersionUID = 7189503313760158369L;
    /**
     * 票补活动Id
     */
    private Integer activityId;
    /**
     * 票补活动类型(活动类型 1：立减 2：立减到 3：满减)
     */
    private Integer activityType;

    /**
     * 活动限制类型(1004: 每人/单（活动） 1005: 每天/单（活动） 1006:每天/张（活动） 1007: 每人/张（活动）)
     */
    private Integer limitType;

    /**
     * 活动限制数量(例：每天X张/每天X单/每人X张/每人X单)
     */
    private Integer limitValue;

    /**
     * 用户剩余可用优惠数量
     */
    private Integer surplusCount;

    /**
     * 活动剩余预算
     */
    private Long remainingBudget;

    /**
     * 活动奖励列表
     */
    List<DataList> ticketSupplementAttributeData;



}
