package com.dhk.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dhk.dao.impl.TransWaterZxcxDao;
import com.dhk.entity.TransWaterZxcx;
import com.dhk.utils.DateTimeUtil;
import com.sunnada.kernel.sql.SQLConf;
import com.xdream.kernel.controller.BaseController;
import com.xdream.kernel.util.ResponseUtil;
 

/**
 * 
	* @ClassName: 快捷支付
	* @Description: TODO
	* @author 
	* @date  
	*
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/zxcx")
public class ZxcxController extends BaseController{

	private static Logger logger = Logger.getLogger(ZxcxController.class);
   
	@Autowired
	TransWaterZxcxDao transWaterZxcxDao;
	
	@ResponseBody
    @RequestMapping(value = "/writelog")
    public void creditPurchase(HttpServletRequest request,HttpServletResponse response) throws Exception{
		TransWaterZxcx tw=new TransWaterZxcx();
        try{
        	String mobile =request.getParameter("mobile");
        	String userName=request.getParameter("userName");
        	String transNo=request.getParameter("transNo");
        	String transAmount=request.getParameter("transAmount");
        	String respCode=request.getParameter("respCode");
        	String respDesc=request.getParameter("respDesc");
        	String fee=request.getParameter("fee");
        	String external=request.getParameter("external");
        	String transType=request.getParameter("transType");
        	if (StringUtils.isBlank(mobile)) {
				ResponseUtil.sendFailJson(response, "手机号码不能为空！");
				return;
			}
        	if (StringUtils.isBlank(userName)) {
				ResponseUtil.sendFailJson(response, "用户名称不能为空");
				return;
			}else if (StringUtils.isBlank(mobile)) {
				ResponseUtil.sendFailJson(response, "手机号不能为空");
				return;
			} else if (StringUtils.isBlank(transNo)) {
				ResponseUtil.sendFailJson(response, "订单号不能为空");
				return;
			} else if (StringUtils.isBlank(transAmount)) {
				ResponseUtil.sendFailJson(response, "订单金额不能为空");
				return;
			} else if (StringUtils.isBlank(respCode)) {
				ResponseUtil.sendFailJson(response, "状态码不能为空");
				return;
			} else if (StringUtils.isBlank(respDesc)) {
				ResponseUtil.sendFailJson(response, "状态信息不能为空");
				return;
			} else if (StringUtils.isBlank(transType)) {
				ResponseUtil.sendFailJson(response, "交易类型不能为空");
				return;
			} 
        	if(StringUtils.isEmpty(fee)){
        		fee="0";
        	}
        	if(StringUtils.isEmpty(external)){
        		external="0";
        	}
        	 
        	String zsql=SQLConf.getSql("transwater", "findByTransNoZxcx");
        	Map<String, Object> zmap=new HashMap<String, Object>();
    		zmap.put("id", transNo);
    		try {
    			TransWaterZxcx transWater = transWaterZxcxDao.findBy(zsql, zmap);
    			ResponseUtil.sendFailJson(response, "订单号已存在");
				return;
			} catch (Exception e) {
				 
			}
    		
    		 
    		tw.setRespCode(respCode);
    		tw.setRespRes(respDesc);
    	    tw.setTransAmount(new BigDecimal(transAmount));
    		tw.setFee(new BigDecimal(fee));
    		tw.setExternal(new BigDecimal(external));
    		
    		tw.setTransNo(transNo);
    		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
    		String time = DateTimeUtil.getNowDateTime("HHmmss");
    		tw.setUserName(userName);
    		tw.setMobile(mobile);
    		tw.setTransDate(date);
    		tw.setTransTime(time);
        	tw.setTransType(transType);
        	String sql=SQLConf.getSql("transwater", "insertzxcx");
    		if(transWaterZxcxDao.insert(sql, tw)==0)
    			{
    			ResponseUtil.sendFailJson(response, "插入失败！");
    			}
    			 
        Map<String, Object>map=ResponseUtil.makeSuccessJson();
		//map.put("transactionType","");
		map.put("code","0000");
		map.put("message","成功");
		String json = JSON.toJSONString(map);
		ResponseUtil.responseJson(response, json);		
		 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			ResponseUtil.sendFailJson(response, e.getMessage());
			return ;
		}
    }
 
 
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
	
 
}
