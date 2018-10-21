package com.dhk.api.controller;
import com.dhk.api.service.IAPPUserService;
import com.dhk.api.entity.APPUser;
import com.xdream.kernel.controller.BaseController;
import com.xdream.kernel.util.JsonUtil;
import com.xdream.kernel.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 上传
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Controller
@RequestMapping(value="/upload")
public class UploadController extends BaseController {

	@Resource(name="APPUserService")
	private IAPPUserService appUserService;
	
	@RequestMapping(value="/uploadImage")
	@ResponseBody
	public void uploadImage(@RequestParam(value = "files", required = true) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userId = request.getParameter("userId");
		try{
			if (StringUtils.isBlank(userId)){
				ResponseUtil.sendFailJson(response, "无用户信息");
				return ;
			}
			APPUser appUser = appUserService.findById(Long.parseLong(userId));
			if (appUser==null){
				ResponseUtil.sendFailJson(response, "无此用户信息");
				return ;
			}
			
			String realPath=request.getServletContext().getRealPath("/");
			String fileType = file.getContentType();
			String fileName = file.getOriginalFilename();
			String realFileName = fileName + ".jpg";
			String saveDir=realPath+"resource"+File.separator+"dhk"+File.separator+"userInfo"+File.separator+userId+File.separator;
			File file1=new File(saveDir);
			if(!file1.exists()){
				file1.mkdirs();
			}
			saveDir += realFileName;
			System.out.println("saveDir:"+saveDir);
			System.out.println("fileName:"+fileName);
			file.transferTo(new File(saveDir));
			
			
			appUserService.updateImageUrl(fileName, Long.parseLong(userId), realFileName);

			Map<String,Object> map = ResponseUtil.makeSuccessJson();
			String json = JsonUtil.toJson(map);
			ResponseUtil.responseJson(response, json);
		} catch(Exception e){
			e.printStackTrace();
			ResponseUtil.sendFailJson(response, "操作失败");
			return;
		}
		
	}
	
	
}
