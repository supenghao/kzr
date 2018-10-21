package com.dhk.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

import java.math.BigDecimal;

@SuppressWarnings("serial")
@Table(name="t_s_unfreeze_ls")
public class UnfreezeLs extends Entity {

	private String transDate;
	private String transTime;
	private BigDecimal amount;
	private Long userId;
	private String cardNo;
	private Long replanRecordid;
	private BigDecimal preAmount;
	private BigDecimal curAmount;
	private String repayMonth;
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Long getReplanRecordid() {
		return replanRecordid;
	}
	public void setReplanRecordid(Long replanRecordid) {
		this.replanRecordid = replanRecordid;
	}
	public BigDecimal getPreAmount() {
		return preAmount;
	}
	public void setPreAmount(BigDecimal preAmount) {
		this.preAmount = preAmount;
	}
	public BigDecimal getCurAmount() {
		return curAmount;
	}
	public void setCurAmount(BigDecimal curAmount) {
		this.curAmount = curAmount;
	}
	public String getRepayMonth() {
		return repayMonth;
	}
	public void setRepayMonth(String repayMonth) {
		this.repayMonth = repayMonth;
	}
	
}
