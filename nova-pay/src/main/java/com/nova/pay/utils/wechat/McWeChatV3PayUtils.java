package com.nova.pay.utils.wechat;

import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.nova.pay.config.WeChatConfig;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 商家微信支付工具类
 * @Author: wangzehui
 * @Date: 2022/9/15 15:13
 */
public class McWeChatV3PayUtils {

    @Resource
    private WeChatConfig wxService;

    /**
     * 商家转账给个人
     *
     * @param args
     * @throws WxPayException
     */
    public static void main(String[] args) throws WxPayException {
        WeChatConfig weChatConfig = new WeChatConfig();

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
        TransferCreateResult result = weChatConfig.getWxV3PayService().getMerchantTransferService().createTransfer(request);
        System.out.println(result);

    }
}
