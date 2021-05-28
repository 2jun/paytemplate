package com.zaj.template.pay.service;

import com.zaj.template.pay.vo.WxPayVO;

/**
 * @Author: aiJun
 * @Date: 2019-09-21 1:01
 * @Version 1.0
 */
public interface PayManagerService {
    Object buySmsService(WxPayVO payParam) throws Exception;

    Object handlerWxOrderPayResult(String notityXml) throws Exception;

    Object checkOrderStatus(String orderId);
}
