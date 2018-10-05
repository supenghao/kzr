package com.dhk.payment.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.kernel.controller.BaseController;
import com.dhk.kernel.util.JsonUtil;
import com.dhk.kernel.util.ResponseUtil;
import com.dhk.payment.config.YblConfig;
import com.dhk.payment.dao.IParamFeeDao;
import com.dhk.payment.dao.IUserDao;
import com.dhk.payment.entity.ParamFee;
import com.dhk.payment.service.IHXPayService;
import com.dhk.payment.util.HttpRequest;
import com.dhk.payment.yilian.MD5Util;
import com.dhk.payment.yilian.QuickPay;

/**
 * 
	* @ClassName: YBLPayController 
	* @Description: TODO
	* @author ZZL
	* @date 2018年5月21日 上午10:57:00 
	*
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/yblpay")
public class YBLPayController extends BaseController{

	private static Logger logger = Logger.getLogger(YBLPayController.class);
	@Resource
	private IHXPayService hxpayService;
	@Resource
	private IUserDao userDao;
	@Resource(name = "ParamFeeDao")
	private IParamFeeDao paramFeeDao;
/*	*//**
	 * 
		* @Title: register 
		* @Description: 注册接口
		* @param @param request
		* @param @param response
		* @param @param registerParam
		* @param @throws Exception    设定文件 
		* @return void    返回类型 
		* @throws
	 *//*
	@ResponseBody
    @RequestMapping(value = "/register")
    public void register(HttpServletRequest request,HttpServletResponse response, @Validated HXRegisterParam registerParam) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	 String client_trans_id;//流水号
            	 String merchant_code;//商户号
            	 String merchant_name;//商户名称
            	 String merchant_province;//商户所在省份
            	 String merchant_city;//商户所在城市
            	 String merchant_address;//商户地址
            	 String family_name;//姓名
            	 String id_card;//证件号
            	 String mobile;//手机号
            	 String payee_bank_id;//总行联行号
            	 String payee_bank_no;//结算账号
            	 String payee_bank_name;//总行名称
            	 String rate_t0;//消费交易手续费扣率
            	 String counter_fee_t0;//单笔消费交易手续费
            	 String counter_fee_t1;//单笔提现手续费
            	 String merchant_oper_flag;//操作标识
            	 String method = "register";//业务接口
    			if (StringUtils.isBlank(registerParam.getClient_trans_id())) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(registerParam.getMerchant_code())) {
    				ResponseUtil.sendFailJson(response, "商户号不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getMerchant_name())) {
    				ResponseUtil.sendFailJson(response, "商户名称不能为空");
    				return;
    			}else if (StringUtils.isBlank(registerParam.getMerchant_province())) {
    				ResponseUtil.sendFailJson(response, "商户所在省份不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getMerchant_city())) {
    				ResponseUtil.sendFailJson(response, "商户所在城市不能为空");
    				return;
    			}else if (StringUtils.isBlank(registerParam.getMerchant_address())) {
    				ResponseUtil.sendFailJson(response, "商户地址不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getFamily_name())) {
    				ResponseUtil.sendFailJson(response, "姓名不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getId_card())) {
    				ResponseUtil.sendFailJson(response, "证件号不能为空");
    				return;
    			}else if (StringUtils.isBlank(registerParam.getMobile())) {
    				ResponseUtil.sendFailJson(response, "手机号不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getPayee_bank_id())) {
    				ResponseUtil.sendFailJson(response, "总行联行号不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getPayee_bank_no())) {
    				ResponseUtil.sendFailJson(response, "结算账号不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getPayee_bank_name())) {
    				ResponseUtil.sendFailJson(response, "总行名称不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getRate_t0())) {
    				ResponseUtil.sendFailJson(response, "费率不能为空");
    				return;
    			} else if (StringUtils.isBlank(registerParam.getMerchant_oper_flag())) {
    				ResponseUtil.sendFailJson(response, "操作标识不能为空");
    				return;
    			}
			quickPay= hxpayService.HXRegister(registerParam);
			ResponseUtil.successJson(response, quickPay);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.errorJson(response, e.getMessage());
			return ;
		}
    }*/
	/**
	 * 
		* @Title: pay 
		* @Description: 消费接口 
		* @param @param request
		* @param @param response
		* @param @param hxpayParam
		* @param @throws Exception    设定文件 
		* @return void    返回类型 
		* @throws
	 */
	@ResponseBody
    @RequestMapping(value = "/creditPurchase")
    public void creditPurchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
		Map<String,Object> reqMapBody = new HashMap<>();
            try{
            	String orderNo = request.getParameter("orderNo"); 
            	String mobile = request.getParameter("mobile"); 
            	String custName = request.getParameter("custName"); 
            	String cardNo = request.getParameter("cardNo"); 
            	String trans_amount = request.getParameter("trans_amount"); 
            	String rate_t0 = request.getParameter("rate_t0"); 
            	String counter_fee_t0 = request.getParameter("counter_fee_t0"); 
            	String bankName = request.getParameter("bankName"); 
            	String merchantId = request.getParameter("merchantId"); 
            	String expire_date = request.getParameter("expire_date"); 
            	String cvv = request.getParameter("cvv"); 
            	if (StringUtils.isBlank(orderNo)) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(mobile)) {
    				ResponseUtil.sendFailJson(response, "手机号不能为空");
    				return;
    			} else if (StringUtils.isBlank(custName)) {
    				ResponseUtil.sendFailJson(response, "商户名称不能为空");
    				return;
    			} else if (StringUtils.isBlank(cardNo)) {
    				ResponseUtil.sendFailJson(response, "信用卡卡号不能为空");
    				return;
    			} else if (StringUtils.isBlank(trans_amount)) {
    				ResponseUtil.sendFailJson(response, "订单金额不能为空");
    				return;
    			} else if (StringUtils.isBlank(rate_t0)) {
    				ResponseUtil.sendFailJson(response, "费率不能为空");
    				return;
    			} else if (StringUtils.isBlank(counter_fee_t0)) {
    				ResponseUtil.sendFailJson(response, "单笔消费交易手续费不能为空");
    				return;
    			} else if (StringUtils.isBlank(bankName)) {
    				ResponseUtil.sendFailJson(response, "卡片名称不能为空");
    				return;
    			} else if (StringUtils.isBlank(merchantId)) {
    				ResponseUtil.sendFailJson(response, "商户编号不能为空");
    				return;
    			} else if (StringUtils.isBlank(expire_date)) {
    				ResponseUtil.sendFailJson(response, "卡有效期不能为空");
    				return;
    			} else if (StringUtils.isBlank(cvv)) {
    				ResponseUtil.sendFailJson(response, "cvv不能为空");
    				return;
    			}
        		/**以下是绑定结算卡接口操作*/
				Map<String,Object> reqBankMapBody = new HashMap<>();
				reqBankMapBody.put("custId", merchantId);
				reqBankMapBody.put("custName", custName);
				reqBankMapBody.put("cardNo", cardNo);
				reqBankMapBody.put("bankName", bankName);
				reqBankMapBody.put("bankMobile",mobile);
				reqBankMapBody.put("bankDate",expire_date);
				reqBankMapBody.put("bankCvn2",cvv);
				String banksign = MD5Util.MD5Encode(JSON.toJSONString(reqBankMapBody)+YblConfig.yblkey);
				Map<String,Object> reqBankMapHead = new HashMap<>();
				reqBankMapHead.put("SIGN", banksign);
				reqBankMapHead.put("SYSID",YblConfig.yblmerNo);
				Map<String,Object> reqBankMapString = new HashMap<>();
				reqBankMapString.put("REQ_BODY", reqBankMapBody);
				reqBankMapString.put("REQ_HEAD", reqBankMapHead);
				reqBankMapString.put("ip", "fe80::5fd0:c9db:b821:88b2");
				logger.info("易佰联绑定结算卡提交报文："+JSON.toJSONString(reqBankMapString));
				String httpBankResult = HttpRequest.sendPost(YblConfig.yblpostUrl+"BYC0003.json","REQ_MESSAGE="+JSON.toJSONString(reqBankMapString));
				if(httpBankResult==null){
					ResponseUtil.sendFailJson(response, "易佰联绑定信用卡异常，返回空");
				}
				logger.info("易佰联绑定信用卡返回的报文："+httpBankResult);
				JSONObject resultBankJosn = JSONObject.parseObject(httpBankResult);
				JSONObject reqBankBody = JSONObject.parseObject(resultBankJosn.get("REP_BODY") != null ? resultBankJosn.get("REP_BODY").toString() : "");
				if ("000000".equals(reqBankBody.get("RSPCOD") != null ? reqBankBody.get("RSPCOD").toString() : "")||"C20996".equals(reqBankBody.get("RSPCOD") != null ? reqBankBody.get("RSPCOD").toString() : "")) {
					
					logger.info("易佰联绑定信用卡返回的报文："+httpBankResult);
					
				}else{
					ResponseUtil.sendFailJson(response, "绑定信用卡："+reqBankBody.get("RSPCOD") + "-" + reqBankBody.get("RSPMSG"));
					return;
				}
            	
        		// 易佰联
            	    trans_amount =  new BigDecimal(trans_amount).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)+"";
	            	reqMapBody.put("oderNo", orderNo);
	            	reqMapBody.put("custId", merchantId);
	            	reqMapBody.put("custName", custName);
	            	reqMapBody.put("payAmt", trans_amount);
	            	reqMapBody.put("cardNo", cardNo);
	            	reqMapBody.put("cardName", bankName);
	            	reqMapBody.put("cardPhone", mobile);
	            	reqMapBody.put("xfRate", rate_t0);
	            	String fee= new BigDecimal(trans_amount).multiply(new BigDecimal(rate_t0)).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)+"";
	            	reqMapBody.put("fee", fee);
	            	reqMapBody.put("streeId", "0004");
	            	reqMapBody.put("serRebackUrl", YblConfig.costCallBackUrl);
            	
        			String sign = MD5Util.MD5Encode(JSON.toJSONString(reqMapBody)+YblConfig.yblkey);
        			Map<String,Object> reqMapHead = new HashMap<>();
        			reqMapHead.put("SIGN", sign);
        			reqMapHead.put("SYSID",YblConfig.yblmerNo);
        			Map<String,Object> reqMapString = new HashMap<>();
        			reqMapString.put("REQ_BODY", reqMapBody);
        			reqMapString.put("REQ_HEAD", reqMapHead);
        			reqMapString.put("ip", "fe80::5fd0:c9db:b821:88b2");
        			logger.info("易佰联消费提交报文："+ JSON.toJSONString(reqMapString));
        			String httpResult = HttpRequest.sendPost(YblConfig.yblpostUrl+"XFBJ001.json", "REQ_MESSAGE="+JSON.toJSONString(reqMapString));
        			
        			if(httpResult==null){
        				quickPay.setRespCode("fail");
            			quickPay.setRespDesc("系统异常:易佰联消费异常，返回空");
        			}
        			logger.info("易佰联消费返回的报文："+httpResult);
        			JSONObject resultJosn = JSONObject.parseObject(httpResult);
        			JSONObject reqbody = JSONObject.parseObject(resultJosn.get("REP_BODY") != null ? resultJosn.get("REP_BODY").toString() : "");
        			if ("000000".equals(reqbody.get("RSPCOD") != null ? reqbody.get("RSPCOD").toString() : "")) {
        				String rmap = reqbody.get("rmap") != null ? reqbody.get("rmap").toString() : "";
        				JSONObject rmapJson = JSONObject.parseObject(rmap);
        				String repCode = rmapJson.get("repCode") != null ? rmapJson.get("repCode").toString() : "";
        				String repMesg = rmapJson.get("repMesg") != null ? rmapJson.get("repMesg").toString() : "";
        				if("000000".equals(repCode)||"111111".equals(repCode)){
        					quickPay.setRespCode("9997");
            				quickPay.setRespDesc("结算中");
        				}else{
        					quickPay.setRespCode("fail");
            				quickPay.setRespDesc(repMesg);
        				}
        			} else {
        				quickPay.setRespCode("fail");
        				quickPay.setRespDesc(reqbody.get("RSPMSG")+"");
        			}
			ResponseUtil.successJson(response, quickPay);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.errorJson(response, e.getMessage());
			return ;
		}
    }
	
/**
	 * 
		* @Title: payQuery 
		* @Description: 消费查询
		* @param @param request
		* @param @param response
		* @param @param hxpayQueryParam
		* @param @throws Exception    设定文件 
		* @return void    返回类型 
		* @throws
	 */
	@ResponseBody
    @RequestMapping(value = "/payQuery")
    public void payQuery(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	String orderNo = request.getParameter("orderNo");//订单号
            	if (StringUtils.isBlank(orderNo)) {
    				ResponseUtil.sendFailJson(response, "原订单号不能为空");
    				return;
    			}
            	Map<String,Object> reqMapBody = new HashMap<>();
            	reqMapBody.put("sysPayNo", orderNo);
            	String sign = MD5Util.MD5Encode(JSON.toJSONString(reqMapBody)+YblConfig.yblkey);
    			Map<String,Object> reqMapHead = new HashMap<>();
    			reqMapHead.put("SIGN", sign);
    			reqMapHead.put("SYSID",YblConfig.yblmerNo);
    			Map<String,Object> reqMapString = new HashMap<>();
    			reqMapString.put("REQ_BODY", reqMapBody);
    			reqMapString.put("REQ_HEAD", reqMapHead);
    			reqMapString.put("ip", "fe80::5fd0:c9db:b821:88b2");
    			logger.info("易佰联查询提交报文："+ JSON.toJSONString(reqMapString));
    			String httpResult = HttpRequest.sendPost(YblConfig.yblpostUrl+"XFBJ002.json", "REQ_MESSAGE="+JSON.toJSONString(reqMapString));
    			
    			if(httpResult==null){
    				quickPay.setRespCode("fail");
        			quickPay.setRespDesc("系统异常:易佰联查询异常，返回空");
    			}
    			logger.info("易佰联查询返回的报文："+httpResult);
    			JSONObject resultJosn = JSONObject.parseObject(httpResult);
    			JSONObject reqbody = JSONObject.parseObject(resultJosn.get("REP_BODY") != null ? resultJosn.get("REP_BODY").toString() : "");
    			if ("000000".equals(reqbody.get("RSPCOD") != null ? reqbody.get("RSPCOD").toString() : "")) {
    				quickPay.setRespCode("0000");
    				quickPay.setRespDesc("交易成功");
    			} else {
    				quickPay.setRespCode("fail");
    				quickPay.setRespDesc(reqbody.get("RSPMSG")+"");
    			}
			ResponseUtil.successJson(response, quickPay);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.errorJson(response, e.getMessage());
			return ;
		}
    }
	
	/**
	 * 余额代付
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/proxyPay")
    public void proxyPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
		Map<String,Object> reqMapBody = new HashMap<>();
		try {
			String customerName = request.getParameter("customerName"); //账户名称
			String transAmt = request.getParameter("transAmt");     //交易金额
			String acctNo = request.getParameter("acctNo");      //卡号
			String cerdId = request.getParameter("cerdId"); //身份证
			String phoneNo = request.getParameter("phoneNo");  //手机
			String orderNo = request.getParameter("orderNo");//订单号
			String orderDate = request.getParameter("orderDate");//订单日期
			String merchantId = request.getParameter("merchantId");//商户号
			String lhh = request.getParameter("lhh");//总行联行号
			String lmc = request.getParameter("lmc");//总行名称
			if (StringUtils.isBlank(customerName)) {
				ResponseUtil.sendFailJson(response, "账户名称不能为空");
				return;
			}
			if (StringUtils.isBlank(transAmt)) {
				ResponseUtil.sendFailJson(response, "金额不能为空");
				return;
			}
			transAmt =  (int)Math.ceil(NumberUtils.toDouble(transAmt)/100)+"";
			if (StringUtils.isBlank(acctNo)) {
				ResponseUtil.sendFailJson(response, "卡号不能为空");
				return;
			}
			if (StringUtils.isBlank(cerdId)) {
				ResponseUtil.sendFailJson(response, "证件号不能为空");
				return;
			}
			if (StringUtils.isBlank(phoneNo)) {
				ResponseUtil.sendFailJson(response, "代付手机号不能为空");
				return;
			}
			if (StringUtils.isBlank(orderNo)) {
				ResponseUtil.sendFailJson(response, "商户订单号不能为空");
				return;
			}
			if (StringUtils.isBlank(orderDate)) {
				ResponseUtil.sendFailJson(response, "订单日期不能为空yyyyMMdd格式");
				return;
			}
			if (StringUtils.isBlank(merchantId)) {
				ResponseUtil.sendFailJson(response, "商户号不能为空");
				return;
			}
			
			reqMapBody.put("oderNo", orderNo);
			reqMapBody.put("casAmt", transAmt);
        	reqMapBody.put("custId", merchantId);
    	    String findFeeSql2 = "select FEE from  t_param_fee  where CODE = :re_cash";
    			Map<String, Object> findFeeMap2 = new HashMap<String, Object>();
    			findFeeMap2.put("re_cash", "re_cash");
    		ParamFee paramFee2 =	paramFeeDao.findBy(findFeeSql2, findFeeMap2);
    		 if (paramFee2!=null&&paramFee2.getFee()!=null) {
    		    	//单笔提现手续费
    		    	reqMapBody.put("casFee", (int)(paramFee2.getFee()*1)+"");
    			} else{
    				Map<String,Object> result = new HashMap<String,Object>();
    				result.put("code", "9997");
    				result.put("message","获取提现手续费异常");
    				String json = JsonUtil.toJson(result);
    				ResponseUtil.responseJson(response, json);
    				return;
    			}	
    		 reqMapBody.put("custName", customerName);
        	reqMapBody.put("cardNo", acctNo);
        	reqMapBody.put("cardName", lmc);
        	reqMapBody.put("cardPhone", phoneNo);
    	
			String sign = MD5Util.MD5Encode(JSON.toJSONString(reqMapBody)+YblConfig.yblkey);
			Map<String,Object> reqMapHead = new HashMap<>();
			reqMapHead.put("SIGN", sign);
			reqMapHead.put("SYSID",YblConfig.yblmerNo);
			Map<String,Object> reqMapString = new HashMap<>();
			reqMapString.put("REQ_BODY", reqMapBody);
			reqMapString.put("REQ_HEAD", reqMapHead);
			reqMapString.put("ip", "fe80::5fd0:c9db:b821:88b2");
			logger.info("易佰联代付提交报文："+ JSON.toJSONString(reqMapString));
			String httpResult = HttpRequest.sendPost(YblConfig.yblpostUrl+"REMIT001.json", "REQ_MESSAGE="+JSON.toJSONString(reqMapString));
			
			if(httpResult==null){
				quickPay.setRespCode("fail");
    			quickPay.setRespDesc("系统异常:易佰联代付异常，返回空");
			}
			logger.info("易佰联代付返回的报文："+httpResult);
			JSONObject resultJosn = JSONObject.parseObject(httpResult);
			JSONObject reqbody = JSONObject.parseObject(resultJosn.get("REP_BODY") != null ? resultJosn.get("REP_BODY").toString() : "");
			if ("000000".equals(reqbody.get("RSPCOD") != null ? reqbody.get("RSPCOD").toString() : "")) {
				
				
				String rmap = reqbody.get("rmap") != null ? reqbody.get("rmap").toString() : "";
				JSONObject rmapJson = JSONObject.parseObject(rmap);
				String repCode = rmapJson.get("repCode") != null ? rmapJson.get("repCode").toString() : "";
				String repMesg = rmapJson.get("repMesg") != null ? rmapJson.get("repMesg").toString() : "";
				if("000000".equals(repCode)||"111111".equals(repCode)){
				
				/*		quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");*/
				quickPay.setRespCode("0000");
				quickPay.setRespDesc("代付成功");
				}else{
					quickPay.setRespCode("fail");
    				quickPay.setRespDesc(repMesg);
				}
			} else {
				quickPay.setRespCode("fail");
				quickPay.setRespDesc(reqbody.get("RSPMSG")+"");
			}
			Map<String, Object>map=ResponseUtil.makeSuccessJson();
			map.put("transactionType","");
			map.put("code",quickPay.getRespCode());
			map.put("message",quickPay.getRespDesc());
			String json = JsonUtil.toJson(map);
			ResponseUtil.responseJson(response, json);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("code", "9997");
			result.put("message", e.getMessage());
			String json = JsonUtil.toJson(result);
			ResponseUtil.responseJson(response, json);
			return;
		}
    }
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
}
