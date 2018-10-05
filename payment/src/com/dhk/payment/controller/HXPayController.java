package com.dhk.payment.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhk.kernel.controller.BaseController;
import com.dhk.kernel.util.JsonUtil;
import com.dhk.kernel.util.ResponseUtil;
import com.dhk.payment.config.HxtcConfig;
import com.dhk.payment.dao.IParamFeeDao;
import com.dhk.payment.dao.IUserDao;
import com.dhk.payment.entity.ParamFee;
import com.dhk.payment.entity.User;
import com.dhk.payment.entity.param.HXPayParam;
import com.dhk.payment.entity.param.HXPayQueryParam;
import com.dhk.payment.entity.param.HXPayWithdrawParam;
import com.dhk.payment.entity.param.HXPayWithdrawQueryParam;
import com.dhk.payment.entity.param.HXRegisterParam;
import com.dhk.payment.service.IHXPayService;
import com.dhk.payment.yilian.QuickPay;

/**
 * 
	* @ClassName: HXPayController 
	* @Description:  汇享支付通道
	* @author ZZL
	* @date 2018年1月7日 下午8:52:09 
	*
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/hxpay")
public class HXPayController extends BaseController{

	private static Logger logger = Logger.getLogger(HXPayController.class);
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
    public void creditPurchase(HttpServletRequest request,HttpServletResponse response, @Validated HXPayParam hxpayParam) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	if (StringUtils.isBlank(hxpayParam.getClient_trans_id())) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(hxpayParam.getMobile())) {
    				ResponseUtil.sendFailJson(response, "手机号不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getFamily_name())) {
    				ResponseUtil.sendFailJson(response, "姓名不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getId_card())) {
    				ResponseUtil.sendFailJson(response, "身份证不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getPay_bank_no())) {
    				ResponseUtil.sendFailJson(response, "信用卡卡号不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getTrans_amount())) {
    				ResponseUtil.sendFailJson(response, "订单金额不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getRate_t0())) {
    				ResponseUtil.sendFailJson(response, "费率不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getCounter_fee_t0())) {
    				ResponseUtil.sendFailJson(response, "单笔消费交易手续费不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getExpire_date())) {
    				ResponseUtil.sendFailJson(response, "有效期不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getCvv())) {
    				ResponseUtil.sendFailJson(response, "安全码不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getBack_notify_url())) {
    				ResponseUtil.sendFailJson(response, "回调地址不能为空");
    				return;
    			} else if (StringUtils.isBlank(hxpayParam.getThird_merchant_code())) {
    				ResponseUtil.sendFailJson(response, "终端商户号不能为空");
    				return;
    			}
			quickPay= hxpayService.HXPay(hxpayParam);
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
    public void payQuery(HttpServletRequest request,HttpServletResponse response, @Validated HXPayQueryParam hxpayQueryParam) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	if (StringUtils.isBlank(hxpayQueryParam.getClient_trans_id())) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(hxpayQueryParam.getOrig_tran_id())) {
    				ResponseUtil.sendFailJson(response, "原订单号不能为空");
    				return;
    			} 
			quickPay= hxpayService.HXPayQuery(hxpayQueryParam);
			ResponseUtil.successJson(response, quickPay);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.errorJson(response, e.getMessage());
			return ;
		}
    }
	
/*	*//**
	 * 
		* @Title: payWithdraw 
		* @Description: 提现接口
		* @param @param request
		* @param @param response
		* @param @param hxpayWithdrawParam
		* @param @throws Exception    设定文件 
		* @return void    返回类型 
		* @throws
	 *//*
	@ResponseBody
    @RequestMapping(value = "/payWithdraw")
    public void payWithdraw(HttpServletRequest request,HttpServletResponse response, @Validated HXPayWithdrawParam hxpayWithdrawParam) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	if (StringUtils.isBlank(hxpayWithdrawParam.getClient_trans_id())) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(hxpayWithdrawParam.getThird_merchant_code())) {
    				ResponseUtil.sendFailJson(response, "商户号不能为空");
    				return;
    			}  else if (StringUtils.isBlank(hxpayWithdrawParam.getTrans_amount())) {
    				ResponseUtil.sendFailJson(response, "订单金额不能为空");
    				return;
    			}  
			quickPay= hxpayService.HXPayWithdraw(hxpayWithdrawParam);
			ResponseUtil.successJson(response, quickPay);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.errorJson(response, e.getMessage());
			return ;
		}
    }*/
	
/*	*//**
	 * 
		* @Title: payWithdrawQuery 
		* @Description: 提现查询
		* @param @param request
		* @param @param response
		* @param @param hxpayWithdrawQueryParam
		* @param @throws Exception    设定文件 
		* @return void    返回类型 
		* @throws
	 */
	@ResponseBody
    @RequestMapping(value = "/payWithdrawQuery")
    public void payWithdrawQuery(HttpServletRequest request,HttpServletResponse response, @Validated HXPayWithdrawQueryParam hxpayWithdrawQueryParam) throws Exception{
		QuickPay quickPay = new QuickPay();
            try{
            	if (StringUtils.isBlank(hxpayWithdrawQueryParam.getClient_trans_id())) {
    				ResponseUtil.sendFailJson(response, "流水号不能为空");
    				return;
    			}else if (StringUtils.isBlank(hxpayWithdrawQueryParam.getOrig_tran_id())) {
    				ResponseUtil.sendFailJson(response, "原订单号不能为空");
    				return;
    			} 
			quickPay= hxpayService.HXPayWithdrawQuery(hxpayWithdrawQueryParam);
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
			if("00000".equals(quickPay.getRespCode())){
				HXPayWithdrawParam hxpayWithdrawParam = new HXPayWithdrawParam();
				hxpayWithdrawParam.setClient_trans_id(orderNo);
				hxpayWithdrawParam.setThird_merchant_code(user.getMerchantid());
			//	int transAmtall = Integer.valueOf(transAmt)+Integer.valueOf(registerParam.getCounter_fee_t0()) ;
				int transAmtall = Integer.valueOf(transAmt);
				hxpayWithdrawParam.setTrans_amount(transAmtall+"");
//				hxpayWithdrawParam.setTrans_date(orderDate);
				hxpayWithdrawParam.setBack_notify_url(HxtcConfig.replanCallBackUrl);
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
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
}
