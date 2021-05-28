package com.zaj.template.pay.controller;

import com.zaj.template.pay.Enum.PayEnum;
import com.zaj.template.pay.Enum.SmsServiceStepEnum;
import com.zaj.template.pay.service.PayManagerService;
import com.zaj.template.pay.util.RandomNumUtil;
import com.zaj.template.pay.vo.WxPayVO;
import com.zaj.template.pay.wxpay.MyWXPayConfig;
import com.zaj.template.pay.wxpay.WXPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: aiJun
 * @Date: 2021/5/27 0027 16:41
 * @Version 1.0
 */
@RestController
public class WxPayController {
    private static final Logger logger = LoggerFactory.getLogger(WxPayController.class);

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    @Autowired
    private MyWXPayConfig myWXPayConfig;

    @Autowired
    private PayManagerService payManagerService;
    @RequestMapping("wxpay/payDemo1")
    public String payDemo1() {
        return "payDemo1";
    }
    @RequestMapping(value = "wxpay/buySmsService", method = RequestMethod.POST)
    public String buySmsService(@RequestBody() WxPayVO payParam) {
        try {
            String orderId = RandomNumUtil.getOrderNo();
            payParam.setOrderId(orderId);
            if (SmsServiceStepEnum.checkIsNormalReq(payParam)) {
                //正常订单->将订单资料插入到数据库
                WXPay wxpay = new WXPay(myWXPayConfig);
                DecimalFormat decimalFormat = new DecimalFormat("###################");
                double pay = payParam.getOrderFe() * 100;
                String payFee = decimalFormat.format(pay);
                logger.info("付款金额为：{}分,订单Id={}", payFee, payParam.getOrderId());
                Map<String, String> data = new HashMap<>();
                //回调地址
                String callBackPath = payParam.getDomain() + PayEnum.WX_PAY_PARAM_NOTIFY_URL.getParamValue();
                data.put(PayEnum.WX_PAY_PARAM_BODY.getParamName(), PayEnum.WX_PAY_PARAM_BODY.getParamValue());
                data.put(PayEnum.WX_PAY_PARAM_DEVICE_INFO.getParamName(), PayEnum.WX_PAY_PARAM_DEVICE_INFO.getParamValue());
                data.put(PayEnum.WX_PAY_PARAM_FEE_TYPE.getParamName(), PayEnum.WX_PAY_PARAM_FEE_TYPE.getParamValue());
                data.put(PayEnum.WX_PAY_PARAM_NOTIFY_URL.getParamName(), callBackPath);
                data.put(PayEnum.WX_PAY_PARAM_TRADE_TYPE.getParamName(), PayEnum.WX_PAY_PARAM_TRADE_TYPE.getParamValue());  // 此处指定为扫码支付
                data.put(PayEnum.WX_PAY_PARAM_PRODUCT_ID.getParamName(), PayEnum.WX_PAY_PARAM_PRODUCT_ID.getParamValue());
                data.put(PayEnum.WX_PAY_PARAM_OUT_TRADE_NO.getParamName(), payParam.getOrderId());
                data.put(PayEnum.WX_PAY_PARAM_TOTAL_FEE.getParamName(), payFee);//1 指的是0.01
                data.put(PayEnum.WX_PAY_PARAM_SPBILL_CREATE_IP.getParamName(), payParam.getIp());
                Map<String, String> resp = wxpay.unifiedOrder(data);
                if (SUCCESS.equals(resp.get("return_code"))) {
                    Map<String, Object> map = new HashMap<>();
                    String payUrl = resp.get("code_url");
                    // 付款URL
                    map.put("codeUrl", payUrl);
                    map.put("orderId", payParam.getOrderId());
//                    入订单库
                    logger.info("wxResp={},付款连接为：{},插入订单的结果为：[{}],回调地址为：【{}】", resp, payUrl, 1, callBackPath);
                    return payUrl;
                } else {
                    logger.error("shopId={},shopTitle={},付款码获取失败，原因：【{}】", payParam.getShopId(), payParam.getShopTitle(), resp.get("return_msg"));
                    return FAILURE;
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            return FAILURE;
        }
    }



    /**
     * 微信支付的回调
     * @param request
     */
    @RequestMapping(value = "wxpay/notify")
    @ResponseBody
    public Object wxpayNotify(HttpServletRequest request) {
        String notityXml = "";
        Object respXml = "";
        logger.info("WxPay支付回调方法进入--->{}",request.toString());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            //sb为微信返回的xml
            notityXml = sb.toString();
            logger.info("接收到的报文：{}", notityXml);
        } catch (IOException e) {
            logger.error("接收到的xml报文获取失败->>>{}", e.getMessage(), e);
        }
        try {
            payManagerService.handlerWxOrderPayResult(notityXml);
            logger.info("即将返回给微信的respXml=【{}】", respXml);
        } catch (Exception e) {
            logger.error("本系统rest发送错误返回错误{}", e.getMessage(), e);
        }
        return respXml;
    }


}
