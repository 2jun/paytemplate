package com.zaj.template.pay.vo;

/**
 * @Author: aiJun
 * @Date: 2019-09-21 0:55
 * @Version 1.0
 */
public class WxPayVO {
    private Long shopId;
    private String shopTitle;
    private String operator;
    private Integer number;
    private Double orderFe;
    private String ip;
    private String orderId;
    private String domain;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getOrderFe() {
        return orderFe;
    }

    public void setOrderFe(Double orderFe) {
        this.orderFe = orderFe;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
