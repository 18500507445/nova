package com.nova.shopping.pay.payment.wechat;

import com.github.binarywang.wxpay.bean.entpay.*;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.nova.shopping.pay.payment.open.WeChatPayment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 企业微信支付v2工具类
 * @author: wzh
 * @date: 2023/3/14 22:17
 * @see <a href="http://www.binary.ac.cn/weixin-java-pay-javadoc/com/github/binarywang/wxpay/service/EntPayService.html">文档地址</a>
 */
@Component
public class EntWeChatV2PayUtils {

    @Resource
    public WeChatPayment weChatPayment;

    /**
     * 企业付款到零钱
     * <pre>
     * 企业付款业务是基于微信支付商户平台的资金管理能力，为了协助商户方便地实现企业向个人付款，针对部分有开发能力的商户，提供通过API完成企业付款的功能。
     * 比如目前的保险行业向客户退保、给付、理赔。
     * 企业付款将使用商户的可用余额，需确保可用余额充足。查看可用余额、充值、提现请登录商户平台“资金管理”https://pay.weixin.qq.com/进行操作。
     * 注意：与商户微信支付收款资金并非同一账户，需要单独充值。
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
     * 接口链接：https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers
     * </pre>
     *
     * @param request 请求对象
     */
    public EntPayResult entPay(EntPayRequest request) throws WxPayException {
        return weChatPayment.getEntWxPayService().getEntPayService().entPay(request);
    }

    /**
     * 查询企业付款到零钱的结果
     * <pre>
     * 查询企业付款API
     * 用于商户的企业付款操作进行结果查询，返回付款操作详细结果。
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3
     * 接口链接：https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo
     * </pre>
     *
     * @param partnerTradeNo 商户订单号
     */
    public EntPayQueryResult queryEntPay(String partnerTradeNo) throws WxPayException {
        return weChatPayment.getEntWxPayService().getEntPayService().queryEntPay(partnerTradeNo);
    }


    /**
     * 获取RSA加密公钥
     * <pre>
     * 获取RSA加密公钥API.
     * RSA算法使用说明（非对称加密算法，算法采用RSA/ECB/OAEPPadding模式）
     * 1、 调用获取RSA公钥API获取RSA公钥，落地成本地文件，假设为public.pem
     * 2、 确定public.pem文件的存放路径，同时修改代码中文件的输入路径，加载RSA公钥
     * 3、 用标准的RSA加密库对敏感信息进行加密，选择RSA_PKCS1_OAEP_PADDING填充模式
     * （eg：Java的填充方式要选 " RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING"）
     * 4、 得到进行rsa加密并转base64之后的密文
     * 5、 将密文传给微信侧相应字段，如付款接口（enc_bank_no/enc_true_name）
     *
     * 接口默认输出PKCS#1格式的公钥，商户需根据自己开发的语言选择公钥格式
     * 文档详见:https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_7&index=4
     * 接口链接：https://fraud.mch.weixin.qq.com/risk/getpublickey
     * </pre>
     *
     * @return the public key
     * @throws WxPayException the wx pay exception
     */
    public String getPublicKey() throws WxPayException {
        return weChatPayment.getEntWxPayService().getEntPayService().getPublicKey();
    }

    /**
     * 企业付款到银行卡
     * <pre>
     * 用于企业向微信用户银行卡付款
     * 目前支持接口API的方式向指定微信用户的银行卡付款。
     * 文档详见：https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_2
     * 接口链接：https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank
     * </pre>
     *
     * @param request 请求对象
     * @return the ent pay bank result
     * @throws WxPayException the wx pay exception
     */
    public EntPayBankResult payBank(EntPayBankRequest request) throws WxPayException {
        return weChatPayment.getEntWxPayService().getEntPayService().payBank(request);
    }

    /**
     * 查询企业付款到银行卡的结果
     * <pre>
     * 用于对商户企业付款到银行卡操作进行结果查询，返回付款操作详细结果。
     * 文档详见：https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3
     * 接口链接：https://api.mch.weixin.qq.com/mmpaysptrans/query_bank
     * </pre>
     *
     * @param partnerTradeNo 商户订单号
     * @return the ent pay bank query result
     * @throws WxPayException the wx pay exception
     */
    public EntPayBankQueryResult queryPayBank(String partnerTradeNo) throws WxPayException {
        return weChatPayment.getEntWxPayService().getEntPayService().queryPayBank(partnerTradeNo);
    }

}
