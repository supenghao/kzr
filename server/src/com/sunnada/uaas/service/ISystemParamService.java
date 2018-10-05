package com.sunnada.uaas.service;

import java.util.List;

import com.sunnada.uaas.entity.SystemParam;
import org.springframework.cache.annotation.Cacheable;

public interface ISystemParamService {

	/**
	 * 根据变量名取参数值
	 * @param paramName
	 * @return
	 */
	@Cacheable(value="t_system_param", key="#paramName")
	public SystemParam findByParamName(String paramName);
	
	/**
	 * 取得系统参数表中产品图片路径
	 * @return
	 */
	public String findProductImageUrl();
	
	/**
	 * 取得系统参数表中用户图片路径
	 * @return
	 */
	public String findUserImageUrl();
	
	/**
	 * 取得系统参数中产品小类图片路径
	 * @return
	 */
	public String findProductCategoryImageUrl();
	
	/**
	 * 查找所有
	 * @return
	 */
	public List<SystemParam> findAll();
	
	public int update(SystemParam param);
}
