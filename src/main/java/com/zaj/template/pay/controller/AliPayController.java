package com.zaj.template.pay.controller;

import com.zaj.template.pay.util.AliPayUtil;
import com.zaj.template.pay.vo.AlipayVO;
import com.zaj.template.pay.vo.RechargeCalVO;
import com.zaj.template.pay.vo.RechargeRecordInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: aiJun
 * @Date: 2021/5/27 0027 16:41
 * @Version 1.0
 */
@RestController
public class AliPayController {
    private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);

    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";
    private static final String OUT_TRADE_NO = "out_trade_no";
    private static final String TRADE_STATUS = "trade_status";
    private static final String APP_ID = "app_id";
    private static final String TOTAL_AMOUNT = "total_amount";
    @Resource
    private AliPayUtil alipayUtil;

    @RequestMapping("/payDemo1")
    public String payDemo1() {
        return "payDemo1";
    }

    @RequestMapping(path = {"/v1/_recharge"}, method = {RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> rechargeCopy(@RequestBody RechargeCalVO calVO) {
        Map<String, Object> map = new HashMap<>();
        String amount = "0";
        String id = "0";
        // 1.新增充值记录

        // 2.生成支付二维码
        AlipayVO payVo = new AlipayVO();
        payVo.setTotal_amount(amount);
        payVo.setOut_trade_no(id);
        String alipay = alipayUtil.alipay(payVo);
        map.put("QRCode", alipay);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    /**
     * Ali支付回调
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/v1/aliPayBack")
    public String aliPayBack(HttpServletRequest request, HttpServletResponse response) {
        // 阿里云主动通知只有两种情况 1 付款成功 TRADE_SUCCESS 2退款通知TRADE_CLOSED
        logger.info("阿里支付成功回调接口");
        String result;
        try {
            Map<String, String> params = alipayUtil.convertRequestParamsToMap(request);
            boolean signVerified = alipayUtil.checkSign(request);
            if (signVerified) {
                String id = params.get(OUT_TRADE_NO); // 交易状态
                logger.info("支付宝回调签名认证成功------>>>>>1 id={}", id);
                String tradeStatus = params.get(TRADE_STATUS); // 交易状态
                double totalAmount = Double.parseDouble(params.get(TOTAL_AMOUNT));
                String appId = params.get(APP_ID);
                // 1.验证参数 TODO 获取订单信息
                RechargeRecordInfoVO body = new RechargeRecordInfoVO();
                logger.info("支付宝回调签名认证成功------>>>>>2");
                alipayUtil.check(body, totalAmount, appId);
                logger.info("支付宝回调签名认证成功------>>>>>3");
                // 2.支付成功
                if (TRADE_SUCCESS.equals(tradeStatus)) {
                    logger.info("支付宝回调签名认证成功------>>>>>4");
                    this.rechargeForMerchant(id, "COPY_CHARGE_STATUS_SUCCESS");
                    logger.info("支付宝回调签名认证成功------>>>>>5");
                }
                result = SUCCESS;
            } else {
                result = FAILURE;
                logger.info("：阿里API校验未通过");
            }
            return result;
        } catch (Exception e) {
            result = FAILURE;
            logger.info("ali pay back is error:" + e.getMessage(), e);
            return result;
        }
    }

    // TODO 处理充值成功的逻辑
    private void rechargeForMerchant(String id, String copy_charge_status_success) {
        return;
    }
}
