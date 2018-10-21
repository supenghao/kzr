package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_n_repay_cost 实体类<br/>
    * 2017-03-07 04:53:33 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_n_repay_cost")
public class RepayCost  extends Entity {
	private Long repay_plan_id;
	private BigDecimal cost_amount;
	private String status;
	private String exec_time;
	public void setRepay_plan_id(Long repay_plan_id){
		this.repay_plan_id=repay_plan_id;
	}
	public Long getRepay_plan_id(){
		return repay_plan_id;
	}
	public void setCost_amount(BigDecimal cost_amount){
		this.cost_amount=cost_amount;
	}
	public BigDecimal getCost_amount(){
		return cost_amount;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public String getExec_time() {
		return exec_time;
	}
	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}
}

