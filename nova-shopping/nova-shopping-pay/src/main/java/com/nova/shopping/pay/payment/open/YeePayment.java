package com.nova.shopping.pay.payment.open;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.nova.shopping.common.constant.Constants;
import com.nova.shopping.pay.entity.param.YeePayParam;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigestAlgEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalSignatureDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 易宝支付
 * https://open.yeepay.com/docs/products/ptssfk/quick-start/62580d1b4fb56f003e54b455
 * @author: wzh
 * @date: 2023/3/30 18:10
 */
@Slf4j
@Component
public class YeePayment {

    /**
     * 交易下单银行卡
     */
    public Map<String, String> tradeOrder(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            request.addParam("merchantNo", data.getMerchantNo());
            //商户收款请求号。可包含字母、数字、下划线；需保证在商户端不重复。合单收款场景中，此参数为合单收款请求号
            request.addParam("orderId", data.getOrderId());
            //订单金额 单位为元，精确到小数点后两位 示例值：100.50
            request.addParam("orderAmount", data.getAmount());
            request.addParam("notifyUrl", data.getNotifyUrl());
            request.addParam("redirectUrl", data.getReturnUrl());
            //商品拓展信息
            JSONObject goodsParamExt = new JSONObject();
            goodsParamExt.put("goodsName", data.getGoodsName());
            request.addParam("goodsParamExt", goodsParamExt);

            YopResponse response = YopRsaClient.post("/rest/v1.0/std/trade/order", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("tradeOrder异常:", e);
        }
        return null;
    }


    /**
     * 唤起收银台接口
     * limitPayType YJZF 一键支付，WALLET_PAY 钱包支付
     * https://open.yeepay.com/docs/products/ptssfk/others/5f4ca53e00f514001b61b553
     */
    public String cashier(YeePayParam data) {
        StringBuilder url = new StringBuilder("https://cash.yeepay.com/cashier/std");
        StringBuilder param = new StringBuilder();
        param.append("appKey=" + data.getAppKey());
        param.append("&merchantNo=" + data.getMerchantNo());
        param.append("&token=").append(data.getToken());
        param.append("&timestamp=").append(System.currentTimeMillis() / 1000);
        param.append("&directPayType=" + "");
        param.append("&cardType=" + "");
        param.append("&userNo=").append(data.getUserName());
        param.append("&userType=USER_ID");
        String sign = getSign(param.toString(), data.getAppKey(), data.getPrivateKey());
        url.append("?sign=").append(sign).append("&").append(param);
        return url.toString();
    }


    /**
     * 退款接口
     * <p>
     * 退款、通知文档
     *
     * @see <a href="https://open.yeepay.com/docs/apis/ptssfk/jiaoyi/options__rest__v1.0__trade__refund#anchor7">易宝支付结果通知</a>
     */
    public Map<String, String> refund(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            //商户收款请求号，商户请求收款的交易单号
            request.addParam("orderId", data.getOrderId());
            //商户退款请求号
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            request.addParam("refundRequestId", uuid);
            request.addParam("uniqueOrderNo", data.getTradeNo());
            request.addParam("refundAmount", data.getAmount());
            YopResponse response = YopRsaClient.upload("/rest/v1.0/std/trade/refund", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception e) {
            log.error("refund异常:", e);
        }
        return null;
    }

    /**
     * 查询订单
     * <p>
     * {
     * "channelOrderId": "202207121051000000191911010012061657596899712404",
     * "code": "OPR00000",
     * "orderId": "1546699558170390528",
     * "bankOrderId": "2022071210000007630052950210408",
     * "merchantFee": "0.00",
     * "paySuccessDate": "2022-07-12 11:35:00",
     * "bizSystemNo": "DS",
     * "customerFee": "0.00",
     * "channel": "NCPAY",
     * "payWay": "ONEKEYPAY",
     * "message": "成功",
     * "uniqueOrderNo": "1013202207120000003980504497",
     * "token": "875E7760EF1D5970084A6A3236368460A94573277C0C5698F335A47F459F6108",
     * "fundProcessType": "DELAY_SETTLE",
     * "orderAmount": "0.01",
     * "payAmount": "0.01",
     * "unSplitAmount": "0.00",
     * "payerInfo": "{\"mobilePhoneNo\":\"155****0926\",\"bankId\":\"CCB\",\"bankCardNo\":\"621700****6623\",\"cardType\":\"DEBIT\"}",
     * "realPayAmount": "0.01",
     * "parentMerchantNo": "10088528644",
     * "totalRefundAmount": "0.00",
     * "merchantNo": "10088528644",
     * "status": "SUCCESS"
     * }
     */
    public Map<String, String> queryOrder(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            request.addParam("orderId", data.getOrderId());

            YopResponse response = YopRsaClient.get("/rest/v1.0/trade/order/query", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("queryOrder异常:", e);
        }
        return null;
    }


    //Todo -----------------------------------------以下是中藏二级的接口，没有经过测试-----------------------------------------

    /**
     * 钱包注册/登录接口
     * 描述：通过该接口获取钱包注册/登录页面
     * 说明：商户调用此接口，如果用户未注册钱包，则首先跳至注册页面；如果用户已注册，则返回钱包首页。
     * <p>
     * 返回：url和token
     */
    public Map<String, String> walletLogin(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //发起方商户编号（标准商户收付款方案中此参数与商编一致，平台商户收付款方案中此参数为平台商商户编号）
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //商户编号,易宝支付分配的的商户唯一标识
            request.addParam("merchantNo", data.getParentMerchantNo());
            //商户用户ID 用户在商户侧的用户id
            request.addParam("merchantUserNo", data.getUserName());
            request.addParam("mobile", data.getMobile());
            request.addParam("name", data.getRealName());
            request.addParam("certificateType", "IDENTITY_CARD");
            request.addParam("certificateNo", data.getIdCard());
            //页面重定向地址 钱包首页返回商户页面地址
            request.addParam("returnUrl", data.getReturnUrl());
            //服务器回调地址 开户完成后通知地址
            request.addParam("notifyUrl", Constants.WALLET_NOTIFY_URL);
            //商户请求流水号 UUID.randomUUID().toString().replaceAll("-", "")
            request.addParam("requestNo", UUID.randomUUID().toString().replaceAll("-", ""));

            YopResponse response = YopRsaClient.post("/rest/v2.0/m-wallet/wallet/index", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception e) {
            log.error("walletIndex异常:", e);
        }
        return null;
    }


    /**
     * 交易下单(二级)
     * 描述：商户需先调用该接口生成预支付订单，易宝返回支付授权token后，再使用token调起相应支付。
     * 说明：本接口支持标准交易下单（单笔），也支持合单收款交易下单（可以实现多商户订单用户一笔付款），合单收款请在subOrderDetail里上送订单明细，目前最多一次可支持99笔订单进行合单收款。
     * <p>
     * {
     * "code": "OPR00000",
     * "orderAmount": "0.02",
     * "orderId": "f50a8d2307104bfaa5812551627b6f4c",
     * "bizSystemNo": "DS",
     * "parentMerchantNo": "10085537650",
     * "message": "成功",
     * "uniqueOrderNo": "1013202207010000003918481504",
     * "merchantNo": "10085537650",
     * "token": "D43B46BF1C7DE59884854D50FEF3D00FEE1E4EF7787A7F09477FA65003534677"
     * }
     */
    public Map<String, String> secondTradeOrder(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //商户收款请求号。可包含字母、数字、下划线；需保证在商户端不重复。合单收款场景中，此参数为合单收款请求号
            request.addParam("orderId", data.getOrderId());
            request.addParam("merchantNo", data.getMerchantNo());
            request.addParam("userNo", data.getUserName());
            request.addParam("userType", "USER_ID");
            request.addParam("goodsName", data.getGoodsName());
            //订单金额 单位为元，精确到小数点后两位 示例值：100.50
            request.addParam("orderAmount", data.getAmount());
            request.addParam("notifyUrl", data.getNotifyUrl());
            /**
             * 支付成功后跳转的URL，如商户指定页面回调地址， 支付完成后会从易宝的支付成功页跳转至商家指定页面，只有走标准收银台的订单此地址才有作用
             * 携带(merchantNo,parentMerchantNo,orderId,sign)
             */
            request.addParam("redirectUrl", data.getReturnUrl());
            request.addParam("csUrl", Constants.CS_NOTIFY_URL);
            //分账标识。不传，默认不分账 DELAY_SETTLE：分账
            request.addParam("fundProcessType", "DELAY_SETTLE");

            //自定义参数信息
            JSONObject businessInfo = new JSONObject();
            businessInfo.put("collectionSeries", data.getGoodsName());
            businessInfo.put("collectionName", data.getGoodsName());
            businessInfo.put("collectionId", data.getWorksId());
            businessInfo.put("marketType", "转卖");
            businessInfo.put("userRegisterMobile", data.getMobile());
            businessInfo.put("registTime", "");
            businessInfo.put("registIp", "");
            businessInfo.put("userRegisterIdNo", data.getIdCard());
            businessInfo.put("registId", data.getUserName());
            request.addParam("businessInfo", businessInfo.toJSONString());

            //付款信息
            JSONObject payerInfo = new JSONObject();
            payerInfo.put("cardName", data.getRealName());
            payerInfo.put("idCardNo", data.getIdCard());
            request.addParam("payerInfo", payerInfo.toJSONString());

            YopResponse response = YopRsaClient.post("/rest/v1.0/trade/order", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("secondTradeOrder异常:", e);
        }
        return null;
    }

    /**
     * 实名认证
     * <p>
     * {
     * "ybOrderId": "CFC04c60b1b825b4c218bc9b260fa423d51",
     * "code": "00000",
     * "message": "SUCCESS",
     * "requestNo": "bd9e3c47f5444431b98bcd8ffa061631",
     * "authType": "FastRealNameVerify",
     * "status": "SUCCESS",
     * "merchantNo": "10085537650"
     * }
     */
    @Deprecated
    public Map<String, String> authRealName(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            request.addParam("merchantNo", data.getMerchantNo());
            String orderId = UUID.randomUUID().toString().replaceAll("-", "");
            request.addParam("requestNo", orderId);
            //ID_NAME:身份证两项（只支持ID类型）、NAME_AUTH:银行卡三项、NAME_VERIFY:手机号四项
            request.addParam("authType", "ID_NAME");
            request.addParam("username", data.getRealName());
            request.addParam("idCardNo", data.getIdCard());
            request.addParam("idCardType", "ID");

            YopResponse response = YopRsaClient.post("/rest/v1.0/auth/multiple-auth", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("authRealName异常:", e);
        }
        return null;
    }

    /**
     * 特约商户入网-小微
     * 入网时候以下参数请注意
     * 1）businessRole 需要选择SHARE_MERCHANT
     * 2）所有图片类资质都需要预先上传到易宝的服务器
     * 3）省市区编码和银行总行编码，需要用易宝手册上的编码字典表
     * <p>
     * 响应字段：applicationNo(申请单编号)
     * applicationStatus:REVIEW_BACK(申请已驳回),AUTHENTICITY_VERIFYING(真实性验证中)，AGREEMENT_SIGNING(协议待签署),COMPLETED(申请已完成)
     */
    public Map<String, String> microAccess(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            request.addParam("requestNo", data.getRequestNo());
            request.addParam("businessRole", "SHARE_MERCHANT");

            JSONObject merchantSubjectInfo = new JSONObject();
            merchantSubjectInfo.put("signName", data.getRealName());
            merchantSubjectInfo.put("shortName", data.getRealName());
            request.addParam("merchantSubjectInfo", merchantSubjectInfo.toJSONString());

            JSONObject merchantCorporationInfo = new JSONObject();
            merchantCorporationInfo.put("legalLicenceType", "ID_CARD");
            merchantCorporationInfo.put("legalLicenceNo", data.getIdCard());
            merchantCorporationInfo.put("legalLicenceFrontUrl", data.getIdCardUrl());
            merchantCorporationInfo.put("legalLicenceBackUrl", data.getIdCardBackUrl());
            merchantCorporationInfo.put("mobile", data.getMobile());
            request.addParam("merchantCorporationInfo", merchantCorporationInfo.toJSONString());

            JSONObject businessAddressInfo = new JSONObject();
            businessAddressInfo.put("province", data.getProvinceId());
            businessAddressInfo.put("city", data.getCityId());
            businessAddressInfo.put("district", data.getAreaId());
            businessAddressInfo.put("address", data.getAddress());
            request.addParam("businessAddressInfo", businessAddressInfo.toJSONString());

            JSONObject accountInfo = new JSONObject();
            accountInfo.put("settlementDirection", "BANKCARD");
            accountInfo.put("bankAccountType", "DEBIT_CARD");
            accountInfo.put("bankCardNo", data.getBankCard());
            accountInfo.put("bankCode", data.getBankCardCode());
            request.addParam("accountInfo", accountInfo.toJSONString());

            JSONArray productArray = new JSONArray();
            JSONObject productInfo = new JSONObject();
            productInfo.put("productCode", "T1");
            productInfo.put("rateType", "SINGLE_FIXED");
            productInfo.put("paymentMethod", "REAL_TIME");
            productInfo.put("fixedRate", "0");
            productArray.add(productInfo);
            request.addParam("productInfo", productArray.toJSONString());

            request.addParam("notifyUrl", Constants.MICRO_NOTIFY_URL);

            YopResponse response = YopRsaClient.post("/rest/v2.0/mer/register/contribute/micro", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception e) {
            log.error("特约商户入网-小微异常:", e);
        }
        return null;
    }


    /**
     * 申请分账
     * <p>
     * 下单时设置为分账订单且接到清算回调之后，该笔订单可以调用分账接口分账，分给谁，分多少，都可以通过接口参数设置，什么时候调用分账接口就会什么时候分账，支付和分账之间的时间间隔长度易宝不做限制，由平台把控。
     * <p>
     * 分账接口手册地址：
     * https://open.yeepay.com/docs/apis/ptssfk/jiaoyi/options__rest__v1.0__divide__apply
     * <p>
     * 生成收银台时以下参数需要注意
     * 1）分账接口实时返回结果
     * 2）分给二级卖家和平台的分账尽量通过一次接口调用完成，较少系统消耗
     * 3）订单金额需要全部分完后不必再调用分账完结接口
     * <p>
     * 多次分账的总金额不会超过订单金额
     */
    public Map<String, String> divideApply(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            //商户收款请求号，商户请求收款的交易单号
            request.addParam("orderId", data.getOrderId());
            //易宝收款订单号,收款交易对应在易宝的收款单号
            request.addParam("uniqueOrderNo", data.getTradeNo());
            //商户分账请求号
            request.addParam("divideRequestId", data.getRequestNo());
            /**
             * 分账详情JSON字符串；ledgerNoFrom：分账发起方编号，非必填。不填默认为收款商编。
             * ledgerNo：分账接收方编号，必填。分账属性为分账给商户时，为接收分账资金的易宝商户编号；分账属性为分账给个人会员时，为接收分账资金的易宝会员
             * amount ：分账金额，必填，两位小数 divideDetailDesc ：分账说明，非必填。
             * ledgerType：分账属性，非必填。可选项如下：MERCHANT2MERCHANT（分账给商户），MERCHANT2MEMBER（分账给个人会员）。不填默认分账给商户。只支持分账给一个个人会员。
             * 示例值：[{"amount":"100.00","ledgerNo":"10000466938","ledgerType":"MERCHANT2MERCHANT","divideDetailDesc":"供应商结算"},{"amount":"100.00","ledgerNo":"212345678912","ledgerNoFrom":"10000466938","ledgerType":"MERCHANT2MEMBER"}]
             */
            JSONObject divideDetail = new JSONObject();
            String collectionStatus = data.getCollectionStatus();
            if (StringUtils.equals("1", collectionStatus)) {
                divideDetail.put("ledgerType", "MERCHANT2MEMBER");
                divideDetail.put("ledgerNo", data.getWalletId());
            } else if (StringUtils.equals("2", collectionStatus)) {
                divideDetail.put("ledgerType", "MERCHANT2MERCHANT");
                divideDetail.put("ledgerNo", data.getMerchantNo());
            }
            //分多少钱
            divideDetail.put("amount", data.getAmount());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(divideDetail);
            request.addParam("divideDetail", jsonArray.toJSONString());

            YopResponse response = YopRsaClient.post("/rest/v1.0/divide/apply", request);
            System.out.println("response:" + JSONObject.toJSONString(request.getParams().asMap()));
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception e) {
            log.error("分账接口异常:", e);
        }
        return null;
    }

    /**
     * 完结分账
     */
    public Map<String, String> divideComplete(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            //商户收款请求号，商户请求收款的交易单号
            request.addParam("orderId", data.getOrderId());
            //易宝收款订单号,收款交易对应在易宝的收款单号
            request.addParam("uniqueOrderNo", data.getTradeNo());
            //商户分账请求号，在商户系统内部唯一（申请分账、完结分账应使用不同的商户分账请求号），同一分账请求号多次请求等同一次。
            request.addParam("divideRequestId", data.getRequestNo());

            YopResponse response = YopRsaClient.post("/rest/v1.0/divide/complete", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (Exception e) {
            log.error("完结分账接口异常:", e);
        }
        return null;
    }


    /**
     * 上传图片接口
     */
    public Map<String, String> upload(File file, YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //指定文件URL
            request.addFile("merQual", file);
            //上传文件
            YopResponse response = YopRsaClient.upload("/yos/v1.0/sys/merchant/qual/upload", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("multipleAuth异常:", e);
        }
        return null;
    }


    /**
     * 新增结算卡(小微入网修改银行卡)
     * https://open.yeepay.com/docs/apis/INDUSTRY_SOLUTION/GENERAL/ptssfk/jiesuan/options__rest__v1.0__settle__settle-card__add
     */
    public Map<String, String> settleCard(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            request.addParam("bankCardNo", data.getBankCard());
            request.addParam("bankCardType", "DEBIT_CARD");
            request.addParam("bankCode", data.getBankCardCode());
            request.addParam("defaultSettleCard", true);

            YopResponse response = YopRsaClient.post("/rest/v1.0/settle/settle-card/add", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("settleCard异常:", e);
        }
        return null;
    }

    /**
     * 结算记录查询
     */
    public Map<String, String> queryRecords(YeePayParam data) {
        try {
            YopRequest request = new YopRequest(data.getAppKey(), data.getPrivateKey());
            //交易发起方商编。与交易下单传入的保持一致
            request.addParam("parentMerchantNo", data.getParentMerchantNo());
            //收款商户编号
            request.addParam("merchantNo", data.getMerchantNo());
            request.addParam("settleRequestBeginTime", data.getStartTime());
            request.addParam("settleRequestEndTime", data.getEndTime());
            YopResponse response = YopRsaClient.get("/rest/v1.0/settle/records/query", request);
            if (null != response && StringUtils.isNotBlank(response.getStringResult())) {
                return JSONObject.parseObject(response.getStringResult(), new TypeReference<Map<String, String>>() {
                });
            }
        } catch (IOException e) {
            log.error("queryRecords异常:", e);
        }
        return null;
    }


    /**
     * 获取获取sign
     *
     * @param param
     * @return
     */
    private static String getSign(String param, String appKey, String privateKey) {
        PrivateKey isvPrivateKey = getPrivateKey(privateKey);
        DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
        digitalSignatureDTO.setAppKey(appKey);
        digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
        digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
        digitalSignatureDTO.setPlainText(param);
        return DigitalEnvelopeUtils.sign0(digitalSignatureDTO, isvPrivateKey);
    }

    /**
     * 实例化公钥
     *
     * @return
     */
    public static PublicKey getPubKey(String pubKey) {
        PublicKey publicKey = null;
        try {
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    new BASE64Decoder().decodeBuffer(pubKey));
            // RSA对称加密算法
            KeyFactory keyFactory;
            keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (Exception e) {
            log.error("异常信息:", e);
        }
        return publicKey;
    }

    /**
     * 获取私钥
     *
     * @param priKey
     * @return
     */
    public static PrivateKey getPrivateKey(String priKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec;
        try {
            pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(priKey));
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            privateKey = rsa.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            log.error("异常信息:", e);
        }
        return privateKey;
    }

    public static void main(String[] args) {

        YeePayment yeePayment = new YeePayment();
        YeePayParam data = new YeePayParam();

        Map<String, String> result;
        String userName = "1519700757182410752";
        String orderId = "1546798670664429568";
        String amount = "2.00";
        String mobile = "18500507445";
        String goodsName = "测试藏品";
        String worksId = "123";
        String createTime = "2022-06-29 11:18:24";
        String ip = "106.37.95.108, 172.30.16.168";
        String idCard = "110226199401131417";

        //String url = "https://image.solisoli.top/zc/00916569381182355435.jpeg";
        String url = "https://image.solisoli.top/zc/00916569879043659172.jpg";
        String realName = "王泽辉";
        String bankCardNo = "6214850127212857";

        String token = "D43B46BF1C7DE59884854D50FEF3D00FEE1E4EF7787A7F09477FA65003534677";

        data.setUserName(userName);
        data.setMobile(mobile);
        data.setIdCardUrl(url);
        data.setPayType(6);
        data.setOrderId(orderId);
        data.setAmount(amount);

        //注册钱包
        //result = yeePayUtil.walletLogin(data);

        //查询订单
        //result = yeePayUtil.queryOrder("1546699558170390528");

        //退款
        //result = yeePayUtil.refund(data);

        //修改子商户银行卡
        data.setMerchantNo("10088550672");
        data.setBankCard("6210307001099730");
        data.setBankCardCode("BCCB");
        result = yeePayment.settleCard(data);
        System.out.println(JSONObject.toJSONString(result));

        //data.setMerchantNo("10088599227");
        //data.setStartTime("2022-07-18 23:20:51");
        //data.setEndTime(DateUtil.now());
        //String settleRecordQueryDtos = yeePayUtil.queryRecords(data).get("settleRecordQueryDtos");
        //List<YeePayRecordDto> yeePayRecordDtos = JSONObject.parseArray(settleRecordQueryDtos, YeePayRecordDto.class);
        //System.out.println(JSONObject.toJSONString(yeePayRecordDtos));

    }


}
