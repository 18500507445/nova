package com.nova.pay.entity.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 苹果退款通知类
 * @Author: wangzehui
 * @Date: 2022/6/18 17:06
 */
@Data
public class RefundIosDto implements Serializable {

    private static final long serialVersionUID = 2761412428811953716L;

    /**
     * environment : sandbox
     * notification_type : REFUND
     * password : aaaaaaaa
     * bid : afafsdf.com
     * unified_receipt : {"status":"0","latest_receipt_info":[{"cancellation_date_ms":"15016625461111","original_transaction_id":"1000000321215919","product_id":"aadfasdfsdfasdf"},{"cancellation_date_ms":"15016625461110","original_transaction_id":"1000000321213686","product_id":"afdsafsdafasdf"}]}
     */

    private String environment;

    private String notification_type;

    private String password;

    private String bid;

    private UnifiedReceiptBean unified_receipt;

    @Data
    public static class UnifiedReceiptBean implements Serializable {
        /**
         * status : 0
         * latest_receipt_info : [{"cancellation_date_ms":"15016625461111","original_transaction_id":"1000000321215919","product_id":"aadfasdfsdfasdf"},{"cancellation_date_ms":"15016625461110","original_transaction_id":"1000000321213686","product_id":"afdsafsdafasdf"}]
         */

        private String status;

        private List<LatestReceiptInfoBean> latest_receipt_info;

        @Data
        public static class LatestReceiptInfoBean implements Serializable {

            /**
             * cancellation_date_ms : 15016625461111
             * original_transaction_id : 1000000321215919
             * product_id : aadfasdfsdfasdf
             */

            private String cancellation_date_ms;

            private String original_transaction_id;

            private String product_id;
        }
    }
}
