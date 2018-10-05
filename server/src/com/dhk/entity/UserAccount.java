package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_user_account 实体类<br/>
    * 2017-01-09 09:04:57 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_user_account")
public class UserAccount  extends Entity {

	@Column(name="USER_ID")
	private Long userId;

	@Column(name="CUR_BALANCE")
	private BigDecimal curBalance;

	@Column(name="UPDATE_DATE")
	private String updateDate;
	
	@Column(name="UPDATE_Time")
	private String updateTime;
	

	@Column(name="RECASH_FREEZE")
	private BigDecimal  recashFreeze;
	
	public String getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * 流入账户金额，如果为负，则表示支出
	 */
	private BigDecimal inAccount;
	
	
	public BigDecimal getInAccount() {
		return inAccount;
	}
	
	
	public void setInAccount(BigDecimal inAccount) {
		this.inAccount = inAccount;
	}
	
	public void setUserId(Long userId){
		this.userId=userId;
	}
	
	public Long getUserId(){
		return userId;
	}
	
	
	
	public void setUpdateDate(String updateDate){
		this.updateDate=updateDate;
	}
	
	public String getUpdateDate(){
		return updateDate;
	}

	

	public BigDecimal getRecashFreeze() {
		return recashFreeze;
	}

	public void setRecashFreeze(BigDecimal recashFreeze) {
		this.recashFreeze = recashFreeze;
	}

	public BigDecimal getCurBalance() {
		return curBalance;
	}

	public void setCurBalance(BigDecimal curBalance) {
		this.curBalance = curBalance;
	}

	
}

