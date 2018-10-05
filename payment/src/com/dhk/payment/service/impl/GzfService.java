package com.dhk.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhk.kernel.util.StringUtil;
import com.dhk.payment.config.GzfConfig;
import com.dhk.payment.service.IGzfService;
import com.dhk.payment.util.BaseUtil;
import com.dhk.payment.util.Util;
import com.dhk.payment.wpay.HttpClientUtil;
import com.dhk.payment.yilian.QuickPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("gzfService")
public class GzfService implements IGzfService {
	static Logger logger = Logger.getLogger(WoFuService.class);

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {



		//数据库查询到用户账户信息，这里这个过程没有数据库连接,先从请求从获取
		QuickPay quickPay = new QuickPay();
		
		boolean flag = true; //不调用接口模拟
		if(flag) {
			quickPay.setRespCode("0000");
			quickPay.setRespDesc("支付成功");
			return quickPay;
		}
		
		try {
			String url = GzfConfig.postUrl+"/fastPay";
			String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
			String commodityName = BaseUtil.objToStr(paramMap.get("commodityName"), null);
			commodityName="商品消费" ;
			String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);  //订单号
			String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);
			String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);   //账户名称
			String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);  //身份证号
			String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);  //手机号
			String expDate = BaseUtil.objToStr(paramMap.get("expDate"), null); //有效期
			String cvn2 = BaseUtil.objToStr(paramMap.get("cvn2"), null);

			SortedMap<String, String> map = new TreeMap<String, String>();
			map.put("merNo", GzfConfig.merno);
			map.put("orderDate", StringUtil.getCurrentDateTime("yyyyMMdd"));
			map.put("orderNo", orderNo);
			map.put("transAmt", transAmt);
			map.put("commodityName", commodityName);
			map.put("phoneNo", phoneNo);
			map.put("customerName", accountName);
			map.put("cerdType", "01");
			map.put("cerdId", cerdId);
			map.put("acctNo", cardNo);
			map.put("cvn2", cvn2);
			map.put("expDate", expDate);

			String sign = Util.getSign(map,GzfConfig.key);

			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("merNo", GzfConfig.merno));
			nvps.add(new BasicNameValuePair("orderDate",  StringUtil.getCurrentDateTime("yyyyMMdd")));
			nvps.add(new BasicNameValuePair("orderNo", orderNo));
			nvps.add(new BasicNameValuePair("transAmt", transAmt));
			nvps.add(new BasicNameValuePair("commodityName", commodityName));
			nvps.add(new BasicNameValuePair("phoneNo", phoneNo));
			nvps.add(new BasicNameValuePair("customerName", accountName));
			nvps.add(new BasicNameValuePair("cerdType", "01"));
			nvps.add(new BasicNameValuePair("cerdId", cerdId));
			nvps.add(new BasicNameValuePair("acctNo", cardNo));
			nvps.add(new BasicNameValuePair("cvn2", cvn2));
			nvps.add(new BasicNameValuePair("expDate", expDate));
			nvps.add(new BasicNameValuePair("sign", sign));
			logger.info("提交的报文为:"+nvps);
			JSONObject jsonRet = HttpClientUtil.sendForGzf(url, nvps);
			logger.info("返回的报文为:"+jsonRet);
			if(jsonRet==null){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
				return quickPay;
			}



			map.clear();
			addMap(map, "merNo",jsonRet.getString("merNo"));
			addMap(map, "orderDate",jsonRet.getString("orderDate"));
			orderNo = jsonRet.getString("orderNo");//商户订单号
			addMap(map, "orderNo",orderNo);
			transAmt = jsonRet.getString("transAmt");//交易金额
			addMap(map, "transAmt",transAmt);
			commodityName = jsonRet.getString("commodityName");//商品描述
			addMap(map, "commodityName",commodityName);
			phoneNo = jsonRet.getString("phoneNo");//手机号
			addMap(map, "phoneNo",phoneNo);
			addMap(map, "customerName",jsonRet.getString("customerName"));
			addMap(map, "cerdType",jsonRet.getString("cerdType"));
			addMap(map, "cerdId",jsonRet.getString("cerdId"));
			addMap(map, "acctNo",jsonRet.getString("acctNo"));
			addMap(map, "cvn2",jsonRet.getString("cvn2"));
			addMap(map, "expDate",jsonRet.getString("expDate"));
			String respCode = jsonRet.getString("respCode");//应答码  例如：0000 成功  可为空
			addMap(map, "respCode",respCode);
			String respType = jsonRet.getString("respType");//应答状态  S成功，E错误，R不确定（对于不确定的可以在三分钟后发起查询）
			addMap(map, "respType",respType);
			String respMsg = jsonRet.getString("respMsg");//应答描述   可为空
			addMap(map, "respMsg",respMsg);
			String signReturn = jsonRet.getString("sign"); //返回签名值

			if("E".equals(respType)){//返回e不去验证签名
				quickPay.setRespCode("fail");
				quickPay.setRespDesc(respMsg);
				return quickPay;
			}

			sign = Util.getSign(map,GzfConfig.key);
			if(!sign.equals(signReturn)){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("签名错误");
				return quickPay;
			}

			if("S".equals(respType)){
				quickPay.setRespCode("0000");
				quickPay.setRespDesc(respMsg);
			}else if("E".equals(respType)){
				quickPay.setRespCode("fail");
				quickPay.setRespDesc(respMsg);
			}else {
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
			}

		} catch (Exception e) {
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("系统异常:"+e.getMessage());
		}

		return quickPay;
	}

	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		QuickPay quickPay = new QuickPay();
		boolean flag = true; //不调用接口模拟
		if(flag) {
			quickPay.setRespCode("0000");
			quickPay.setRespDesc("交易成功");
			return quickPay;
		}
		
		try {
			String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);
			String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
			String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);
			String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);
			String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);
			String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);
			String isT1 = BaseUtil.objToStr(paramMap.get("isT1"), null);
			logger.info("proxyPay Star:accountName"+accountName+"|transAmt："+transAmt+"|cardNo："+cardNo+"|cerdId："+cerdId+"|orderNo："+orderNo);
			String cardBin="";
			String lhh="";
			String lhmc="";
			if(cardNo.length()>=6){
				cardBin=cardNo.substring(0,6);
				Map<String, Object> cardBinMap =jdbcTemplate.queryForMap("select * from t_card_bin where cardBin like '"+cardBin+"%' and  lhh is  not null and  lhmc is  not null ") ;
				if (cardBinMap!=null){
					lhh=(String)cardBinMap.get("LHH");
					lhmc=(String)cardBinMap.get("LHMC");
				}else{
					quickPay.setRespCode("fail");
					quickPay.setRespDesc("银行卡信息异常");
					return quickPay;
				}
			}
			SortedMap<String, String> map = new TreeMap();
			addMap(map, "merNo", GzfConfig.merno);
			addMap(map, "orderDate", StringUtil.getCurrentDateTime("yyyyMMdd"));
			addMap(map, "orderNo", orderNo);
			addMap(map, "transAmt", transAmt);
			addMap(map, "phoneNo", phoneNo);
			addMap(map, "customerName", accountName);
			addMap(map, "cerdId", cerdId);
			addMap(map, "acctNo", cardNo);
			addMap(map, "cerdType", "01");
			addMap(map, "accBankNo", lhh);
			addMap(map, "accBankName", lhmc);
			addMap(map, "note", "");
			addMap(map, "isT1", isT1);
			String sign = Util.getSign(map,GzfConfig.key); //签名的报文
			logger.info("加密后为:"+sign);
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("merNo",  GzfConfig.merno));
			nvps.add(new BasicNameValuePair("orderDate", StringUtil.getCurrentDateTime("yyyyMMdd")));
			nvps.add(new BasicNameValuePair("orderNo", orderNo));
			nvps.add(new BasicNameValuePair("transAmt", transAmt));
			nvps.add(new BasicNameValuePair("phoneNo", phoneNo));
			nvps.add(new BasicNameValuePair("customerName", accountName));
			nvps.add(new BasicNameValuePair("cerdId", cerdId));
			nvps.add(new BasicNameValuePair("acctNo", cardNo));
			nvps.add(new BasicNameValuePair("cerdType", "01"));
			nvps.add(new BasicNameValuePair("accBankNo", lhh));
			nvps.add(new BasicNameValuePair("accBankName", lhmc));
			nvps.add(new BasicNameValuePair("note", ""));
			nvps.add(new BasicNameValuePair("isT1", isT1));
			nvps.add(new BasicNameValuePair("sign", sign));
			logger.info("提交的报文为:"+nvps);
			JSONObject jsonRet = HttpClientUtil.sendForGzf(GzfConfig.postUrl+"/withdraw", nvps);
			logger.info("返回的报文为:"+jsonRet);
			if(jsonRet==null){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
				return quickPay;
			}
			map.clear();
			addMap(map, "merNo", jsonRet.getString("merNo"));
			addMap(map, "orderNo", jsonRet.getString("orderNo"));
			addMap(map, "orderDate", jsonRet.getString("orderDate"));
			addMap(map, "transAmt", jsonRet.getString("transAmt"));
			addMap(map, "orderTime", jsonRet.getString("orderTime"));
			addMap(map, "respCode", jsonRet.getString("respCode"));
			String respType = jsonRet.getString("respType");//应答状态  S成功，E错误，R不确定（对于不确定的可以在三分钟后发起查询）
			addMap(map, "respType", respType);
			String respMsg = jsonRet.getString("respMsg");//应答描述   可为空
			addMap(map, "respMsg", respMsg);
			String signReturn = jsonRet.getString("sign"); //返回签名值


			if("E".equals(respType)){//返回e不去验证签名
				quickPay.setRespCode("fail");
				quickPay.setRespDesc(respMsg);
				return quickPay;
			}
			sign = Util.getSign(map,GzfConfig.key);
			if(!sign.equals(signReturn)){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("签名错误");
				return quickPay;
			}
			
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");

		}catch (Exception e) {
			e.printStackTrace();
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("系统异常:"+e.getMessage());
		}

		return quickPay;
	}


	private void addMap(SortedMap<String, String> map,String key,String value){
		if(map!=null&& StringUtils.isNotEmpty(key)&&StringUtils.isNotEmpty(value))
			map.put(key, value);
	}
}

