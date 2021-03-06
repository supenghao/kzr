package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_message 实体类<br/>
    * 2017-02-15 11:41:36 bianzk
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_message")
public class Message  extends Entity {
	
	private Long user_id;
	private Long transwater_id;
	private String createtime;
	private String status;
	private String message_content;
	private Long repay_id;
	private Long cost_id;
	
	
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getTranswater_id() {
		return transwater_id;
	}
	public void setTranswater_id(Long transwater_id) {
		this.transwater_id = transwater_id;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public Long getRepay_id() {
		return repay_id;
	}
	public void setRepay_id(Long repay_id) {
		this.repay_id = repay_id;
	}
	public Long getCost_id() {
		return cost_id;
	}
	public void setCost_id(Long cost_id) {
		this.cost_id = cost_id;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
}

