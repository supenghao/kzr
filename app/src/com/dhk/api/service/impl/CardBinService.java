package com.dhk.api.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.dhk.api.dao.ICardBinDao;
import com.dhk.api.dto.GetBankInfoDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.CardBin;
import com.dhk.api.service.ICardBinService;
import com.dhk.api.service.ITokenService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("CardBinService")
public class CardBinService implements ICardBinService {

	@Resource(name = "CardBinDao")
	private ICardBinDao cardBinDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public QResponse getInfo(GetBankInfoDto dto) {
		boolean t = tokenService.checkToken(dto);
		if (t) {
			String carNo = dto.getCardNo();
			if (carNo == null || carNo.length() < 7) {
				return new QResponse(false, "缺少卡号");
			}
			String tem = carNo.substring(0, 6) + "%'";
			String sql = "select * from t_card_bin where cardBin like '" + tem;
			
			String cardType = dto.getCardType();
			if ("debit".equals(cardType)){
				sql = sql + " and (cardType='借记卡' or cardType is null or cardType='')";
			}else if ("credit".equals(cardType)){
				sql = sql + " and (cardType='贷记卡' or cardType='准贷记卡' or cardType is null or cardType='')";
			}
			
			List<CardBin> li = cardBinDao.find(sql, null);
			if (li == null || li.size() == 0) {
				CardBin b = new CardBin();
				b.setBankCode("000000");
				b.setBankName("未识别该卡所属银行");
				b.setCardbin(tem);
				b.setCardName("未识别卡");
				b.setCardType("未识别卡");
				return new QResponse(b);
				//return new QResponse(false, "未识别该卡号");
			} else {
				CardBin b = li.get(0);
				b.setId(null);
				b.setCardbin(null);
				return new QResponse(b);
			}
		}
		return QResponse.ERROR_SECURITY;
	}

	/**
	 * 根据银行卡号获取cardbin--信用卡
	 * @param cardBin
	 * @return
	 */
	@Cacheable(value="t_card_bin", key="#cardBin")
	public CardBin getCarbinByCardNo(String cardBin) {
		String sql = "select * from t_card_bin where cardBin like '" + cardBin  + " and (cardType='贷记卡' or cardType='准贷记卡' or cardType is null or cardType='')";
		List<CardBin> li = cardBinDao.find(sql, null);
		if (li == null || li.size() == 0) {
			//return null;
			CardBin b = new CardBin();
			b.setBankCode("000000");
			b.setBankName("未识别该卡所属银行");
			b.setCardbin(cardBin);
			b.setCardName("未识别卡");
			b.setCardType("未识别卡");
			return b;
		} else {
			CardBin b = li.get(0);
			b.setId(null);
			return b;
		}
	}

}
