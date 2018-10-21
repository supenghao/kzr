package com.dhk.api.dto;

public class EditCreditCarDto extends DelCreditCarDto {

	private String bill_date, repay_date, mail_addr, mail_passwd,cvn2, expDate,maxmon;

	public String getMaxmon() {
		return maxmon;
	}

	public void setMaxmon(String maxmon) {
		this.maxmon = maxmon;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getRepay_date() {
		return repay_date;
	}

	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}

	public String getMail_addr() {
		return mail_addr;
	}

	public void setMail_addr(String mail_addr) {
		this.mail_addr = mail_addr;
	}

	public String getMail_passwd() {
		return mail_passwd;
	}

	public void setMail_passwd(String mail_passwd) {
		this.mail_passwd = mail_passwd;
	}
	

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EditCreditCarDto [bill_date=" + bill_date + ", repay_date="
				+ repay_date + ", mail_addr=" + mail_addr + ", mail_passwd="
				+ mail_passwd + "]";
	}
}
