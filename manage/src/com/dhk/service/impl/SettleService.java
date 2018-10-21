package com.dhk.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhk.dao.ISettleDao;
import com.dhk.entity.FeeParam;
import com.dhk.entity.OrgProfit;
import com.dhk.entity.OrgRate;
import com.dhk.entity.Settle;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.service.IFeeParamService;
import com.dhk.service.IOrgProfitService;
import com.dhk.service.IOrgRateService;
import com.dhk.service.IOrgService;
import com.dhk.service.ISettleService;
import com.dhk.service.ITransWaterService;
import com.sunnada.kernel.sql.SQLConf;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("SettleService")
public class SettleService implements ISettleService {

	@Resource(name = "SettleDao") 
	private ISettleDao settleDao;
	
	@Resource(name = "OrgRateService")
	private IOrgRateService orgRateService;
	
	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;
	
	@Resource(name = "OrgProfitService")
	private IOrgProfitService orgProfitService;
	@Resource(name = "OrgService")
	private IOrgService orgService;
	@Resource(name="FeeParamService")
	private IFeeParamService feeParamService;
	@Autowired
	JedisPool jedisPool;

	private static final Logger log= LogManager.getLogger();

	
	public long doInsertSettle(Settle settle) {
		String sql= SQLConf.getSql("settle", "insert");

		return settleDao.insert(sql,settle);
		 
	}

	public Settle findByOrgIdAndCurDate(long orgId) {
		String sql= SQLConf.getSql("settle", "findByOrgIdAndCurDate");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("orgId", orgId);
		try{
			return settleDao.findBy(sql,map);
		}catch (Exception e) {
			
			return null;
		}
	}

	public boolean doUpdateSettle(Settle settle) {
		String sql= SQLConf.getSql("settle", "update");
		try{
			if(settleDao.updateBy(sql,settle)>0){
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean doUpdateSettle(BigDecimal balance ,int count,long id) {
		String sql= "update t_s_settle set BALANCE=BALANCE+:balance,TRANS_COUNT=TRANS_COUNT+:transCount where id=:id";
		Map paramMap = new HashMap();
		paramMap.put("balance",balance.doubleValue());
		paramMap.put("transCount",count);
		paramMap.put("id",id);
		try{
			if(settleDao.update(sql,paramMap)>0){
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	
	public void txOperatoerSett(BigDecimal amount,Long transId) throws Exception{
		//更新到运营商账户
		orgService.seriUpdateBalance(1L, amount);
		
		//将交易流水的运营商结算状态改为T
		transWaterService.updateIsBus(transId, "T");
	}

	/**
	 * 结算
	 */
	public void doSettle(List<TransWater> transWaters) throws Exception{
		if(transWaters!=null && !transWaters.isEmpty()){
			int size=transWaters.size();
			for(int i=0;i<size;i++){
				TransWater tw=transWaters.get(i);
				Long qrcodeId=tw.getQrcodeId();
				if(qrcodeId!=null){
					if("1".equals(tw.getTrans_type())){
					txSettle(tw.getId(),qrcodeId,tw.getRelationNo(),tw.getTransAmount(),tw);
					}else if("0".equals(tw.getTrans_type())) {
				    txSettleOnlyCost(tw.getId(),qrcodeId,tw.getRelationNo(),tw.getTransAmount(),tw);
					}
					}
			}
		}
	}
	
	/**
	 * 结算Test
	 */
	public void doSettleTest(List<TransWater> transWaters) throws Exception{
	
	}


	
	private void txSettle(long transId,long qrCodeId,String relatinNo,BigDecimal transAmount,TransWater tw) throws Exception{
		List<OrgRate> orgRates = orgRateService.findByQrcodeId(qrCodeId);
		if(orgRates.isEmpty()){
			log.error("没有找到二维码ID对应的代理商费率信息:"+transId);
			return;
		}

		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
            List<OrgProfit> orgProfits = new ArrayList<>();
            //消费用户 ~ 对于代理商信息
            OrgProfit orgProfit0 = new OrgProfit();
            orgProfit0.setUserOrgName(tw.getUserName());
            orgProfit0.setUserOrgId(tw.getUserId()+"");
            orgProfit0.setUserType(1);
            orgProfit0.setAddTime(new Date());
            orgProfit0.setCardNo(tw.getCardNo());
            orgProfit0.setOrderNo(tw.getTransNo());
            orgProfit0.setTransAmount(tw.getTransAmount());
            orgProfit0.setTransType(tw.getTrans_type());
            orgProfit0.setTransDate(tw.getTransDate());
            orgProfit0.setTransTime(tw.getTransTime());
            String lastOrgName =""; 
            String lastOrgId =""; 
            String lastRelationNo = "";
            String lastChannelId = "";
            Map<String,String> orgParentProfitMap = new HashMap<>();//存放父级费率
			for (OrgRate orgRate : orgRates) {
				//代理商~各级代理商信息
				OrgProfit orgProfit = new OrgProfit();
				orgProfit.setAddTime(new Date());
				orgProfit.setCardNo(tw.getCardNo());
				orgProfit.setChannelId(orgRate.getChannelId());
				orgProfit.setOrderNo(tw.getTransNo());
				orgProfit.setTransAmount(tw.getTransAmount());
				orgProfit.setTransType(tw.getTrans_type());
				orgProfit.setTransDate(tw.getTransDate());
				orgProfit.setTransTime(tw.getTransTime());
				orgProfit.setUserOrgName(orgRate.getOrg_name());
				orgProfit.setUserOrgId(orgRate.getOrgId()+"");
				orgProfit.setParentUserOrgName(orgRate.getParent_org_name());
				orgProfit.setParentUserOrgId(orgRate.getParent_org_id()+"");
				orgProfit.setRelationNo(orgRate.getRelation_no());
				orgProfit.setUserType(2);
				long orgId = orgRate.getOrgId();
				// 计算利润
				BigDecimal profit;
				if (transAmount != null) {
					if(orgRate.getOrgId()==orgRate.getQrcode_org_id()){    //最后一个代理商 ，他没有下级
						FeeParam feeInfo=feeParamService.findBy("purchase");
						BigDecimal bigDecimal=feeInfo.getFee();
						profit = transAmount.multiply(
								(bigDecimal.subtract(orgRate.getRate()).divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						
						lastOrgName=orgRate.getOrg_name();
						lastOrgId=orgRate.getOrgId()+"";
						lastRelationNo = orgRate.getRelation_no();
						lastChannelId = orgRate.getChannelId();
						orgProfit.setFee( bigDecimal+"%~"+orgRate.getRate()+"%");
					}else{
						profit = transAmount.multiply(
								(orgRate.getDiffRate().divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						orgProfit.setFee( orgRate.getRate().add(orgRate.getDiffRate())+"%~"+orgRate.getRate()+"%");
					}
					orgProfit.setProfit(profit);
					//平台商利润
					if("1".equals(orgRate.getParent_org_id())){
						FeeParam feeInfo=feeParamService.findBy("purchase_cost");
						BigDecimal purchase_cost=feeInfo.getFee();
						BigDecimal profitpt = transAmount.multiply(
								(orgRate.getRate().subtract(purchase_cost).divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						//一级代理商~平台商信息
			        	OrgProfit orgProfitpt = new OrgProfit();//平台商
						orgProfitpt.setAddTime(new Date());
						orgProfitpt.setCardNo(tw.getCardNo());
						orgProfitpt.setChannelId(orgRate.getChannelId());
						orgProfitpt.setOrderNo(tw.getTransNo());
						orgProfitpt.setTransAmount(tw.getTransAmount());
						orgProfitpt.setTransType(tw.getTrans_type());
						orgProfitpt.setTransDate(tw.getTransDate());
						orgProfitpt.setTransTime(tw.getTransTime());
						orgProfitpt.setUserOrgName(orgRate.getParent_org_name());
						orgProfitpt.setUserOrgId(orgRate.getParent_org_id()+"");
						orgProfitpt.setParentUserOrgName(orgRate.getParent_org_name());
						orgProfitpt.setParentUserOrgId(orgRate.getParent_org_id()+"");
						orgProfitpt.setFee(orgRate.getRate()+"%~"+purchase_cost+"%");
						orgProfitpt.setRelationNo("1.");
						orgProfitpt.setUserType(2);
						orgProfitpt.setProfit(profitpt);
//						orgProfits.add(orgProfitpt);
						orgParentProfitMap.put(orgProfitpt.getUserOrgId(), orgProfitpt.getFee()+"#"+orgProfitpt.getProfit());//放入map用于取出父级费率
					}
					
				} else {
					profit = new BigDecimal("0");
				}
				orgParentProfitMap.put(orgProfit.getUserOrgId(), orgProfit.getFee()+"#"+orgProfit.getProfit());//放入map
				orgProfits.add(orgProfit);
				jedis.hincrByFloat("settleMap",orgId+"",profit.doubleValue()<0?0:profit.doubleValue());
				jedis.hincrBy("settleCountMap",orgId+"",1);
			}
            orgProfit0.setParentUserOrgName(lastOrgName);
            orgProfit0.setParentUserOrgId(lastOrgId);
            orgProfit0.setProfit(new BigDecimal("0.00"));
            orgProfit0.setRelationNo(lastRelationNo); 
            orgProfit0.setChannelId(lastChannelId);
            orgProfits.add(orgProfit0);
            //循环替换费率为父级费率
          for (OrgProfit orgProfitlist:orgProfits) {
			if(orgParentProfitMap.get(orgProfitlist.getParentUserOrgId())!=null){
				String feeAndProfit = orgParentProfitMap.get(orgProfitlist.getParentUserOrgId());
				orgProfitlist.setFee(feeAndProfit.split("#")[0]);
				orgProfitlist.setProfit(new BigDecimal(feeAndProfit.split("#")[1]));
			}
	      }
			orgProfitService.batchInsert(orgProfits);
			transWaterService.updateSettle(transId);
		}catch (Exception e){
			e.printStackTrace();
			log.error("transId:"+transId+"|"+e.getMessage());
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
	}
	
	
	private void txSettleOnlyCost(long transId,long qrCodeId,String relatinNo,BigDecimal transAmount,TransWater tw) throws Exception{
		log.error("供应商结算:"+transId+"-"+qrCodeId+"-"+relatinNo+"-"+transAmount);
		List<OrgRate> orgRates = orgRateService.findByQrcodeId(qrCodeId);
		if(orgRates.isEmpty()){
			log.error("没有找到二维码ID对应的代理商费率信息:"+transId);
			return;
		}

		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
            List<OrgProfit> orgProfits = new ArrayList<>();
            //消费用户 ~ 对于代理商信息
            OrgProfit orgProfit0 = new OrgProfit();
            orgProfit0.setUserOrgName(tw.getUserName());
            orgProfit0.setUserOrgId(tw.getUserId()+"");
            orgProfit0.setUserType(1);
            orgProfit0.setAddTime(new Date());
            orgProfit0.setCardNo(tw.getCardNo());
            orgProfit0.setOrderNo(tw.getTransNo());
            orgProfit0.setTransAmount(tw.getTransAmount());
            orgProfit0.setTransType(tw.getTrans_type());
            orgProfit0.setTransDate(tw.getTransDate());
            orgProfit0.setTransTime(tw.getTransTime());
            String lastOrgName =""; 
            String lastOrgId =""; 
            String lastRelationNo = "";
            String lastChannelId = "";
            Map<String,String> orgParentProfitMap = new HashMap<>();//存放父级费率
			for (OrgRate orgRate : orgRates) {
				//代理商~各级代理商信息
				OrgProfit orgProfit = new OrgProfit();
				orgProfit.setAddTime(new Date());
				orgProfit.setCardNo(tw.getCardNo());
				orgProfit.setChannelId(orgRate.getChannelId());
				orgProfit.setOrderNo(tw.getTransNo());
				orgProfit.setTransAmount(tw.getTransAmount());
				orgProfit.setTransType(tw.getTrans_type());
				orgProfit.setTransDate(tw.getTransDate());
				orgProfit.setTransTime(tw.getTransTime());
				orgProfit.setUserOrgName(orgRate.getOrg_name());
				orgProfit.setUserOrgId(orgRate.getOrgId()+"");
				orgProfit.setParentUserOrgName(orgRate.getParent_org_name());
				orgProfit.setParentUserOrgId(orgRate.getParent_org_id()+"");
				orgProfit.setRelationNo(orgRate.getRelation_no());
				orgProfit.setUserType(2);
				long orgId = orgRate.getOrgId();
				orgRate.getRelation_no();
				if(StringUtils.isBlank(orgRate.getRelation_no())){
					log.error("供应商层级异常:"+orgRate.getOrg_name());
					return;
				}
				String []relations = orgRate.getRelation_no().split("\\.");
				if(relations==null || relations.length<2){
					log.error("供应商层级信息异常:"+orgRate.getOrg_name());
					return;
				}
				// 计算利润
				BigDecimal profit;
				if (transAmount != null) {
					if(orgRate.getOrgId()==orgRate.getQrcode_org_id()){    //最后一个代理商 ，他没有下级
						//FeeParam feeInfo=feeParamService.findBy("purchase");
						BigDecimal rate = Constant.feeMaps.get("user_only_purchase_cost").getFee();//用户费率
						BigDecimal rateExternal = Constant.feeMaps.get("user_only_purchase_cost").getExternal();//用户额外费用
//						BigDecimal bigDecimal=rate.add(rateExternal);//feeInfo.getFee();
						log.info("最后一级代理商:"+orgRate.getOrg_name()+"-"+"org_only_purchase_cost_"+(relations.length-1));
						BigDecimal lastRate = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getFee();//最后一级代理商费率
						BigDecimal lastRateExternal = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getExternal();//最后一级代理商额外费用
//						BigDecimal lastOrgRate = lastRate.add(lastRateExternal);
						
						profit = transAmount.multiply(
								(rate.subtract(lastRate).divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						profit = profit.add(rateExternal.subtract(lastRateExternal));
						lastOrgName=orgRate.getOrg_name();
						lastOrgId=orgRate.getOrgId()+"";
						lastRelationNo = orgRate.getRelation_no();
						lastChannelId = orgRate.getChannelId();
						orgProfit.setFee( rate+"%+"+rateExternal+"~"+lastRate+"%+"+lastRateExternal);
					}else{
						log.info("代理商:"+orgRate.getOrg_name()+"-"+"org_only_purchase_cost_"+(relations.length-1));
						BigDecimal orgRateaRate = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getFee();//代理商费率
						BigDecimal orgRateaRateExternal = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getExternal();//代理商额外费用
//						BigDecimal orgRatea = orgRateaRate.add(orgRateaRateExternal);
						log.info("子代理商:"+orgRate.getOrg_name()+"-"+"org_only_purchase_cost_"+(relations.length));
						BigDecimal childOrgRateRate = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length)).getFee();//子级代理商费率
						BigDecimal childOrgRateRateExternal = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length)).getExternal();//子级代理商额外费用
//						BigDecimal childOrgRate =childOrgRateRate.add(childOrgRateRateExternal);
						
						BigDecimal diffRate = childOrgRateRate.subtract(orgRateaRate);
						profit = transAmount.multiply(
								(diffRate.divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						
						profit = profit.add(childOrgRateRateExternal.subtract(orgRateaRateExternal));
						
						orgProfit.setFee(childOrgRateRate+"%+"+childOrgRateRateExternal+"~"+orgRateaRate+"%+"+orgRateaRateExternal);
					}
					orgProfit.setProfit(profit);
					//平台商利润
					if("1".equals(orgRate.getParent_org_id())){
				/*		FeeParam feeInfo=feeParamService.findBy("purchase_cost");
						BigDecimal purchase_cost=feeInfo.getFee();*/
						log.info("一级代理商代理商:"+orgRate.getOrg_name()+"-"+"org_only_purchase_cost_"+(relations.length));
						BigDecimal orgRateaRate = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getFee();//一级代理商费率
						BigDecimal orgRateaRateExternal = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-1)).getExternal();//一级代理商额外费用
//						BigDecimal orgRatea = orgRateaRate.add(orgRateaRateExternal);
						log.info("平台商代理商:"+orgRate.getOrg_name()+"-"+"org_only_purchase_cost_"+(relations.length-2));
						BigDecimal parentOrgRateRate = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-2)).getFee();//平台商费率
						BigDecimal parentOrgRateRateExternal = Constant.feeMaps.get("org_only_purchase_cost_"+(relations.length-2)).getExternal();//平台商额外费用
//						BigDecimal parentOrgRate = parentOrgRateRate.add(parentOrgRateRateExternal);
						BigDecimal profitpt = transAmount.multiply(
								(orgRateaRate.subtract(parentOrgRateRate).divide(new BigDecimal("100")))
						).setScale(2, RoundingMode.HALF_UP);
						profitpt = profitpt.add(orgRateaRateExternal.subtract(parentOrgRateRateExternal));
						//一级代理商~平台商信息
			        	OrgProfit orgProfitpt = new OrgProfit();//平台商
						orgProfitpt.setAddTime(new Date());
						orgProfitpt.setCardNo(tw.getCardNo());
						orgProfitpt.setChannelId(orgRate.getChannelId());
						orgProfitpt.setOrderNo(tw.getTransNo());
						orgProfitpt.setTransAmount(tw.getTransAmount());
						orgProfitpt.setTransType(tw.getTrans_type());
						orgProfitpt.setTransDate(tw.getTransDate());
						orgProfitpt.setTransTime(tw.getTransTime());
						orgProfitpt.setUserOrgName(orgRate.getParent_org_name());
						orgProfitpt.setUserOrgId(orgRate.getParent_org_id()+"");
						orgProfitpt.setParentUserOrgName(orgRate.getParent_org_name());
						orgProfitpt.setParentUserOrgId(orgRate.getParent_org_id()+"");
						orgProfitpt.setFee(parentOrgRateRate+"%+"+parentOrgRateRateExternal+"~"+orgRateaRate+"%+"+orgRateaRateExternal);
						orgProfitpt.setRelationNo("1.");
						orgProfitpt.setUserType(2);
						orgProfitpt.setProfit(profitpt);
//						orgProfits.add(orgProfitpt);
						orgParentProfitMap.put(orgProfitpt.getUserOrgId(), orgProfitpt.getFee()+"#"+orgProfitpt.getProfit());//放入map用于取出父级费率
					}
					
				} else {
					profit = new BigDecimal("0");
				}
				orgParentProfitMap.put(orgProfit.getUserOrgId(), orgProfit.getFee()+"#"+orgProfit.getProfit());//放入map
				orgProfits.add(orgProfit);
				log.info("供应商:"+orgRate.getOrg_name()+" "+tw.getTransNo()+" "+tw.getTransAmount()+" "+profit);
				jedis.hincrByFloat("settleMap",orgId+"",profit.doubleValue()<0?0:profit.doubleValue());
				jedis.hincrBy("settleCountMap",orgId+"",1);
			}
            orgProfit0.setParentUserOrgName(lastOrgName);
            orgProfit0.setParentUserOrgId(lastOrgId);
            orgProfit0.setProfit(new BigDecimal("0.00"));
            orgProfit0.setRelationNo(lastRelationNo); 
            orgProfit0.setChannelId(lastChannelId);
            orgProfits.add(orgProfit0);
            //循环替换费率为父级费率
          for (OrgProfit orgProfitlist:orgProfits) {
			if(orgParentProfitMap.get(orgProfitlist.getParentUserOrgId())!=null){
				String feeAndProfit = orgParentProfitMap.get(orgProfitlist.getParentUserOrgId());
				orgProfitlist.setFee(feeAndProfit.split("#")[0]);
				orgProfitlist.setProfit(new BigDecimal(feeAndProfit.split("#")[1]));
			}
	      }
			orgProfitService.batchInsert(orgProfits);
			transWaterService.updateSettle(transId);
		}catch (Exception e){
			e.printStackTrace();
			log.error("transId:"+transId+"|"+e.getMessage());
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}
	}
	
	public static void main(String arg[]){
		System.out.println("1.".split("\\.").length);
	}
}

