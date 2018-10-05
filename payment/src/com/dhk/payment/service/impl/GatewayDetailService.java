package com.dhk.payment.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dhk.payment.dao.IGatewayDetailDao;
import com.dhk.payment.service.IGatewayDetailService;

@Service("gatewayDetailService")
public class GatewayDetailService implements IGatewayDetailService {
	@Resource(name = "gatewayDetailDao") 
	private IGatewayDetailDao gatewayDetailDao;
}

