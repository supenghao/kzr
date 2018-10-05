package com.aimi;


import com.aimi.bean.base.BaseBeanRequest;
import com.aimi.bean.base.BaseBeanResponse;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 统一请求工具类
 * 
 * @author juxin-ecitic
 *
 */
public class AimiHttpUtils {

	static Logger logger = Logger.getLogger(AimiHttpUtils.class);
	
	/**
	 * 这里最好根据自己开发需求替换成log日志输出
	 * 
	 * @param req 请求对象
	 * @param cls 返回对象
	 * @param url 请求url
	 * @return
	 * @throws Exception
	 */
	public static <REQ extends BaseBeanRequest, RSP extends BaseBeanResponse> RSP sendPost(REQ req, Class<RSP> cls,
																						   String url) throws Exception {
		Map<String, Object> paramMap = MapUtils.objectToMap(req);
		logger.info("1.aimi请求参数[未加密]:"+paramMap);
		Map<String, Object> requestMap = AimiHttpUtils.getRequestParam(paramMap);
		//logger.info("3.aimi最终发送数据包[加密后]:"+requestMap);
		String result = HttpClient4Utils.sendPost(url, requestMap);
		//logger.info("4.aimi返回数据包:"+result);
		if (result == null || "".equals(result)) {
			RSP rsp= cls.newInstance();
			rsp.setCode("-1");
			rsp.setMsg("结算中");
			return rsp;
		}
		String decryptDate = AESUtil.decrypt(result, AimiConfig.AES_KEY);

		logger.info("5.aimi解密数据包结果:"+decryptDate);
		
		boolean flag = Signature.checkIsSignValidFromMap(JSONObject.parseObject(decryptDate,new TypeReference<TreeMap<String, Object>>() {
		}), AimiConfig.MD5_KEY);

		//logger.info("6.aimi验证签名:"+flag);
		
		RSP rsp = JSONObject.parseObject(decryptDate, cls);
		if(rsp==null){
		 	 rsp= cls.newInstance();
			 rsp.setCode("-1");
			 rsp.setMsg(decryptDate);
			 return rsp;
		}
		if (!flag) {
			rsp.setCode("-1");
			rsp.setMsg("返回签名验证失败!");
		}
		return rsp;

		
	}

	/**
	 * 封装请求参数
	 * 
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> getRequestParam(Map<String, Object> paramMap) {
		String sign = Signature.getSign(paramMap, AimiConfig.MD5_KEY);
		paramMap.put("sign", sign);
		String encryptData = AESUtil.encrypt(JSONObject.toJSONString(paramMap), AimiConfig.AES_KEY);
		Map<String, Object> requestParam = new HashMap<String, Object>();
		requestParam.put("accessId", AimiConfig.ACCESSID);
		requestParam.put("data", encryptData);
		return requestParam;
	}


}
