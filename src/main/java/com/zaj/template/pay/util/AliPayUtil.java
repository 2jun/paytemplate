package com.zaj.template.pay.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zaj.template.pay.config.AlipayConfig;
import com.zaj.template.pay.vo.AlipayVO;
import com.zaj.template.pay.vo.RechargeRecordInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AliPayUtil
 * @Description TODO
 * @Date 2020-12-02 17:20
 * @Version 1.0
 **/
public class AliPayUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliPayUtil.class);
    private static final String format = "json";
    @Autowired
    private AlipayConfig alipayConfig;

    /**
     *
     * @param vo 商户订单号，商户网站订单系统中唯一订单号，必填 对应缴费记录的orderNo
     * @return
     */
    public String alipay(AlipayVO vo) {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(), format, alipayConfig.getCharset(), alipayConfig.getPublicKey(),
                alipayConfig.getSignType());
        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        for (int i = 3; i > 0; i++) {
            try {
                alipayRequest.setBizContent(JSON.toJSONString(vo));

                // 请求
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                LOGGER.info("*********************\n返回结果为：" + result);
                if (StrUtil.isBlank(result)) {
                    continue;
                }
                return result;
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 支付宝的验签方法
     *
     * @param req
     * @return
     */
    public boolean checkSign(HttpServletRequest req) {
        Map<String, String[]> requestMap = req.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        requestMap.forEach((key, values) -> {
            String strs = "";
            for (String value : values) {
                strs = strs + value;
            }
            LOGGER.info(("key值为" + key + "value为：" + strs));
            paramsMap.put(key, strs);
        });

        // 调用SDK验证签名
        try {
            return AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.getPublicKey(), alipayConfig.getCharset(),
                    alipayConfig.getSignType());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("*********************验签失败********************");
            return false;
        }
    }

    // 将request中的参数转换成Map
    public Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }

    /**
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     *
     * @param body
     * @throws RuntimeException
     */
    public void check(RechargeRecordInfoVO body, double totalAmount, String appId) throws RuntimeException {
        // 1.商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号
        if (body == null) {
            throw new RuntimeException("out_trade_no错误");
        }

        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        double dis = 1e-6;
        if (Math.abs(body.getPayAmount() - totalAmount) > dis) {
            throw new RuntimeException("error total_amount");
        }

        // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
        // 第三步可根据实际情况省略

        // 4、验证app_id是否为该商户本身。
        if (!appId.equals(alipayConfig.getAppId())) {
            throw new RuntimeException("app_id不一致");
        }
    }
}

