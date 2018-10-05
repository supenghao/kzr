package com.dhk.service.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.dhk.BaseNetwork;
import com.dhk.NetworkInterface.NetworkParams;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.ICallRemotePayService;
import com.sunnada.kernel.util.JsonUtil;
import com.sunnada.uaas.entity.SystemParam;
import com.sunnada.uaas.service.ISystemParamService;

@Service("CallRemotePayService")
public class CallRemotePayService implements ICallRemotePayService{

	@Resource(name="systemParamService")
	ISystemParamService systemParamService;
	private static final Logger log= LogManager.getLogger();
	
	public PayResult queryBankCardBalance(String cardType) throws Exception{
		PayResult payResult = null;
		
		if("credit".equals(cardType)){
			SystemParam sp = systemParamService.findByParamName("creditQueryBalanceServerUrl");
			String serverUrl = sp.getParam_text();
			payResult = trans(new NetworkParams().addParam("gg", ""),serverUrl);
		}else if("debit".equals(cardType)){
			SystemParam sp = systemParamService.findByParamName("debitQueryBalanceServerUrl");
			String serverUrl = sp.getParam_text();
			payResult = trans(new NetworkParams().addParam("gg", ""),serverUrl);
		}else{
			payResult = new PayResult();
			payResult.setCode("fail");
			payResult.setMessage("不支持的卡类型");					
		}
		return payResult;
		
	}
	
	
	
    public PayResult creditPurchase(PayRequest payRequest) throws Exception{
    	NetworkParams p = new NetworkParams();
    	String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/pay/creditPurchase";
//    	String url = "http://127.0.0.1:8080/payment/hxpay/creditPurchase";
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderNo", payRequest.getOrderNo());
		p.addParam("transAmt", payRequest.getTransAmt()+"");
		p.addParam("commodityName", "商品");
		p.addParam("phoneNo", payRequest.getPhoneNo());
		p.addParam("accountName", payRequest.getCustomerName());
		p.addParam("cerdType", payRequest.getCerdType());
		p.addParam("cerdId", payRequest.getCerdId());
		p.addParam("cardNo", payRequest.getAcctNo());
		p.addParam("cvn2", payRequest.getCvn2());
		p.addParam("expDate", payRequest.getExpDate());
		String merchantId = payRequest.getMerchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("merchantId", merchantId);
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderTime", payRequest.getOrderTime());
		String lhh = payRequest.getLhh();
		if(StringUtils.isEmpty(lhh))
			lhh ="";
		p.addParam("lhh",lhh);
		String lmc = payRequest.getLhmc();
		if(StringUtils.isEmpty(lmc))
			lmc ="";
		p.addParam("lmc", lmc);
		String bindId = payRequest.getBindId();
		if(StringUtils.isEmpty(bindId))
			bindId ="";
		p.addParam("bindId", bindId);
		
		String userId = payRequest.getUerId();
		if(StringUtils.isEmpty(userId))
			userId ="";
		p.addParam("userId", userId);

		log.info("creditPurchase参数:"+p);
		PayResult payResult =trans(p,url);
		return payResult;
    }
	
    public PayResult hxtcCreditPurchase(PayRequest payRequest) throws Exception{
    	NetworkParams p = new NetworkParams();
       	String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/hxpay/creditPurchase";
//    	String url = "http://127.0.0.1:8080/payment/hxpay/creditPurchase";
//		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("client_trans_id", payRequest.getOrderNo());
		p.addParam("mobile", payRequest.getPhoneNo());
		p.addParam("family_name", payRequest.getCustomerName());
		p.addParam("id_card", payRequest.getCerdId());
		p.addParam("pay_bank_no", payRequest.getAcctNo());
		p.addParam("trans_amount", payRequest.getTransAmt()+"");
		p.addParam("rate_t0", payRequest.getPurchase_cost());
		p.addParam("counter_fee_t0", payRequest.getRe_cash());
		String expire_date = "";
		if(payRequest.getExpDate()!=null){
			expire_date = payRequest.getExpDate().substring(2,4)+payRequest.getExpDate().substring(0,2);
		}
		p.addParam("expire_date", expire_date);
		p.addParam("cvv", payRequest.getCvn2());
		String merchantId = payRequest.getMerchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("third_merchant_code", merchantId);
		log.info("creditPurchase参数:"+p);
		PayResult payResult =trans(p,url);
		return payResult;
    }
    
    
    
    
    public PayResult hxtcCreditProxyPay(PayRequest payRequest) throws Exception{
		NetworkParams p = new NetworkParams();
		String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/hxpay/proxyPay";
    	//String url = "http://127.0.0.1:8080/payment/hxpay/proxyPay";

	    p.addParam("customerName", payRequest.getCustomerName());
	    p.addParam("transAmt", payRequest.getTransAmt()+"");
	    p.addParam("acctNo", payRequest.getAcctNo());
	    p.addParam("cerdId", payRequest.getCerdId());
	    p.addParam("phoneNo", payRequest.getPhoneNo());
	    p.addParam("orderNo", payRequest.getOrderNo());
	    p.addParam("bankName",payRequest.getAccBankName());
	    String merchantId = payRequest.getMerchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("merchantId", merchantId);
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderTime", payRequest.getOrderTime());
		String lhh = payRequest.getLhh();
		if(StringUtils.isEmpty(lhh))
			lhh ="";
		p.addParam("lhh",lhh);
		String lmc = payRequest.getLhmc();
		if(StringUtils.isEmpty(lmc))
			lmc ="";
		p.addParam("lmc", lmc);
		String bindId = payRequest.getBindId();
		if(StringUtils.isEmpty(bindId))
			bindId ="";
		p.addParam("bindId", bindId);
		String userId = payRequest.getUerId();
		if(StringUtils.isEmpty(userId))
			userId ="";
		p.addParam("userId", userId);

		PayResult resx = trans(p,url);
		log.info("creditProxyPay参数:"+p);
		return resx;
	}
    
    
    
    public PayResult yblCreditPurchase(PayRequest payRequest) throws Exception{
    	NetworkParams p = new NetworkParams();
    	String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/yblpay/creditPurchase";
  //  	String url = "http://127.0.0.1:8080/payment/yblpay/creditPurchase";
//    	String url = "http://127.0.0.1:8080/payment/hxpay/creditPurchase";
//		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderNo", payRequest.getOrderNo());
		p.addParam("mobile", payRequest.getPhoneNo());
		p.addParam("custName", payRequest.getCustomerName());
		p.addParam("id_card", payRequest.getCerdId());
		p.addParam("cardNo", payRequest.getAcctNo());
		p.addParam("trans_amount", payRequest.getTransAmt()+"");
		p.addParam("rate_t0", payRequest.getPurchase_cost());
		p.addParam("counter_fee_t0", payRequest.getRe_cash());
		p.addParam("bankName", payRequest.getLhmc());
		String expire_date = "";
		if(payRequest.getExpDate()!=null){
			expire_date = payRequest.getExpDate().substring(2,4)+payRequest.getExpDate().substring(0,2);
		}
		p.addParam("expire_date", expire_date);
		p.addParam("merchantId", payRequest.getByl_merchantId());
		p.addParam("cvv", payRequest.getCvn2());
		String merchantId = payRequest.getByl_merchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("custId", merchantId);
		log.info("creditPurchase参数:"+p);
		PayResult payResult =trans(p,url);
		return payResult;
    }
    
    
    public PayResult yblCreditProxyPay(PayRequest payRequest) throws Exception{
		NetworkParams p = new NetworkParams();
		String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/yblpay/proxyPay";
//    	String url = "http://127.0.0.1:8080/payment/hxpay/proxyPay";
//		String url = "http://127.0.0.1:8080/payment/yblpay/proxyPay";
	    p.addParam("customerName", payRequest.getCustomerName());
	    p.addParam("transAmt", payRequest.getTransAmt()+"");
	    p.addParam("acctNo", payRequest.getAcctNo());
	    p.addParam("cerdId", payRequest.getCerdId());
	    p.addParam("phoneNo", payRequest.getPhoneNo());
	    p.addParam("orderNo", payRequest.getOrderNo());
	    p.addParam("bankName",payRequest.getAccBankName());
	    String merchantId = payRequest.getByl_merchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("merchantId", merchantId);
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderTime", payRequest.getOrderTime());
		String lhh = payRequest.getLhh();
		if(StringUtils.isEmpty(lhh))
			lhh ="";
		p.addParam("lhh",lhh);
		String lmc = payRequest.getLhmc();
		if(StringUtils.isEmpty(lmc))
			lmc ="";
		p.addParam("lmc", lmc);
		String bindId = payRequest.getBindId();
		if(StringUtils.isEmpty(bindId))
			bindId ="";
		p.addParam("bindId", bindId);
		String userId = payRequest.getUerId();
		if(StringUtils.isEmpty(userId))
			userId ="";
		p.addParam("userId", userId);

		PayResult resx = trans(p,url);
		log.info("creditProxyPay参数:"+p);
		return resx;
	}
    
    public PayResult kjCreditPurchase(PayRequest payRequest) throws Exception{
    	NetworkParams p = new NetworkParams();
        String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/kjpay/creditPurchase";
        url = "http://127.0.0.1:8080/payment/kjpay/creditPurchase";
//		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("client_trans_id", payRequest.getOrderNo());
		p.addParam("mobile", payRequest.getPhoneNo());
		p.addParam("family_name", payRequest.getCustomerName());
		p.addParam("id_card", payRequest.getCerdId());
		p.addParam("pay_bank_no", payRequest.getAcctNo());
		p.addParam("trans_amount", payRequest.getTransAmt()+"");
		p.addParam("rate_t0", payRequest.getPurchase_cost());
		p.addParam("counter_fee_t0", payRequest.getRe_cash());
		p.addParam("kjKey", payRequest.getKjkey());
		p.addParam("kjMero", payRequest.getKjmerno());
		String expire_date = "";
		if(payRequest.getExpDate()!=null){
			expire_date = payRequest.getExpDate().substring(2,4)+payRequest.getExpDate().substring(0,2);
		}
		p.addParam("expire_date", expire_date);
		p.addParam("cvv", payRequest.getCvn2());
		String merchantId = payRequest.getMerchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("third_merchant_code", merchantId);
		log.info("creditPurchase参数:"+p);
		PayResult payResult =trans(p,url);
		return payResult;
    }
    public PayResult kjCreditProxyPay(PayRequest payRequest) throws Exception{
		NetworkParams p = new NetworkParams();
		String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/kjpay/proxyPay";
		url = "http://127.0.0.1:8080/payment/kjpay/proxyPay";
	    p.addParam("customerName", payRequest.getCustomerName());
	    p.addParam("transAmt", payRequest.getTransAmt()+"");
	    p.addParam("acctNo", payRequest.getAcctNo());
	    p.addParam("cerdId", payRequest.getCerdId());
	    p.addParam("phoneNo", payRequest.getPhoneNo());
	    p.addParam("orderNo", payRequest.getOrderNo());
	    p.addParam("bankName",payRequest.getAccBankName());
	    String merchantId = payRequest.getByl_merchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("merchantId", merchantId);
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderTime", payRequest.getOrderTime());
		p.addParam("kjKey", payRequest.getKjkey());
		p.addParam("kjMero", payRequest.getKjmerno());
		String lhh = payRequest.getLhh();
		if(StringUtils.isEmpty(lhh))
			lhh ="";
		p.addParam("lhh",lhh);
		String lmc = payRequest.getLhmc();
		if(StringUtils.isEmpty(lmc))
			lmc ="";
		p.addParam("lmc", lmc);
		String bindId = payRequest.getBindId();
		if(StringUtils.isEmpty(bindId))
			bindId ="";
		p.addParam("bindId", bindId);
		String userId = payRequest.getUerId();
		if(StringUtils.isEmpty(userId))
			userId ="";
		p.addParam("userId", userId);

		PayResult resx = trans(p,url);
		log.info("creditProxyPay参数:"+p);
		return resx;
	}
    public PayResult creditProxyPay(PayRequest payRequest) throws Exception{
		NetworkParams p = new NetworkParams();
		String url = systemParamService.findByParamName("payment_url_i").getParam_text()+"/payment/pay/hxtcproxyPay";
//    	String url = "http://127.0.0.1:8080/payment/hxpay/proxyPay";

	    p.addParam("accountName", payRequest.getCustomerName());
	    p.addParam("transAmt", payRequest.getTransAmt()+"");
	    p.addParam("cardNo", payRequest.getAcctNo());
	    p.addParam("cerdId", payRequest.getCerdId());
	    p.addParam("phoneNo", payRequest.getPhoneNo());
	    p.addParam("orderNo", payRequest.getOrderNo());
	    p.addParam("bankName",payRequest.getAccBankName());
	    String merchantId = payRequest.getMerchantId();
		if(merchantId==null)
			merchantId = "";
		p.addParam("merchantId", merchantId);
		p.addParam("orderDate", payRequest.getOrderDate());
		p.addParam("orderTime", payRequest.getOrderTime());
		String lhh = payRequest.getLhh();
		if(StringUtils.isEmpty(lhh))
			lhh ="";
		p.addParam("lhh",lhh);
		String lmc = payRequest.getLhmc();
		if(StringUtils.isEmpty(lmc))
			lmc ="";
		p.addParam("lhmc", lmc);
		String bindId = payRequest.getBindId();
		if(StringUtils.isEmpty(bindId))
			bindId ="";
		p.addParam("bindId", bindId);
		String userId = payRequest.getUerId();
		if(StringUtils.isEmpty(userId))
			userId ="";
		p.addParam("userId", userId);

		PayResult resx = trans(p,url);
		log.info("creditProxyPay参数:"+ JSON.toJSONString(p)+" 访问地址："+url);
		return resx;
	}
	


	
	private PayResult trans(NetworkParams p,String url) {
		BaseNetwork net = new BaseNetwork(url);
		String retstr;
		PayResult resx;
		try {
			retstr = net.getResultStr(p);
			if(retstr==null){
				resx=PayResult.genCustomFailPayResult("server-结算中");
				resx.setCode("9997");
				return resx;
			}
			log.info("trans返回参数:"+retstr);
			resx = (PayResult)JsonUtil.toObj(retstr, PayResult.class);
		}catch (Exception e) {
			resx=PayResult.genCustomFailPayResult("server-结算中");
			resx.setCode("9997");
			log.info(e.getMessage());
		}
		
		return resx;
	}
	
	public static void main(String[] args) {
		
	}
}
