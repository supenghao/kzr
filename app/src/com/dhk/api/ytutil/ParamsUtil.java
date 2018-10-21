package com.dhk.api.ytutil;

import java.text.SimpleDateFormat;
import java.util.Date;

 


/** 
 *@author Lao.ZH 
 *@date 2017-2-13 上午9:45:40 
 *@version V1.0
 */
public class ParamsUtil {

	/**
	 * 拼接请求参数
	 *@return 
	 *@author Lao.ZH
	 */
	public String getParams(){
        
        String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="0.01";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3101";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";      
		
		RequestMsg requestMsg = new RequestMsg();             
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
		requestMsg.setADD1("ADD1");                           //预留字段，原样返回  
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
			"&GOODS_DESC="+GOODS_DESC;

        return param;
    }
	
	/**
	 * 拼接绑卡接口请求参数
	 * @return
	 */
	public String getBindCardParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String    USER_TYPE ="02";          
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";            
		String    BUS_CODE  ="1011";  
		String    CHECK_TYPE = "01";
		String    ID_NO = "351201199908231738";
		String    NAME  ="测试";
		String    ACCT_NO ="6217001830001732";
		String    PHONE_NO = "15394741289";
		
		RequestMsg bean = new RequestMsg();
		bean.setORDER_ID(ORDER_ID);
		bean.setUSERTYPE(USER_TYPE);
		bean.setUSER_TYPE(USER_TYPE);
		bean.setUSER_ID(USER_ID);
		bean.setSIGN_TYPE(SIGN_TYPE);
		bean.setMSG_TYPE(BUS_CODE);
		bean.setCHECK_TYPE(CHECK_TYPE);
		bean.setID_NO(ID_NO);
		bean.setNAME(NAME);
		bean.setACCT_NO(ACCT_NO);
		bean.setPHONE_NO(PHONE_NO);
		
		SignUtil signUtil = new SignUtil();
		String param  = "ORDER_ID="+ORDER_ID
				+"&USER_TYPE="+USER_TYPE
				+"&USER_ID="+USER_ID
				+"&SIGN="+signUtil.makeBindCardSign(bean)
				+"&SIGN_TYPE="+SIGN_TYPE
				+"&BUS_CODE="+BUS_CODE
				+"&CHECK_TYPE="+CHECK_TYPE
				+"&ID_NO="+ID_NO
				+"&NAME="+NAME
				+"&ACCT_NO="+ACCT_NO
				+"&PHONE_NO="+PHONE_NO;
		
		return param;
	}
	
	
	/**
	 * 拼接绑卡接口请求参数
	 * @return
	 */
	public String getBindCardByYKParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String    USER_TYPE ="02";          
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";            
		String    BUS_CODE  ="1011";  
		String    CHECK_TYPE = "01";
		String    ID_NO = "352121197712102939";
		String    NAME  ="温启勇";
		String    ACCT_NO ="5124660009282832";
		String    PHONE_NO = "18950099902";
		String    CVN2 = "064";
		String    VALID ="0518";
		String    STATEDATE = "08";
		String    PAYDATE = "24";
		
		RequestMsg bean = new RequestMsg();
		bean.setORDER_ID(ORDER_ID);
		bean.setUSERTYPE(USER_TYPE);
		bean.setUSER_TYPE(USER_TYPE);
		bean.setUSER_ID(USER_ID);
		bean.setSIGN_TYPE(SIGN_TYPE);
		bean.setMSG_TYPE(BUS_CODE);
		bean.setCHECK_TYPE(CHECK_TYPE);
		bean.setID_NO(ID_NO);
		bean.setNAME(NAME);
		bean.setACCT_NO(ACCT_NO);
		bean.setPHONE_NO(PHONE_NO);
		
		SignUtil signUtil = new SignUtil();
		String param  = "ORDER_ID="+ORDER_ID
				+"&USER_TYPE="+USER_TYPE
				+"&USER_ID="+USER_ID
				+"&SIGN="+signUtil.makeBindCardSign(bean)
				+"&SIGN_TYPE="+SIGN_TYPE
				+"&BUS_CODE="+BUS_CODE
				+"&CHECK_TYPE="+CHECK_TYPE
				+"&ID_NO="+ID_NO
				+"&NAME="+NAME
				+"&ACCT_NO="+ACCT_NO
				+"&CVN2="+CVN2
				+"&VALID="+VALID
				+"&STATEDATE="+STATEDATE
				+"&PAYDATE="+PAYDATE
				+"&PHONE_NO="+PHONE_NO;
		
		return param;
	}
	
	/**
	 * 拼接实名认证请求参数
	 * @return
	 */
	public String getCertificationParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    USER_TYPE ="02";                                         
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="1013";               //业务代码         
		String    CHECK_TYPE = "01";
		String    ACCT_NO ="6217001830001732";
		String    PHONE_NO = "15394741289";
		String    ID_NO = "351201199908231738";
		String    NAME  ="测试";
		String    ADD1  = "ADD1";
		
		RequestMsg requestMsg = new RequestMsg();             
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setTRANS_ATIME(ORDER_TIME);                //订单时间
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setMSG_TYPE(BUS_CODE);                     //业务代码
		requestMsg.setADD1(ADD1);                           //预留字段，原样返回
		requestMsg.setCHECK_TYPE(CHECK_TYPE);
		requestMsg.setUSERTYPE(USER_TYPE);
		requestMsg.setACCT_NO(ACCT_NO);
		requestMsg.setPHONE_NO(PHONE_NO);
		requestMsg.setID_NO(ID_NO);
		requestMsg.setNAME(NAME);
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
        		"&ORDER_TIME="+ORDER_TIME+
        		"&USER_TYPE="+USER_TYPE+
        		"&USER_ID="+USER_ID+
        		"&SIGN_TYPE="+SIGN_TYPE+
        		"&BUS_CODE="+BUS_CODE+
        		"&CHECK_TYPE="+CHECK_TYPE+
        		"&ACCT_NO="+ACCT_NO+
        		"&PHONE_NO="+PHONE_NO+
        		"&ID_NO="+ID_NO+
        		"&NAME="+NAME+
        		"&ADD1="+ADD1+
        		"&SIGN="+signUtil.makeBindCardSign(requestMsg);
        		
		return param;
	}
	
	public String getPayByOtherParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="0.01";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                              
		String    USER_TYPE ="02";                                         
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="01";                               
		String    BUS_CODE  ="1006";               //业务代码
		String    BANK_CODE = "03050000";
		String    BANK_NAME = "中国民生银行";
		String    ACCOUNT_NO = "5124660009282832";
		String    ACCOUNT_NAME = "温大勇";
		String    CCT       ="CNY";                                 
		
		RequestMsg requestMsg = new RequestMsg();             
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setTRANS_AMT1(ORDER_AMT);                 //金额，字符串型
		requestMsg.setTRANS_ATIME(ORDER_TIME);                //订单时间
		requestMsg.setPAY_TYPE(PAY_TYPE);                     //支付类型
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setMSG_TYPE(BUS_CODE);                     //业务代码
		requestMsg.setCCT(CCT);                               //币种
		requestMsg.setBANK_CODE(BANK_CODE);
		requestMsg.setACCOUNT_NO(ACCOUNT_NO);
		requestMsg.setACCOUNT_NAME(ACCOUNT_NAME);
		
		requestMsg.setADD1("ADD1");                           //预留字段，原样返回  
		requestMsg.setADD2("ADD2");                           //预留字段，原样返回  
		requestMsg.setADD3("ADD3");                           //预留字段，原样返回  
		requestMsg.setADD4("ADD4");                           //预留字段，原样返回  
		requestMsg.setADD5("ADD5");                           //预留字段，原样返回  
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&ORDER_AMT="+ORDER_AMT+
			"&ORDER_TIME="+ORDER_TIME+
			"&PAY_TYPE="+PAY_TYPE+
			"&USER_TYPE="+USER_TYPE+
			"&USER_ID="+USER_ID+
			"&SIGN="+signUtil.makeSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&CCT="+CCT+
			"&BANK_CODE="+BANK_CODE+
			"&BANK_NAME="+BANK_NAME+
			"&ACCOUNT_NO="+ACCOUNT_NO+
			"&ACCOUNT_NAME="+ACCOUNT_NAME;

        return param;
		
	}
	
	public String getQuickPayParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="0.01";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3003";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";      
		 
		RequestMsg requestMsg = new RequestMsg();             
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
		requestMsg.setADD1("ADD1");                           //预留字段，原样返回  
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
			"&SIGN="+signUtil.makePayByOtherSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&CCT="+CCT+
			"&GOODS_NAME="+GOODS_NAME+
			"&GOODS_DESC="+GOODS_DESC;
		
		
		return param;
	}
	
	public String getQuickPayJNParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="0.01";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3001";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";  
		String    AGEN_NO   = "123456123456123";
		String    ID_NO     = "350216499201311254";
		String    SETT_ACCT_NO = "123456789123456";
		String    CARD_INST_NAME = "中国农业银行";
		String    NAME        = "测试";
		String    SETT_AMT   = "10.00";
		 
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setID_NO(ID_NO);
		requestMsg.setAGEN_NO(AGEN_NO);
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
		requestMsg.setADD1("ADD1");                           //预留字段，原样返回  
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
			"&SIGN="+signUtil.makePayByOtherSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&CCT="+CCT+
			"&GOODS_NAME="+GOODS_NAME+
			"&GOODS_DESC="+GOODS_DESC+
			"&AGEN_NO="+AGEN_NO+
			"&ID_NO="+ID_NO+
			"&SETT_ACCT_NO="+SETT_ACCT_NO+
			"&CARD_INST_NAME="+CARD_INST_NAME+
			"&NAME="+NAME+
			"&SETT_AMT="+SETT_AMT;
		
		
		return param;
	}
	
	/***
	 * 入网参数
	 * @return
	 */
	public String getAppInfoParams(){
		String    ORDER_ID  = "Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); //订单号                        
		String    USER_ID   = "990393000051003"; //商户号
		String    MERCHANT_NAME = "西红柿便利店";  //商户名称                                
		String    MERCHANT_PROVINCE ="福建省";  //商户所在省份                              
		String    MERCHANT_CITY  ="厦门市";  //商户所在城市                                     
		String    MERCHANT_ADDRESS ="思明区豆仔尾路"; //                                        
		String    FAMILY_NAME    ="许仙"; //姓名
		String    ID_CARD  ="352120199712102939"; //证件号
		String    MOBILE ="18950099123";  //手机号                               
		String    PAYEE_BANK_NO  ="6227001935880278375";  //
		String    PAYEE_BANK_NAME ="建设银行";    //                             
		String    PAYEE_BANK_ID ="105100000017"; //                                 
		String    PAYEE_BRANCH_NAME="建设银行"; //  
		String    PAYEE_BRANCH_CODE   = "105100000017"; //
		String    PAYEE_BRANCH_PROVINCE     = "福建省"; //
		String    PAYEE_BRANCH_CITY = "厦门市"; //
		String    MERCHANT_OPER_FLAG = "A"; //A:新增 M:修改

		 
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setORDER_ID(ORDER_ID); 
		requestMsg.setID_CARD(ID_CARD);
		requestMsg.setMOBILE(MOBILE);
		requestMsg.setPAYEE_BANK_NO(PAYEE_BANK_NO);
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&USER_ID="+USER_ID+
			"&MERCHANT_NAME="+MERCHANT_NAME+
			"&MERCHANT_PROVINCE="+MERCHANT_PROVINCE+
			"&MERCHANT_CITY="+MERCHANT_CITY+
			"&MERCHANT_ADDRESS="+MERCHANT_ADDRESS+
			"&FAMILY_NAME="+FAMILY_NAME+
			"&SIGN="+signUtil.makePayByAppInfoSign(requestMsg)+
			"&ID_CARD="+ID_CARD+
			"&MOBILE="+MOBILE+
			"&PAYEE_BANK_NO="+PAYEE_BANK_NO+
			"&PAYEE_BANK_NAME="+PAYEE_BANK_NAME+
			"&PAYEE_BANK_ID="+PAYEE_BANK_ID+
			"&PAYEE_BRANCH_NAME="+PAYEE_BRANCH_NAME+
			"&PAYEE_BRANCH_CODE="+PAYEE_BRANCH_CODE+
			"&PAYEE_BRANCH_PROVINCE="+PAYEE_BRANCH_PROVINCE+
			"&PAYEE_BRANCH_CITY="+PAYEE_BRANCH_CITY+
			"&MERCHANT_OPER_FLAG="+MERCHANT_OPER_FLAG;
		
		return param;
	}
	
	
	public String getQuickPayJNH5Params(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="100.00";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990393000051003";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3001";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";  
		String    ID_NO     = "350216499201311254";
		String    SETT_ACCT_NO = "123456789123456";
		String    CARD_INST_NAME = "中国农业银行";
		String    NAME        = "测试";
		String    PHONE_NO   = "15606001601";
		String    ADD1   = "49845112157194111"; //支付卡号
		 
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
	
	public String getQuickPayYKParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="100.00";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="5029";               //业务代码                     
		String    PHONE_NO   = "18950099202";
		String    ADD1   = "512466000924832"; //支付卡号
		String    ADD2   = "温大勇";//姓名
		String    ADD3   = "02";//卡类型
		String    ADD4   = "3521211977122102939";//身份证
		String    CVN2   = "064";
		String    VALID  = "0518";
		
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setTRANS_AMT1(ORDER_AMT);                 //金额，字符串型
		requestMsg.setTRANS_ATIME(ORDER_TIME);                //订单时间
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setMSG_TYPE(BUS_CODE);                     //业务代码
		
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&ORDER_AMT="+ORDER_AMT+
			"&ORDER_TIME="+ORDER_TIME+
			"&USER_TYPE="+USER_TYPE+
			"&PAY_TYPE="+PAY_TYPE+
			"&BG_URL="+BG_URL+
			"&USER_ID="+USER_ID+
			"&SIGN="+signUtil.makeYkSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE+
			"&BUS_CODE="+BUS_CODE+
			"&ADD1="+ADD1+
			"&ADD2="+ADD2+
			"&ADD3="+ADD3+
			"&ADD4="+ADD4+
			"&ADD4="+ADD4+
			"&PHONE_NO="+PHONE_NO+
			"&CVN2="+CVN2+
			"&VALID="+VALID;
		
		return param;
	}
	
	
	public String getH5ZQParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="100.00";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990393000051003";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3001";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";  
		String    ID_NO     = "350216499201311254";
		String    SETT_ACCT_NO = "123456789123456";
		String    CARD_INST_NAME = "中国农业银行";
		String    NAME        = "测试";
		String    PHONE_NO   = "15095959311";
		String    ADD1   = "49845112157194111"; //支付卡号
		 
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
	
	
	public String getH5FDParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORDER_AMT ="100.00";                                     
		String    ORDER_TIME=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());                                  
		String    PAY_TYPE  ="13";                                       
		String    USER_TYPE ="02";                                         
		String    BG_URL    ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    PAGE_URL  ="http://123.182.255.83:8090/NMobPay/yzf002.xml";
		String    USER_ID   ="990393000051003";       //商户号
		String    SIGN_TYPE ="03";                               
		String    BUS_CODE  ="3001";               //业务代码                     
		String    CCT       ="CNY";                                 
		String    GOODS_NAME="测试商品";                                  
		String    GOODS_DESC="测试商品";  
		String    ID_NO     = "350216499201311254";
		String    SETT_ACCT_NO = "123456789123456";
		String    CARD_INST_NAME = "中国农业银行";
		String    NAME        = "测试";
		String    PHONE_NO   = "15095959311";
		String    ADD1   = "49845112157194111"; //支付卡号
		String   SETT_AMT = "99.00";
		 
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
			"&SETT_AMT="+SETT_AMT+
			"&ADD1="+ADD1;
		
		return param;
	}
	
	
	
	/***
	 * 订单查询
	 * @return
	 */
	public String getQueryOrderParams(){
		String    ORDER_ID  ="Test"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());        //Test20170206164539185                        
		String    ORI_ORDER_ID ="Test20171103142944816";                                     
		String    USER_TYPE ="02";                                         
		String    USER_ID   ="990581000011021";       //商户号
		String    SIGN_TYPE ="03";                               
		 
		RequestMsg requestMsg = new RequestMsg();     
		requestMsg.setORDER_ID(ORDER_ID);                     //订单号
		requestMsg.setUSER_TYPE(USER_TYPE);                   //用户类型
		requestMsg.setUSER_ID(USER_ID);                       //测试商户号
		requestMsg.setSIGN_TYPE(SIGN_TYPE);                   //签名类型
		requestMsg.setORI_ORDER_ID(ORI_ORDER_ID);    
		SignUtil signUtil = new SignUtil();
        String param ="ORDER_ID="+ORDER_ID+
			"&ORG_ORDER_ID="+ORI_ORDER_ID+
			"&USER_TYPE="+USER_TYPE+
			"&USER_ID="+USER_ID+
			"&SIGN="+signUtil.makeQueryOrderSign(requestMsg)+
			"&SIGN_TYPE="+SIGN_TYPE;
		return param;
	}
	
	
}
