package com.dhk.payment.service;


import com.dhk.payment.yilian.QuickPay;

import java.util.Map;

public interface IhlbService {
    /**
     * 快捷支付
     * @param paramMap
     * @return
     * @throws Exception
     */
    public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception;

    /**
     * 代付
     * @param paramMap
     * @return
     * @throws Exception
     */
    public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception;

}

