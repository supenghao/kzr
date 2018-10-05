package com.dhk.payment.service;

public interface IPayService {
    /**
     *
     * @param checkTime
     * @param cardBin
     * @param type   1|消费；2|代付
     * @return
     */
    public String getChannel(boolean checkTime,String cardBin,int type);
}

