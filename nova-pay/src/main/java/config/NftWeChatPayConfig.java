package config;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: NFT微信配置类
 * @Author: wangzehui
 * @Date: 2022/6/18 11:08
 */
public class NftWeChatPayConfig implements WXPayConfig {
    /**
     * 微信APP_ID
     */
    private final String appId;

    /**
     * 商户号
     */
    private final String mchId;

    /**
     * 支付秘钥，商户秘钥
     */
    private final String paySecret;

    /**
     * P12证书路径
     */
    private final String certPath;

    /**
     * 连接超时时间
     */
    private final Integer httpConnectTimeoutMs;
    /**
     * 读取超时时间
     */
    private final Integer httpReadTimeoutMs;

    public NftWeChatPayConfig(String appId, String mchId, String paySecret, String certPath) {
        this.appId = appId;
        this.mchId = mchId;
        this.paySecret = paySecret;
        this.certPath = certPath;
        this.httpConnectTimeoutMs = 8000;
        this.httpReadTimeoutMs = 10000;
    }

    public NftWeChatPayConfig(String appId, String mchId, String paySecret, String certPath, Integer httpConnectTimeoutMs, Integer httpReadTimeoutMs) {
        this.appId = appId;
        this.mchId = mchId;
        this.paySecret = paySecret;
        this.certPath = certPath;
        this.httpConnectTimeoutMs = httpConnectTimeoutMs;
        this.httpReadTimeoutMs = httpReadTimeoutMs;
    }

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.paySecret;
    }

    @Override
    public InputStream getCertStream() {
        try {
            //File file = ResourceUtils.getFile(this.certPath);
            return new FileInputStream("");
        } catch (IOException e) {
            throw new RuntimeException("路径:" + this.certPath + " 下找不到证书", e);
        }
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return this.httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return this.httpReadTimeoutMs;
    }
}
