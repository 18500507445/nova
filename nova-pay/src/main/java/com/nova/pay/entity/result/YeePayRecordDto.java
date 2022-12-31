package com.nova.pay.entity.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 易宝银行卡结算dto
 * @author: wzh
 * @date: 2022/7/27 10:42
 */
@Data
public class YeePayRecordDto implements Serializable {

    private static final long serialVersionUID = -1006374653798721736L;
    /**
     * realAmount : 72.07
     * realFee : 0.0
     * statusDesc : 结算成功
     * createTime : 2022-07-25 05:11:09
     * settleAmount : 72.07
     * settleRecordDetailsDtos : [{"realAmount":72.07,"accountNameMast":"吴*雄","channelRequestNo":"SETTLE612e1b18db52475e809b81cd0ea07d1e","statusDesc":"已到账","correct":false,"accountNo":"6236***********4929","accountType":"DEBIT_CARD","bankCardMast":"6236***********4929","accountTypeDesc":"借记卡","status":"SUCCESS"}]
     * summaryNo : SETTLE3731eb643f11454580b67b4141f6fa31
     * settleType : T1
     * status : SUCCESS
     */

    private double realAmount;
    private double realFee;
    private String statusDesc;
    private String createTime;
    private double settleAmount;
    private String summaryNo;
    private String settleType;
    private String status;
    private String settleRecordJson;
    private List<SettleRecordDetailsDtosBean> settleRecordDetailsDtos;

    @Data
    public static class SettleRecordDetailsDtosBean implements Serializable {

        private static final long serialVersionUID = 4780442533345288191L;
        /**
         * realAmount : 72.07
         * accountNameMast : 吴*雄
         * channelRequestNo : SETTLE612e1b18db52475e809b81cd0ea07d1e
         * statusDesc : 已到账
         * correct : false
         * accountNo : 6236***********4929
         * accountType : DEBIT_CARD
         * bankCardMast : 6236***********4929
         * accountTypeDesc : 借记卡
         * status : SUCCESS
         */

        private double realAmount;
        private String accountNameMast;
        private String channelRequestNo;
        private String statusDesc;
        private boolean correct;
        private String accountNo;
        private String accountType;
        private String bankCardMast;
        private String accountTypeDesc;
        private String status;
    }
}
