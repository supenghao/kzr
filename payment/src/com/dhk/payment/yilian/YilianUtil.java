package com.dhk.payment.yilian;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dhk.payment.config.YiLianConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

public class YilianUtil {
	
	/**
	 * 
	 * @Title agentPayment 
	 * @Description TODO // 易联代付工具
	 * @param InsteadPayurl
	 * @param AccountName //代付账户名称
	 * @param Amount
	 * @param CardNo
	 * @param IdentityId
	 * @param MobileNo
	 * @param NotifyUrl
	 * @param OrderCode
	 * @param RequestNo  
	 * @author jaysonQiu
	 * @throws Exception 
	 */
	public static List<BasicNameValuePair> agentPayment(String AccountName,String Amount,String CardNo,String IdentityId,String MobileNo,String NotifyUrl,String OrderCode,String RequestNo) throws Exception{
		 	
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	        nvps.add(new BasicNameValuePair("AccountName", AccountName));//代付账户名称
	        nvps.add(new BasicNameValuePair("AccType", "2"));//对公对私标识1为对公,2为对私
	        nvps.add(new BasicNameValuePair("Amount", Amount));
	        nvps.add(new BasicNameValuePair("CardNo", CardNo));//银行卡号
	        nvps.add(new BasicNameValuePair("IdentityId", IdentityId));//证件号
	        nvps.add(new BasicNameValuePair("MerchantId", YiLianConfig.merchantId));//机构商户号
	        nvps.add(new BasicNameValuePair("MobileNo", MobileNo));//代付银行手机号
	        nvps.add(new BasicNameValuePair("NotifyUrl", NotifyUrl));//异步请求地址
	        nvps.add(new BasicNameValuePair("OrderCode", OrderCode));//商户订单号
	        nvps.add(new BasicNameValuePair("ProductId", YiLianConfig.productId));//产品类型1001
	        nvps.add(new BasicNameValuePair("RequestNo", RequestNo));//请求流水号
	        nvps.add(new BasicNameValuePair("ShopNo", YiLianConfig.shopNo));//机构子商户号
	        nvps.add(new BasicNameValuePair("Version", YiLianConfig.version));//新接口V1.0
	        
	        String signature=MD5Util.MD5Encode(SignUtils.signData(nvps)+YiLianConfig.key, "utf-8");
	        
	        nvps.add(new BasicNameValuePair("signature", signature));
	        return nvps;
	}

	/**
	 * @Title signAgentPayment 
	 * @Description TODO // 代付验签数据
	 * @param sPay
	 * @return  String
	 * @author jaysonQiu
	 */
	public static boolean signAgentPayment(QuickPay sPay){
		String sigs="";
    	if (!StringUtils.isBlank(sPay.getAccountName())) {
			sigs=sigs+"AccountName"+"="+sPay.getAccountName()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getAccType())) {
    		sigs=sigs+"AccType"+"="+sPay.getAccType()+"&";//
		}
    	if (!StringUtils.isBlank(sPay.getAmount())) {
    		sigs=sigs+"Amount"+"="+sPay.getAmount()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getBankName())) {
    		sigs=sigs+"BankName"+"="+sPay.getBankName()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getCardNo())) {
    		sigs=sigs+"CardNo"+"="+sPay.getCardNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getIdentityId())) {
    		sigs=sigs+"IdentityId"+"="+sPay.getIdentityId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getMerchantId())) {
    		sigs=sigs+"MerchantId"+"="+sPay.getMerchantId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getMobileNo())) {
    		sigs=sigs+"MobileNo"+"="+sPay.getMobileNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getNote())) {
    		sigs=sigs+"Note"+"="+sPay.getNote()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getNotifyUrl())) {
    		sigs=sigs+"NotifyUrl"+"="+sPay.getNotifyUrl()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getOrderCode())) {
    		sigs=sigs+"OrderCode"+"="+sPay.getOrderCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getProductId())) {
    		sigs=sigs+"ProductId"+"="+sPay.getProductId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRequestNo())) {
    		sigs=sigs+"RequestNo"+"="+sPay.getRequestNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespCode())) {
    		sigs=sigs+"RespCode"+"="+sPay.getRespCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespDesc())) {
    		sigs=sigs+"RespDesc"+"="+sPay.getRespDesc()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getShopNo())) {
    		sigs=sigs+"ShopNo"+"="+sPay.getShopNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getVersion())) {
    		sigs=sigs+"Version"+"="+sPay.getVersion();
    	}
    	
    	String sig1=MD5Util.MD5Encode(sigs+YiLianConfig.key, "utf-8");
    	
    	if (sig1.equals(sPay.getSignature())) {
			return true;
		}
    	return false;
	}
	
	/**
	 * @Title noCardPayment 
	 * @Description TODO // 无卡消费工具
	 * @param Amount
	 * @param CommodityName
	 * @param CustomerInfo
	 * @param NotifyCallBackUrl
	 * @param OrderCode
	 * @param RequestNo
	 * @param WebCallBackUrl
	 * @return  List<BasicNameValuePair>
	 * @author jaysonQiu
	 * @throws Exception 
	 */
	public static List<BasicNameValuePair> noCardPayment(String Amount,String CommodityName,String CustomerInfo,String NotifyCallBackUrl,String OrderCode,String RequestNo,String WebCallBackUrl) throws Exception{
		 List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	        nvps.add(new BasicNameValuePair("Amount", Amount));//交易金额
	        nvps.add(new BasicNameValuePair("CommodityName", CommodityName));//商品名称
	        nvps.add(new BasicNameValuePair("CustomerInfo", CustomerInfo));//客户卡信息，加密详见1.5.3.2	加密/解密机制
	        nvps.add(new BasicNameValuePair("MerchantId", YiLianConfig.merchantId));//主商户号
	        nvps.add(new BasicNameValuePair("NotifyCallBackUrl", NotifyCallBackUrl));//异步通知地址 
	        nvps.add(new BasicNameValuePair("OrderCode", OrderCode));//商户订单号new SimpleDateFormat("yyyyMMdd").format(new Date())
	        nvps.add(new BasicNameValuePair("ProductId", YiLianConfig.productId));//产品类型1001
	        nvps.add(new BasicNameValuePair("RequestNo", RequestNo));//请求流水号
	        nvps.add(new BasicNameValuePair("ShopNo", YiLianConfig.shopNo));//商户号
	        nvps.add(new BasicNameValuePair("Version", YiLianConfig.version));//版本号
	        nvps.add(new BasicNameValuePair("WebCallBackUrl", WebCallBackUrl));//页面通知地址
	        String signature=MD5Util.MD5Encode(SignUtils.signData(nvps)+YiLianConfig.key, "utf-8");
	        nvps.add(new BasicNameValuePair("signature",signature));
	        return nvps;
	}
	
	/**
	 * @Title signNoCardPayment 
	 * @Description TODO // 无卡支付验签
	 * @param sPay
	 * @return  boolean
	 * @author jaysonQiu
	 */
	public static boolean signNoCardPayment(QuickPay sPay){
		String sigs="";
    	if (!StringUtils.isBlank(sPay.getAmount())) {
			sigs=sigs+"Amount"+"="+sPay.getAmount()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getCommodityName())) {
    		sigs=sigs+"CommodityName"+"="+sPay.getCommodityName()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getCustomerInfo())) {
    		sigs=sigs+"CustomerInfo"+"="+sPay.getCustomerInfo()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getMerchantId())) {
    		sigs=sigs+"MerchantId"+"="+sPay.getMerchantId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getNotifyCallBackUrl())) {
    		sigs=sigs+"NotifyCallBackUrl"+"="+sPay.getNotifyCallBackUrl()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getOrderCode())) {
    		sigs=sigs+"OrderCode"+"="+sPay.getOrderCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getProductId())) {
    		sigs=sigs+"ProductId"+"="+sPay.getProductId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRequestNo())) {
    		sigs=sigs+"RequestNo"+"="+sPay.getRequestNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespCode())) {
    		sigs=sigs+"RespCode"+"="+sPay.getRespCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespDesc())) {
    		sigs=sigs+"RespDesc"+"="+sPay.getRespDesc()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getShopNo())) {
    		sigs=sigs+"ShopNo"+"="+sPay.getShopNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getVersion())) {
    		sigs=sigs+"Version"+"="+sPay.getVersion()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getWebCallBackUrl())) {
    		sigs=sigs+"WebCallBackUrl"+"="+sPay.getWebCallBackUrl();
    	}
    	String sig1=MD5Util.MD5Encode(sigs+YiLianConfig.key, "utf-8");
    	if (sig1.equals(sPay.getSignature())) {
			return true;
		}
    	return false;
	}
	
	/**
	 * @Title findTradingStatus 
	 * @Description TODO // 查询交易、代付订单工具
	 * @param OrderCode //商户订单
	 * @param RequestNo  //交易请求流水
	 * @author jaysonQiu
	 * @throws Exception 
	 */
	public static List<BasicNameValuePair> findTradingStatus(String OrderCode,String RequestNo) throws Exception{
		 	List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	        nvps.add(new BasicNameValuePair("MerchantId", "10053100001"));//机构商户号
	        nvps.add(new BasicNameValuePair("OrderCode", OrderCode));//商户订单号
	        nvps.add(new BasicNameValuePair("RequestNo", RequestNo));//交易请求流水号
	        nvps.add(new BasicNameValuePair("Version", "V1.0"));//新接口V1.0
	        String signature=MD5Util.MD5Encode(SignUtils.signData(nvps)+YiLianConfig.key, "utf-8");
	        nvps.add(new BasicNameValuePair("signature", signature));
	        return nvps;
	}
	
	/**
	 * @Title signFindTringStatus 
	 * @Description TODO //查询交易、代付订单状态验签
	 * @param sPay
	 * @return  boolean
	 * @author jaysonQiu
	 */
	public static boolean signFindTringStatus(QuickPay sPay){
		
		String sigs="";
    	if (!StringUtils.isBlank(sPay.getAmount())) {
			sigs=sigs+"Amount"+"="+sPay.getAmount()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getMerchantId())) {
    		sigs=sigs+"MerchantId"+"="+sPay.getMerchantId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getOrderCode())) {
    		sigs=sigs+"OrderCode"+"="+sPay.getOrderCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getProductId())) {
    		sigs=sigs+"ProductId"+"="+sPay.getProductId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRequestNo())) {
    		sigs=sigs+"RequestNo"+"="+sPay.getRequestNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespCode())) {
    		sigs=sigs+"RespCode"+"="+sPay.getRespCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespDesc())) {
    		sigs=sigs+"RespDesc"+"="+sPay.getRespDesc()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getVersion())) {
    		sigs=sigs+"Version"+"="+sPay.getVersion();
    	}

    	String sig1=MD5Util.MD5Encode(sigs+YiLianConfig.key, "utf-8");
    	if (sig1.equals(sPay.getSignature())) {
			return true;
		}
    	return false;
	}
	
	/**
	 * @Title findBalance 
	 * @Description TODO // 查询账户余额工具
	 * @param RequestNo //请求流水
	 * @return  List<BasicNameValuePair>
	 * @author jaysonQiu
	 * @throws Exception 
	 */
	public static List<BasicNameValuePair> findBalance(String RequestNo) throws Exception{
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
	        nvps.add(new BasicNameValuePair("MerchantId", YiLianConfig.merchantId));//机构商户号
	        nvps.add(new BasicNameValuePair("ProductId", YiLianConfig.productId));//产品类型
	        nvps.add(new BasicNameValuePair("RequestNo", RequestNo));//交易请求流水号
	        nvps.add(new BasicNameValuePair("ShopNo", YiLianConfig.shopNo));//机构子商户号
	        nvps.add(new BasicNameValuePair("Version", YiLianConfig.version));//新接口V1.0
	        String signature=MD5Util.MD5Encode(SignUtils.signData(nvps)+YiLianConfig.key, "utf-8");
	        nvps.add(new BasicNameValuePair("signature", signature));
	        return nvps;
	}
	
	/**
	 * @Title signFindBalance 
	 * @Description TODO // 验签账户余额
	 * @param sPay
	 * @return  boolean
	 * @author jaysonQiu
	 */
	public static boolean signFindBalance(QuickPay sPay){
		
		String sigs="";
    	if (!StringUtils.isBlank(sPay.getBalance())) {
			sigs=sigs+"Balance"+"="+sPay.getBalance()+"&";
		}
    	if (!StringUtils.isBlank(sPay.getCanBalance())) {
    		sigs=sigs+"CanBalance"+"="+sPay.getCanBalance()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getFrozenBalance())) {
    		sigs=sigs+"FrozenBalance"+"="+sPay.getFrozenBalance()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getMerchantId())) {
    		sigs=sigs+"MerchantId"+"="+sPay.getMerchantId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getProductId())) {
    		sigs=sigs+"ProductId"+"="+sPay.getProductId()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRequestNo())) {
    		sigs=sigs+"RequestNo"+"="+sPay.getRequestNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespCode())) {
    		sigs=sigs+"RespCode"+"="+sPay.getRespCode()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getRespDesc())) {
    		sigs=sigs+"RespDesc"+"="+sPay.getRespDesc()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getShopNo())) {
    		sigs=sigs+"ShopNo"+"="+sPay.getShopNo()+"&";
    	}
    	if (!StringUtils.isBlank(sPay.getVersion())) {
    		sigs=sigs+"Version"+"="+sPay.getVersion();
    	}
    	String sig1=MD5Util.MD5Encode(sigs+YiLianConfig.key, "utf-8");
    	if (sig1.equals(sPay.getSignature())) {
			return true;
		}
    	return false;
	}
	
	/**
	 * 
	 * @Title BigToString 
	 * @Description TODO //保留两位小数点
	 * @param money
	 * @return  String
	 * @author jaysonQiu
	 */
	public static String BigToString(BigDecimal money){
		return money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
}
