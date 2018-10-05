package com.dhk.payment.entity;

import com.dhk.kernel.dao.jdbc.Table;
import com.dhk.kernel.entity.Entity;

/**
 * 支付通道
 */
@SuppressWarnings("serial")
@Table(name="t_payment_channels")
public class PaymentChannels extends Entity {
    private String name;
    private Integer consumptionWeight;
    private Integer proxyPayWeight;
    private Integer weight;
    private String beginTime;
    private String endTime;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getConsumptionWeight() {
        return consumptionWeight;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setConsumptionWeight(Integer consumptionWeight) {
        this.consumptionWeight = consumptionWeight;
    }

    public Integer getProxyPayWeight() {
        return proxyPayWeight;
    }

    public void setProxyPayWeight(Integer proxyPayWeight) {
        this.proxyPayWeight = proxyPayWeight;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

