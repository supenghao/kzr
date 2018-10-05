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

import com.dhk.kernel.controller.BaseController;
import com.dhk.kernel.util.JsonUtil;
import com.dhk.kernel.util.ResponseUtil;
import com.dhk.payment.config.KjConfig;
import com.dhk.payment.dao.IParamFeeDao;
import com.dhk.payment.dao.IUserDao;
import com.dhk.payment.entity.ParamFee;
import com.dhk.payment.yilian.QuickPay;
import com.fast.pay.FastPay;
import com.fast.pay.core.FastPayResponse;
import com.fast.pay.core.product.AgencyPayMoneyRequest;
import com.fast.pay.core.product.CreditCardRefundRequest;
import com.fast.pay.core.product.MerchantRequest;
import com.fast.pay.core.product.OrderRequest;
import com.fast.pay.core.product.PayChannelRequest;

/**
 * 
	* @ClassName: 快捷支付
	* @Description: TODO
	* @author 
	* @date  
	*
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/kjpay")
public class KJPayController extends BaseController{

	private static Logger logger = Logger.getLogger(KJPayController.class);
	
	//初始化通道
	private static String[] gateway = {"HeLiBao-CreditCardRefund","YTKuaiJie-CreditCardRefund"}; 
 
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
        try{
        	String orderNo = request.getParameter("client_trans_id"); 
        	String mobile = request.getParameter("mobile"); 
        	String custName = request.getParameter("custName"); 
        	String cardNo = request.getParameter("pay_bank_no"); 
        	String trans_amount = request.getParameter("trans_amount"); 
        	String rate_t0 = request.getParameter("rate_t0"); 
        	String counter_fee_t0 = request.getParameter("counter_fee_t0"); 
        	String bankName = request.getParameter("bankName"); 
        	String merchantId = request.getParameter("merchantId"); 
        	String expire_date = request.getParameter("expire_date"); 
        	String cvv = request.getParameter("cvv"); 
        	String kj_merno=request.getParameter("kjMero");
        	String kj_key=request.getParameter("kjKey");
        	if (StringUtils.isBlank(kj_merno)) {
				ResponseUtil.sendFailJson(response, "请进行报户！");
				return;
			}
        	if (StringUtils.isBlank(orderNo)) {
				ResponseUtil.sendFailJson(response, "流水号不能为空");
				return;
			}else if (StringUtils.isBlank(mobile)) {
				ResponseUtil.sendFailJson(response, "手机号不能为空");
				return;
			} else if (StringUtils.isBlank(cardNo)) {
				ResponseUtil.sendFailJson(response, "信用卡卡号不能为空");
				return;
			} else if (StringUtils.isBlank(trans_amount)) {
				ResponseUtil.sendFailJson(response, "订单金额不能为空");
				return;
			} 
        	trans_amount =  new BigDecimal(trans_amount).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)+"";
        	String findFeeSql2 = "select FEE from  t_param_fee  where CODE = :re_cash";
			Map<String, Object> findFeeMap2 = new HashMap<String, Object>();
			findFeeMap2.put("re_cash", "re_cash");
			ParamFee paramFee2 =	paramFeeDao.findBy(findFeeSql2, findFeeMap2);
			int re_cash=1;
			String purchase="0.79";
			  if (paramFee2!=null&&paramFee2.getFee()!=null) {
				 re_cash=(int)(paramFee2.getFee()*100)/100 ;//单笔提现手续费
				} else{
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("code", "9997");
					result.put("message","获取手续费异常");
					String json = JsonUtil.toJson(result);
					ResponseUtil.responseJson(response, json);
					return;
				}	
				String findFeeSql = "select FEE from  t_param_fee  where CODE = :purchase";
			    Map<String, Object> findFeeMap = new HashMap<String, Object>();
				findFeeMap.put("purchase", "purchase");
				ParamFee paramFee =	paramFeeDao.findBy(findFeeSql, findFeeMap);
				if (paramFee!=null&&paramFee.getFee()!=null) {
					purchase=(String.format("%.2f",paramFee.getFee()));
				} else{
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("code", "9997");
					result.put("message","获取费率异常");
					String json = JsonUtil.toJson(result);
					ResponseUtil.responseJson(response, json);
					return;
				}
        	        FastPay pay = new FastPay(KjConfig.kjpostUrl,kj_merno,kj_key);
                    //修改费率
		        	PayChannelRequest payChannelRequest = new PayChannelRequest();
					payChannelRequest.setAction(PayChannelRequest.Action.PAY_UPR);
					payChannelRequest.put("product_code", "CreditCardRefund");
					payChannelRequest.put("pay_ratio", purchase);
					payChannelRequest.put("mct_number", kj_merno);
					payChannelRequest.put("pay_charge", re_cash);
					logger.info("快捷修改费率提交报文："+payChannelRequest);
//		            if(1==1){
//		            	System.out.println(payChannelRequest);
//		            	quickPay.setRespCode("0000");
//        				quickPay.setRespDesc("成功");
//        				Map<String, Object>map=ResponseUtil.makeSuccessJson();
//        				map.put("transactionType","");
//        				map.put("code",quickPay.getRespCode());
//        				map.put("message",quickPay.getRespDesc());
//        				String json = JsonUtil.toJson(map);
//        				ResponseUtil.responseJson(response, json);
//        				return;
//		            }
					//执行请求
					FastPayResponse flfastPayResponse = pay.execute(payChannelRequest);
					logger.info("快捷修改费率返回报文："+flfastPayResponse);
					if("A0000".equals(flfastPayResponse.getError() != null ? flfastPayResponse.getError().toString() : "")){
						//创建信用卡还款请求对象
	        			CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
	        			creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYO);
	        			creditCardRefundRequest.put("card_number", cardNo);
	        			creditCardRefundRequest.put("order_code", orderNo);
	        			creditCardRefundRequest.put("order_money", trans_amount);
	        			creditCardRefundRequest.put("pay_code", "YTKuaiJie-CreditCardRefund");
	        			logger.info("快捷消费提交报文："+creditCardRefundRequest);
	        			//提交请求
	        			FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
	        			logger.info("快捷消费返回报文："+fastPayResponse);
	        			if ("A0000".equals(fastPayResponse.getError() != null ? fastPayResponse.getError().toString() : "")) {
	        				/*try {
	        					if(fastPayResponse.getMessage().indexOf("成功")==-1){
		        					ResponseUtil.sendFailJson(response, fastPayResponse.getMessage());
		        					return;
		        				}
							} catch (Exception e) {
								// TODO: handle exception
							}*/
	        				
	        				quickPay.setRespCode("9997");//所有都置为结算中
	        				quickPay.setRespDesc(fastPayResponse.getMessage());
	        				Map<String, Object>map=ResponseUtil.makeSuccessJson();
	        				map.put("transactionType","");
	        				map.put("code",quickPay.getRespCode());
	        				map.put("message",quickPay.getRespDesc());
	        				String json = JsonUtil.toJson(map);
	        				ResponseUtil.responseJson(response, json);
	        			} else {
	        				ResponseUtil.sendFailJson(response, fastPayResponse.getMessage());
	        				return;
	        			}
					}else{
						if(StringUtils.isEmpty(flfastPayResponse.getMessage())){
						ResponseUtil.sendFailJson(response, "设置账户出现异常！");
						}else{
						ResponseUtil.sendFailJson(response, flfastPayResponse.getMessage());
						}
        				return;
					}
        			
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
            	FastPay pay = new FastPay(KjConfig.kjpostUrl,KjConfig.kjmerNo,
       				 KjConfig.kjkey);

            	//创建订单请求对象
    			OrderRequest orderRequest = new OrderRequest();
    			orderRequest.setAction(OrderRequest.Action.ODR_SRH);
    			orderRequest.put("trade_order_code", orderNo);
    			logger.info("快捷支付查询提交报文："+orderRequest);
    			//提交请求
    			FastPayResponse fastPayResponse = pay.execute(orderRequest);			
    			logger.info("快捷支付查询返回报文："+fastPayResponse);
    			if ("A0000".equals(fastPayResponse.getError() != null ? fastPayResponse.getError().toString() : "")) {
    				quickPay.setRespCode("0000");
    				quickPay.setRespDesc("交易成功");
    				
    			} else {
    				quickPay.setRespCode("fail");
    				quickPay.setRespDesc(fastPayResponse.getMessage());
    			}
    			Map<String, Object>map=ResponseUtil.makeSuccessJson();
    			map.put("transactionType","");
    			map.put("code",quickPay.getRespCode());
    			map.put("message",quickPay.getRespDesc());
    			String json = JsonUtil.toJson(map);
    			ResponseUtil.responseJson(response, json);
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
			String kj_merno=request.getParameter("kjMero");
        	String kj_key=request.getParameter("kjKey");
        	if (StringUtils.isBlank(kj_merno)) {
				ResponseUtil.sendFailJson(response, "请进行报户！");
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
			
			
			if (StringUtils.isBlank(orderNo)) {
				ResponseUtil.sendFailJson(response, "商户订单号不能为空");
				return;
			}
			 
			
			
//    	        String findFeeSql2 = "select FEE from  t_param_fee  where CODE = :re_cash";
//    			Map<String, Object> findFeeMap2 = new HashMap<String, Object>();
//    			findFeeMap2.put("re_cash", "re_cash");
//    		    ParamFee paramFee2 =	paramFeeDao.findBy(findFeeSql2, findFeeMap2);
//    		 if (paramFee2!=null&&paramFee2.getFee()!=null) {
//    		    	//单笔提现手续费
//    			 transAmt=new BigDecimal(transAmt).add(new BigDecimal(paramFee2.getFee()*1))+"";
//    			} else{
//    				Map<String,Object> result = new HashMap<String,Object>();
//    				result.put("code", "9997");
//    				result.put("message","获取提现手续费异常");
//    				String json = JsonUtil.toJson(result);
//    				ResponseUtil.responseJson(response, json);
//    				return;
//    			}	
    	
//			transAmt =  new BigDecimal(transAmt).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)+"";
			FastPay pay = new FastPay(KjConfig.kjpostUrl,kj_merno,
				 kj_key);
			//创建信用卡还款请求对象
			CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
			creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYI);
			creditCardRefundRequest.put("card_number", acctNo);
			creditCardRefundRequest.put("order_code", orderNo);
			//
			creditCardRefundRequest.put("order_money",transAmt);
			creditCardRefundRequest.put("pay_code", "YTKuaiJie-CreditCardRefund");
			logger.info("快捷代付提交报文："+creditCardRefundRequest);
			
			
			//提交请求
			FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
			logger.info("快捷代付返回的报文："+fastPayResponse);
			 
			if ("A0000".equals(fastPayResponse.getError() != null ? fastPayResponse.getError().toString() : "")) {
				
				
				quickPay.setRespCode("0000");
				quickPay.setRespDesc("代付成功");
				 
			} else {
				ResponseUtil.sendFailJson(response, fastPayResponse.getMessage());
				return;
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
	
	
	public static void main(String[] args) {
		FastPay pay = new FastPay("http://api.duodianbao.net/FastPay/","K20180708703919",
				 "6afefdffc655a18ec34828d1c75c0d6f");
//		FastPay pay = new FastPay("http://debug.hgesy.com:8080/FastPay/","qlqw_test",
//		 "52c495b2aa2621a47c024f638157cb52");
		//创建信用卡还款请求对象
		//创建信用卡还款请求对象
//		CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYI);
//		creditCardRefundRequest.put("card_number", "438******16495");
//		creditCardRefundRequest.put("order_code", "TST1234567890");
//		creditCardRefundRequest.put("order_money", "100");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
		//创建信用卡还款请求对象
//		CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_GVC);
//		creditCardRefundRequest.put("phone", "13799776360");
//		creditCardRefundRequest.put("card_number", "6259063515694381");
//		
//		
	//	CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_BDC);
//		creditCardRefundRequest.put("phone", "13779952024");
//		creditCardRefundRequest.put("user_name", "陈根辉");
//		creditCardRefundRequest.put("card_number", "6259063515694381");
//		creditCardRefundRequest.put("id_card_number", "350206198810210014");
//		creditCardRefundRequest.put("card_month_year", "1121");
//		creditCardRefundRequest.put("car_cvn", "023");
//		creditCardRefundRequest.put("validate_code", "944360");
////		
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYI);
//		creditCardRefundRequest.put("card_number", "6226890016686395");
//		creditCardRefundRequest.put("order_code", "PL9B66416171640039654205131615744");
//		creditCardRefundRequest.put("order_money", "98");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
		 
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYO);
//		creditCardRefundRequest.put("card_number", "6217001930004911609");
//		creditCardRefundRequest.put("order_code", "TST1234567822a");
//		creditCardRefundRequest.put("order_money", "5.1");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
//		MerchantRequest merchantRequest = new MerchantRequest();
//		merchantRequest.setAction(MerchantRequest.Action.MCT_RST);
//		merchantRequest.put("parent_mct_number", "qlqw_test");
//		merchantRequest.put("mct_number", "cgh");
//		merchantRequest.put("mct_name", "陈根辉");
//		merchantRequest.put("phone", "13779952024");
//		merchantRequest.put("id_card_number", "350206198810210014");
//		merchantRequest.put("id_card_img_a", new File("图片路径"));
//		merchantRequest.put("id_card_img_b", new File("图片路径"));
//		merchantRequest.put("img_face", new File("图片路径"));
		
		//提交请求
//		FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
 
//		FastPay pay1 = new FastPay("http://api.duodianbao.net/FastPay/","K20180708297321",
//				 "9974101d81ccbdf8a6bd472008c5d68f");
//		//创建商户请求对象
//		MerchantRequest merchantRequest = new MerchantRequest();
//		merchantRequest.setAction(MerchantRequest.Action.MCT_BDC_A);
//		merchantRequest.put("card_number", "6217233006001349448");
//		merchantRequest.put("user_name", "兰涛");
//		merchantRequest.put("phone", "18909992207");
//		merchantRequest.put("bank_img",new File("bankCard.jpg"));
//		merchantRequest.put("bank_name", "工商银行");
//		merchantRequest.put("bank_branch_name", "工商银行");
//		merchantRequest.put("id_card_number", "65410119840528202X");

		
//		OrderRequest orderRequest = new OrderRequest();
//		orderRequest.setAction(OrderRequest.Action.ODR_SRH);
//		orderRequest.put("trade_order_code", "K18071020154200525");
//		//提交请求
//		FastPayResponse fastPayResponse = pay.execute(orderRequest);
//	 
	 
				//创建支付通道配置请求对象
//					PayChannelRequest payChannelRequest = new PayChannelRequest();
//					payChannelRequest.setAction(PayChannelRequest.Action.PAY_UPR);
//					payChannelRequest.put("product_code", "ReceiveMoney");
////					payChannelRequest.put("pay_code", "YMF-RCE-Money"); 
//					payChannelRequest.put("pay_ratio", "0.6");
//					payChannelRequest.put("mct_number", "K20180708901604");
//					payChannelRequest.put("pay_charge", "2");
	
	 

				//创建商户请求对象
				 MerchantRequest merchantRequest = new MerchantRequest();
				 merchantRequest.setAction(MerchantRequest.Action.MCT_SBM);
				 merchantRequest.put("pay_code", "HeLiBao-CreditCardRefund");
				 merchantRequest.put("card_number", "6226580046807687");

				//提交请求
				FastPayResponse fastPayResponse = pay.execute(merchantRequest);
		//创建收款请求对象
//		 ReceiveMoneyRequest receiveMoneyRequest = new ReceiveMoneyRequest();
//		 receiveMoneyRequest.setAction(ReceiveMoneyRequest.Action.RCM_RH5);
//		 receiveMoneyRequest.put("order_code", "zyx12346");
//		 receiveMoneyRequest.put("pay_code", "YMF-RCE-Money");
//		 receiveMoneyRequest.put("order_money", 50);
//		 receiveMoneyRequest.put("card_number", "6226890016686395");

		//提交请求
		//FastPayResponse fastPayResponse1 = pay.execute(payChannelRequest);
//		
//		AgencyPayMoneyRequest agencyPayMoneyRequest = new AgencyPayMoneyRequest();
//		 agencyPayMoneyRequest.setAction(AgencyPayMoneyRequest.Action.DPM_PAY);
//		 agencyPayMoneyRequest.put("order_code", "K1808211937350716-BD");
//		 agencyPayMoneyRequest.put("order_money", 6000);
//		 agencyPayMoneyRequest.put("card_number", "6222530793452827");
//		 agencyPayMoneyRequest.put("user_name", "王敏兰");
//		 agencyPayMoneyRequest.put("bank_name", "交通银行");
//
//		 FastPayResponse fastPayResponse = pay.execute(agencyPayMoneyRequest);
		
		System.out.println(fastPayResponse);
	}
}
