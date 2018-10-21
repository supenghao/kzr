package com.dhk.common;

import java.util.List;

import javax.annotation.Resource;

import com.sunnada.uaas.entity.SystemParam;
import com.sunnada.uaas.service.ISystemParamService;

public class Param {
	@Resource(name = "systemParamService")
	ISystemParamService systemParamService;
	
	public  List<SystemParam> paramList=systemParamService.findAll();

}
