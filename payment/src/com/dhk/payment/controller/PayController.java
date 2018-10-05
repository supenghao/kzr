package com.dhk.payment.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xiajinsuo.epay.sdk.HttpUtils;
import org.xiajinsuo.epay.sdk.RRParams;
import org.xiajinsuo.epay.sdk.ResponseDataWrapper;

import com.alibaba.fastjson.JSON;
import com.dhk.kernel.controller.BaseController;
import com.dhk.kernel.util.JsonUtil;
import com.dhk.kernel.util.ResponseUtil;
import com.dhk.payment.config.HxtcConfig;
import com.dhk.payment.dao.IParamFeeDao;
import com.dhk.payment.dao.IUserDao;
import com.dhk.payment.entity.ParamFee;
import com.dhk.payment.entity.User;
import com.dhk.payment.entity.param.HXPayWithdrawParam;
import com.dhk.payment.entity.param.HXRegisterParam;
import com.dhk.payment.entity.result.HXPayBaseReult;
import com.dhk.payment.service.IFRService;
import com.dhk.payment.service.IGzfService;
import com.dhk.payment.service.IHXPayService;
import com.dhk.payment.service.IhlbService;
import com.dhk.payment.util.BeanConvertUtil;
import com.dhk.payment.util.RsaDataEncryptUtil;
import com.dhk.payment.util.RsaDataEncryptUtilDh;
import com.dhk.payment.yilian.QuickPay;

/**
 * 沃付
 * @author bian
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/pay")
public class PayController extends BaseController{

	@Resource
	private IFRService frService;
	@Resource
	private IGzfService gzfService;
	@Resource
	private IHXPayService hxpayService;
	
	@Resource
	private IhlbService hlbService;
	@Resource
	private IUserDao userDao;
	@Resource(name = "ParamFeeDao")
	private IParamFeeDao paramFeeDao;

	static Logger logger = Logger.getLogger(PayController.class);
	/**
	 * 无卡消费
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/creditPurchase")
    public void fastPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
		try {
			String transAmt = request.getParameter("transAmt");
			String commodityName = request.getParameter("commodityName");
			String orderNo = request.getParameter("orderNo");
			String cardNo = request.getParameter("cardNo");
			String accountName = request.getParameter("accountName");
			String cerdId = request.getParameter("cerdId");
			String cerdType = request.getParameter("cerdType");
			String phoneNo = request.getParameter("phoneNo");
			String expDate = request.getParameter("expDate");
			String cvn2 = request.getParameter("cvn2");

			if (StringUtils.isBlank(transAmt)) {
				ResponseUtil.sendFailJson(response, "交易金额不能为空");
				return;
			}else if (StringUtils.isBlank(commodityName)) {
				ResponseUtil.sendFailJson(response, "商品名称不能为空");
				return;
			} else if (StringUtils.isBlank(orderNo)) {
				ResponseUtil.sendFailJson(response, "订单号不能为空");
				return;
			}else if (StringUtils.isBlank(cardNo)) {
				ResponseUtil.sendFailJson(response, "卡号不能为空");
				return;
			} else if (StringUtils.isBlank(accountName)) {
				ResponseUtil.sendFailJson(response, "账户名称不能为空");
				return;
			}else if (StringUtils.isBlank(cerdId)) {
				ResponseUtil.sendFailJson(response, "证件编号不能为空");
				return;
			} else if (StringUtils.isBlank(cerdType)) {
				ResponseUtil.sendFailJson(response, "证件类型不能为空");
				return;
			} else if (StringUtils.isBlank(expDate)) {
				ResponseUtil.sendFailJson(response, "有效日期不能为空");
				return;
			}else if (StringUtils.isBlank(cvn2)) {
				ResponseUtil.sendFailJson(response, "cvn2不能为空");
				return;
			} else if (StringUtils.isBlank(phoneNo)) {
				ResponseUtil.sendFailJson(response, "手机号不能为空");
				return;
			}
			Map<String, Object> paramMap = this.getParamsToMap(request);


//			 if(expDate.length()==4){
//				 paramMap.put("expDate",expDate.substring(2,4)+expDate.substring(0,2));
//			 }else{
//			 	logger.error("expDate格式不对："+expDate);
//			 }
			/**
			String cardBin="";
			if(cardNo.length()>=6){
				cardBin=cardNo.substring(0,6);
			}
			String  channel = payService.getChannel(false,cardBin,1);

			logger.info("channel for creditPurchase:"+channel);
			if("YL".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.creditPurchase(paramMap);
			}else if("WF".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay = woFuService.creditPurchase(paramMap);
			}else if("JN".equals(channel)){
				quickPay = jieNiuService.creditPurchase(paramMap);
			}else {
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.creditPurchase(paramMap);
			}
			 **/
			//quickPay= gzfService.creditPurchase(paramMap);
			quickPay= hlbService.creditPurchase(paramMap);

			Map<String, Object>map=ResponseUtil.makeSuccessJson();
			map.put("data", quickPay);
			map.put("code",quickPay.getRespCode());
			map.put("message",quickPay.getRespDesc());
			map.put("transactionType","");
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
		try {
			String accountName = request.getParameter("accountName"); //
			String transAmt = request.getParameter("transAmt");     //
			String cardNo = request.getParameter("cardNo");      //
			String cerdId = request.getParameter("cerdId"); //
			String phoneNo = request.getParameter("phoneNo");  //
			String orderNo = request.getParameter("orderNo");//

			if (StringUtils.isBlank(accountName)) {
				ResponseUtil.sendFailJson(response, "账户名称不能为空");
				return;
			}
			if (StringUtils.isBlank(transAmt)) {
				ResponseUtil.sendFailJson(response, "金额不能为空");
				return;
			}
			if (StringUtils.isBlank(cardNo)) {
				ResponseUtil.sendFailJson(response, "卡号不能为空");
				return;
			}
			if (StringUtils.isBlank(cerdId)) {
				ResponseUtil.sendFailJson(response, "证件号不能为空");
				return;
			}
			if (StringUtils.isBlank(phoneNo)) {
				ResponseUtil.sendFailJson(response, "手机号不能为空");
				return;
			}
			if (StringUtils.isBlank(orderNo)) {
				ResponseUtil.sendFailJson(response, "商户订单号不能为空");
				return;
			}

			Map<String, Object> paramMap = this.getParamsToMap(request);

			/**
			String cardBin="";
			if(cardNo.length()>=6){
				cardBin=cardNo.substring(0,5);
			}

			String  channel = payService.getChannel(true,cardBin,2);
			logger.info("channel for proxyPay:"+channel);
			if("YL".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.proxyPay(paramMap);
			}else if("WF".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay = woFuService.proxyPay(paramMap);
			}else if("JN".equals(channel)) {
				quickPay = jieNiuService.proxyPay(paramMap);
			}else {
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.proxyPay(paramMap);
			}
			 **/
			paramMap.put("isT1","0");
			quickPay= hlbService.proxyPay(paramMap);
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

	
	/**
	 * 余额代付
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/proxyPay1")
    public void proxyPay1(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
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
			String lhmc = request.getParameter("lhmc");//总行名称
			if (StringUtils.isBlank(customerName)) {
				ResponseUtil.sendFailJson(response, "账户名称不能为空");
				return;
			}
			if (StringUtils.isBlank(transAmt)) {
				ResponseUtil.sendFailJson(response, "金额不能为空");
				return;
			}
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
			 String userSql = "select * from  t_s_user  where merchantId = :merchantId";
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("merchantId", merchantId);
			List<User> users =	userDao.find(userSql, userMap);
			User user = null;
			if(users!=null && users.size()>0){
				user = users.get(0);
			}else{
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("code", "9997");
				result.put("message","该商户号异常");
				String json = JsonUtil.toJson(result);
				ResponseUtil.responseJson(response, json);
				return;
			}
//			Map<String, Object> paramMap = this.getParamsToMap(request);
			HXRegisterParam registerParam = new HXRegisterParam();
			registerParam.setClient_trans_id(generateTransId());
			registerParam.setMerchant_name(customerName);
			registerParam.setFamily_name(customerName);
			registerParam.setMerchant_code(user.getMerchantid());
			registerParam.setMerchant_province(user.getProvince_code());
			registerParam.setMerchant_city(user.getCity_code());
			registerParam.setMerchant_address(user.getDistrict_code());
			registerParam.setId_card(cerdId);
			registerParam.setMobile(phoneNo);
			registerParam.setPayee_bank_id(lhh);
			registerParam.setPayee_bank_no(acctNo);
			registerParam.setPayee_bank_name(lhmc);
		    String findFeeSql2 = "select FEE from  t_param_fee  where CODE = :re_cash";
						Map<String, Object> findFeeMap2 = new HashMap<String, Object>();
						findFeeMap2.put("re_cash", "re_cash");
					ParamFee paramFee2 =	paramFeeDao.findBy(findFeeSql2, findFeeMap2);
					 if (paramFee2!=null&&paramFee2.getFee()!=null) {
						 registerParam.setCounter_fee_t0((int)(paramFee2.getFee()*100)+"");//单笔提现手续费
						} else{
							Map<String,Object> result = new HashMap<String,Object>();
							result.put("code", "9997");
							result.put("message","获取提现手续费异常");
							String json = JsonUtil.toJson(result);
							ResponseUtil.responseJson(response, json);
							return;
						}	
			 String findFeeSql = "select FEE from  t_param_fee  where CODE = :purchase";
			Map<String, Object> findFeeMap = new HashMap<String, Object>();
			findFeeMap.put("purchase", "purchase");
		ParamFee paramFee =	paramFeeDao.findBy(findFeeSql, findFeeMap);
		if (paramFee!=null&&paramFee.getFee()!=null) {
	    	registerParam.setRate_t0(String.format("%.2f",paramFee.getFee()));
		} else{
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("code", "9997");
			result.put("message","获取费率异常");
			String json = JsonUtil.toJson(result);
			ResponseUtil.responseJson(response, json);
			return;
		}
//		registerParam.setCounter_fee_t0("0");
		registerParam.setMerchant_oper_flag("M");
			quickPay= hxpayService.HXRegister(registerParam);
			if("000000".equals(quickPay.getRespCode())){
				HXPayWithdrawParam hxpayWithdrawParam = new HXPayWithdrawParam();
				hxpayWithdrawParam.setClient_trans_id(generateTransId());
				hxpayWithdrawParam.setThird_merchant_code(user.getMerchantid());
				hxpayWithdrawParam.setTrans_amount(transAmt);
//				hxpayWithdrawParam.setTrans_date(orderDate);
				quickPay = hxpayService.HXPayWithdraw(hxpayWithdrawParam);
			}else{
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("code", "9997");
				result.put("message","设置提现账户异常:"+quickPay.getRespDesc());
				String json = JsonUtil.toJson(result);
				ResponseUtil.responseJson(response, json);
				return;
			}
			/**
			String cardBin="";
			if(cardNo.length()>=6){
				cardBin=cardNo.substring(0,5);
			}

			String  channel = payService.getChannel(true,cardBin,2);
			logger.info("channel for proxyPay:"+channel);
			if("YL".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.proxyPay(paramMap);
			}else if("WF".equals(channel)){
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay = woFuService.proxyPay(paramMap);
			}else if("JN".equals(channel)) {
				quickPay = jieNiuService.proxyPay(paramMap);
			}else {
				BigDecimal amt = new BigDecimal(transAmt);
				amt=amt.divide(new BigDecimal("100"));
				paramMap.put("transAmt",amt.toString());
				quickPay= ylService.proxyPay(paramMap);
			}
			 **/
		/*	paramMap.put("isT1","0");
			quickPay= gzfService.proxyPay(paramMap);
			if("fail".equals(quickPay.getRespCode())&&"商户余额不足".equals(quickPay.getRespDesc())){
				paramMap.put("isT1","1");
				quickPay= gzfService.proxyPay(paramMap);
			}*/
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
	/**
	 * 余额代付
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/hxtcproxyPay")
    public void hxtcproxyPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
			String customerName = request.getParameter("accountName"); //账户名称
			String transAmt = request.getParameter("transAmt");     //交易金额
			String acctNo = request.getParameter("cardNo");      //卡号
			String cerdId = request.getParameter("cerdId"); //身份证
			String phoneNo = request.getParameter("phoneNo");  //手机
			String orderNo = request.getParameter("orderNo");//订单号
			String orderDate = request.getParameter("orderDate");//订单日期
//			String merchantId = request.getParameter("merchantId");//商户号
			String lhh = request.getParameter("lhh");//总行联行号
			String lhmc = request.getParameter("lhmc");//总行名称
			if (StringUtils.isBlank(customerName)) {
				ResponseUtil.sendFailJson(response, "账户名称不能为空");
				return;
			}
			if (StringUtils.isBlank(transAmt)) {
				ResponseUtil.sendFailJson(response, "金额不能为空");
				return;
			}
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
	/*		if (StringUtils.isBlank(merchantId)) {
				ResponseUtil.sendFailJson(response, "商户号不能为空");
				return;
			}*/
			String trans_type = "AGENCY_PAYMENT";
			long trans_timestamp = System.currentTimeMillis();
			Map<String, Object> businessReq =  new HashMap<>();
			String method = "PAY";
			businessReq.put("method", method);
			businessReq.put("trans_amount", transAmt);
			businessReq.put("family_name", customerName);
			businessReq.put("id_card", cerdId);
			businessReq.put("bank_no", acctNo);
			businessReq.put("mobile", phoneNo);
			businessReq.put("bank_name", lhmc);
			businessReq.put("bank_id", lhh);
			String memo = "佣金下发";
			businessReq.put("memo", memo);
			String back_notify_url =  HxtcConfig.proxyPayCallBackUrl;
			businessReq.put("back_notify_url", back_notify_url);
			try{
				/**设置公用参数并签名*/
				RRParams requestData = RRParams.newBuilder().setAppId(HxtcConfig.appIddh).setClientTransId(orderNo)
						.setTransTimestamp(trans_timestamp).setTransType(trans_type).build();
				logger.info("提交的报文为:"+businessReq+" orderNo:"+orderNo+" appId:"+HxtcConfig.appIddh+" transType:"+trans_type);
				/**请求接口*/
				ResponseDataWrapper rdw = HttpUtils.post(HxtcConfig.request_url, requestData, businessReq,
						RsaDataEncryptUtilDh.rsaDataEncryptPri, RsaDataEncryptUtilDh.rsaDataEncryptPub);
				HXPayBaseReult hxPayBaseReult = BeanConvertUtil.beanConvert(rdw, HXPayBaseReult.class);
				logger.info("返回的报文为:"+JSON.toJSONString(hxPayBaseReult));
				String respCode = rdw.getRespCode();
				if (respCode.equals("000000")) {
					@SuppressWarnings("rawtypes")
					Map responseData = rdw.getResponseData();
					System.out.println("--:"+responseData);
					if("PAY_SUBMIT".equals(responseData.get("resp_code")+"") || "PAY_SUCCESS".equals(responseData.get("resp_code")+"")){
						quickPay.setRespCode("9997");
						quickPay.setRespDesc("结算中");
					}else if("PAY_FAILURE".equals(responseData.get("resp_code")+"")){
						quickPay.setRespCode("fail");
						quickPay.setRespDesc(responseData.get("resp_msg")+"");
					}else{
						quickPay.setRespCode("9997");
						quickPay.setRespDesc("结算中");
					}
				} else {
					logger.info("rdw.getRespCode():"+rdw.getRespCode() +"  rdw.getRespMsg():"+rdw.getRespMsg());
					quickPay.setRespCode("fail");
					quickPay.setRespDesc(rdw.getRespMsg());
				}
				ResponseUtil.successJson(response, quickPay);
			} catch (Exception e) {
				quickPay.setRespCode("fail");
				quickPay.setRespDesc("系统异常:"+e.getMessage());
				logger.error(e.getMessage(), e);
			}
    }
	
	@ResponseBody
    @RequestMapping(value = "/hxtcproxyPayQuery")
    public void hxtcproxyPayQuery(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QuickPay quickPay = new QuickPay();
        		String orig_tran_id = request.getParameter("orderNo");//订单号
				Map<String, String> businessReq = new HashMap();
				businessReq.put("method", "pay_qry");
				businessReq.put("orig_tran_id",orig_tran_id);
				String trans_type = "AGENCY_PAYMENT";
				long trans_timestamp = System.currentTimeMillis();
				try{
					/**设置公用参数并签名*/
					RRParams requestData = RRParams.newBuilder().setAppId(HxtcConfig.appId).setClientTransId(generateTransId())
							.setTransTimestamp(trans_timestamp).setTransType(trans_type).build();
					logger.info("提交的报文为:"+businessReq);
					/**请求接口*/
					ResponseDataWrapper rdw = HttpUtils.post(HxtcConfig.request_url, requestData, businessReq,
							RsaDataEncryptUtil.rsaDataEncryptPri, RsaDataEncryptUtil.rsaDataEncryptPub);
					HXPayBaseReult hxPayBaseReult = BeanConvertUtil.beanConvert(rdw, HXPayBaseReult.class);
					logger.info("返回的报文为:"+JSON.toJSONString(hxPayBaseReult));
					String respCode = rdw.getRespCode();
					if (respCode.equals("000000")) {
						@SuppressWarnings("rawtypes")
						Map responseData = rdw.getResponseData();
						System.out.println(responseData);
						if("SUCCESS".equals(responseData.get("resp_code")+"")){
							quickPay.setRespCode("9997");
							quickPay.setRespDesc("结算中");
						}else{
							quickPay.setRespCode("fail");
							quickPay.setRespDesc(responseData.get("resp_msg")+"");
						}
						if(responseData.get("resp_code")!=null){
							quickPay.setRespCode(responseData.get("resp_code")+"");
							quickPay.setRespDesc(responseData.get("resp_msg")+"");
						}
					} else {
						logger.info("rdw.getRespCode():"+rdw.getRespCode() +"  rdw.getRespMsg():"+rdw.getRespMsg());
						quickPay.setRespCode("fail");
						quickPay.setRespDesc(rdw.getRespMsg());
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
	 * 把请求参数 转换为map
	 */
	public Map<String, Object> getParamsToMap(HttpServletRequest request) {

		Map<String,Object>  res = new HashMap<String,Object>();
		Map<String,String[]>  parameter = request.getParameterMap();
		Iterator<String> it = parameter.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String[]  val = parameter.get(key);
			if(val!=null&&val.length>0){
				if(val[0]!=null&&!"".equals(val[0])){
					res.put(key, val[0].trim());
				}
			}
		}
		return res;
	}
	
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
}
