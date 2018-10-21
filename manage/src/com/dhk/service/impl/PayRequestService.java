package com.dhk.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dhk.FeeParamCode;
import com.dhk.dao.ICardBinDao;
import com.dhk.entity.APPUser;
import com.dhk.entity.CardBin;
import com.dhk.entity.CreditCard;
import com.dhk.entity.FeeParam;
import com.dhk.entity.Org;
import com.dhk.payment.PayRequest;
import com.dhk.service.IAPPUserService;
import com.dhk.service.IFeeParamService;
import com.dhk.service.IPayRequestService;
import com.dhk.utils.DateTimeUtil;

@Service("PayRequestService")
public class PayRequestService  implements IPayRequestService{

	private  static final String PREFIX = "K";
	@Resource(name = "APPUserService") 
	private IAPPUserService appUserService;
	@Resource(name = "FeeParamService")
	private IFeeParamService feeParamService;
	@Resource(name = "CardBinDao")
	private ICardBinDao cardBinDao;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private static final Logger log= LogManager.getLogger();
	
	public PayRequest getCreditCardCostRequest(APPUser user, BigDecimal transAmount, CreditCard card,String orderNo) {
		String phoneNo=card.getPhoneNo();
		PayRequest payRequest=getBaseRequest(user,transAmount,card.getCardNo());
		payRequest.setPhoneNo(phoneNo);
		payRequest.setOrderNo(orderNo);
		payRequest.setKjmerno(user.getKjMerno());
    	payRequest.setKjkey(user.getKjKey());
		payRequest.setCommodityName("妥妥商品");
    	payRequest.setCvn2(card.getCvn2());
    	payRequest.setExpDate(card.getExpDate());
    	payRequest.setBindId(card.getBindId()); //帮卡id
    	String userId = user.getId()+"";
    	int cnt = 14-userId.length();
		for(int i=0;i<cnt;i++)
			userId = "0"+userId;
		String P3_userId = PREFIX+userId;//商户编号
    	payRequest.setUerId(P3_userId); //id+username为登录userid
    	return payRequest;
	}
	
	
	/**
	 *信用卡还款请求参数
	 * @param user
	 * @param transAmount
	 * @param card
	 * @return
	 */
	public PayRequest getCreditCardRepayRequest(APPUser user, BigDecimal transAmount, String orderNo,CreditCard card) {
		
		String phoneNo=card.getPhoneNo();
		PayRequest payRequest=getBaseRequest(user,transAmount,card.getCardNo());
		payRequest.setKjmerno(user.getKjMerno());
    	payRequest.setKjkey(user.getKjKey());
		payRequest.setPhoneNo(phoneNo);
		payRequest.setAccBankName(card.getBankName());  //信用卡的银行
		payRequest.setOrderNo(orderNo);
/*		String cardBin="";
		String lhh="";
		String lhmc="";
		String cardNo = card.getCardNo();
		if(cardNo.length()>=6){
			cardBin=cardNo.substring(0,6);
			Map<String, Object> cardBinMap =jdbcTemplate.queryForMap("select * from t_card_bin where cardBin like '"+cardBin+"%' and  lhh is  not null and  lhmc is  not null ") ;
			if (cardBinMap!=null){
				lhh=(String)cardBinMap.get("LHH");
				lhmc=(String)cardBinMap.get("LHMC");
				payRequest.setLhh(lhh); //设置总行联行号
				payRequest.setLhmc(lhmc); //设置总行联行名称
			}
		}*/
		
    	payRequest.setBindId(card.getBindId()); //帮卡id
    	String userId = user.getId()+"";
    	int cnt = 14-userId.length();
		for(int i=0;i<cnt;i++)
			userId = "0"+userId;
		String P3_userId = PREFIX+userId;//商户编号
    	payRequest.setUerId(P3_userId); //id+username为登录userid    
    	
    	return payRequest;
	}
	
	
	
	public static class ProxyPayType{
		public static final String T1="0201";
		public static final String D0="0203";
		
	}
	
	private PayRequest getBaseRequest(APPUser user, BigDecimal transAmount, String cardNo) {
		String date=DateTimeUtil.getNowDateTime("yyyyMMdd");
		PayRequest payRequest=new PayRequest();
    	payRequest.setOrderDate(date);
		String orderTime=DateTimeUtil.getNowDateTime("HHmmss");
    	payRequest.setOrderTime(orderTime);
    	payRequest.setTransAmt( transAmount.multiply(new BigDecimal("100")).longValue());
		payRequest.setAccBankName(user.getBankName());   //储蓄卡的银行
    	payRequest.setRealName(user.getRealname());
    	payRequest.setCustomerName(user.getRealname());
    	payRequest.setCerdId(user.getIdNumber());
    	payRequest.setCerdType("01");//证件类型为身份证
    	payRequest.setAcctNo(cardNo);
		payRequest.setMerchantId(user.getMerchantid());
		payRequest.setByl_merchantId(user.getYbl_merchantid());
		String cardBin="";
		String lhh="";
		String lhmc="";
		if(cardNo.length()>=6){
			cardBin=cardNo.substring(0,6);
/*			Map<String, Object> cardBinMap =jdbcTemplate.queryForMap("select * from t_card_bin where cardBin like '"+cardBin+"%' and  lhh is  not null and  lhmc is  not null ") ;
			if (cardBinMap!=null){
				lhh=(String)cardBinMap.get("LHH");
				lhmc=(String)cardBinMap.get("LHMC");
				payRequest.setLhh(lhh); //设置总行联行号
				payRequest.setLhmc(lhmc); //设置总行联行名称
			}*/
			
			String sql="select * from t_card_bin where cardBin like :cardBin and  lhh is  not null and  lhmc is  not null ";
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("cardBin", cardBin+"%");
			List<CardBin> cardbins = cardBinDao.find(sql, map);
			if(cardbins!=null && cardbins.size()==1){
				CardBin cardBinObj = cardbins.get(0);
				lhh=cardBinObj.getLhh();
				lhmc=cardBinObj.getLhmc();
				payRequest.setLhh(lhh); //设置总行联行号
				payRequest.setLhmc(lhmc); //设置总行联行名称
			}else if(cardbins!=null && cardbins.size()>1){
				for (CardBin cardBindouble:cardbins) {
					if(cardNo.length()==18 && cardBindouble!=null && cardBindouble.getCardName().contains("工商")){
						lhh=cardBindouble.getLhh();
						lhmc=cardBindouble.getLhmc();
						payRequest.setLhh(lhh); //设置总行联行号
						payRequest.setLhmc(lhmc); //设置总行联行名称
						log.info("卡号："+cardNo+" lhh "+lhh+" lhmc "+lhmc);
						break;
					}else{
						lhh=cardBindouble.getLhh();
						lhmc=cardBindouble.getLhmc();
						payRequest.setLhh(lhh); //设置总行联行号
						payRequest.setLhmc(lhmc); //设置总行联行名称
						log.info("卡号："+cardNo+" lhh "+lhh+" lhmc "+lhmc);
					}
				}
			}else{
				log.info("卡号："+cardNo+" 未找到对应的卡bin信息");
			}
		}
		FeeParam feeparam =  feeParamService.findBy(FeeParamCode.PUSERCHASE);
		payRequest.setPurchase_cost(feeparam.getFee()+"");
		FeeParam feeparam2 =  feeParamService.findBy(FeeParamCode.RECASH);
		payRequest.setRe_cash(feeparam2.getFee()+"");
    	return payRequest;
	}
	/**
	 * 借记卡提现请求参数
	 * @param user
	 * @param transAmount
	 * @param card
	 * @param payType
	 * @return
	 */
	public PayRequest getDebitCarRecashRequest(String orderNo,APPUser user, BigDecimal transAmount, String cardNo) {

		PayRequest payRequest=getBaseRequest(user,transAmount,cardNo);
		payRequest.setOrderNo(orderNo);
		payRequest.setPhoneNo(user.getMobilephone());
		return payRequest;
	}
	public PayRequest getDebitCarRecashRequest(String orderNo, Org org, BigDecimal transAmount, String cardNo) {

		PayRequest payRequest=new PayRequest();
		String date=DateTimeUtil.getNowDateTime("yyyyMMdd");
    	payRequest.setOrderDate(date);
		String orderTime=DateTimeUtil.getNowDateTime("HHmmss");
    	payRequest.setOrderTime(orderTime);
    	payRequest.setTransAmt( transAmount.multiply(new BigDecimal("100")).longValue());
		payRequest.setAccBankName(org.getBankName());   //储蓄卡的银行
    	payRequest.setRealName(org.getRealName());
    	payRequest.setCustomerName(org.getRealName());
    	payRequest.setCerdId(org.getIdNo());
    	payRequest.setCerdType("01");//证件类型为身份证
    	payRequest.setPhoneNo(org.getBindPhone());
    	payRequest.setOrderNo(orderNo);
    	payRequest.setAcctNo(cardNo);
		String cardBin="";
		String lhh="";
		String lhmc="";
		if(cardNo.length()>=6){
			cardBin=cardNo.substring(0,6);
/*			Map<String, Object> cardBinMap =jdbcTemplate.queryForMap("select * from t_card_bin where cardBin like '"+cardBin+"%' and  lhh is  not null and  lhmc is  not null ") ;
			if (cardBinMap!=null){
				lhh=(String)cardBinMap.get("LHH");
				lhmc=(String)cardBinMap.get("LHMC");
				payRequest.setLhh(lhh); //设置总行联行号
				payRequest.setLhmc(lhmc); //设置总行联行名称
			}*/
			String sql="select * from t_card_bin where cardBin like :cardBin and  lhh is  not null and  lhmc is  not null ";
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("cardBin", cardBin+"%");
			List<CardBin> cardbins = cardBinDao.find(sql, map);
			if(cardbins!=null && cardbins.size()==1){
				CardBin cardBinObj = cardbins.get(0);
				lhh=cardBinObj.getLhh();
				lhmc=cardBinObj.getLhmc();
				payRequest.setLhh(lhh); //设置总行联行号
				payRequest.setLhmc(lhmc); //设置总行联行名称
			}else if(cardbins!=null && cardbins.size()>1){
				for (CardBin cardBindouble:cardbins) {
					if(cardNo.length()==18 && cardBindouble!=null && cardBindouble.getCardName().contains("工商")){
						lhh=cardBindouble.getLhh();
						lhmc=cardBindouble.getLhmc();
						payRequest.setLhh(lhh); //设置总行联行号
						payRequest.setLhmc(lhmc); //设置总行联行名称
						log.info("卡号："+cardNo+" lhh "+lhh+" lhmc "+lhmc);
						break;
					}else{
						lhh=cardBindouble.getLhh();
						lhmc=cardBindouble.getLhmc();
						payRequest.setLhh(lhh); //设置总行联行号
						payRequest.setLhmc(lhmc); //设置总行联行名称
						log.info("卡号："+cardNo+" lhh "+lhh+" lhmc "+lhmc);
					}
				}
			}else{
				log.info("卡号："+cardNo+" 未找到对应的卡bin信息");
			}
		}
		FeeParam feeparam =  feeParamService.findBy(FeeParamCode.PUSERCHASE);
		payRequest.setPurchase_cost(feeparam.getFee()+"");
		FeeParam feeparam2 =  feeParamService.findBy(FeeParamCode.RECASH);
		payRequest.setRe_cash(feeparam2.getFee()+"");
		return payRequest;
	}

}
