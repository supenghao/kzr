package com.dhk.payment.service.impl;
import com.dhk.payment.dao.IPaymentChannelsDao;
import com.dhk.payment.entity.PaymentChannels;
import com.dhk.payment.service.IPaymentChannelsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("paymentChannelsService")
public class PaymentChannelsService implements IPaymentChannelsService {
	@Resource(name = "paymentChannelsDao")
	private IPaymentChannelsDao paymentChannelsDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Cacheable(value="t_payment_channels", key="'get'")
	public List<PaymentChannels> getAll(){
		return paymentChannelsDao.find(null);
	}

	@Cacheable(value="t_channels_dis_card", key="#channel")
	public List<String> getDisCard(String channel){
		List<String> list = jdbcTemplate.queryForList("select CARDBIN from t_channels_dis_card where channel=?",String.class,new String[]{channel});
		return list==null?new ArrayList<String>():list;
	}
}

