package com.sunnada.kernel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sunnada.kernel.entity.Entity;

public interface IBaseService<T extends Entity, PK extends Serializable> {

	 public long insert(T t);
		
		public int update(T t);
		
		public int delete(PK pk);
		
		public T load(PK pk);

		//public List<T> findForPage(Map<String,Object> map,Integer start,Integer size);
		
		public List<T> findForPage(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size);
		
		public com.sunnada.kernel.dao.Pager<T> findForPage(Map<String,Object> map, String orderName, String orderType, com.sunnada.kernel.dao.Pager<T> pager);
		
		public com.sunnada.kernel.dao.Pager<T> findForPage(String sql, Map<String,Object> map, com.sunnada.kernel.dao.Pager<T> pager);
		
		public List<T> find(Map<String,Object> map);
		
		public List<T> find(Map<String,Object> map,String orderName,String orderType);
		
		public List<T> find(String sql,Map<String,Object> map);
}
