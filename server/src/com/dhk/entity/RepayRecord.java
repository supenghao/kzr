package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_n_repay_record 实体类<br/>
    * 2017-02-19 02:13:28 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_n_repay_record")
public class RepayRecord  extends Entity {
	private String repay_month;
	private String card_no;
	private BigDecimal amount;
	private int repay_count;
	private long user_id;
	private String isUnFreeze;
	private String orderNo;//订单号
	private String status;//状态 0|未处理或处理中；1|成功；2|失败；

	   public String getStatus() {
		   return status;
	   }

	   public void setStatus(String status) {
		   this.status = status;
	   }

	   public void setRepay_month(String repay_month){
	    this.repay_month=repay_month;
	}
	public String getRepay_month(){
		return repay_month;
	}
	public void setCard_no(String card_no){
		this.card_no=card_no;
	}
	public String getCard_no(){
		return card_no;
	}
	public void setAmount(BigDecimal amount){
		this.amount=amount;
	}
	public BigDecimal getAmount(){
		return amount;
	}
	public int getRepay_count() {
		return repay_count;
	}
	public void setRepay_count(int repay_count) {
		this.repay_count = repay_count;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getIsUnFreeze() {
		return isUnFreeze;
	}
	public void setIsUnFreeze(String isUnFreeze) {
		this.isUnFreeze = isUnFreeze;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
}

