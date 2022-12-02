package com.nova.pay.payment.open;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nova.pay.entity.param.KsPayParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/11/15 14:10
 * @see <a href="https://mp.kuaishou.com/docs/develop/server/epay/interfaceDefinitionWithoutChannel.html">支付流程图</a>
 */
@Slf4j
@Component
public class KsPayment {

    /**
     * queryParamUrl
     */
    public static final String QUERY_URL = "?app_id={0}&access_token={1}";

    /**
     * 商品图对应的imgId（通过文件上传接口获取），要求长宽比1:1，最大不超过10MB。需与用户下单时的商品图保持一致
     */
    public static final String IMG_ID = "5acfa4c3ccc8244ef443de600cad6cc7725e38d515a2";

    public static final String SETTLE_NOTIFY_URL = "https://newpay.fengkuangtiyu.cn/api/payNotify/ksPaySettle";

    public static final String REFUND_NOTIFY_URL = "https://newpay.fengkuangtiyu.cn/api/payNotify/ksRefund";

    public static void main(String[] args) {

    }

    /**
     * 获取调用凭证
     *
     * @param data
     * @return
     */
    public Map<String, Object> getAccessToken(KsPayParam data) {
        String url = "https://open.kuaishou.com/oauth2/access_token";
        Map<String, Object> result = new HashMap<>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("app_secret", data.getAppSecret());
        map.put("grant_type", "client_credentials");
        String body = HttpUtil.createPost(url).form(map).execute().body();
        if (StrUtil.isNotBlank(body)) {
            result = JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return result;
    }

    /**
     * 获取参数 Map 的签名结果
     *
     * @param signParamsMap 含义见上述示例
     * @return 返回签名结果
     * @see <a href="https://mp.kuaishou.com/docs/develop/server/epay/appendix.html">请求签名算法</a>
     */
    public static String calcSign(Map<String, Object> signParamsMap, String appSecret) {
        // 去掉 value 为空的
        Map<String, Object> trimmedParamMap = signParamsMap.entrySet()
                .stream()
                .filter(item -> ObjectUtil.isNotNull(item.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 按照字母排序
        Map<String, Object> sortedParamMap = trimmedParamMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // 组装成待签名字符串(替换了guava的工具类)
        String paramStr = MapUtil.join(sortedParamMap, "&", "=");
        String signStr = paramStr + appSecret;
        // 生成签名返回。(注，引用了commons-codec工具)
        return DigestUtils.md5Hex(signStr);
    }

    /**
     * 交易下单(无收银台版)
     */
    public Map<String, Object> tradeOrder(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/create_order_with_channel";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());

        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("open_id", data.getOpenId());
        map.put("total_amount", data.getTotalAmount());
        map.put("subject", data.getSubject());
        map.put("detail", data.getSubject());
        /**
         * 商品类型，不同商品类目的编号见（目前写死）
         * @see <a href="https://mp.kuaishou.com/docs/operate/platformAgreement/epayServiceCharge.html">担保支付商品类目编号</a>
         */
        map.put("type", 1286);
        //订单过期时间，单位秒，300s - 172800s
        map.put("expire_time", 300);
        map.put("notify_url", data.getNotifyUrl());
        JSONObject jsonObject = new JSONObject();
        if (ObjectUtil.equals(9, data.getPayType())) {
            jsonObject.set("provider", "WECHAT");
        } else {
            jsonObject.set("provider", "ALIPAY");
        }
        jsonObject.set("provider_channel_type", "NORMAL");
        map.put("provider", jsonObject);
        map.put("sign", calcSign(map, data.getAppSecret()));

        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 取消订单(无收银台版)
     */
    public Map<String, Object> cancelOrder(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/cancel_channel";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("sign", calcSign(map, data.getAppSecret()));
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 查询订单
     */
    public Map<String, Object> queryOrder(KsPayParam data) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("sign", calcSign(map, data.getAppSecret()));
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/query_order";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 同步订单
     *
     * @param data
     * @return
     */
    public Map<String, Object> reportOrder(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/order/v1/report";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("out_biz_order_no", data.getOutOrderNo());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("open_id", data.getOpenId());
        map.put("order_create_time", System.currentTimeMillis());
        map.put("order_status", 11);
        map.put("order_path", "/pages/home");
        map.put("product_cover_img_id", IMG_ID);
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 上传图片url
     * <p>
     * {
     * "result": 1,
     * "error_msg": "success",
     * "data" : {
     * "imgId" : "ssssss"
     * }
     * }
     */
    public Map<String, Object> upload(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/file//img/uploadWithUrl";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken()) + "&url=" + data.getUrl();
        String body = HttpUtil.createPost(url).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 结算规则:
     * 1、订单履约完成后发起结算，结算周期为 订单到达核销状态满3天后可发起结算。核销状态通过订单接口进行同步。
     * 2、需要主动调用结算接口后，才能进行后续资金的提现。
     * 3、结算时，小程序平台会收取整笔交易的平台服务费。若结算后发生退款，则平台服务费不作退还。
     * <p>
     * 结算订单
     * 接口频次: 30QPS(小程序app_id维度)
     */
    public Map<String, Object> createSettle(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/settle";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("out_settle_no", data.getOutSettleNo());
        map.put("reason", "订单[" + data.getOutOrderNo() + "]结算");
        map.put("notify_url", SETTLE_NOTIFY_URL);
        map.put("settle_amount", data.getTotalAmount());
        map.put("sign", calcSign(map, data.getAppSecret()));
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 查询结算订单
     */
    public Map<String, Object> querySettle(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/query_settle";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_settle_no", data.getOutOrderNo());
        map.put("sign", calcSign(map, data.getAppSecret()));
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 退款
     */
    public Map<String, Object> refund(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/apply_refund";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_order_no", data.getOutOrderNo());
        map.put("out_refund_no", data.getOutOrderNo());
        map.put("reason", "订单[" + data.getOutOrderNo() + "]退款");
        map.put("notify_url", REFUND_NOTIFY_URL);
        map.put("sign", calcSign(map, data.getAppSecret()));
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }

    /**
     * 查询退款
     */
    public Map<String, Object> queryRefund(KsPayParam data) {
        String url = "https://open.kuaishou.com/openapi/mp/developer/epay/query_refund";
        url = url + StrUtil.indexedFormat(QUERY_URL, data.getAppId(), data.getAccessToken());
        Map<String, Object> map = new HashMap<>(16);
        map.put("app_id", data.getAppId());
        map.put("out_refund_no", data.getOutOrderNo());
        map.put("sign", calcSign(map, data.getAppSecret()));
        String body = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(map)).execute().body();
        if (StrUtil.isNotBlank(body)) {
            return JSONUtil.toBean(body, new TypeReference<Map<String, Object>>() {
            }, true);
        }
        return null;
    }
}
