package com.dhk.api.tool;

import java.text.ParseException;

import com.dhk.api.core.impl.PayRequest;

public class Test {

	public static void main(String[] args) throws ParseException {
		PayRequest re = new PayRequest();
		re.setAccBankName("1");
		re.setAccBankNo("2");
		re.setCerdId("3");
		re.setCerdType("4");
		System.out.println(BeenUtil.been2Map(re));
	}

}
