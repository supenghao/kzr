package com.dhk.kernel.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.dhk.kernel.dao.jdbc.Ids;
import com.dhk.kernel.entity.Entity;

public interface IBaseDao <T extends Entity,PK extends Serializable>{

	/**
	 * 
	 * @Title: insert
	 * @Description: 添加数据
	 * @param 实体类
	 * @return int
	 */
	public int insert(T t);

	/**
	 * 
	 * @Title: update
	 * @Description: 更新数据
	 * @param 实体类
	 * @return int
	 */
	public int update(T t);

	/**
	 * 
	 * @Title: delete
	 * @Description:删除数据
	 * @param 实体类
	 * @return int
	 */
	public int delete(PK pk);
	
	/**
	 * 根据ID查找单条记录
	 * @param id
	 * @return
	 */
	public T load(PK pk);
	/**
	 * 自定义查询，单表
	 * @param map
	 * @return
	 */
	public List<T> find(Map<String,Object> map);
	
	
	
	public List<T> find(Map<String,Object> map,String orderName,String orderType);
	
	
	/**
	 * 自定义查询，可以多表查询,--也可分页
	 * @param sql
	 * @param map
	 * @return
	 */
	public List<T> find(String sql,Map<String,Object> map);
	
	public List<Map<String,Object>> queryForList(String sql,Map<String,Object> map);
	/**
	 * 分页mysql
	 */
	//public List<T> findForPage(Map<String,Object> map,Integer start,Integer size);
	public List<T> findForPage(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size);
	
	//public List<T> findForPage(Map<String,Object> map,Pager pager);
	public Pager<T> findForPage(Map<String,Object> map,String orderName,String orderType,Pager<T> pager);
	
	/**
	 * @param sql
	 * @param map
	 * @param pager
	 * @return
	 */
	public Pager<T> findForPage(String sql,Map<String,Object> map,Pager<T> pager);
	
	public Pager<T> queryForPage(String sql,Map<String,Object> map,Pager<T> pager);
	
	public int insert(String sql,T t);
	
	
	
	
	public int update(String sql,Map<String,Object> map);
	
	public int updateBy(String sql,T t);
	
	public int delete(String sql,Map<String,Object> map);
	
	public int deleteBy(String sql,T t);
	
	public T findBy(String sql,Map<String,Object> map);
	
	public void batchUpdate(String sql,List<Ids> a);
	
	public void batchExc(String sql,List<T> a);
	
	public Pager<T> findForPage2OrderBy(String sql,Map<String,Object> map,Pager<T> pager,String orderBySql);
	
	public Integer count2Integer(String sql,Map<String,Object> map);
	
	public Long count2Long(String sql,Map<String,Object> map);
	
	
	
}
