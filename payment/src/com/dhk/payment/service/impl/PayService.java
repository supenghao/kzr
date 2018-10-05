package com.dhk.payment.service.impl;

import com.dhk.kernel.util.StringUtil;
import com.dhk.payment.entity.PaymentChannels;
import com.dhk.payment.service.IPayService;
import com.dhk.payment.service.IPaymentChannelsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


@Service("payService")
public class PayService implements IPayService {

	@Resource(name = "paymentChannelsService")
	private IPaymentChannelsService paymentChannelsService;

	@Resource(
			name = "jdbcTemplate"
	)
	private JdbcTemplate jdbcTemplate;


	public String getChannel(boolean checkTime,String cardBin,int type){
		try {
			String curTime = StringUtil.getCurrentDateTime("HHmmss");
			List<PaymentChannels> list = paymentChannelsService.getAll();
			Integer weightSum = 0;
			Iterator<PaymentChannels> it = list.iterator();
			while(it.hasNext()){        //正确做法
				PaymentChannels paymentChannels = it.next();
				if(checkTime){
					String beginTime = paymentChannels.getBeginTime();
					String endTime = paymentChannels.getEndTime();
					if(Integer.parseInt(beginTime)>Integer.parseInt(curTime)||Integer.parseInt(endTime)<Integer.parseInt(curTime)){    //不在时间方位内
						it.remove();
						continue;
					}
				}
				if(paymentChannelsService.getDisCard(paymentChannels.getName()).contains(cardBin)){
					it.remove();
					continue;
				}
				if(type==1){
					weightSum += paymentChannels.getConsumptionWeight();
				}else{
					weightSum += paymentChannels.getProxyPayWeight();
				}
			}
			Random random = new Random();
			Integer n = random.nextInt(weightSum);
			Integer m = 0;
			for (PaymentChannels paymentChannels : list) {
				if(type==1){
					if (m <= n && n < m + paymentChannels.getConsumptionWeight()) {
						return paymentChannels.getName();
					}
					m += paymentChannels.getConsumptionWeight();
				}else{
					if (m <= n && n < m + paymentChannels.getProxyPayWeight()) {
						return paymentChannels.getName();
					}
					m += paymentChannels.getProxyPayWeight();
				}

			}
			if(list.size()>0){
				return list.get(0).getName();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "YL";
	}

	
}

