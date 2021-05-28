package com.zaj.template.pay.wxpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author: aiJun
 * @Date: 2019-09-10 17:25
 * @Version 1.0
 */
@Component
public class MyWXPayConfig extends  WXPayConfig{
    private byte[] certData;
    private String appID;
    private String mchID;
    private String key;

    public MyWXPayConfig() throws Exception {
//        #TODO 修改凭证
//        ClassPathResource classPathResource = new ClassPathResource("apiclient_cert.p12");
//        //获取文件流
//        InputStream certStream = classPathResource.getInputStream();
//        this.certData = IoUtil.readBytes(certStream);
//        certStream.read(this.certData);
//        certStream.close();
    }

    @Value("${wxpay.appID}")
    public void setAppID(String appID) {
        this.appID = appID;
    }
    @Value("${wxpay.mchID}")
    public void setMchID(String mchID) {
        this.mchID = mchID;
    }
    @Value("${wxpay.key}")
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getAppID() {
        return appID;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        return new IWXPayDomain() {
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
