package com.dhk.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dhk.api.dao.IUserDao;
import com.dhk.api.dao.impl.UserDao;
import com.dhk.api.dto.CardInfo;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.UserInfo;
import com.dhk.api.entity.User;
import com.dhk.api.service.ILinkFaceService;
import com.dhk.api.third.LinkFaceHttpClientPost;
@Service("LinkFaceService")
public class LinkFaceService implements ILinkFaceService {
    private String idCardUrl = "https://cloudapi.linkface.cn/ocr/idcard";
    private String bankCardUrl = "https://cloudapi.linkface.cn/ocr/bankcard";
   // private String imgPath ="/data/app/app/webapps/ROOT/resource/dhk/userInfo";
    private final String TABLE_NAME = " t_s_user ";
    @Resource(name = "UserDao")
	private IUserDao UserDao;

	@Override
	public QResponse validateIdCard(IdentityDto dto,String imgPath) {
		// TODO Auto-generated method stub
		String userId = dto.getUserId();
		String sql = "select * from" + TABLE_NAME + "where id=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", dto.getUserId());
		List<User> li = UserDao.find(sql, map);
		User us = new User();
		if (li == null || li.size() == 0) {
			return new QResponse(false, "未找到该用户信息");
		} else {
			User u = li.get(0);
			String saveDir=imgPath+"resource"+File.separator+"dhk"+File.separator+"userInfo";
			String frontUrl = u.getId_front_url();	
			String backUrl = u.getId_opposite_url();
			UserInfo userInfo = new UserInfo();
			try {
				String result = LinkFaceHttpClientPost.HttpClientPost(idCardUrl,saveDir+File.separator+userId+File.separator+frontUrl);
				Map resultMap =(Map)JSON.parse(result);
				Map infoMap = (Map)resultMap.get("info");
				Map validateMap = (Map)resultMap.get("validity");
				if(infoMap==null||validateMap==null){
					return new QResponse(false, "身份证背面验证失败");
				}
				boolean flag = true;
				if((boolean) validateMap.get("name")){
					userInfo.setName((String)infoMap.get("name"));
				}else{
					flag = false;
				}
				if((boolean) validateMap.get("sex")){
					userInfo.setGender((String)infoMap.get("sex"));
				}else{
					flag = false;
				}
				if((boolean) validateMap.get("birthday")){
					String birthday = (String)infoMap.get("year")+"-"+(String)infoMap.get("month")+"-"+(String)infoMap.get("day");
					userInfo.setBirthday(birthday);
				}else{
					flag = false;
				}
				if((boolean) validateMap.get("address")){
					userInfo.setAddress((String)infoMap.get("address"));
				}else{
					flag = false;
				}
				if((boolean) validateMap.get("number")){
					userInfo.setIdCard((String)infoMap.get("number"));
				}else{
					flag = false;
				}
				userInfo.setEthnicGroup((String)infoMap.get("nation"));
				
				if(!flag){
					return new QResponse(false, "身份证正面验证失败");
				}		

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				String result = LinkFaceHttpClientPost.HttpClientPost(idCardUrl,saveDir+File.separator+userId+File.separator+backUrl);
				Map resultMap =(Map)JSON.parse(result);
				Map infoMap = (Map)resultMap.get("info");
				Map validateMap = (Map)resultMap.get("validity");
				if(validateMap==null){
					return new QResponse(false, "身份证背面验证失败");
				}
				boolean flag = true;
				if((boolean) validateMap.get("authority")){
					//
				}else{
					flag = false;
				}
				if((boolean) validateMap.get("timelimit")){
					//us.setUsername((String)infoMap.get("timelimit"));
				}else{
					flag = false;
				}
				if(!flag){
					return new QResponse(false, "身份证背面验证失败");
				}
				return new QResponse(userInfo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}
		
		return   QResponse.ERROR_SECURITY;
	}

	@Override
	public QResponse validateBankCard(IdentityDto dto,String imgPath) {
		// TODO Auto-generated method stub
		String userId = dto.getUserId();
		String sql = "select * from" + TABLE_NAME + "where id=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", dto.getUserId());
		List<User> li = UserDao.find(sql, map);
		User us = new User();
		if (li == null || li.size() == 0) {
			return new QResponse(false, "未找到该用户信息");
		} else {
			User u = li.get(0);
			String frontUrl = u.getBank_pic_url();	
			String saveDir=imgPath+"resource"+File.separator+"dhk"+File.separator+"userInfo";
			try {
				
				String result = LinkFaceHttpClientPost.HttpClientPost(bankCardUrl,saveDir+File.separator+userId+File.separator+frontUrl);
				Map resultMap =(Map)JSON.parse(result);
				if("OK".equals(resultMap.get("status"))){
					if(resultMap.get("result")==null){
						return new QResponse(false, "银行卡验证失败");
					}
					CardInfo info = new CardInfo();
					Map bankMap  =(Map)resultMap.get("result");
					info.setBankName((String)bankMap.get("bank_name"));
					info.setBankCode((String)bankMap.get("bank_identification_number"));
					info.setCardNum((String)bankMap.get("card_number"));
					info.setCardType((String)bankMap.get("card_type"));
					info.setCardName((String)bankMap.get("card_name"));
					return new QResponse(info); 
				}else{
					return new QResponse(false, "银行卡验证失败");
				}
//				if((boolean) validateMap.get("birthday")){
//					us.set((String)infoMap.get("birthday"));
//				}else{
//					flag = false;
//				}
//				if((boolean) validateMap.get("address")){
//					us.setUsername((String)infoMap.get("address"));
//				}else{
//					flag = false;
//				}
					

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	}
		
		return   QResponse.ERROR_SECURITY;
	}

}
