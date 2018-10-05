package com.dhk.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.dao.ICreditCardBillDao;
import com.dhk.service.ICreditCardBillService;

@Service("CreditCardBillService")
public class CreditCardBillService implements ICreditCardBillService{

	@Resource(name="CreditCardBillDao")
	ICreditCardBillDao creditCardBillDao;
	


}
