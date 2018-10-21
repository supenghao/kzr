package com.aimi.demo.callback;

import com.aimi.demo.common.AimiConfig;
import com.aimi.demo.utils.AESUtil;
import com.aimi.demo.utils.Signature;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CallBackDemo {
	
    
	/**
	 * 支付成功回调示例
	 * @param request
	 * @param response
	 */
    public void notify(HttpServletRequest request, HttpServletResponse response){
        String accessId = request.getParameter("accessId");
        String encryptData = request.getParameter("data");
        //截取key的前16位对data进行解密
        String decryptData = AESUtil.decrypt(encryptData,AimiConfig.AES_KEY);
        //解密出来的data转为map对象
        Map<String, Object> result  = JSON.parseObject(decryptData,new TypeReference<TreeMap<String, Object>>() {});
        //验证签名
        if(!Signature.checkIsSignValidFromMap(result,AimiConfig.MD5_KEY)){
            System.out.println("签名验不过!");
            return ;
        }
        Map<String, Object> map = new HashMap<String,Object>();
        String out_trade_no = map.get("out_trade_no").toString();
        String amt = map.get("amt").toString();
        String actualamt = map.get("actualamt").toString();
        String pay_time = map.get("pay_time").toString();
        String transaction_id = map.get("transaction_id").toString();
        String result_code= map.get("result_code").toString();
        String attach = map.get("attach").toString();
        String pay_Ztype = map.get("pay_type").toString();
          
        try {
			response.getWriter().println("SUCCESS");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
    }

}
