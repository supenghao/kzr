package com.dhk.payment.dao.impl;

import com.dhk.kernel.dao.jdbc.JdbcBaseDaoSupport;
import com.dhk.payment.dao.IPaymentChannelsDao;
import com.dhk.payment.entity.PaymentChannels;
import org.springframework.stereotype.Repository;

@Repository("paymentChannelsDao")
public class PaymentChannelsDao extends JdbcBaseDaoSupport<PaymentChannels, Long> implements IPaymentChannelsDao {
}

