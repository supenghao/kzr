package com.dhk.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.xiajinsuo.epay.sdk.HttpUtils;
import org.xiajinsuo.epay.sdk.RRParams;
import org.xiajinsuo.epay.sdk.ResponseDataWrapper;

import com.alibaba.fastjson.JSON;
import com.dhk.api.entity.param.HXPayBindConfirmParam;
import com.dhk.api.entity.param.HXPayBindParam;
import com.dhk.api.entity.param.HXPayParam;
import com.dhk.api.entity.param.HXPayQueryParam;
import com.dhk.api.entity.param.HXPayWithdrawParam;
import com.dhk.api.entity.param.HXPayWithdrawQueryParam;
import com.dhk.api.entity.param.HXRegisterParam;
import com.dhk.api.entity.result.HXPayBaseReult;
import com.dhk.api.service.IHXPayService;
import com.dhk.api.tool.BeanConvertUtil;
import com.dhk.api.tool.RsaDataEncryptUtil;
import com.dhk.init.Constant;

@Service("HXPayService")
public class HXPayService implements IHXPayService {
	static Logger logger = Logger.getLogger(HXPayService.class);
	private static final String appId = "8904707ccbfa4882b79a29eda1d1cca5";
	private static final String transType = "SHORTCUTPAY";
	private static String REQUEST_URL = "http://api.mypays.cn/api/service.json";
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/* (非 Javadoc) 
	* <p>Title: HXRegister</p> 
	* <p>Description: </p> 
	* @param hxregisterParam
	* @return
	* @throws Exception 
	* @see com.dhk.payment.service.impl.IHXPayService#HXRegister(com.dhk.payment.entity.param.HXRegisterParam) 
	*/
	@Override
	public Map HXRegister(HXRegisterParam hxregisterParam) throws Exception {
		Map quickPay = handlePay(hxregisterParam, hxregisterParam.getClient_trans_id());
		return quickPay;
	}

	@Override
	public Map HXBind(HXPayBindParam bindParam) throws Exception {
		Map quickPay = handlePay(bindParam, bindParam.getClient_trans_id());
		return quickPay;
	}
	
	@Override
	public Map HXBindConfirm(HXPayBindConfirmParam bindConfirmParam) throws Exception {
		Map quickPay = handlePay(bindConfirmParam, bindConfirmParam.getClient_trans_id());
		return quickPay;
	}
	/* (非 Javadoc) 
	* <p>Title: HXPay</p> 
	* <p>Description: </p> 
	* @param hxpayParam
	* @return
	* @throws Exception 
	* @see com.dhk.payment.service.impl.IHXPayService#HXPay(com.dhk.payment.entity.param.HXPayParam) 
	*/
	@Override
	public Map HXPay(HXPayParam hxpayParam) throws Exception {
			//处理请求
			Map quickPay =  handlePay(hxpayParam, hxpayParam.getClient_trans_id());
		return quickPay;
	}
	
	/* (非 Javadoc) 
	* <p>Title: HXPayQuery</p> 
	* <p>Description: </p> 
	* @param hxpayQueryParam
	* @return
	* @throws Exception 
	* @see com.dhk.payment.service.impl.IHXPayService#HXPayQuery(com.dhk.payment.entity.param.HXPayQueryParam) 
	*/
	@Override
	public Map HXPayQuery(HXPayQueryParam hxpayQueryParam) throws Exception {
			//处理请求
			Map quickPay = handlePay(hxpayQueryParam, hxpayQueryParam.getClient_trans_id());
		return quickPay;
	}
	
	/* (非 Javadoc) 
	* <p>Title: HXPayWithdraw</p> 
	* <p>Description: </p> 
	* @param hxpayWithdrawParam
	* @return
	* @throws Exception 
	* @see com.dhk.payment.service.impl.IHXPayService#HXPayWithdraw(com.dhk.payment.entity.param.HXPayWithdrawParam) 
	*/
	@Override
	public Map HXPayWithdraw(HXPayWithdrawParam hxpayWithdrawParam) throws Exception {
			//处理请求
			Map quickPay = handlePay(hxpayWithdrawParam, hxpayWithdrawParam.getClient_trans_id());
		return quickPay;
	}
	
	/* (非 Javadoc) 
	* <p>Title: HXPayWithdrawQuery</p> 
	* <p>Description: </p> 
	* @param hxpayWithdrawQueryParam
	* @return
	* @throws Exception 
	* @see com.dhk.payment.service.impl.IHXPayService#HXPayWithdrawQuery(com.dhk.payment.entity.param.HXPayWithdrawQueryParam) 
	*/
	@Override
	public Map HXPayWithdrawQuery(HXPayWithdrawQueryParam hxpayWithdrawQueryParam) throws Exception {
			//处理请求
		Map quickPay = handlePay(hxpayWithdrawQueryParam, hxpayWithdrawQueryParam.getClient_trans_id());

		return quickPay;
	}
	
	/**
	 * 
		* @Title: handlePay 
		* @Description: 处理接口请求
		* @param @param businessReq
		* @param @param clientTransId
		* @param @return    设定文件 
		* @return Map    返回类型 
		* @throws
	 */
	protected Map  handlePay(Object obj,String clientTransId){
		Map  responseData = new HashMap ();
		try{
		Map<String, Object> businessReq = BeanConvertUtil.convertBean2Map(obj);
		/**设置公用参数并签名*/
		RRParams requestData = RRParams.newBuilder().setAppId(Constant.appId).setClientTransId(clientTransId)
				.setTransTimestamp(System.currentTimeMillis()).setTransType(Constant.transType).build();
		logger.info("提交的报文为:"+businessReq);
		/**请求接口*/
		ResponseDataWrapper rdw = HttpUtils.post(Constant.requestUrl, requestData, businessReq,
				RsaDataEncryptUtil.rsaDataEncryptPri, RsaDataEncryptUtil.rsaDataEncryptPub);
		HXPayBaseReult hxPayBaseReult = BeanConvertUtil.beanConvert(rdw, HXPayBaseReult.class);
		logger.info("返回的报文为:"+JSON.toJSONString(hxPayBaseReult));
		String respCode = rdw.getRespCode();
		if (respCode.equals("000000")) {
			 responseData = rdw.getResponseData();
			System.out.println(responseData);
		} else {
			responseData.put("respCode", rdw.getRespCode());
			responseData.put("respMsg", rdw.getRespMsg());
			System.out.println(rdw.getRespCode());
			System.out.println(rdw.getRespMsg());
		}
		responseData.put("respCode", rdw.getRespCode());
		responseData.put("respMsg", rdw.getRespMsg());
	} catch (Exception e) {
		responseData.put("respCode","fail");
		responseData.put("respMsg", "系统异常:"+e.getMessage());
		logger.error(e.getMessage(), e);
	}
		return responseData;
	}
	
}

