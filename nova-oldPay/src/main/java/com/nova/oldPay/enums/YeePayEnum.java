package com.nova.oldPay.enums;

/**
 * @Description: 易宝入网枚举
 * @Author: wangzehui
 * @Date: 2022/7/8 11:55
 */
public enum YeePayEnum {

    DEFAULT("DEFAULT", 5),

    COMPLETED("COMPLETED", 1),

    REVIEWING("REVIEWING", 4),

    BUSINESS_OPENING("BUSINESS_OPENING", 4),

    REVIEW_BACK("REVIEW_BACK", 5);

    /**
     * 易宝审核状态 applicationStatus:REVIEW_BACK(申请已驳回),AUTHENTICITY_VERIFYING(真实性验证中)，AGREEMENT_SIGNING(协议待签署),COMPLETED(申请已完成)
     */
    private String applicationStatus;

    /**
     * 体育nt_user_setting表开户状态 入网状态 0未开户 1开户 2待审核 3已识别 4审核中 5审核拒绝
     */
    private Integer openAccountStatus;

    public String getApplicationStatus() {
        return applicationStatus;
    }


    public Integer getOpenAccountStatus() {
        return openAccountStatus;
    }

    YeePayEnum(String applicationStatus, Integer openAccountStatus) {
        this.applicationStatus = applicationStatus;
        this.openAccountStatus = openAccountStatus;
    }

    public static YeePayEnum valuesOf(String applicationStatus) {
        switch (applicationStatus) {
            case "COMPLETED":
                return COMPLETED;
            case "REVIEWING":
                return REVIEWING;
            case "BUSINESS_OPENING":
                return BUSINESS_OPENING;
            case "REVIEW_BACK":
                return REVIEW_BACK;
            default:
                return DEFAULT;
        }
    }

}
