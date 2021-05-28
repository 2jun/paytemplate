package com.zaj.template.pay.vo;

/**
 * 充值计算VO
 */
public class RechargeCalVO {
    // 充值方式 1套餐包(不需要计算金额)、2条数(需要计算金额)
    private int rechargeType;
    // 充值参数 rechargeType=1则是套餐包ID、rechargeType=2则是充值短信条数
    private long rechargeParam;

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }

    public long getRechargeParam() {
        return rechargeParam;
    }

    public void setRechargeParam(long rechargeParam) {
        this.rechargeParam = rechargeParam;
    }
}
