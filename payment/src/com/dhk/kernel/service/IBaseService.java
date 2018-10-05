package com.dhk.kernel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.dhk.kernel.dao.Pager;
import com.dhk.kernel.entity.Entity;

public interface IBaseService<T extends Entity, PK extends Serializable> {

	 public int insert(T t);
		
		public int update(T t);
		
		public int delete(PK pk);
		
		public T load(PK pk);

		//public List<T> findForPage(Map<String,Object> map,Integer start,Integer size);
		
		public List<T> findForPage(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size);
		
		public Pager<T> findForPage(Map<String,Object> map,String orderName,String orderType,Pager<T> pager);
		
		public Pager<T> findForPage(String sql,Map<String,Object> map,Pager<T> pager);
		
		public List<T> find(Map<String,Object> map);
		
		public List<T> find(Map<String,Object> map,String orderName,String orderType);
		
		public List<T> find(String sql,Map<String,Object> map);
}
