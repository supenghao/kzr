package com.dhk.api.ytutil;

 

/** 
 *@author Lao.ZH 
 *@date 2017-2-13 上午9:33:42 
 *@version V1.0
 */
public class SignUtil {

	/**
	 * 校验签名
	 *@param RM
	 *@return 
	 *@author Lao.ZH
	 */
    public Boolean checkSIGN(RequestMsg RM){
		
		String signStr;
		String singMsg;
		String key = "0984234B4545ABDF43535ABCD1234988";
		
		if(RM.getMSG_TYPE().equals("3003")){
			signStr = RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getUSER_ID()+RM.getMSG_TYPE()+
					  RM.getRESP_CODE()+RM.getRESP_DESCR();
		}else if(RM.getMSG_TYPE().equals("5029")){
			signStr = RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+RM.getUSER_TYPE()+RM.getUSER_ID()+RM.getSIGN_TYPE()
					+RM.getMSG_TYPE()+RM.getRESP_CODE();
		}else if(RM.getMSG_TYPE().equals("1007")){
			signStr = RM.getORDER_ID()+RM.getORI_ORDER_ID()+RM.getUSER_ID()
					+RM.getRESP_CODE()+RM.getRESP_DESCR();
		}
		else{
			signStr = RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+RM.getUSER_ID()+RM.getMSG_TYPE()+
					  RM.getRESP_CODE()+RM.getRESP_DESCR();
		}
		
		
		System.out.println("签名字符串=["+signStr+"]");
		String singMsg1 =null;
        if(RM.getUSER_TYPE().equals("01")) {
        	singMsg = EncryptUtil.AppEncrypt(signStr);
        }else{
        	singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
        	System.out.println("第一次MD5："+singMsg1);
    		String signMsg2 = singMsg1+key;
    		singMsg = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
    		System.out.println("第二次MD5："+singMsg);
        }

    	if(singMsg.equals(RM.getSIGN())){
    		return true;
    	}else{
    		return false;
    	}
		
	}
    
    /**
     * 签名
     *@param RM
     *@return 
     *@author Lao.ZH
     */
    public  String makeSign(RequestMsg RM){
		
		String key = "0984234B4545ABDF43535ABCD1234988";  //  0123456789ABCDEF0123456789ABCDEF
		
		String signStr =RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+
    			RM.getPAY_TYPE()+RM.getUSER_TYPE()+RM.getUSER_ID()+RM.getMSG_TYPE();
		
	    System.out.println("签名字符串："+signStr);
		String singMsg1 = null;
		String signMsg = null ;
    	if(RM.getSIGN_TYPE().equals("03")){
    		singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
    		System.out.println("第一次MD5："+singMsg1);
			String signMsg2 = singMsg1+key;
			signMsg = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
			System.out.println("第二次MD5："+signMsg);
		}else{  //签名类型为01、02
			singMsg1 = EncryptUtil.MD5(signStr, 0);    
			signMsg = EncryptUtil.DES_3(singMsg1, key, 0);
		}
	    
		return signMsg;
	}
    
    
    /**
     * 签名
     *@param RM
     *@return 
     *@author Lao.ZH
     */
    public  String makeYkSign(RequestMsg RM){
		
		String key = "0984234B4545ABDF43535ABCD1234988";  //  0123456789ABCDEF0123456789ABCDEF
		
		String signStr = RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+RM.getUSER_TYPE()+RM.getUSER_ID()+RM.getSIGN_TYPE()
				+RM.getMSG_TYPE();
		
	    System.out.println("签名字符串："+signStr);
		String singMsg1 = null;
		String signMsg = null ;
        if(RM.getUSER_TYPE().equals("01")) {
        	signMsg = EncryptUtil.AppEncrypt(signStr);
        }else if(RM.getSIGN_TYPE().equals("03")){
    		singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
    		System.out.println("第一次MD5："+singMsg1);
			String signMsg2 = singMsg1+key;
			signMsg = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
			System.out.println("第二次MD5："+signMsg);
		}else{  //签名类型为01、02
			singMsg1 = EncryptUtil.MD5(signStr, 0);    
			signMsg = EncryptUtil.DES_3(singMsg1, key, 0);
		}
	    
		return signMsg;
	}
    
    /**
     * 获取绑卡接口签名
     * @param bean
     * @return
     */
    public String makeBindCardSign(RequestMsg RM){
    	String key = "0984234B4545ABDF43535ABCD1234988";
    	String signStr =RM.getORDER_ID()+RM.getUSERTYPE()+RM.getUSER_ID()+RM.getMSG_TYPE()+RM.getCHECK_TYPE()+RM.getACCT_NO()+ RM.getPHONE_NO();
    	System.out.println("参与签名字符串:"+signStr);
    	String sing;
    	if(RM.getUSER_TYPE().equals("01")){
	    	sing = EncryptUtil.AppEncrypt(signStr);
    	}else{
	    	String singMsg1 =null;
	    	if(RM.getSIGN_TYPE().equals("03")){
	    		singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
	    		System.out.println("{"+RM.getORDER_ID()+"}  第一次MD5："+singMsg1);
				String signMsg2 = singMsg1+key;
				sing = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
				System.out.println("{"+RM.getORDER_ID()+"}  第二次MD5："+sing);
			}else{
				singMsg1 = EncryptUtil.MD5(signStr, 0);    
		    	sing = EncryptUtil.DES_3(singMsg1, key, 0);
			}
    	}
    	
    	return sing;
    }
    
    public String makePayByOtherSign(RequestMsg RM){
    	String key_comp = "0984234B4545ABDF43535ABCD1234988";
    	String sing = null;      
    	String signStr;   
    	if(RM.getUSER_TYPE().equals("01")){
    		signStr =RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+
	    			RM.getPAY_TYPE()+RM.getUSERTYPE()+RM.getUSER_ID()+RM.getMSG_TYPE();
	    	sing = EncryptUtil.AppEncrypt(signStr);
    	}else if(RM.getUSER_TYPE().equals("02")||RM.getUSER_TYPE().equals("04")){
	    	
	    	signStr =RM.getORDER_ID()+RM.getTRANS_AMT1()+RM.getTRANS_ATIME()+
	    			RM.getPAY_TYPE()+RM.getUSERTYPE()+RM.getUSER_ID()+RM.getMSG_TYPE();
	    	
	    	String singMsg1 =null;
	    	if(RM.getSIGN_TYPE().equals("03")){
	    		singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
				String signMsg2 = singMsg1+key_comp;
				sing = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
			}else{
				singMsg1 = EncryptUtil.MD5(signStr, 0);    
		    	sing = EncryptUtil.DES_3(singMsg1, key_comp, 0);
			}
    	}
    	
    	return sing;
    }
    
    /**入网信息签名**
     * 
     * @param RM
     * @return
     */
    public String makePayByAppInfoSign(RequestMsg RM){
    	String key_comp = "0984234B4545ABDF43535ABCD1234988";
    	String signStr =RM.getORDER_ID()+RM.getID_CARD()+RM.getMOBILE()+RM.getPAYEE_BANK_NO();
    	String singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
		String signMsg2 = singMsg1+key_comp;
		return EncryptUtil.MD5(signMsg2, 0).toUpperCase();
    }
    
    
    /**
     * 签名
     *@param RM
     *@return 
     *@author Lao.ZH
     */
    public  String makeQueryOrderSign(RequestMsg RM){
		
		String key = "0984234B4545ABDF43535ABCD1234988";  //  0123456789ABCDEF0123456789ABCDEF
		
		String signStr = RM.getORDER_ID()+RM.getORI_ORDER_ID()+RM.getUSER_ID();
		
	    System.out.println("签名字符串："+signStr);
		String singMsg1 = null;
		String signMsg = null ;
    	if(RM.getSIGN_TYPE().equals("03")){
    		singMsg1 = EncryptUtil.MD5(signStr, 1).toUpperCase();
    		System.out.println("第一次MD5："+singMsg1);
			String signMsg2 = singMsg1+key;
			signMsg = EncryptUtil.MD5(signMsg2, 0).toUpperCase();;
			System.out.println("第二次MD5："+signMsg);
		}else{  //签名类型为01、02
			singMsg1 = EncryptUtil.MD5(signStr, 0);    
			signMsg = EncryptUtil.DES_3(singMsg1, key, 0);
		}
	    
		return signMsg;
	}
    
    
}
