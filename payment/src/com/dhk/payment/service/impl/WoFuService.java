package com.dhk.payment.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.dhk.payment.service.IWoFuService;
import com.dhk.payment.util.BaseUtil;
import com.dhk.payment.util.DESPlus;
import com.dhk.payment.wpay.Content;
import com.dhk.payment.wpay.FastpayData;
import com.dhk.payment.wpay.Head;
import com.dhk.payment.wpay.HttpClientUtil;
import com.dhk.payment.wpay.SearchData;
import com.dhk.payment.wpay.SendData;
import com.dhk.payment.wpay.TransResult;
import com.dhk.payment.wpay.TransferData;
import com.dhk.payment.config.WoFuConfig;
import com.dhk.payment.yilian.QuickPay;

/**
 * 沃付聚合支付
 * @author Administrator
 *
 */
@Service("woFuService")
public class WoFuService implements IWoFuService {

	static Logger logger = Logger.getLogger(WoFuService.class);

	/**
	 * 快捷支付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay creditPurchase(Map<String, Object> paramMap) throws Exception {
		//获取参数
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), "0");//交易金额

		//数据库查询到用户账户信息，这里这个过程没有数据库连接,先从请求从获取
		String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);//账户名
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);//卡号
		String cerdId = BaseUtil.objToStr(paramMap.get("cerdId"), null);//身份证号
		String phoneNo = BaseUtil.objToStr(paramMap.get("phoneNo"), null);//预留手机号
		String cvn2 = BaseUtil.objToStr(paramMap.get("cvn2"), null);//cvn2
		String expDate = BaseUtil.objToStr(paramMap.get("expDate"), null);//有效期，月年
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);//有效期，月年

		QuickPay quickPay = new QuickPay();
		//参数完整性及合法性判断，这里随便写个金额判断
		BigDecimal amt = new BigDecimal(transAmt);

		logger.info("accountName="+accountName+" ||cardNo="+cardNo+" ||cerdId="+cerdId+" ||phoneNo="+phoneNo+"||expDate="+expDate+" ||orderNo="+orderNo+" ||cvn2="+cvn2+" ||transAmount="+amt.toString());
		if(amt.compareTo(BigDecimal.ZERO) <= 0){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("交易金额不能小于0");
			return quickPay;
		}

		//组装发送请求数据
		SendData sd = new SendData();
		sd.setBizName("fastPay");//固定
		FastpayData fd = new FastpayData();
		//String orderNo = "";//TimeUtil.getCurrentTime("yyyyMMddHHmmss");//订单号自己写
		fd.setOrderNo(orderNo);
		fd.setTransAmount(amt.toString());
		fd.setAccountName(accountName);
		fd.setAccountNo(cardNo);
		fd.setIdCardNo(cerdId);
		fd.setMobileNo(phoneNo);
		fd.setCvn2(cvn2);
		fd.setExpDate(expDate);
		fd.setCallbackUrl(WoFuConfig.callBackUrl);
		sd.setData(fd);
		String json = JSON.toJSONString(sd);
		//发送请求
		DESPlus des = new DESPlus(WoFuConfig.urlKey);
		json = des.encrypt(json);//加密
		String produceUrl = WoFuConfig.transUrl+ WoFuConfig.appKey+"&data="+json;
        String result = HttpClientUtil.sendForWf(produceUrl);
        if (result == null){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
			return quickPay;
        }

		logger.info("解密后Result: "+des.decrypt(result));
        //解析返回数据
        TransResult r = JSON.parseObject(des.decrypt(result), TransResult.class);
        Head head = r.getHead();
        Content content = new Content();
        String bizName = head.getBiz_name();//交易类型
        String resultCode = head.getResult_code();
        String resultMsg = head.getResult_msg();
        if(StringUtils.isEmpty(resultMsg)){
			resultMsg=content.getResult_msg();
		}

        String retrunOrderNo = content.getOrder_no();
        quickPay.setOrderCode(retrunOrderNo);
		if ("SUCCESS".equals(resultCode)) {
			quickPay.setRespCode("0000");
			quickPay.setRespDesc("业务处理成功");
		}else if ("FAIL".equals(resultCode)) {
			quickPay.setRespCode("fail");
			quickPay.setRespDesc(resultMsg);
		} else  {
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}
		return quickPay;
	}


	/**
	 * 代付
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public QuickPay proxyPay(Map<String, Object> paramMap) throws Exception {
		//获取参数
		String accountName = BaseUtil.objToStr(paramMap.get("accountName"), null);//账户名
		String cardNo = BaseUtil.objToStr(paramMap.get("cardNo"), null);//卡号
		String transAmt = BaseUtil.objToStr(paramMap.get("transAmt"), "0");//交易金额
		String orderNo = BaseUtil.objToStr(paramMap.get("orderNo"), null);

		QuickPay quickPay = new QuickPay();
		//参数完整性及合法性判断，这里随便写个金额判断
		BigDecimal amt = new BigDecimal(transAmt);
		logger.info("accountName="+accountName+" ||cardNo="+cardNo+" ||orderNo="+orderNo+" ||transAmount="+amt.toString());
		if(amt.compareTo(BigDecimal.ZERO) <= 0){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("交易金额不能小于0");
			return quickPay;
		}

		//组装请求数据
		SendData sd = new SendData();
		sd.setBizName("transfer");//固定
		TransferData td = new TransferData();
		//订单号自己写、账户名、卡号从数据库查
		td.setOrderNo(orderNo);
		td.setAccountName(accountName);
		td.setAccountNo(cardNo);
		td.setTransAmount(amt.toString());
		td.setCallbackUrl(WoFuConfig.callBackUrl);
		sd.setData(td);
		String json = JSON.toJSONString(sd);
		System.out.println("代付json:"+json);
		//加密
		DESPlus des = new DESPlus(WoFuConfig.urlKey);
		json = des.encrypt(json);

		//发送数据
		String produceUrl = WoFuConfig.transUrl+ WoFuConfig.appKey+"&data="+json;
        String result = HttpClientUtil.sendForWf(produceUrl);
		if (result == null){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
			return quickPay;
		}
		logger.info("代付解密后Result: "+des.decrypt(result));
		

        //解析返回数据
        TransResult r = JSON.parseObject(des.decrypt(result), TransResult.class);
        Head head = r.getHead();
        Content content = new Content();
        String bizName = head.getBiz_name();//交易类型
        String resultCode = head.getResult_code();
        String resultMsg = head.getResult_msg();
		if(StringUtils.isEmpty(resultMsg)){
			resultMsg=content.getResult_msg();
		}
        String retrunOrderNo = content.getOrder_no();

        quickPay.setOrderCode(retrunOrderNo);

		if ("SUCCESS".equals(resultCode)) {
			quickPay.setRespCode("0000");
			quickPay.setRespDesc("业务处理成功");
		}else if ("FAIL".equals(resultCode)) {
			quickPay.setRespCode("fail");
			quickPay.setRespDesc(resultMsg);
		} else  {
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}
		return quickPay;
	}


	/**
	 * 交易查询
	 * @param bizName 交易名称
	 * @param orderNo 订单号
	 * @return
	 * @throws Exception
	 */
	public QuickPay search(String bizName, String orderNo) throws Exception {
		QuickPay quickPay = new QuickPay();
		//判断参数完整性
		if(StringUtils.isBlank(bizName) || StringUtils.isBlank(orderNo)){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("参数不完整");
			return quickPay;
		}else if(orderNo.length() > 20){
			quickPay.setRespCode("fail");
			quickPay.setRespDesc("订单号过长");
			return quickPay;
		}


		//组装请求数据
		SendData sd = new SendData();
		sd.setBizName("search");//固定
		SearchData data = new SearchData();
		data.setOrderNo(orderNo);
		data.setTransType(bizName);
		sd.setData(data);
		String json = JSON.toJSONString(sd);
		System.out.println("查询json:"+json);
		//加密
		DESPlus des = new DESPlus(WoFuConfig.urlKey);
		json = des.encrypt(json);


		//发送数据
		String produceUrl = WoFuConfig.transUrl+ WoFuConfig.appKey+"&data="+json;
        String result = HttpClientUtil.sendForWf(produceUrl);
		if (result == null){
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
			return quickPay;
		}
		logger.info("查询解密后Result: "+des.decrypt(result));


        //解析返回数据
        TransResult r = JSON.parseObject(des.decrypt(result), TransResult.class);
        Head head = r.getHead();
        Content content = new Content();
        String returnBizName = head.getBiz_name();//交易类型
        String resultCode = head.getResult_code();
        String resultMsg = head.getResult_msg();
        String retrunOrderNo = content.getOrder_no();

        quickPay.setOrderCode(retrunOrderNo);
		if ("SUCCESS".equals(resultCode)) {
			quickPay.setRespCode("0000");
			quickPay.setRespDesc("业务处理成功");
		}else if ("FAIL".equals(resultCode)) {
			quickPay.setRespCode("fail");
			quickPay.setRespDesc(resultMsg);
		} else  {
			quickPay.setRespCode("9997");
			quickPay.setRespDesc("结算中");
		}
		return quickPay;
	}

}
