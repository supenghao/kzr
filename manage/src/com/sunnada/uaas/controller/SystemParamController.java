package com.sunnada.uaas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sunnada.kernel.util.ResponseUtil;
import com.sunnada.uaas.entity.SystemParam;
import com.sunnada.uaas.service.ISystemParamService;

/**
 * 系统参数
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/system/param")
public class SystemParamController extends com.sunnada.kernel.controller.BaseController {

	@Resource(name="systemParamService")
	ISystemParamService systemParamService;
	@RequestMapping(value="/list")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<SystemParam> params = systemParamService.findAll();

		
		map.put("systemParams", params);
		
        ModelAndView mv = new ModelAndView("system_param/system_param",map);
		
		return mv;
	}
	/**
	 * 修改
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/update")
	public void update(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		String varNameValue = request.getParameter("varNameValue");
		if (StringUtils.isBlank(id)){
			ResponseUtil.sendFailJson(response, "参数不完整-ID");
			return;
		}
		if (StringUtils.isBlank(varNameValue)){
			ResponseUtil.sendFailJson(response, "参数不完整-值");
			return;
		}
		
		try{
			SystemParam param = new SystemParam();
			param.setId(Long.parseLong(id));
			param.setParam_text(varNameValue);
			
			systemParamService.update(param);
			Map<String,Object> map = ResponseUtil.makeSuccessJson();
			String json = com.sunnada.kernel.util.JsonUtil.toJson(map);
			ResponseUtil.responseJson(response, json);
		}catch(Exception e){
			e.printStackTrace();
			ResponseUtil.sendFailJson(response, "操作失败");
			return;
		}
		
	}
}
