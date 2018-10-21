package com.dhk.api.service;

import com.dhk.api.entity.CreditCard;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.Token;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.EditCreditCarDto;
import com.dhk.api.dto.QResponse;

/**
 * t_s_user_creditcard service 接口<br/>
 * 2017-02-08 09:31:12 qch
 */
public interface ICreditCardService {

	QResponse addCreditCar(AddCreditCarDto dto) throws Exception ;

	Token editCreditCar(EditCreditCarDto dto) throws Exception ;

	Token deleteCreditCar(DelCreditCarDto dto) throws Exception ;

	CreditCard getCreditCarInfo(String USERID, String card_no);

	/**
	 * 获取信用卡列表信息
	 * 
	 * @param dto
	 * @return
	 */
	QResponse getCreditCarList(IdentityDto dto)  throws Exception;


	QResponse getCreditCardDetail(DelCreditCarDto dto)  throws Exception;
	
	Token txUnfreeze(DelCreditCarDto dto) throws Exception ;

	QResponse addCreditCarSendMsg(AddCreditCarDto dto);
	
	QResponse addCreditCarSendMsgKj(AddCreditCarDto dto);
}
