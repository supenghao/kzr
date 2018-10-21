package com.sunnada.uaas.service;

import java.util.List;

import com.sunnada.kernel.dao.jdbc.Ids;
import com.sunnada.uaas.entity.DictItem;

public interface IDictItemService {


	
	public DictItem findById(Long id);
	
	public int insert(DictItem item);
	
	public int update(DictItem item);
	
	public void batchDelete(List<Ids> ids);
}
