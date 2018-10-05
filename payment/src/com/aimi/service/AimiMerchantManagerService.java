package com.aimi.service;


import com.aimi.AimiHttpUtils;
import com.aimi.AimiRequestURL;
import com.aimi.bean.*;
import org.springframework.stereotype.Service;

/**
 * 商户管理
 * @author juxin-ecitic
 *
 */

@Service("aimiMerchantManagerService")
public class AimiMerchantManagerService {

	/**
	 * 子商户注册
	 */
	public MerchantRegisterResponse merchantRegister(MerchantRegisterRequest request) throws Exception {
	    return AimiHttpUtils.sendPost(request,MerchantRegisterResponse.class, AimiRequestURL.MERCHANT_REGISTER_URL);
	}

	/**
	 * 子商户修改
	 */
	public MerchantModifyResponse merchantModify(MerchantModifyRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,MerchantModifyResponse.class, AimiRequestURL.MERCHANT_MODIFY_URL);
	}

	/**
	 * 子商户资质上传
	 */
	public MerchantUploadImageResponse merchantUploadImage(MerchantUploadImageRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request,MerchantUploadImageResponse.class, AimiRequestURL.MERCHANT_UPLOADIAMGE_URL);
	}
	
	/**
	 * 子商户信息查询
	 */
	public MerchantInfQueryResponse merchantInfQuery(MerchantInfQueryRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, MerchantInfQueryResponse.class, AimiRequestURL.MERCHANT_QUERY_URL);
	}


	/**
	 * 子商户费率设置
	 */
	public MerchantSettingFeeResponse merchantSettingFee(MerchantSettingFeeRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, MerchantSettingFeeResponse.class, AimiRequestURL.MERCHANT_SETTINGFEE_URL);
	}

	/**
	 * 子商户费率查询
	 */
	public MerchantFeeQueryResponse merchantFeeQuery(MerchantFeeQueryRequest request) throws Exception {
		return AimiHttpUtils.sendPost(request, MerchantFeeQueryResponse.class, AimiRequestURL.MERCHANT_FEEQUERY_URL);
	}

}
 