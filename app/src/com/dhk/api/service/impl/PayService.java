package com.dhk.api.service.impl;

import io.netty.util.internal.MathUtil;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.NetworkInterface;
import com.dhk.api.core.impl.BaseNetwork;
import com.dhk.api.core.impl.IssuePayResultData;
import com.dhk.api.core.impl.PayRequest;
import com.dhk.api.core.impl.PayResult;
import com.dhk.api.dao.ICreditCardDao;
import com.dhk.api.dao.IRegionDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.APPUser;
import com.dhk.api.entity.CardBin;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.service.IAPPUserService;
import com.dhk.api.service.ICardBinService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.api.service.ITransWaterService;
import com.dhk.api.tool.HttpClientUtils;
import com.dhk.api.tool.M;
import com.dhk.api.tool.Signature;
import com.dhk.api.ytutil.HttpHelper;
import com.dhk.api.ytutil.RequestMsg;
import com.dhk.api.ytutil.SignUtil;
import com.dhk.init.Constant;
import com.fast.pay.FastPay;
import com.fast.pay.core.FastPayResponse;
import com.fast.pay.core.product.PayChannelRequest;
import com.fast.pay.core.product.ReceiveMoneyRequest;



@Service("PayService")
public class PayService implements IPayService {
	protected static Logger logger = Logger.getLogger(PayService.class);
	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;
	@Resource(name = "CardBinService")
	private ICardBinService cardBinService;
	@Resource(name ="TransWaterService")
	private ITransWaterService transWaterService;
	@Resource(name = "RegionDao")
	private IRegionDao regionDao;
	@Resource(name = "CreditCardDao")
	private ICreditCardDao creditCardDao;
	@Override
	public PayResult creditPurchase(PayRequest re) {
		BaseNetwork net = new BaseNetwork( systemParamService.findParam("app_creditcard_server_url"));
		NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
		p.addParam("transAmt", re.getTransAmt());
		p.addParam("phoneNo", re.getPhoneNo());
		p.addParam("customerName", re.getCustomerName());
		p.addParam("cerdType", re.getCerdType());
		p.addParam("cerdId", re.getCerdId());
		p.addParam("acctNo", re.getAcctNo());
		p.addParam("cvn2", re.getCvn2());
		p.addParam("expDate", re.getExpDate());
		p.addParam("userId", re.getUserId());
		PayResult resx = new PayResult();
		String retstr = null;
		try {
			retstr = net.getResultStr(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (retstr == null)
			return resx;
		JSONObject job = JSON.parseObject(retstr);
		resx.setCode(job.getString("code"));
		resx.setMessage(job.getString("message"));
		M.logger.debug("creditPurchase PayResult:" + resx);
		return resx;
	}

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Override
	public PayResult debitPurchase(PayRequest re) {
		BaseNetwork net = new BaseNetwork(systemParamService.findParam("app_usercard_server_url"));
		NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
		p.addParam("transAmt", re.getTransAmt());
		p.addParam("phoneNo", re.getPhoneNo());
		p.addParam("customerName", re.getCustomerName());
		p.addParam("cerdType", re.getCerdType());
		p.addParam("cerdId", re.getCerdId());
		p.addParam("acctNo", re.getAcctNo());
		p.addParam("userId", re.getUserId());
		PayResult resx = new PayResult();
		String retstr = null;
		try {
			retstr = net.getResultStr(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (retstr == null)
			return resx;
		JSONObject job = JSON.parseObject(retstr);
		resx.setCode(job.getString("code"));
		resx.setMessage(job.getString("message"));
		M.logger.debug("debitPurchase PayResult:" + resx);
		return resx;
	}

	public PayResult debitPurchase_recharge(PayRequest re) {
		BaseNetwork net = new BaseNetwork(systemParamService.findParam("server_url_i")+"/recharge");
		NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
		p.addParam("transAmt", re.getTransAmt());
		p.addParam("phoneNo", re.getPhoneNo());
		p.addParam("customerName", re.getCustomerName());
		p.addParam("cerdType", re.getCerdType());
		p.addParam("cerdId", re.getCerdId());
		p.addParam("acctNo", re.getAcctNo());
		p.addParam("userId", re.getUserId());
		
		p.addParam("cvn2", re.getCvn2());
		p.addParam("expDate", re.getExpDate());
		PayResult resx = new PayResult();
		String retstr = null;
		try {
			PayResult s = new PayResult();
			retstr = net.getResultStr(p);
		}catch (SocketTimeoutException e){
			e.printStackTrace();
			resx.setCode("9997");
			return resx;
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		if (retstr == null)return resx;
		JSONObject job = JSON.parseObject(retstr);
		resx.setCode(job.getString("code"));
		resx.setMessage(job.getString("message"));
		return resx;
	}

	@Override
	public PayResult fudeIssue(PayRequest re) {
		BaseNetwork net = new BaseNetwork(systemParamService.findParam("app_userissue_server_url"));
		NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
		p.addParam("insuredName", re.getCustomerName());
		p.addParam("insuredSex", "9");
		p.addParam("insuredCertificateNo", re.getCerdId());
		p.addParam("issueType", "D000033242");
		p.addParam("insuredId", re.getUserId());
		PayResult resx = new PayResult();
		String retstr = null;
		try {
			retstr = net.getResultStr(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (retstr == null)
			return resx;
		JSONObject job = JSON.parseObject(retstr);
		resx.setCode(job.getString("code"));
		resx.setMessage(job.getString("message"));
		JSONObject data = job.getJSONObject("data");
		IssuePayResultData d = new IssuePayResultData();
		d.setApplyNo(data.getString("applyNo"));
		d.setPolicyNo(data.getString("policyNo"));
		d.setInsuranceBeginDate(data.getString("insuranceBeginDate"));
		d.setInsuranceEndDate(data.getString("insuranceEndDate"));
		d.setInsuredAmount(data.getString("insuredAmount"));
		d.setPremium(data.getString("premium"));
		resx.setData(d);
		M.logger.debug("fudeIssue PayResult:" + resx);
		return resx;
	}

	@Test
	public void issueTest() {
		BaseNetwork net = new BaseNetwork(
				"http://120.24.37.139:8083/payment/funde/issue");
		NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
		p.addParam("insuredName", "去春华");
		p.addParam("insuredSex", "9");
		p.addParam("insuredCertificateNo", "350823199498787654");
		p.addParam("issueType", "D000033242");
		p.addParam("insuredId", 12);
		PayResult resx = new PayResult();
		String retstr = null;
		try {
			retstr = net.getResultStr(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (retstr == null)
			System.out.println("fail");
		JSONObject job = JSON.parseObject(retstr);
		resx.setCode(job.getString("code"));
		resx.setMessage(job.getString("message"));
		JSONObject data = job.getJSONObject("data");
		IssuePayResultData d = new IssuePayResultData();
		d.setApplyNo(data.getString("applyNo"));
		d.setPolicyNo(data.getString("policyNo"));
		d.setInsuranceBeginDate(data.getString("insuranceBeginDate"));
		d.setInsuranceEndDate(data.getString("insuranceEndDate"));
		d.setInsuredAmount(data.getString("insuredAmount"));
		d.setPremium(data.getString("premium"));
		resx.setData(d);
		System.out.println(data);
		System.out.println("d:" + d);
		System.out.println("fudeIssue PayResult:" + resx);
	}
	
	public PayResult XrPayQuickzxEPay(String amt,IdentityDto dto,String cardNo){
		PayResult resx = new PayResult();
		//报文所需参数
		String pmerNo = Constant.pmerNo;;//服务商编码写死
		String key = Constant.key;//密钥也是写死
		//报文必填值
		String versionId = "1.0";//总金额,以分为单位【必填】
		String orderAmount = (Integer.valueOf(amt)*100)+"";//inParam.getString("amt", null);//总金额,以分为单位【必填】
		String orderDate =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//
		String currency = "RMB";//
		String transType = "0008";//
		String asynNotifyUrl =Constant.asynNotifyUrl; //NodeConfig.domain + XrPay_Config.map.get(XrPayConstants.PAYTYPE56).getNotify_url();//
		String synNotifyUrl = Constant.synNotifyUrl+"?user_id="+dto.getUserId()+"&token="+dto.getToken();//NodeConfig.domain + XrPay_Config.map.get(XrPayConstants.PAYTYPE56).getFont_url();//
		String signType = "MD5";//
		APPUser appUser = appUserService.findById(NumberUtils.toLong(dto.getUserId()));
		String sub_merId = "";//inParam.getString("upMerId", null);//子商户号
	if(appUser!=null){
		if(!"1".equals(appUser.getIsAuth())){
			resx.setCode("0004");
			resx.setMessage("请先认证");
			return resx;
		}
		if(appUser.getSaleMerchantid()==null){
			resx.setCode("0002");
			resx.setMessage("请先报户");
			return resx;
		}
		sub_merId = appUser.getSaleMerchantid();
		}else{
			resx.setCode("0001");
			resx.setMessage("请先登录");
			return resx;
		}
		String prdOrdNo = appUserService.getOrderNo(NumberUtils.toLong(dto.getUserId()), appUser.getMobilephone());//inParam.getString("merchOrderId", null);//3
		String payMode = "00049";//支付方式
		String receivableType ="D00";//到账类型
		
		String pphoneNo = appUser.getMobilephone();//付款手机号
		String customerName = appUser.getRealname();//付款人姓名
		String prdName = "小优商品";//商品名称
		if(StringUtils.isBlank(cardNo)){
			resx.setCode("0003");
			resx.setMessage("卡号不能为空");
			return resx;
		}
		
		String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
		Map<String, Object> maparam = new HashMap<String, Object>();
		maparam.put("userId", dto.getUserId());
		maparam.put("card_no", cardNo);
		CreditCard creditCard = null;
		try{
		List<CreditCard> t = creditCardDao.find_Personal(sql, maparam);
		if (t != null && t.size() > 0) {
			creditCard = t.get(0);
		  }
			} catch (Exception e) {
				e.printStackTrace();
				resx.setCode("0006");
				resx.setMessage("查询卡信息异常");
				return resx;
			}
		if(creditCard==null || StringUtils.isEmpty(creditCard.getCvn2())  || StringUtils.isEmpty(creditCard.getExpdate())){
			resx.setCode("0005");
			resx.setMessage("卡信息异常");
			return resx;
		}
		
		String expDate = "";
		if(creditCard.getExpdate()!=null){
			expDate = creditCard.getExpdate().substring(2,4)+creditCard.getExpdate().substring(0,2);
		}
		String cvn2  = creditCard.getCvn2();
		
		String cardBin=cardNo.substring(0,6);
		
		String acctNo = cardNo;//消费卡号
		String tranChannel ="";//银行编号
		CardBin cardBinObj= cardBinService.getCarbinByCardNo(cardBin+"%'");
		  for (Map.Entry<String, String> entry : Constant.bankCode.entrySet()) {
			   if(cardBinObj.getBankName().trim()!=null&&cardBinObj.getBankName().trim().contains(entry.getKey())){
				   tranChannel = entry.getValue();
				   break;
			   }
			  }
		  if(StringUtils.isBlank(tranChannel)){
			  resx.setCode("0005");
				resx.setMessage("该卡不支持");
				return resx;
		  }
		  Map<String,String> map = new  HashMap<>();
		  map.put("merId", pmerNo);//商户编号
		  map.put("versionId", versionId);//服务版本号
		  map.put("orderAmount", orderAmount);//订单金额
		  map.put("orderDate", orderDate);//订单日期
		  map.put("expDate", expDate);//信用卡有效期
		  map.put("cvn2", cvn2);//cvn2
		  map.put("currency", currency);//货币类型
		  map.put("transType", transType);//交易类别
		  map.put("asynNotifyUrl", asynNotifyUrl);//异步通知URL
		  map.put("synNotifyUrl", synNotifyUrl);//同步返回UR
		  map.put("signType", signType);//加密方式
		  map.put("sub_merId", sub_merId);//子商户号
		  map.put("prdOrdNo", prdOrdNo);//商户订单号
		  map.put("payMode", payMode);//支付方式
		  map.put("receivableType", receivableType);//到账类型
		  map.put("pphoneNo", pphoneNo);//付款手机号
		  map.put("customerName", customerName);//付款人姓名
		  map.put("prdName", prdName);//商品名称
		  map.put("acctNo", acctNo);//消费卡号
		  map.put("tranChannel", tranChannel);//银行编码
		  //写流水
		  BigDecimal fee = new BigDecimal(amt).multiply(new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getFee())).divide(new BigDecimal(100));
		  BigDecimal  external =new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getExternal());
			long translsId = transWaterService.addTransls(prdOrdNo, appUser, cardNo, new BigDecimal(amt), fee,external, 0l,0l,"0");
			String sign = Signature.createSign(map, key);
			map.put("signData", sign);
			// 调用汇享报户
			try {
				logger.info("提交的报文："+map);
		//	String result = 	generateAutoSubmitForm(Constant.postUrl, map);
				String result = 	HttpClientUtils.httpPostRequest(Constant.postUrl, map);
			logger.info("返回的报文："+result);
					resx.setCode("0000");
					resx.setMessage(result);
					return resx;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resx.setCode("0007");
				resx.setMessage("报户成功，更新用户状态异常");
				return resx;
			}
	}
	
	public PayResult KjPayQuickzxEPay(String amt,IdentityDto dto,String cardNo,String payCode){
		PayResult resx = new PayResult();
		 
		APPUser appUser = appUserService.findById(NumberUtils.toLong(dto.getUserId()));
		 
	    if(appUser!=null){
		if(!"1".equals(appUser.getIsAuth())){
			resx.setCode("0004");
			resx.setMessage("请先认证");
			return resx;
		}
		if(appUser.getKjMerno()==null){
			resx.setCode("0002");
			resx.setMessage("请先报户");
			return resx;
		}
		 
		}else{
			resx.setCode("0001");
			resx.setMessage("请先登录");
			return resx;
		}
		String prdOrdNo = appUserService.getOrderNo(NumberUtils.toLong(dto.getUserId()), appUser.getMobilephone());//inParam.getString("merchOrderId", null);//3
		 
		String pphoneNo = appUser.getMobilephone();//付款手机号
		String customerName = appUser.getRealname();//付款人姓名
		 
		if(StringUtils.isBlank(cardNo)){
			resx.setCode("0003");
			resx.setMessage("卡号不能为空");
			return resx;
		}
		
		if(StringUtils.isBlank(payCode)){
			resx.setCode("0005");
			resx.setMessage("请选择通道");
			return resx;
		}

		
		String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
		Map<String, Object> maparam = new HashMap<String, Object>();
		maparam.put("userId", dto.getUserId());
		maparam.put("card_no", cardNo);
		CreditCard creditCard = null;
		try{
		List<CreditCard> t = creditCardDao.find_Personal(sql, maparam);
		if (t != null && t.size() > 0) {
			creditCard = t.get(0);
		  }
			} catch (Exception e) {
				e.printStackTrace();
				resx.setCode("0006");
				resx.setMessage("查询卡信息异常");
				return resx;
			}
		if(creditCard==null || StringUtils.isEmpty(creditCard.getCvn2())  || StringUtils.isEmpty(creditCard.getExpdate())){
			resx.setCode("0005");
			resx.setMessage("卡信息异常");
			return resx;
		}
		
		String expDate = "";
		if(creditCard.getExpdate()!=null){
			expDate = creditCard.getExpdate().substring(2,4)+creditCard.getExpdate().substring(0,2);
		}
		String cvn2  = creditCard.getCvn2();
		
		String cardBin=cardNo.substring(0,6);
		
		String acctNo = cardNo;//消费卡号
		String tranChannel ="";//银行编号
		CardBin cardBinObj= cardBinService.getCarbinByCardNo(cardBin+"%'");
		  for (Map.Entry<String, String> entry : Constant.bankCode.entrySet()) {
			   if(cardBinObj.getBankName().trim()!=null&&cardBinObj.getBankName().trim().contains(entry.getKey())){
				   tranChannel = entry.getValue();
				   break;
			   }
			  }
		  if(StringUtils.isBlank(tranChannel)){
			  resx.setCode("0005");
				resx.setMessage("该卡不支持");
				return resx;
		  }
		  Map<String,String> map = new  HashMap<>();
		 
		  //写流水
		  BigDecimal fee = new BigDecimal(amt).multiply(new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getFee())).divide(new BigDecimal(100));
		  BigDecimal  external =new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getExternal());
		  long translsId = transWaterService.addTransls(prdOrdNo, appUser, cardNo, new BigDecimal(amt), fee,external, 0l,0l,"0");
		 
			// 调用
			try {
				logger.info("用户商户号："+appUser.getKjMerno()+"，用户key："+appUser.getKjKey());
				//先修改费率
				FastPay pay = new FastPay(Constant.kjpostUrl,appUser.getKjMerno(),
						appUser.getKjKey());
                //修改费率
	        	PayChannelRequest payChannelRequest = new PayChannelRequest();
				payChannelRequest.setAction(PayChannelRequest.Action.PAY_UPR);
				payChannelRequest.put("product_code", "ReceiveMoney");
//				payChannelRequest.put("pay_code", "YMF-RCE-Money"); 
				payChannelRequest.put("pay_ratio", String.format("%.2f",new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getFee())));
				payChannelRequest.put("mct_number", appUser.getKjMerno());
				payChannelRequest.put("pay_charge", external);
				logger.info("快捷修改费率提交报文："+payChannelRequest);
 
				//执行请求
				FastPayResponse flfastPayResponse = pay.execute(payChannelRequest);
				logger.info("快捷修改费率返回报文："+flfastPayResponse);
				if("A0000".equals(flfastPayResponse.getError() != null ? flfastPayResponse.getError().toString() : "")){
				
				 pay = new FastPay(Constant.kjpostUrl,appUser.getKjMerno(),
						appUser.getKjKey());

			   //创建信用卡还款请求对象
				 ReceiveMoneyRequest receiveMoneyRequest = new ReceiveMoneyRequest();
				 receiveMoneyRequest.setAction(ReceiveMoneyRequest.Action.RCM_RH5);
				 receiveMoneyRequest.put("order_code", prdOrdNo);
				 //receiveMoneyRequest.put("pay_code", "ZM-RCE-Money");
				 receiveMoneyRequest.put("pay_code", payCode);
				 receiveMoneyRequest.put("order_money", amt);
				 receiveMoneyRequest.put("card_number", cardNo);
			     logger.info("提交的报文："+receiveMoneyRequest);
			//提交请求
			        FastPayResponse fastPayResponse = pay.execute(receiveMoneyRequest);
			 
		        	logger.info("返回的报文："+fastPayResponse);
		        	if("A0000".equals(fastPayResponse.getError())){
		        		resx.setCode("0000");
		        		JSONObject json=new JSONObject(fastPayResponse);
						resx.setMessage(json.getString("data"));
		        	}else{
		        		resx.setCode("0007");
						resx.setMessage(fastPayResponse.getMessage());
		        	}
					
					
				}else{
					resx.setCode("0007");
					resx.setMessage(flfastPayResponse.getMessage());
				}
				return resx;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resx.setCode("0007");
				resx.setMessage("调用接口失败");
				return resx;
			}
	}
	
	
	public PayResult YtPayQuickzxEPay(String amt,IdentityDto dto,String cardNo){
		PayResult resx = new PayResult();
		 
		APPUser appUser = appUserService.findById(NumberUtils.toLong(dto.getUserId()));
	 
	if(appUser!=null){
		if(!"1".equals(appUser.getIsAuth())){
			resx.setCode("0004");
			resx.setMessage("请先认证");
			return resx;
		}
		}else{
			resx.setCode("0001");
			resx.setMessage("请先登录");
			return resx;
		}
		String prdOrdNo = appUserService.getOrderNo(NumberUtils.toLong(dto.getUserId()), appUser.getMobilephone());//inParam.getString("merchOrderId", null);//3
		 
		if(StringUtils.isBlank(cardNo)){
			resx.setCode("0003");
			resx.setMessage("卡号不能为空");
			return resx;
		}
		
		String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
		Map<String, Object> maparam = new HashMap<String, Object>();
		maparam.put("userId", dto.getUserId());
		maparam.put("card_no", cardNo);
		CreditCard creditCard = null;
		try{
		List<CreditCard> t = creditCardDao.find_Personal(sql, maparam);
		if (t != null && t.size() > 0) {
			creditCard = t.get(0);
		  }
			} catch (Exception e) {
				e.printStackTrace();
				resx.setCode("0006");
				resx.setMessage("查询卡信息异常");
				return resx;
			}
		if(creditCard==null || StringUtils.isEmpty(creditCard.getCvn2())  || StringUtils.isEmpty(creditCard.getExpdate())){
			resx.setCode("0005");
			resx.setMessage("卡信息异常");
			return resx;
		}
		
		 
		 
		
		String cardBin=cardNo.substring(0,6);
		 
		String tranChannel ="";//银行编号
		CardBin cardBinObj= cardBinService.getCarbinByCardNo(cardBin+"%'");
		  for (Map.Entry<String, String> entry : Constant.bankCode.entrySet()) {
			   if(cardBinObj.getBankName().trim()!=null&&cardBinObj.getBankName().trim().contains(entry.getKey())){
				   tranChannel = entry.getValue();
				   break;
			   }
			  }
		  if(StringUtils.isBlank(tranChannel)){
			  resx.setCode("0005");
				resx.setMessage("该卡不支持");
				return resx;
		  }
		  amt="0";
		  //写流水
		  BigDecimal fee = new BigDecimal(0).multiply(new BigDecimal(Constant.feeMaps.get("user_only_purchase_cost").getFee())).divide(new BigDecimal(100));
		  BigDecimal  external =new BigDecimal(0);
		  long translsId = transWaterService.addTransls(prdOrdNo, appUser, cardNo, new BigDecimal(amt), fee,external, 0l,0l,"6","境外交易");
		 
			 
			try {
				// 拼接请求参数
				String params = this.getQuickPayJNH5Params(prdOrdNo, appUser.getIdNumber(), appUser.getRealname(), appUser.getMobilephone(), cardNo, dto);
				 logger.info("请求参数："+params);
				String url =Constant.YT_POSTURL; //入网地址
				//发送请求
				String result = HttpHelper.doHttp(url, HttpHelper.POST, "UTF-8", params, "60000");
			    logger.info("返回的报文："+result);
					resx.setCode("0000");
					resx.setMessage(result.replaceAll("HTML", "html"));
					return resx;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resx.setCode("0007");
				resx.setMessage("消费失败！");
				return resx;
			}
	}
	/**
	 * 优选
	 * @param amt
	 * @param dto
	 * @param cardNo
	 * @return
	 */
	public PayResult YtyxPayQuickzxEPay(String amt,IdentityDto dto,String cardNo){
		PayResult resx = new PayResult();
		 
		APPUser appUser = appUserService.findById(NumberUtils.toLong(dto.getUserId()));
	 
	if(appUser!=null){
		if(!"1".equals(appUser.getIsAuth())){
			resx.setCode("0004");
			resx.setMessage("请先认证");
			return resx;
		}
		}else{
			resx.setCode("0001");
			resx.setMessage("请先登录");
			return resx;
		}
		String prdOrdNo = appUserService.getOrderNo(NumberUtils.toLong(dto.getUserId()), appUser.getMobilephone());//inParam.getString("merchOrderId", null);//3
		 
		if(StringUtils.isBlank(cardNo)){
			resx.setCode("0003");
			resx.setMessage("卡号不能为空");
			return resx;
		}
		
		String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
		Map<String, Object> maparam = new HashMap<String, Object>();
		maparam.put("userId", dto.getUserId());
		maparam.put("card_no", cardNo);
		CreditCard creditCard = null;
		try{
		List<CreditCard> t = creditCardDao.find_Personal(sql, maparam);
		if (t != null && t.size() > 0) {
			creditCard = t.get(0);
		  }
			} catch (Exception e) {
				e.printStackTrace();
				resx.setCode("0006");
				resx.setMessage("查询卡信息异常");
				return resx;
			}
		if(creditCard==null || StringUtils.isEmpty(creditCard.getCvn2())  || StringUtils.isEmpty(creditCard.getExpdate())){
			resx.setCode("0005");
			resx.setMessage("卡信息异常");
			return resx;
		}
		
		 
		 
		
		String cardBin=cardNo.substring(0,6);
		 
		String tranChannel ="";//银行编号
		CardBin cardBinObj= cardBinService.getCarbinByCardNo(cardBin+"%'");
		  for (Map.Entry<String, String> entry : Constant.bankCode.entrySet()) {
			   if(cardBinObj.getBankName().trim()!=null&&cardBinObj.getBankName().trim().contains(entry.getKey())){
				   tranChannel = entry.getValue();
				   break;
			   }
			  }
		  if(StringUtils.isBlank(tranChannel)){
			  resx.setCode("0005");
				resx.setMessage("该卡不支持");
				return resx;
		  }
		 
		  //写流水
		  BigDecimal fee = new BigDecimal(amt).multiply(new BigDecimal(Constant.feeMaps.get("user_only_purchase_yx_cost").getFee())).divide(new BigDecimal(100));
		  BigDecimal  external =new BigDecimal(Constant.feeMaps.get("user_only_purchase_yx_cost").getExternal());
		  long translsId = transWaterService.addTransls(prdOrdNo, appUser, cardNo, new BigDecimal(amt), fee,external, 0l,0l,"0");
		 
			 
			try {
				// 拼接请求参数
				String params = this.getyxQuickPayParams(prdOrdNo, appUser.getIdNumber(),amt ,appUser.getRealname(), appUser.getMobilephone(), cardNo, dto,appUser.getCardNo(),appUser.getBankName());
				 logger.info("请求参数："+params);
				String url =Constant.YTJX_POSTURL; //入网地址
				//发送请求
				String result = HttpHelper.doHttp(url, HttpHelper.POST, "UTF-8", params, "60000");
			    logger.info("返回的报文："+result);
					resx.setCode("0000");
					resx.setMessage(result.replaceAll("HTML", "html"));
					return resx;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resx.setCode("0007");
				resx.setMessage("消费失败！");
				return resx;
			}
	}
	/**
	 * 精选
	 * @param amt
	 * @param dto
	 * @param cardNo
	 * @return
	 */
	public PayResult YtjxPayQuickzxEPay(String amt,IdentityDto dto,String cardNo){
		PayResult resx = new PayResult();
		 
		APPUser appUser = appUserService.findById(NumberUtils.toLong(dto.getUserId()));
	 
	if(appUser!=null){
		if(!"1".equals(appUser.getIsAuth())){
			resx.setCode("0004");
			resx.setMessage("请先认证");
			return resx;
		}
		if(appUser.getYtjx_status()==null){
			resx.setCode("0002");
			resx.setMessage("请先报户");
			return resx;
		}
		}else{
			resx.setCode("0001");
			resx.setMessage("请先登录");
			return resx;
		}
		String prdOrdNo = appUserService.getOrderNo(NumberUtils.toLong(dto.getUserId()), appUser.getMobilephone());//inParam.getString("merchOrderId", null);//3
		 
		if(StringUtils.isBlank(cardNo)){
			resx.setCode("0003");
			resx.setMessage("卡号不能为空");
			return resx;
		}
		
		String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
		Map<String, Object> maparam = new HashMap<String, Object>();
		maparam.put("userId", dto.getUserId());
		maparam.put("card_no", cardNo);
		CreditCard creditCard = null;
		try{
		List<CreditCard> t = creditCardDao.find_Personal(sql, maparam);
		if (t != null && t.size() > 0) {
			creditCard = t.get(0);
		  }
			} catch (Exception e) {
				e.printStackTrace();
				resx.setCode("0006");
				resx.setMessage("查询卡信息异常");
				return resx;
			}
		if(creditCard==null || StringUtils.isEmpty(creditCard.getCvn2())  || StringUtils.isEmpty(creditCard.getExpdate())){
			resx.setCode("0005");
			resx.setMessage("卡信息异常");
			return resx;
		}
		
		 
		 
		
		String cardBin=cardNo.substring(0,6);
		 
		String tranChannel ="";//银行编号
		CardBin cardBinObj= cardBinService.getCarbinByCardNo(cardBin+"%'");
		  for (Map.Entry<String, String> entry : Constant.bankCode.entrySet()) {
			   if(cardBinObj.getBankName().trim()!=null&&cardBinObj.getBankName().trim().contains(entry.getKey())){
				   tranChannel = entry.getValue();
				   break;
			   }
			  }
		  if(StringUtils.isBlank(tranChannel)){
			  resx.setCode("0005");
				resx.setMessage("该卡不支持");
				return resx;
		  }
		  amt="0";
		  //写流水
		  BigDecimal fee = new BigDecimal(amt).multiply(new BigDecimal(Constant.feeMaps.get("user_only_purchase_jx_cost").getFee())).divide(new BigDecimal(100));
		  BigDecimal  external =new BigDecimal(Constant.feeMaps.get("user_only_purchase_jx_cost").getExternal());
		  long translsId = transWaterService.addTransls(prdOrdNo, appUser, cardNo, new BigDecimal(amt), fee,external, 0l,0l,"0");
		 
			 
			try {
				// 拼接请求参数
				String params = this.getjxQuickPayParams(prdOrdNo, appUser.getIdNumber(),amt ,appUser.getRealname(), appUser.getMobilephone(), cardNo, dto,appUser.getCardNo(),appUser.getBankName(),appUser);
				 logger.info("请求参数："+params);
				String url =Constant.YTJX_POSTURL; //入网地址
				//发送请求
				String result = HttpHelper.doHttp(url, HttpHelper.POST, "UTF-8", params, "60000");
			    logger.info("返回的报文："+result);
					resx.setCode("0000");
					resx.setMessage(result.replaceAll("HTML", "html"));
					return resx;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				resx.setCode("0007");
				resx.setMessage("消费失败！");
				return resx;
			}
	}
	
	
	public String getQuickPayJNH5Params(String orderno,String idcrad,String username,String phone,String yhkh,IdentityDto dto){
		String    ORDER_ID  =orderno;        //Test20170206164539185                        
	//	String    ORDER_AMT =String.format("%.2f",Double.valueOf(amt));                                     
	//	String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
//		String    PAY_TYPE  ="13";                                       
//		String    USER_TYPE ="02";                                         
		String    BG_URL    =Constant.BG_URL;
		
		String    PAGE_URL  =Constant.PAGE_URL+"?userid="+dto.getUserId()+"&token="+dto.getToken();
		String    USER_ID   =Constant.USER_ID;       //商户号
//		String    SIGN_TYPE ="03";                               
		String    AGENT_NO  =Constant.AGENT_NO;               //业务代码                     
//		String    CCT       ="CNY";                                 
//		String    GOODS_NAME=Constant.GOODS_NAME;                                  
//		String    GOODS_DESC=Constant.GOODS_DESC;  
		String    ID_NO     = idcrad;
//		String    SETT_ACCT_NO = settacctno;
//		String    CARD_INST_NAME = acctbankname;
		String    NAME        = username;
		String    PHONE_NO   = phone;
		String    ACCT_NO   = yhkh; //支付卡号
		String    ADDRESS="收货地址"; 
		 
		
//		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
        		"&USER_ID="+USER_ID+
			"&AGENT_NO="+AGENT_NO+
			"&NAME="+NAME+
			"&ID_NO="+ID_NO+
			"&BG_URL="+BG_URL+
			"&PAGE_URL="+PAGE_URL+
			//"&SIGN="+signUtil.makeSign(requestMsg)+
			"&ADDRESS="+ADDRESS+
			"&ACCT_NO="+ACCT_NO+
			"&PHONE_NO="+PHONE_NO;
		
		return param;
	}
	
	public String getjxQuickPayParams(String orderno,String idcrad,String amt,String username,String phone,String yhkh,IdentityDto dto,String settacctno,String acctbankname,APPUser user){
		String    ORDER_ID  =orderno;        //Test20170206164539185                        
			String    ORDER_AMT =String.format("%.2f",Double.valueOf(amt));                                     
			String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
			String    PAY_TYPE  ="13";                                       
			String    USER_TYPE ="02";                                         
			String    BG_URL    =Constant.BG_URL;
			String    BUS_CODE  =Constant.BUS_CODE_JX;
			String    PAGE_URL  =Constant.PAGE_URL+"?userid="+dto.getUserId();
			String    USER_ID   =user.getYtjx_merno();       //商户号
			String    SIGN_TYPE ="03";                               
		    String    CCT       ="CNY";                                 
			String    GOODS_NAME=Constant.GOODS_NAME;                                  
			String    GOODS_DESC=Constant.GOODS_DESC;  
			String    ID_NO     = idcrad;
			String    SETT_ACCT_NO = settacctno;
			String    CARD_INST_NAME = acctbankname;
			String    NAME        = username;
			String    PHONE_NO   = phone;
			String    ADD1   = yhkh; //支付卡号
		 
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setID_NO(ID_NO);
		requestMsg.setPHONE_NO(PHONE_NO);
		requestMsg.setSETT_ACCT_NO(SETT_ACCT_NO);
		requestMsg.setNAME(NAME);
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setTRANS_AMT1(ORDER_AMT);                 //金额，字符串型
		requestMsg.setTRANS_ATIME(ORDER_TIME);                //订单时间
		requestMsg.setPAY_TYPE(PAY_TYPE);                     //支付类型
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setBGURL(BG_URL);                          //后台通知地址
		requestMsg.setPAGEURL(PAGE_URL);                      //页面通知地址
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setMSG_TYPE(BUS_CODE);                     //业务代码
		requestMsg.setCCT(CCT);                               //币种
		requestMsg.setGOODS_NAME(GOODS_NAME);                 //商品名
		requestMsg.setGOODS_DESC(GOODS_DESC);                 //商品描述
		requestMsg.setADD1(ADD1);                           //预留字段，原样返回  
		requestMsg.setADD2("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD3("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD4("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD5("ADD1");                           //预留字段，原样返回  
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&ORDER_AMT="+ORDER_AMT+
			"&ORDER_TIME="+ORDER_TIME+
			"&PAY_TYPE="+PAY_TYPE+
			"&USER_TYPE="+USER_TYPE+
			"&BG_URL="+BG_URL+
			"&PAGE_URL="+PAGE_URL+
			"&USER_ID="+USER_ID+
			"&SIGN="+signUtil.makeSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&CCT="+CCT+
			"&GOODS_NAME="+GOODS_NAME+
			"&GOODS_DESC="+GOODS_DESC+
			"&PHONE_NO="+PHONE_NO+
			"&ID_NO="+ID_NO+
			"&SETT_ACCT_NO="+SETT_ACCT_NO+
			"&CARD_INST_NAME="+CARD_INST_NAME+
			"&NAME="+NAME+
			"&ADD1="+ADD1;
		
		return param;
	}
	
	public String getyxQuickPayParams(String orderno,String idcrad,String amt,String username,String phone,String yhkh,IdentityDto dto,String settacctno,String acctbankname){
		String    ORDER_ID  =orderno;        //Test20170206164539185                        
			String    ORDER_AMT =String.format("%.2f",Double.valueOf(amt));                                     
			String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
			String    PAY_TYPE  ="13";                                       
			String    USER_TYPE ="02";                                         
			String    BG_URL    =Constant.BG_URL;
			String    BUS_CODE  =Constant.BUS_CODE_YX;
			String    PAGE_URL  =Constant.PAGE_URL+"?userid="+dto.getUserId()+"&token="+dto.getToken();
			String    USER_ID   =Constant.YX_USER_ID;       //商户号
			String    SIGN_TYPE ="03";                               
		    String    CCT       ="CNY";                                 
			String    GOODS_NAME=Constant.GOODS_NAME;                                  
			String    GOODS_DESC=Constant.GOODS_DESC;  
			String    ID_NO     = idcrad;
			String    SETT_ACCT_NO = settacctno;
			String    CARD_INST_NAME = acctbankname;
			String    NAME        = username;
			String    PHONE_NO   = phone;
			String    ADD1   = yhkh; //支付卡号
		 
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setID_NO(ID_NO);
		requestMsg.setPHONE_NO(PHONE_NO);
		requestMsg.setSETT_ACCT_NO(SETT_ACCT_NO);
		requestMsg.setNAME(NAME);
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setTRANS_AMT1(ORDER_AMT);                 //金额，字符串型
		requestMsg.setTRANS_ATIME(ORDER_TIME);                //订单时间
		requestMsg.setPAY_TYPE(PAY_TYPE);                     //支付类型
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setBGURL(BG_URL);                          //后台通知地址
		requestMsg.setPAGEURL(PAGE_URL);                      //页面通知地址
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setMSG_TYPE(BUS_CODE);                     //业务代码
		requestMsg.setCCT(CCT);                               //币种
		requestMsg.setGOODS_NAME(GOODS_NAME);                 //商品名
		requestMsg.setGOODS_DESC(GOODS_DESC);                 //商品描述
		requestMsg.setADD1(ADD1);                           //预留字段，原样返回  
		requestMsg.setADD2("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD3("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD4("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD5("ADD1");                           //预留字段，原样返回  
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&ORDER_AMT="+ORDER_AMT+
			"&ORDER_TIME="+ORDER_TIME+
			"&PAY_TYPE="+PAY_TYPE+
			"&USER_TYPE="+USER_TYPE+
			"&BG_URL="+BG_URL+
			"&PAGE_URL="+PAGE_URL+
			"&USER_ID="+USER_ID+
			"&SIGN="+signUtil.makeSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&CCT="+CCT+
			"&GOODS_NAME="+GOODS_NAME+
			"&GOODS_DESC="+GOODS_DESC+
			"&PHONE_NO="+PHONE_NO+
			"&ID_NO="+ID_NO+
			"&SETT_ACCT_NO="+SETT_ACCT_NO+
			"&CARD_INST_NAME="+CARD_INST_NAME+
			"&NAME="+NAME+
			"&ADD1="+ADD1;
		
		return param;
	}
	private static String generateAutoSubmitForm(String actionUrl, Map<String, String> paramMap) {
		StringBuilder html = new StringBuilder();
		html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
		html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(actionUrl).append("\" method=\"post\">\n");
		for (String key : paramMap.keySet()) {
			if(paramMap.get(key)!=null)
				html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
		}
		html.append("</form>\n");
		return html.toString();
	}
	
	 
	
}
