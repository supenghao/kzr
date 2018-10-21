package com.dhk.api.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.NetworkInterface;
import com.dhk.api.core.Verify;
import com.dhk.api.dto.AddUserCarDto;
import com.dhk.api.tool.M;

/**
 * 芝麻信用分校验
 * 
 */
public class UserCardVerify implements Verify {

	private String url = null;
	private String error;
	private String name, bankCard, phone, certNo;

	/**
	 * 验证借记卡是否有效
	 * 
	 * @param dto
	 */
	public UserCardVerify(AddUserCarDto dto, String url) {
		this.url = url;
		name = dto.getRealname();
		bankCard = dto.getCardNo();
		phone = dto.getPhoneNo();
		certNo = dto.getId_number();
	}

	@Override
	public boolean isValueable() {
		if (url == null)
			return false;
		try {
			BaseNetwork net = new BaseNetwork(url);
			NetworkInterface.NetworkParams p = new NetworkInterface.NetworkParams();
			p.addParam("name", name);
			p.addParam("phone", phone);
			p.addParam("certNo", certNo);
			p.addParam("bankCard", bankCard);
			String ret = net.getResultStr(p);
			if (M.printDebug)
				System.out.println("ret:" + ret);
			if (ret != null && !ret.isEmpty()) {
				JSONObject j = JSONObject.parseObject(ret);
				if ("0000".equals(j.getString("code"))) {
					String scode = j.getString("data");
					j = JSONObject.parseObject(scode);
					int t = Integer.parseInt(j.getString("ivsScore"));
					if (M.printDebug)
						System.out.println("scode:" + t);
					if (t >= 70)
						return true;
					else
						return false;
				} else {
					M.logger.error("芝麻验证接口返回未知代码!!!");
				}
			} else {
				M.logger.error("借记卡验证接口URL失效!!!");
			}
		} catch (Exception e) {
			M.logger.error("验证借记卡未知异常", e);
		}
		return false;
	}

	@Override
	public String errorMsg() {
		return error;
	}

	// {"message":"操作成功","data":{"ivsScore":79},"code":"0000"}
	public static void main(String[] args) {
		M.debug = false;
		AddUserCarDto dto = new AddUserCarDto();
		dto.setRealname("丘春华");
		dto.setCardNo("6227001882710116082");
		dto.setId_number("350823199405202073");
		// dto.setId_number("350823199405202073");
		dto.setPhoneNo("18860125204");
		String url = "http://120.24.37.139:8083/payment/zmxy/ivsByNameAndBankCard";
		UserCardVerify f = new UserCardVerify(dto, url);
		System.out.println(f.isValueable());
		M.debug = true;
	}

}
