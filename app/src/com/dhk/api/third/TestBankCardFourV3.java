package com.dhk.api.third;

import com.google.gson.JsonObject;
//import cn.minivision.constant.ParamsConfig;
import com.xdream.kernel.util.JsonUtil;

/**
 * 
 * Tile TestBankCardFourV3
 * Desc 银行卡信息四维验证V3
 * Company 甄视科技
 * @author minivision
 * date 2017年2月27日
 *
 */

public class TestBankCardFourV3 {
	
	private static String userName = "tuotuoxinxi";
	private static String password = "tuotuoxinxi0626";
	private static String nameIDCardPhoneAccountVerifyV3Url = "https://www.miniscores.cn:8313/CreditFunc/v2.1/NameIDCardPhoneAccountVerifyV3";
	

	/**
	 * 银行卡信息四维验证V3参数形成
	 * 
	 * @param name
	 *            姓名
	 * @param idCard
	 *            身份证号
	 * @param account
	 *            银行卡号
	 * @param phone
	 *            电话号码
	 * @return
	 */
	public static String getNameIDAccountPhoneParas(String name, String idCard, String account, String phone) {
		JsonObject totalJsonObj = new JsonObject();
		totalJsonObj.addProperty("loginName", userName);
		totalJsonObj.addProperty("pwd", password);
		totalJsonObj.addProperty("serviceName", "NameIDCardPhoneAccountVerifyV3");
		//totalJsonObj.addProperty("reqType", "demo");
		//System.out.println(totalJsonObj.toString());

		JsonObject paramJsonObj = new JsonObject();
		paramJsonObj.addProperty("idCard", idCard);
		paramJsonObj.addProperty("name", name);
		paramJsonObj.addProperty("accountNo", account);
		paramJsonObj.addProperty("mobile", phone);
		totalJsonObj.addProperty("param", paramJsonObj.toString());
		return totalJsonObj.toString();
	}

	// 银行卡四维验证V3
	public static BankCardMsg testNameIDCardPhoneAccountVerifyV3(String name, String idCard, String mobile, String accountNo) throws Exception {
		String postStr;
		String result;
		postStr = getNameIDAccountPhoneParas(name, idCard, accountNo, mobile);
		result = HttpsClientUtil.doPost(nameIDCardPhoneAccountVerifyV3Url, postStr, "utf-8");
		BankCardMsg msg=(BankCardMsg) JsonUtil.toObj(result, BankCardMsg.class);
//		System.out.println(msg.getGuid());
//		System.out.println(msg.getRESULT());
//		System.out.println(msg.getMESSAGE());
//		System.out.println("------银行卡四维验证------");
//		System.out.println("addC111111123111111"+result);
		return msg;
		
	}

	public static void main(String args[]) {

		String mobile = "***";
		String name = "***";
		String idCard = "***";
		String accountNo = "***";
		
		try {
			// 银行卡四维验证V3测试
			testNameIDCardPhoneAccountVerifyV3(name, idCard, mobile, accountNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
