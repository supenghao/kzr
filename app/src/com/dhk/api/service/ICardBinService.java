package com.dhk.api.service;

import com.dhk.api.dto.GetBankInfoDto;
import com.dhk.api.entity.CardBin;
import com.dhk.api.dto.QResponse;

/**
 * t_card_bin service 接口<br/>
 * 2017-02-07 09:22:44 qch
 */
public interface ICardBinService {

	public QResponse getInfo(GetBankInfoDto dto);

	/**
	 * 根据银行卡号获取cardbin
	 * 
	 * @param cardNo
	 * @return
	 */
	public CardBin getCarbinByCardNo(String cardNo);
}
