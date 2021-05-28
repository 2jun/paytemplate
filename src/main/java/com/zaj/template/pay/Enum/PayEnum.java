package com.zaj.template.pay.Enum;

/**
 * @Author: aiJun
 * @Date: 2019-09-21 2:01
 * @Version 1.0
 */
public enum PayEnum {
    WX_PAY_PARAM_BODY("body","收款业务名称",true,"订单的商品内容"),
    WX_PAY_PARAM_DEVICE_INFO("device_info","WEB",true,"支付方式"),
    WX_PAY_PARAM_FEE_TYPE("fee_type","CNY",true,"币种"),
//    TODO 配置微信回调
    WX_PAY_PARAM_NOTIFY_URL("notify_url","公网可以访问到的回调",true,"回调地址，公网可以访问，不能携带参数"),
    WX_PAY_PARAM_TRADE_TYPE("trade_type","NATIVE",true,"付款类型：NATIVE指定为扫码支付"),
    WX_PAY_PARAM_PRODUCT_ID("product_id","1",true,"商品ID"),
    WX_PAY_PARAM_OUT_TRADE_NO("out_trade_no","",false,"订单号，自己生成,不能重复"),
    WX_PAY_PARAM_TOTAL_FEE("total_fee","",false,"付款金额,1 指的是0.01元"),
    WX_PAY_PARAM_SPBILL_CREATE_IP("spbill_create_ip","",false,"ip");


    private String paramName;
    private String paramValue;
    private boolean isFixecValue;
    private String desc;

    PayEnum(String paramName, String paramValue, boolean isFixecValue, String desc) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.isFixecValue = isFixecValue;
        this.desc = desc;
    }


    public String getParamName() {
        return paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public boolean isFixecValue() {
        return isFixecValue;
    }

    public String getDesc() {
        return desc;
    }
}
