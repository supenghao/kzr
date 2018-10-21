package com.sunnada.kernel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public  abstract class ServiceFacade <T extends com.sunnada.kernel.entity.Entity, PK extends Serializable> implements IBaseService<T, PK>{

public abstract com.sunnada.kernel.dao.IBaseDao<T,PK> getDao();
	
    public long insert(T t){
    	return getDao().insert(t);
    }
	
	public int update(T t){
		return getDao().update(t);
	}
	
	public int delete(PK pk){
		return getDao().delete(pk);
	}
	
	public T load(PK pk){
		return getDao().load(pk);
	}
	
//    public List<T> findForPage(Map<String,Object> map,Integer start,Integer size){
//    	return getDao().findForPage(map, start, size);
//    }
	
	public List<T> findForPage(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size){
		return getDao().findForPage(map, orderName, orderType, start, size);
	}
	
	public com.sunnada.kernel.dao.Pager<T> findForPage(Map<String,Object> map, String orderName, String orderType, com.sunnada.kernel.dao.Pager<T> pager){
		return getDao().findForPage(map, orderName, orderType, pager);
	}
	
	public com.sunnada.kernel.dao.Pager<T> findForPage(String sql, Map<String,Object> map, com.sunnada.kernel.dao.Pager<T> pager){
		return getDao().findForPage(sql, map, pager);
	}
	
	public List<T> find(String sql,Map<String,Object> map){
		return getDao().find(sql, map);
	}
	
	public List<T> find(Map<String,Object> map){
		return getDao().find(map);
	}
	public List<T> find(Map<String,Object> map,String orderName,String orderType){
		return getDao().find(map, orderName, orderType);
	}
}
