package com.dhk.kernel.dao.jdbc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.dhk.kernel.dao.IBaseDao;
import com.dhk.kernel.dao.Pager;
import com.dhk.kernel.entity.Entity;

public abstract class JdbcBaseDaoSupport <T extends Entity, PK extends Serializable> implements IBaseDao<T, PK>{
	Logger logger = Logger.getLogger(this.getClass());
	public static final String SQL_INSERT = "insert";  
	public static final String SQL_UPDATE = "update";  
	public static final String SQL_DELETE = "delete";
	public static final String SQL_SIMPLE_SELECT = "select"; 
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate nameParameterJdbcTemplate;
	
	/** 具体操作的实体类对象 */
    private Class<T> entityClass;
    
    
    private void instanEntity(){
    	if (entityClass==null){
    		Type superclass = getClass().getGenericSuperclass();
            ParameterizedType type = (ParameterizedType) superclass;
            entityClass = (Class<T>) type.getActualTypeArguments()[0];
    	}
    }
    
    public int insert(T t){
    	instanEntity();
    	String insertSql = packSql(SQL_INSERT);
    	logger.debug("insert sql:"+insertSql);
    	SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
    	KeyHolder keyholder=new GeneratedKeyHolder();
    	getNameParameterJdbcTemplate().update(insertSql, ps,keyholder);
    	
    	//加上KeyHolder这个参数可以得到添加后主键的值
        int m=keyholder.getKey().intValue();
        
        return m;
    }
    
    public int update(T t){
    	instanEntity();
    	String updateSql = packSql(SQL_UPDATE);
    	logger.debug("update sql:"+updateSql);
    	SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
    	
    	return getNameParameterJdbcTemplate().update(updateSql, ps);
    }
    
    public int delete(PK pk){
    	instanEntity();
    	String deleteSql = packSql(SQL_DELETE);
    	logger.debug("delete sql:"+deleteSql);
    	Map<String,Object> map=new HashMap<String,Object>();
    	map.put("id", pk);
    	return getNameParameterJdbcTemplate().update(deleteSql,map);
    }
    
    public T load(PK pk){
    	instanEntity();
    	String selectSql = packSql(SQL_SIMPLE_SELECT);
    	logger.debug("select sql:"+selectSql);
    	Map<String,Object> map=new HashMap<String,Object>();
    	map.put("id", pk);
    	
    	return (T)getNameParameterJdbcTemplate().queryForObject(selectSql, map, new BeanPropertyRowMapper(entityClass));
    }
    
   
   
   public int insert(String sql,T t){
	   KeyHolder keyholder=new GeneratedKeyHolder();
	   SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
	   getNameParameterJdbcTemplate().update(sql, ps,keyholder);
	   //加上KeyHolder这个参数可以得到添加后主键的值

       int m=keyholder.getKey().intValue();
       
       //Map map=keyholder.getKeys();//这样可以得到联合主键的值

       //keyholder.getKeyList();//这样可以得到一些主主键值，若一次添加好几条记录
       
       return m;
   }


	
	public int update(String sql,Map<String,Object> map){
		return getNameParameterJdbcTemplate().update(sql, map);
	}
	
	public int updateBy(String sql,T t){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
		return getNameParameterJdbcTemplate().update(sql, ps);
	}
	
	public int delete(String sql,Map<String,Object> map){
		return getNameParameterJdbcTemplate().update(sql, map);
	}
	
	public int deleteBy(String sql,T t){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(t);
		return getNameParameterJdbcTemplate().update(sql, ps);
	}
	
	public T findBy(String sql,Map<String,Object> map){
		instanEntity();
		return (T)getNameParameterJdbcTemplate().queryForObject(sql, map, new BeanPropertyRowMapper(entityClass));
	}
	
	public void batchUpdate(String sql,List<Ids> a){
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(a.toArray());
		getNameParameterJdbcTemplate().batchUpdate(sql, params);
	}
	
	public void batchExc(String sql,List<T> a){
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(a.toArray());
		getNameParameterJdbcTemplate().batchUpdate(sql, params);
	}
    
    
    public List<T> find(Map<String,Object> map){
    	return find(map,null,null);
    }
    public List<T> find(Map<String,Object> map,String orderName,String orderType){
    	instanEntity();
    	String complexSelectSql = packComplexSelectSql(map,orderName,orderType,null,null);
    	return getNameParameterJdbcTemplate().query(complexSelectSql,map,new BeanPropertyRowMapper(entityClass));
    }
    
    public List<T> find(String sql,Map<String,Object> map){
    	instanEntity();
    	return getNameParameterJdbcTemplate().query(sql, map, new BeanPropertyRowMapper(entityClass));
    }
    
    public List<Map<String,Object>> queryForList(String sql,Map<String,Object> map){
    	return getNameParameterJdbcTemplate().queryForList(sql, map);
    }
    
    public List<T> findForPage(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size){
    	instanEntity();
    	String complexSelectSql = packComplexSelectSql(map,orderName,orderType,start,size);
    	return getNameParameterJdbcTemplate().query(complexSelectSql,map,new BeanPropertyRowMapper(entityClass));
    }
    
//    public List<T> findForPage(Map<String,Object> map,Integer start,Integer size){
//    	return findForPage(map,null,null,start,size);
//    }
    
//    public List<T> findForPage(Map<String,Object> map,Pager pager){
//    	return findForPage(map,null,null,pager);
//    }
    public Pager<T> findForPage(Map<String,Object> map,String orderName,String orderType,Pager<T> pager){
    	instanEntity();
    	String complexCountSql = packeComplexCountSql(map);
    	int count = getNameParameterJdbcTemplate().queryForInt(complexCountSql, map);
    	pager.setTotalCount(count);
    	logger.debug("count:"+count);   	
    	int start = pager.getPageStart();
    	int size = pager.getPageSize();
    	String complexSelectSql = packComplexSelectSql(map,orderName,orderType,start,size);
    	List<T> list = getNameParameterJdbcTemplate().query(complexSelectSql,map,new BeanPropertyRowMapper(entityClass));
    	pager.setLists(list);
    	return pager;
    }
    public Pager<T> findForPage(String sql,Map<String,Object> map,Pager<T> pager){
    	instanEntity();
    	int index = sql.indexOf("from");
    	String psql = sql.substring(index,sql.length());
    	String countRS = "select count(1) nums " + psql;
//    	StringBuffer sb = new StringBuffer();
//    	if (map!=null){
//	        for (Entry<String, Object> entry: map.entrySet()) {
//	        	String key = entry.getKey();
//	            //value = entry.getValue();
//	        	sb.append(" and "+key+"=:"+key);
//	        }
//	        countRS += " " + sb.toString();
//        }
    	logger.debug("sqlPageCountSql:"+countRS);
    	int count = getNameParameterJdbcTemplate().queryForInt(countRS, map);
    	pager.setTotalCount(count);
    	logger.debug("count:"+count+"=="+pager.getCurPageNum());   	
    	int start = pager.getPageStart();
    	int size = pager.getPageSize();
    	String complexSelectSql = sql + " limit "+ start + "," + size;
    	logger.debug("complexSelectSql:"+complexSelectSql);
    	List<T> list = getNameParameterJdbcTemplate().query(complexSelectSql,map,new BeanPropertyRowMapper(entityClass));
    	pager.setLists(list);
//    	return getNameParameterJdbcTemplate().query(sql, map, new BeanPropertyRowMapper(entityClass));
    	return pager;
    }
    
    public Pager<T> queryForPage(String sql,Map<String,Object> map,Pager<T> pager){
    	int index = sql.indexOf("from");
    	String psql = sql.substring(index,sql.length());
    	String countRS = "select count(1) nums " + psql;
    	logger.debug("sqlPageCountSql:"+countRS);
    	int count = getNameParameterJdbcTemplate().queryForInt(countRS, map);
    	pager.setTotalCount(count);
    	logger.debug("count:"+count);   	
    	int start = pager.getPageStart();
    	int size = pager.getPageSize();
    	String complexSelectSql = sql + " limit "+ start + "," + size;
    	logger.debug("complexSelectSql:"+complexSelectSql);
    	
    	List<Map<String,Object>> maps = getNameParameterJdbcTemplate().queryForList(complexSelectSql, map);
    	pager.setMaps(maps);
    	return pager;
    	
    }
    
    public Pager<T> findForPage2OrderBy(String sql,Map<String,Object> map,Pager<T> pager,String orderBySql){
    	instanEntity();
    	int index = sql.indexOf("from");
    	String psql = sql.substring(index,sql.length());
    	String countRS = "select count(1) nums " + psql;

    	
    	//logger.debug("sqlPageCountSql:"+countRS);
    	int count = getNameParameterJdbcTemplate().queryForInt(countRS, map);
    	pager.setTotalCount(count);
    	//logger.debug("count:"+count+"=="+pager.getCurPageNum());   	
    	int start = pager.getPageStart();
    	int size = pager.getPageSize();
    	if (!StringUtils.isBlank(orderBySql)){
    		sql = sql + " " + orderBySql;
    	}
    	String complexSelectSql = sql + " limit "+ start + "," + size;
    	//System.out.println("complexSelectSql:"+complexSelectSql);
    	List<T> list = getNameParameterJdbcTemplate().query(complexSelectSql,map,new BeanPropertyRowMapper(entityClass));
    	pager.setLists(list);
//    	return getNameParameterJdbcTemplate().query(sql, map, new BeanPropertyRowMapper(entityClass));
    	return pager;
    }
    
    public Integer count2Integer(String sql,Map<String,Object> map){
    	Integer count = getNameParameterJdbcTemplate().queryForInt(sql, map);
    	return count;
    }
    
    public Long count2Long(String sql,Map<String,Object> map){
    	Long count = getNameParameterJdbcTemplate().queryForLong(sql, map);
    	return count;
    }

    
    private Column getColumn(Field field){
    	String name = field.getName();//成员变量名
    	Class type = field.getType();
    	Column column = null;
    	if (field.isAnnotationPresent(Column.class)){
    		column = (Column)field.getAnnotation(Column.class);
    	}
    	return column;
    }
//    private String packComplexSelectSql(Map<String,Object> map){
//    	return packComplexSelectSql(map,null,null);
//    }
    private String packeComplexCountSql(Map<String,Object> map){
    	StringBuffer sql = new StringBuffer();
    	//取得表名-通过注解获取
        String tableName = entityClass.getSimpleName();
        if (entityClass.isAnnotationPresent(Table.class)){
        	Table table = (Table)entityClass.getAnnotation(Table.class);
        	tableName = table.name();
        }
        sql.append(" select count(1) from " + tableName +" where (1=1) ");
        if (map!=null){
	        for (Entry<String, Object> entry: map.entrySet()) {
	        	String key = entry.getKey();
	            //value = entry.getValue();
	        	sql.append(" and "+key+"=:"+key);
	        }
        }
        logger.debug("packeComplexCountSql=" + sql);  
        return sql.toString();
    }
    private String packComplexSelectSql(Map<String,Object> map,String orderName,String orderType,Integer start,Integer size){
    	StringBuffer sql = new StringBuffer();
    	
    	//取得表名-通过注解获取
        String tableName = entityClass.getSimpleName();
        if (entityClass.isAnnotationPresent(Table.class)){
        	Table table = (Table)entityClass.getAnnotation(Table.class);
        	tableName = table.name();
        }
    	
        sql.append(" select * from " + tableName +" where (1=1) ");
        if (map!=null){
	        for (Entry<String, Object> entry: map.entrySet()) {
	        	String key = entry.getKey();
	            //value = entry.getValue();
	        	sql.append(" and "+key+"=:"+key);
	        }
        }
        if (orderName!=null && !orderName.trim().equals("")){
        	sql.append(" order by "+orderName);
        	if (orderType!=null && !orderType.trim().equals("")){
        		sql.append(" "+orderType);
        	}
        }else{
        	//sql.append(" order by id desc");
        }
        if (start!=null && size!=null){
        	sql.append(" limit "+start.intValue() + "," + size.intValue());
        }
        logger.debug("packComplexSelectSql=" + sql);  
        return sql.toString();
    }
    // 组装SQL  
    private String packSql(String sqlFlag) {
        StringBuffer sql = new StringBuffer();
        StringBuffer values = new StringBuffer();
        
        //取得表名-通过注解获取
        String tableName = entityClass.getSimpleName();
        if (entityClass.isAnnotationPresent(Table.class)){
        	Table table = (Table)entityClass.getAnnotation(Table.class);
        	tableName = table.name();
        }
                
        Field[] fields = entityClass.getDeclaredFields();
        
        if (sqlFlag.equals(SQL_INSERT)) {  
            sql.append(" insert into " + tableName);  
            sql.append("(");  
            for (int i = 0; fields != null && i < fields.length; i++) {  
                fields[i].setAccessible(true); // 暴力反射  
                String column = fields[i].getName(); 
                Column annColumn = getColumn(fields[i]);
                if (annColumn!=null){
                	column = annColumn.name();
                	sql.append(column).append(","); 
                    values.append(":"+column).append(",");
                }
                
            }  
            values.deleteCharAt(values.length() - 1);
            sql = sql.deleteCharAt(sql.length() - 1);  
            sql.append(") values (");  
//            for (int i = 0; fields != null && i < fields.length; i++) {  
//                sql.append("?,");            	
//            }  
//            sql = sql.deleteCharAt(sql.length() - 1);  
            sql.append(values.toString());
            sql.append(")");  
        } else if (sqlFlag.equals(SQL_UPDATE)) {  
            sql.append(" update " + tableName + " set ");  
            for (int i = 0; fields != null && i < fields.length; i++) {  
                fields[i].setAccessible(true); // 暴力反射  
                String column = fields[i].getName(); 
                Column annColumn = getColumn(fields[i]);
                if (annColumn!=null){
                	column = annColumn.name();
                	sql.append(column).append("=").append(":"+column).append(",");
                }
                if (column.equals("id")) { // id 代表主键  
                    continue;  
                }
                
                //sql.append(column).append("=").append("?,");  
            }  
            sql = sql.deleteCharAt(sql.length() - 1);  
            sql.append(" where id=:id");  
        } else if (sqlFlag.equals(SQL_DELETE)) {  
            sql.append(" delete from " + tableName + " where id=:id");  
        } else if (sqlFlag.equals(SQL_SIMPLE_SELECT)){
        	sql.append(" select * from " + tableName + " where id=:id");
        }
        logger.debug("makeSql=" + sql);  
        return sql.toString();  

    }
    
    
    

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		nameParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public NamedParameterJdbcTemplate getNameParameterJdbcTemplate() {
		if (nameParameterJdbcTemplate==null)
			nameParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		return nameParameterJdbcTemplate;
	}

	public void setNameParameterJdbcTemplate(NamedParameterJdbcTemplate nameParameterJdbcTemplate) {
		this.nameParameterJdbcTemplate = nameParameterJdbcTemplate;
	}
	
	
	
	
	
	


}
