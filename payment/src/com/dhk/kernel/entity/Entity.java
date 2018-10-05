package com.dhk.kernel.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Entity implements Serializable{

	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	//附加方法
	public void afterSave(){
	}

	public boolean beforeDelete() throws Exception{
		return true;
	}

	public void afterDelete(){
	}
	public boolean beforeSave() throws Exception{
		return true;
	}

	public boolean beforeUpdate() throws Exception{
		return true;
	}
	public void afterUpdate(){
	}
}
