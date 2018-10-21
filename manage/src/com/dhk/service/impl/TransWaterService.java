package com.dhk.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.entity.Org;
import com.dhk.payment.PayResult;
import org.springframework.stereotype.Service;

import com.dhk.dao.ITransWaterDao;
import com.dhk.entity.APPUser;
import com.dhk.entity.TransWater;
import com.dhk.service.ITransWaterService;
import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.sql.SQLConf;
@Service("TransWaterService")
public class TransWaterService implements ITransWaterService {
	@Resource(name = "TransWaterDao") 
	private ITransWaterDao transWaterDao;

	public long doInsert(TransWater tw) {
	
		String sql=SQLConf.getSql("transwater", "insert");
		return transWaterDao.insert(sql, tw);
	}
	public TransWater findById(long id) {
		String sql=SQLConf.getSql("transwater", "findById");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		try {
			return transWaterDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
	}

	public TransWater findByOrderId(long id) {
		String sql=SQLConf.getSql("transwater", "findById");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		try {
			return transWaterDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
	}

	public int modifyTransls(TransWater tw){
		String sql = SQLConf.getSql("transwater", "modifyTransls");
		return transWaterDao.deleteBy(sql, tw);
	}
	/*------------------------------20170306-------------------------------*/

	public void  modifyTransls(Long translsId,String transNo,String proxyPayType,String respCode,String respMsg,String transDate,String transTime,String transactionType){
		TransWater tw = new TransWater();
		tw.setId(translsId);
		tw.setTransNo(transNo);
		tw.setProxyPayType(proxyPayType);
		tw.setRespCode(respCode);
		tw.setRespRes(respMsg);
		tw.setTransDate(transDate);
		tw.setTransTime(transTime);
		tw.setHostTransDate(transDate);
		tw.setHostTransTime(transTime);
		tw.setHostTransNo(transNo);
		tw.setTransactionType(transactionType);

		
		modifyTransls(tw);
		
	}



	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType){
		TransWater tw = new TransWater();
		tw.setRespCode("init");
		tw.setRespRes("等待交易结果");
		tw.setCardNo(cardNo);
		tw.setOrgId(user.getOrgId());
		tw.setUserId(user.getId());
		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));
		tw.setQrcodeId(user.getQrcodeId());
		tw.setRelationNo(user.getRelationNo());
		tw.setIsOrgRecah("0");
		tw.setTrans_type(transType);//快速还款
		tw.setCardType("1");//信用卡
		tw.setPlanId(planId);
		tw.setCostId(costId);
		tw.setProxyPayChannel("1");
		tw.setProxyPayType("");
		tw.setTransNo(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHostTransNo(transNo);
		tw.setHostTransDate(date);
		tw.setHostTransTime(time);
		tw.setTransDate(date);
		tw.setTransTime(time);
		
		return this.doInsert(tw);
	}

	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
						   BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType,String respCode,String respRes){
		TransWater tw = new TransWater();
		tw.setRespCode(respCode);
		tw.setRespRes(respRes);
		tw.setCardNo(cardNo);
		tw.setOrgId(user.getOrgId());
		tw.setUserId(user.getId());
		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));
		tw.setQrcodeId(user.getQrcodeId());
		tw.setRelationNo(user.getRelationNo());
		tw.setIsOrgRecah("0");
		tw.setTrans_type(transType);//快速还款
		tw.setCardType("1");//信用卡
		tw.setPlanId(planId);
		tw.setCostId(costId);
		tw.setProxyPayChannel("1");
		tw.setProxyPayType("");
		tw.setTransNo(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHostTransNo(transNo);
		tw.setHostTransDate(date);
		tw.setHostTransTime(time);
		tw.setTransDate(date);
		tw.setTransTime(time);

		return this.doInsert(tw);
	}

	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
						   BigDecimal fee,BigDecimal external,Long planId,String transType,String cardType,String isOrgRecash){

		TransWater tw = new TransWater();

		tw.setRespCode("init");
		tw.setRespRes("初始化流水");
		tw.setCardNo(cardNo);
		tw.setOrgId(user.getOrgId());
		tw.setUserId(user.getId());
		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));
		tw.setQrcodeId(user.getQrcodeId());
		tw.setRelationNo(user.getRelationNo());
		tw.setIsOrgRecah(isOrgRecash);//default 0

		tw.setTrans_type(transType);//快速还款
		tw.setCardType(cardType);//1 信用卡
		tw.setPlanId(planId);
		tw.setProxyPayChannel("1");
		tw.setProxyPayType("");
		tw.setTransNo(transNo);
//		String date = transNo.substring(0, 8);
//		String time = transNo.substring(8, 14);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHostTransNo(transNo);
		tw.setHostTransDate(date);
		tw.setHostTransTime(time);
		tw.setTransDate(date);
		tw.setTransTime(time);

		return this.doInsert(tw);
	}

	private  TransWater getBaseTransWater(APPUser user,String cardNo,BigDecimal amount,
										  BigDecimal fee,BigDecimal external,PayResult pr){
		if(pr==null){
			throw new NullPointerException("PayResult is null");
		}
		TransWater tw = new TransWater();
		tw.setRespCode(pr.getCode());
		tw.setRespRes(pr.getMessage());
		tw.setCardNo(cardNo);
		tw.setOrgId(user.getOrgId());
		tw.setUserId(user.getId());
		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));
		tw.setQrcodeId(user.getQrcodeId());
		tw.setRelationNo(user.getRelationNo());
		tw.setIsOrgRecah("0");
		return tw;
	}

	/**
	 * 查询未结算且时间在当前时间之前的
	 * @param isBusiness
	 * @param transDate
	 * @return
	 * @throws Exception
	 */
	public List<TransWater> findByIsBusAndDate(String isBusiness,
											   String transDate) throws Exception {
		String sql=SQLConf.getSql("transwater", "findByIsBusAndDate");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("isBusiness", isBusiness);
		map.put("transDate", transDate);
		return transWaterDao.find(sql,map);
	}

	/**
	 * 更新流水状态为运营商已结算
	 * @param id
	 * @param isBusiness
	 * @throws Exception
	 */
	public void updateIsBus(Long id, String isBusiness) throws Exception {
		// TODO Auto-generated method stub
		String sql = SQLConf.getSql("transwater", "updateIsBus");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("isBusiness", isBusiness);
		transWaterDao.update(sql,map);
	}

	public List<TransWater> findUnSettedTransWaters(){
		String sql=SQLConf.getSql("transwater", "findUnSettedTransWaters");

		return transWaterDao.find(sql,null);

	}

	public boolean updateSettle(Long id){
		String sql = SQLConf.getSql("transwater", "updateSettle");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		try {

			if(transWaterDao.update(sql,map)>0){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void  writeFastRepayTransWater(String transNo,APPUser user,String cardNo,BigDecimal amount,
										  BigDecimal fee,BigDecimal external,Long planId,String proxyPayType,
										  String proxyPayChannel,PayResult pr){
		TransWater tw=getBaseTransWater(user, cardNo, amount, fee, external, pr);
		tw.setTrans_type("2");//快速还款
		tw.setCardType("1");//信用卡
		tw.setPlanId(planId);
		tw.setProxyPayChannel(proxyPayChannel);
		tw.setProxyPayType(proxyPayType);
		tw.setTransNo(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");

		tw.setTransDate(date);
		tw.setTransTime(time);
		doInsert(tw);

	}

	public long writeRechageTransWater(String transNo,APPUser user, String debiteCardNo, BigDecimal amount, BigDecimal fee,
									   BigDecimal external, PayResult pr) {
		TransWater tw=getBaseTransWater(user, debiteCardNo, amount, fee, external, pr);
		tw.setTrans_type("4");//提现
		tw.setCardType("0");//借记卡
		tw.setProxyPayChannel("0");
		tw.setTransNo(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");

		tw.setTransDate(date);
		tw.setTransTime(time);
		tw.setTransactionType(pr.getTransactionType());
		return doInsert(tw);

	}

	public void  writeRecashTransWater(String transNo,APPUser user,String cardNo,BigDecimal amount,
									   BigDecimal fee,BigDecimal external,
									   String proxyPayChannel,String proxyPayType,PayResult pr){
		TransWater tw=getBaseTransWater(user, cardNo, amount, fee, external, pr);
		tw.setTrans_type("5");//提现
		tw.setCardType("0");//借记卡

		tw.setProxyPayChannel(proxyPayChannel);
		tw.setProxyPayType(proxyPayType);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");

		tw.setTransDate(date);
		tw.setTransTime(time);
		tw.setTransNo(transNo);
		doInsert(tw);
	}
	public void  writeRecashTransWater(String transNo,Org org,String cardNo,BigDecimal amount,
									   BigDecimal fee,BigDecimal external,
									   String proxyPayChannel,String proxyPayType,PayResult pr){
		if(pr==null){
			throw new NullPointerException("PayResult is null");
		}
		TransWater tw = new TransWater();
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");

		tw.setRespCode(pr.getCode());
		tw.setRespRes(pr.getMessage());
		tw.setCardNo(cardNo);
		tw.setOrgId(org.getId());

		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));

		tw.setRelationNo(org.getOrgRelationNo());
		tw.setTrans_type("5");//提现
		tw.setCardType("0");//借记卡

		tw.setProxyPayChannel(proxyPayChannel);
		tw.setProxyPayType(proxyPayType);
//			String date = transNo.substring(0, 8);
//			String time = transNo.substring(8, 14);
		tw.setTransDate(date);
		tw.setTransTime(time);
		tw.setTransNo(transNo);
		tw.setIsOrgRecah("1");
		doInsert(tw);
	}

	public long addTransls(String transNo, Org org, String cardNo, BigDecimal amount,
						   BigDecimal fee, BigDecimal external, Long planId, String transType, String cardType){

		TransWater tw = new TransWater();

		tw.setRespCode("init");
		tw.setRespRes("等待交易结果");
		tw.setCardNo(cardNo);
		tw.setOrgId(org.getId());
		tw.setUserId(null);
		tw.setTransAmount(amount.setScale(2, RoundingMode.HALF_UP));
		tw.setFee(fee);
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP));
		tw.setQrcodeId(null);
		tw.setRelationNo(org.getOrgRelationNo());
		tw.setIsOrgRecah("1");//default 0

		tw.setTrans_type(transType);//快速还款
		tw.setCardType(cardType);//1 信用卡
		tw.setPlanId(planId);
		tw.setProxyPayChannel("1");
		tw.setProxyPayType("");
		tw.setTransNo(transNo);
//		String date = transNo.substring(0, 8);
//		String time = transNo.substring(8, 14);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHostTransNo(transNo);
		tw.setHostTransDate(date);
		tw.setHostTransTime(time);
		tw.setTransDate(date);
		tw.setTransTime(time);

		return this.doInsert(tw);
	}
	@Override
	public TransWater findByTransNo(String transNo) {
		String sql=SQLConf.getSql("transwater", "findByTransNo");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", transNo);
		try {
			return transWaterDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
	}
}

