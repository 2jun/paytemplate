package com.zaj.template.pay.vo;


import java.sql.Timestamp;

/**
 * 充值记录表
 *
 * @date 2021-03-11 23:13:25
 */
public class RechargeRecordInfoVO {

    /**
     * 充值记录ID
     */
    private long id;
    /**
     * 店铺名称
     */
    private String packName;
    /**
     * 付款时间
     */
    private Timestamp payTime;
    /**
     * 付款金额，
     */
    private double payAmount;
    /**
     * 充值条数
     */
    private long rechargeCount;
    /**
     * 状态，1已生效、2充值失败
     */
    private int status;
    /**
     * 备注
     */
    private String memo;
    /**
     * 所属店铺ID
     */
    private long merchantId;
    /**
     * 店铺ID
     */
    private long shopId;

    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public RechargeRecordInfoVO(long id, String shopName, long rechargeCount,
                                double payAmount, long merchantId,
                                long shopId, int status,
                                Timestamp payTime) {
        this.id = id;
        this.packName = shopName;
        this.rechargeCount = rechargeCount;
        this.payAmount = payAmount;
        this.merchantId = merchantId;
        this.shopId = shopId;
        this.status = status;
        this.payTime = payTime;
    }

    public RechargeRecordInfoVO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public long getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(long rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
