package com.zaj.template.pay.service.impl;

import com.zaj.template.pay.service.PayManagerService;
import com.zaj.template.pay.vo.WxPayVO;
import com.zaj.template.pay.wxpay.MyWXPayConfig;
import com.zaj.template.pay.wxpay.WXPay;
import com.zaj.template.pay.wxpay.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: aiJun
 * @Date: 2021/5/28 0028 10:33
 * @Version 1.0
 */
@Service
public class PayManagerServiceImpl implements PayManagerService {
    private static final Logger logger = LoggerFactory.getLogger(PayManagerServiceImpl.class);

    private static final String SUCCESS = "SUCCESS";
    @Autowired
    private MyWXPayConfig myWXPayConfig;
    @Override
    public Object buySmsService(WxPayVO payParam) throws Exception {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object handlerWxOrderPayResult(String notityXml) throws Exception {
        //获取校验签名并获取支付结果
        Map<String, String> data = new HashMap<>();
        data.put("data", getResultStr(notityXml));
        return "success";
    }

    private String getResultStr(String notityXml) throws Exception {
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notityXml);  // 转换成map
        WXPay wxpay = new WXPay(myWXPayConfig);
        if (wxpay.isResponseSignatureValid(notifyMap)) {
            // 签名正确
            // 进行处理。
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
            //判断处理结果是否成功
            if (SUCCESS.equals(notifyMap.get("return_code"))) {
                String orderId = notifyMap.get("out_trade_no");
//                TODO 处理成功的业务逻辑
                logger.info("通过orderId={},更新订单状态-交易成功时间结果为：{},更新短信最新条数结果为{}", orderId, 1, 1);
                return WxCallbackXml.SUCCESS.getValue();
            } else {
                //失败

            }
            //不做任何操作

            return "";

        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            logger.error("签名验证错误--->不执行任何操作");
            return "";
        }
    }

    public enum WxCallbackXml {
        SUCCESS("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        private String value;

        WxCallbackXml(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    @Override
    public Object checkOrderStatus(String orderId) {
        return null;
    }
}
