package com.dhk.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.xiajinsuo.epay.sdk.HttpUtils;
import org.xiajinsuo.epay.sdk.RRParams;
import org.xiajinsuo.epay.sdk.ResponseDataWrapper;

import com.alibaba.fastjson.JSON;
import com.dhk.payment.config.HxtcConfig;
import com.dhk.payment.entity.param.HXPayParam;
import com.dhk.payment.entity.param.HXPayQueryParam;
import com.dhk.payment.entity.param.HXPayWithdrawParam;
import com.dhk.payment.entity.param.HXPayWithdrawQueryParam;
import com.dhk.payment.entity.param.HXRegisterParam;
import com.dhk.payment.entity.result.HXPayBaseReult;
import com.dhk.payment.service.IHXPayService;
import com.dhk.payment.util.BeanConvertUtil;
import com.dhk.payment.util.RsaDataEncryptUtil;
import com.dhk.payment.yilian.QuickPay;

@Service("hxayService")
public class HXPayService implements IHXPayService {
	static Logger logger = Logger.getLogger(HXPayService.class);
//	private static  String appId = HxtcConfig.appId;
//	private static  String transType = HxtcConfig.transType;
//	private static String REQUEST_URL = HxtcConfig.request_url;
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
	public QuickPay HXRegister(HXRegisterParam hxregisterParam) throws Exception {
		QuickPay quickPay = handleRegistPay(hxregisterParam, hxregisterParam.getClient_trans_id());
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
	public QuickPay HXPay(HXPayParam hxpayParam) throws Exception {
			//处理请求
		    hxpayParam.setCounter_fee_t0(null);
			QuickPay quickPay =  handlePay(hxpayParam, hxpayParam.getClient_trans_id());
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
	public QuickPay HXPayQuery(HXPayQueryParam hxpayQueryParam) throws Exception {
			//处理请求
			QuickPay quickPay = handlePay(hxpayQueryParam, hxpayQueryParam.getClient_trans_id());
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
	public QuickPay HXPayWithdraw(HXPayWithdrawParam hxpayWithdrawParam) throws Exception {
			//处理请求
			QuickPay quickPay = handlePay(hxpayWithdrawParam, hxpayWithdrawParam.getClient_trans_id());
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
	public QuickPay HXPayWithdrawQuery(HXPayWithdrawQueryParam hxpayWithdrawQueryParam) throws Exception {
			//处理请求
		QuickPay quickPay = handlePay(hxpayWithdrawQueryParam, hxpayWithdrawQueryParam.getClient_trans_id());

		return quickPay;
	}
	
	/**
	 * 
		* @Title: handlePay 
		* @Description: 处理接口请求
		* @param @param businessReq
		* @param @param clientTransId
		* @param @return    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	protected QuickPay handlePay(Object obj,String clientTransId){
		QuickPay quickPay = new QuickPay();
		try{
		Map<String, Object> businessReq = BeanConvertUtil.convertBean2Map(obj);
		/**设置公用参数并签名*/
		RRParams requestData = RRParams.newBuilder().setAppId(HxtcConfig.appId).setClientTransId(clientTransId)
				.setTransTimestamp(System.currentTimeMillis()).setTransType(HxtcConfig.transType).build();
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
			logger.info("responseData:"+responseData);
			if("SUCCESS".equals(responseData.get("status")+"")){
				quickPay.setRespCode("9997");
				quickPay.setRespDesc("结算中");
			}else{
				quickPay.setRespCode("fail");
				quickPay.setRespDesc(responseData.get("err_msg")+"");
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
		
	} catch (Exception e) {
		quickPay.setRespCode("fail");
		quickPay.setRespDesc("系统异常:"+e.getMessage());
		logger.error(e.getMessage(), e);
	}
		return quickPay;
	}
	/**
	 * 
		* @Title: handlePay 
		* @Description: 处理接口请求
		* @param @param businessReq
		* @param @param clientTransId
		* @param @return    设定文件 
		* @return QuickPay    返回类型 
		* @throws
	 */
	protected QuickPay handleRegistPay(Object obj,String clientTransId){
		QuickPay quickPay = new QuickPay();
		try{
		Map<String, Object> businessReq = BeanConvertUtil.convertBean2Map(obj);
		/**设置公用参数并签名*/
		RRParams requestData = RRParams.newBuilder().setAppId(HxtcConfig.appId).setClientTransId(clientTransId)
				.setTransTimestamp(System.currentTimeMillis()).setTransType(HxtcConfig.transType).build();
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
			if(responseData.get("third_merchant_code")!=null){
				quickPay.setRespCode("00000");
				quickPay.setRespDesc("修改报户成功");
			}
		} else {
			logger.info("rdw.getRespCode():"+rdw.getRespCode() +"  rdw.getRespMsg():"+rdw.getRespMsg());
			quickPay.setRespCode("fail");
			quickPay.setRespDesc(rdw.getRespMsg());
		}
		
	} catch (Exception e) {
		quickPay.setRespCode("fail");
		quickPay.setRespDesc("系统异常:"+e.getMessage());
		logger.error(e.getMessage(), e);
	}
		return quickPay;
	}
}

