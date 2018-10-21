package com.dhk.api.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.dhk.api.dao.ICardBillDao;
import com.dhk.api.service.ICardBillService;

@Service("CardBillService")
public class CardBillService implements ICardBillService {
	@Resource(name = "CardBillDao") 
	private ICardBillDao CardBillDao;
}

