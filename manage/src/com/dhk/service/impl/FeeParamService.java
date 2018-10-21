package com.dhk.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dhk.FeeInfo;
import com.dhk.dao.IFeeParamDao;
import com.dhk.entity.FeeParam;
import com.dhk.service.IFeeParamService;
import com.sunnada.kernel.sql.SQLConf;
@Service("FeeParamService")
public class FeeParamService implements IFeeParamService {
	@Resource(name = "FeeParamDao") 
	private IFeeParamDao feeParamDao;


	@Cacheable(value="t_param_fee", key="'findBy_'+#code")
	public FeeParam findBy(String code) {
		String sql=SQLConf.getSql("feeparam", "findByCode");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", code);
		try {
			return feeParamDao.findBy(sql, map);
		} catch (Exception e) {
			return null;
		}
	}

	@Cacheable(value="t_param_fee", key="findAll")
	public List<FeeParam> findAll() {
		String sql=SQLConf.getSql("feeparam", "findAll");
		try {
			return feeParamDao.find(sql, null);
		} catch (Exception e) {
			return null;
		}
	}



	/**
	 * 计算消费金额所需手续费信息
	 * @param amount 消费金额
	 * @return
	 */
	public  FeeInfo computeFeeInfo(BigDecimal amount,String feeCode,String costCode) {
			FeeInfo feeInfo=new FeeInfo();
			FeeParam pParam=findBy(feeCode);//终端费率信息
		    System.out.println("feeCode:"+feeCode);
		    System.out.println("pParam:"+pParam);
		    System.out.println("pParam:"+ JSONObject.toJSONString(feeInfo));
			String feeType = pParam.getFeeType();
			BigDecimal feeRate=pParam.getFee().setScale(2, RoundingMode.HALF_UP);
			BigDecimal fee;
			if ( "0".equals(feeType) ){
				//按费率 计算手续费
				fee= amount.multiply(feeRate).divide(new BigDecimal(100));
				fee=getFee(pParam.getLowerlimit(),pParam.getUplimit(), fee);
				feeInfo.setFee(fee.setScale(2, RoundingMode.HALF_UP));
			}else {
				//按笔算手续费
				feeInfo.setFee(feeRate);
			}
			feeInfo.setExternal(pParam.getExternal().setScale(2, RoundingMode.HALF_UP));
			FeeParam pcParam=findBy(costCode);//平台商成本
			String feeTypeCost = pcParam.getFeeType();
			BigDecimal  feeCost = pcParam.getFee().setScale(2, RoundingMode.HALF_UP);
			//计算运营商成本
			BigDecimal cost;

			if ("0".equals(feeTypeCost) ){
				//按百分比
				cost= amount.multiply(feeCost).divide(new BigDecimal(100));
				cost=getFee(pcParam.getLowerlimit(),pcParam.getUplimit(), cost);
				cost = cost.add(pcParam.getExternal()).setScale(2, RoundingMode.HALF_UP);
			}else{
				//按笔
				cost=feeCost.add(pcParam.getExternal()).setScale(2, RoundingMode.HALF_UP);
			}

			feeInfo.setCost(cost.setScale(2, RoundingMode.HALF_UP));
			return feeInfo;
	}
	
	private BigDecimal getFee(BigDecimal lower,BigDecimal upper, BigDecimal fee){
		if(lower.compareTo(new BigDecimal("0"))>0&&lower.compareTo(fee)>=0){
			fee=lower;
		}
		if(upper.compareTo(new BigDecimal("0"))>0&&upper.compareTo(fee)<=0){
			fee=upper;
		}
		return fee;
	}

}

