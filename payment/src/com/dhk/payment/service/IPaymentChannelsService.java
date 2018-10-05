package com.dhk.payment.service;


import com.dhk.payment.entity.PaymentChannels;

import java.util.List;

public interface IPaymentChannelsService {

    public List<PaymentChannels> getAll();

    public List<String> getDisCard(String channel);
}

