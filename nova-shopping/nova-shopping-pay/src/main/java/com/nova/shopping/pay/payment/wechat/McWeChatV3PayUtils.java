package com.nova.shopping.pay.payment.wechat;

import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall.pay.payment.open.WeChatPayment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 商家微信支付工具类
 * @author: wzh
 * @date: 2023/3/14 22:17
 */
@Component
public class McWeChatV3PayUtils {

    /**
     * 商家转账给个人
     *
     * @param args
     * @throws WxPayException
     */
    public static void main(String[] args) throws WxPayException {
        WeChatPayment weChatPayment = new WeChatPayment();

        TransferCreateRequest request = new TransferCreateRequest();
        long orderId = System.currentTimeMillis();
        request.setBatchName("红包");
        request.setBatchRemark("红包兑换");
        request.setTotalAmount(1);
        request.setTotalNum(1);
        request.setOutBatchNo("batch" + orderId);

        List<TransferCreateRequest.TransferDetailList> list = new ArrayList<>();
        TransferCreateRequest.TransferDetailList data = new TransferCreateRequest.TransferDetailList();
        data.setOutDetailNo("detail" + orderId);
        data.setTransferAmount(1);
        data.setTransferRemark("红包兑换");
        data.setOpenid("o7vUD6TaZ4J9D8u7lUr-rEy5LNGY");

        list.add(data);
        request.setTransferDetailList(list);
        TransferCreateResult result = weChatPayment.getWxV3PayService().getMerchantTransferService().createTransfer(request);
        System.out.println(result);

    }
}
