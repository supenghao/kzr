package com.dhk.payment.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UwApplyBidPropertyDomain")
public class ApplyBidPropertyDomain implements Serializable{

	private String fieldaj;

	public String getFieldaj() {
		return fieldaj;
	}

	public void setFieldaj(String fieldaj) {
		this.fieldaj = fieldaj;
	}
	
}
