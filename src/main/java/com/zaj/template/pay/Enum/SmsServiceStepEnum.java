package com.zaj.template.pay.Enum;


import com.zaj.template.pay.vo.WxPayVO;

/**
 * @Author: aiJun
 * @Date: 2019-09-20 17:44
 * @Version 1.0
 */
public enum SmsServiceStepEnum {
    SMS_SERVICE_STEP1(0.062,0,1000,"量<1000"),
    SMS_SERVICE_STEP2(0.060,1000,5000,"1000≤量<5000"),
    SMS_SERVICE_STEP3(0.055,5000,10000,"5000≤量<1万"),
    SMS_SERVICE_STEP4(0.047,10000,20000,"1万≤量<2万"),
    SMS_SERVICE_STEP5(0.043,20000,30000,"2万≤量<3万"),
    SMS_SERVICE_STEP6(0.041,30000,Integer.MAX_VALUE,"量≥3万");
    private double price;
    private int minNum;
    private int maxNum;
    private String desc;

    /**
     * 涉及到钱根据->数量-区间-金额判断充值金额是否正确
     * @param payParam
     * @return
     */
    public static boolean checkIsNormalReq(WxPayVO payParam) {
        Integer buyNum = payParam.getNumber();
        Double totalFee = payParam.getOrderFe();
        for (SmsServiceStepEnum smsStep : SmsServiceStepEnum.values()) {
            if (smsStep.getMinNum() <= buyNum && buyNum < smsStep.getMaxNum()) {
                String callLTotalFee = String.format("%.2f", smsStep.getPrice() * buyNum);
                if (totalFee.equals(Double.valueOf(callLTotalFee))) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return false;
    }



    SmsServiceStepEnum(double price, int minNum, int maxNum, String desc) {
        this.price = price;
        this.minNum = minNum;
        this.maxNum = maxNum;
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public int getMinNum() {
        return minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public String getDesc() {
        return desc;
    }
}

