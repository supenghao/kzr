package com.aimi.bean;


import com.aimi.bean.base.BaseBeanRequest;

/**
 * 子商户上传资质
 * @author juxin-ecitic
 *
 */
public class MerchantUploadImageRequest extends BaseBeanRequest {
	public String requestId;//商户请求号
	public String fileType;//文件类型
	public String imgStr;//文件 照片base64后的字符串
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getImgStr() {
		return imgStr;
	}
	public void setImgStr(String imgStr) {
		this.imgStr = imgStr;
	}
	
	

}
