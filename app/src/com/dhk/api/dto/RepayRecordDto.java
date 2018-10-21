package com.dhk.api.dto;

public class RepayRecordDto extends IdentityDto {
	
	private String cardNo;

	/**
	 * 获取 cardNo 变量
	 * 
	 * @return 返回 cardNo 变量
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * 设置 cardNo 变量
	 * 
	 * @param cardNo
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DelCreditCarDto [cardNo=" + cardNo + ", toString()="
				+ super.toString() + "]";
	}
}
