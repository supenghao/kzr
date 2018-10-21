package com.dhk.api.service;

import java.util.List;

import com.dhk.api.dto.MessageGDto;
import com.dhk.api.entity.Notices;

/**
 * t_s_org_notice service 接口<br/>
 * 2016-12-30 11:40:21 qch
 */
public interface INoticesService {
	List<Notices> getMessageList(MessageGDto dto);

	Notices getMessageDetail(MessageGDto dto);
	
	List<Notices> getNotices2id(Long id);
	
	Integer getMaxNoticesId();
}
