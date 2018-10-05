package com.dhk.payment.service.impl;

import com.google.gson.Gson;
import com.dhk.payment.service.IYLService;
import com.dhk.payment.util.BaseUtil;
import com.dhk.payment.util.ThirdResponseObj;
import com.dhk.payment.wpay.HttpClientUtil;
import com.dhk.payment.yilian.DESedeUtil;
import com.dhk.payment.yilian.QuickPay;
import com.dhk.payment.config.YiLianConfig;
import com.dhk.payment.yilian.YilianUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("ylService")
public class YLService implements IYLService {
	static Logger logger = Logger.getLogger(WoFuService.class);

	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {

		//数据库查询到用户账户信息，这里这个过程没有数据库连接,先从请求从获取
		QuickPay quickPay = new QuickPay();
		try {
			String url =YiLianConfig.url+"OrderQuickPayByAcc";
			String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
			String commodityName = BaseUtil.objToStr(paramMap.get("commodityName"), null);
			String notifyCallBackUrl = YiLianConfig.callBackUrl;
			String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);  //订单号
			String webCallBackUrl = YiLianConfig.callBackUrl;
			String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);
			String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);   //账户名称
			String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);  //身份证号
			String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);  //手机号
			String expDate = BaseUtil.objToStr(paramMap.get("expDate"), null); //有效期
			String cvn2 = BaseUtil.objToStr(paramMap.get("cvn2"), null);

			String info =cardNo+"|"+accountName+"|"+cerdId+"|"+phoneNo+"|"+expDate+"|"+cvn2;
			logger.info(info);
			String customerInfo= DESedeUtil.encode(info, YiLianConfig.key.substring(0, 24));
			//保留两位小数
			String money = YilianUtil.BigToString(new BigDecimal(transAmt));

			List<BasicNameValuePair> nvps = YilianUtil.noCardPayment(money, commodityName, customerInfo, notifyCallBackUrl, orderNo, orderNo, webCallBackUrl);
			ThirdResponseObj obj= HttpClientUtil.sendForYl(url, nvps);
			if(obj==null){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
				return quickPay;
			}

			if ("success".equals(obj.getCode())) {

				String entity= StringEscapeUtils.unescapeJava(obj.getResponseEntity().substring(1, obj.getResponseEntity().length()-1));
				Gson gson=new Gson();
				quickPay=gson.fromJson(entity, QuickPay.class);
				//验签
				if (!YilianUtil.signNoCardPayment(quickPay)) {
					quickPay.setRespCode("fail");
					quickPay.setRespDesc("验签数据异常");
				}
				quickPay.setRespDesc("r-"+quickPay.getRespDesc());
			}else {
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
				return quickPay;
			}
		} catch (Exception e) {
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("系统异常:"+e.getMessage());
		}

		if("00".equals(quickPay.getRespCode())||"0000".equals(quickPay.getRespCode())||"P000".equals(quickPay.getRespCode())
				|| "9997".equals(quickPay.getRespCode())
				|| "9999".equals(quickPay.getRespCode())){          
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}else{
			quickPay.setRespCode("Fail");
		}

		return quickPay;
	}

	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), null);
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);
		String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);
		String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);
		logger.info("proxyPay Star:accountName"+accountName+"|transAmt："+transAmt+"|cardNo："+cardNo+"|cerdId："+cerdId+"|orderNo："+orderNo);
		QuickPay quickPay = new QuickPay();
		String InsteadPayurl = YiLianConfig.url+"InsteadPay";
		//金额保留两位小数
		String money = YilianUtil.BigToString(new BigDecimal(transAmt));
		List<BasicNameValuePair> nvps=YilianUtil.agentPayment(accountName, money, cardNo, cerdId, phoneNo, YiLianConfig.callBackUrl, orderNo, orderNo);
		ThirdResponseObj obj= HttpClientUtil.sendForYl(InsteadPayurl, nvps);
		if(obj==null){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
			return quickPay;
		}
		 //不管怎样都是返回9997
		if ("success".equals(obj.getCode())) {
			String entity= StringEscapeUtils.unescapeJava(obj.getResponseEntity().substring(1, obj.getResponseEntity().length()-1));
			Gson gson=new Gson();
			quickPay=gson.fromJson(entity, QuickPay.class);
			//验签
			if (!YilianUtil.signAgentPayment(quickPay)) {
				quickPay.setRespCode("fail");
				quickPay.setRespDesc("验签数据异常");
			}
		}

		logger.info("proxyPay end:orderNo："+orderNo+"|RespCode:"+quickPay.getRespCode()+"|RespDesc:"+quickPay.getRespDesc());
		quickPay.setRespCode("9997");     //代付都要等待回调
		quickPay.setRespDesc("结算中");

		return quickPay;
	}
}

