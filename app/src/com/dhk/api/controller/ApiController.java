package com.dhk.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dhk.api.dao.IUserDao;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.AddUserCarDto;
import com.dhk.api.dto.AppVersionDto;
import com.dhk.api.dto.CostPlanDto;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.EditCreditCarDto;
import com.dhk.api.dto.ForgetPwdDto;
import com.dhk.api.dto.GetBankInfoDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.LoginDto;
import com.dhk.api.dto.MemberInfoDto;
import com.dhk.api.dto.MessageGDto;
import com.dhk.api.dto.PhoneDto;
import com.dhk.api.dto.PinganLoanDto;
import com.dhk.api.dto.RechargeDto;
import com.dhk.api.dto.RegistDto;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.dto.SuperDto;
import com.dhk.api.dto.TransWaterDto;
import com.dhk.api.dto.UpdatePwdDto;
import com.dhk.api.dto.UserMsgDto;
import com.dhk.api.dto.loginOutDto;
import com.dhk.api.tool.M;
import com.dhk.api.tool.StringUtils;

@Controller
@RequestMapping("")
@Scope("singleton")
public class  ApiController {

	@Resource(name = "api")
	private Api api;
	@Resource(name = "UserDao")
	private IUserDao userDao;

	@RequestMapping(value = "/getYzm", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getYzm(PhoneDto dto, HttpServletRequest request,
				  HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getYzm(dto, request, response);
	}

	@RequestMapping(value = "/register", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String registUser(RegistDto dto, HttpServletRequest request,
					  HttpServletResponse response) {
		M.logger.debug(dto);
		return api.registUser(dto, request, response);
	}

	@RequestMapping(value = "/registernew", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String registUserNew(RegistDto dto, HttpServletRequest request,
					  HttpServletResponse response) {
		M.logger.debug(dto);
		return api.registUserNew(dto, request, response);
	}
	
	@RequestMapping(value = "/forgetPwd", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String forgetPwd(ForgetPwdDto dto, HttpServletRequest request,
					 HttpServletResponse response) {
		M.logger.debug(dto);
		return api.forgetPwd(dto, request, response);
	}

	@RequestMapping(value = "/login", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String login(LoginDto dto, HttpServletRequest request,
				 HttpServletResponse response) {
		M.logger.debug(dto);
		response.setHeader("Access-Control-Allow-Origin", "*");
		return api.login(dto, request, response);
	}

	@RequestMapping(value = "/smrz", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String addUserCard(AddUserCarDto dto, HttpServletRequest request,
					   HttpServletResponse response) {
		M.logger.debug(dto);
		return api.addUserCard(dto, request, response);
	}
	
	@RequestMapping(value = "/smrznew", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String addUserCardNew(AddUserCarDto dto, HttpServletRequest request,
					   HttpServletResponse response) {
		M.logger.debug(dto);
		return api.addUserCardNew(dto, request, response);
	}
	@RequestMapping(value = "/getBankInfo", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBankInfo(GetBankInfoDto dto, HttpServletRequest request,
                       HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getBankInfo(dto, request, response);
	}

	@RequestMapping(value = "/getUserInfo", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBaseInfo(IdentityDto dto, HttpServletRequest request,
                       HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getBaseInfo(dto, request, response);
	}

	@RequestMapping(value = "/updatePwd", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String updatePwd(UpdatePwdDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.updatePwd(dto, request, response);
	}

	@RequestMapping(value = "/logout", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String loginOut(loginOutDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.loginOut(dto, request, response);
	}



	@RequestMapping(value = "/editUser", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String editMemberInfo(MemberInfoDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.editMemberInfo(dto, request, response);
	}


	@RequestMapping(value = "/getNoticeList", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getMessageList(MessageGDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getMessageList(dto, request, response);
	}


	@RequestMapping(value = "/addCreditCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String addCreditCar(AddCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.addCreditCar(dto, request, response);
	}
	
	@RequestMapping(value = "/addCreditCarSendMsg", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String addCreditCarSendMsg(AddCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.addCreditCarSendMsg(dto, request, response);
//		PhoneDto dtophone = new PhoneDto();
//		dtophone.setPhone(dto.getPhoneNo());
//		return api.getYzm(dtophone, request, response);
	}
	
	@RequestMapping(value = "/addCreditCardNew", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String addCreditCardNew(AddCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
	//	return api.addCreditCardNew(dto, request, response);
		return api.addCreditCar(dto, request, response);
	}

	@RequestMapping(value = "/editCreditCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String editCreditCar(EditCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.editCreditCar(dto, request, response);
	}

	@RequestMapping(value = "/deleteCreditCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String deleteCreditCar(DelCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.deleteCreditCar(dto, request, response);
	}

	@RequestMapping(value = "/getCreditCarList", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getCreditCarList(IdentityDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api
				.getCreditCarList(dto, request, response);
	}

	@RequestMapping(value = "/getCreditCardDetail", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getCreditCardDetail(DelCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getCreditCardDetail(dto, request,
				response);
	}

	@RequestMapping(value = "/setRepayPlan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String setRepayPlan(RepayPlanDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.setRepayPlan(dto, request, response);
	}

	@RequestMapping(value = "/getRepayPlanList", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getRepayPlanList(IdentityDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api
				.getRepayPlanList(dto, request, response);
	}

	@RequestMapping(value = "/setCostPlan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String setCostPlan(CostPlanDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.setCostPlan(dto, request, response);
	}

	@RequestMapping(value = "/getCostPlanList", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getCostPlanList(DelCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getCostPlanList(dto, request, response);
	}

	@RequestMapping(value = "/genRepayPlan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String genRepayPlan(DelCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.genRepayPlan(dto, request, response);
	}

	@RequestMapping(value = "/getTransWater", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getTransWater(TransWaterDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getTransWater(dto, request, response);
	}

	/**
	 * 首页广告
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSygg", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getHomeAd(SuperDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getHomeAd(dto, request, response);
	}

	@RequestMapping(value = "/checkVersion", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String update(AppVersionDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.update(dto, request, response);
	}

	@RequestMapping(value = "/recharge", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String recharge(RechargeDto dto,HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.recharge(dto,request, response);
	}

	@RequestMapping(value = "/rechargeWithBindCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String rechargeWithBindCard(RechargeDto dto,HttpServletRequest request,
					HttpServletResponse response) {
		M.logger.debug(dto);
		return api.rechargeWithBindCard(dto,request, response);
	}

	@RequestMapping(value = "/withdrawCash", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String withdrawCash(RechargeDto dto,HttpServletRequest request) {
		M.logger.debug(dto);
		return api.withdrawCash(dto,request);
	}

	@RequestMapping(value = "/getwithdrawCash", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getwithdrawCash(IdentityDto dto) {
		M.logger.debug(dto);
		return api.getwithdrawCash(dto);
	}

	@RequestMapping(value = "/getShortMsg", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getShortMsg(UserMsgDto dto) {
		M.logger.debug(dto);
		return api.getShortMsg(dto);
	}

	@RequestMapping(value = "/getUserAccount", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getUserAccount(IdentityDto dto) {
		M.logger.debug(dto);
		return api.getUserAccount(dto);
	}

	@RequestMapping(value = "/upFile")
	@ResponseBody
	public String upFile(
			@RequestParam(value = "files", required = true) MultipartFile files,
			IdentityDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.upFile(files, dto, request, response);
	}

	@RequestMapping(value = "/getInsuressList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getInsuressList(IdentityDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.getInsuressList(dto, request, response);
	}
	

	@RequestMapping(value = "/getNoticInfo", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getNoticInfo(IdentityDto dto, HttpServletRequest request,HttpServletResponse response) {
		
		M.logger.debug(dto);
		return api.getNoticInfo(dto, request, response);
	}
	
	@RequestMapping(value = "/unFreezePlan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String unFreezePlan(DelCreditCarDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		M.logger.debug(dto);
		return api.unFreezePlan(dto, request, response);
	}
	
	@RequestMapping(value = "/cancelRepayPlanTem", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String cancelRepayPlanTem(RepayPlanDto dto,HttpServletRequest request,HttpServletResponse response){
		M.logger.debug(dto);
		return api.cancelRepayPlanTem(dto, request, response);
	}
	@RequestMapping(value = "/cancelPurchasePlan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String cancelPurchasePlan(DelCreditCarDto dto, HttpServletRequest request,HttpServletResponse response) {
		M.logger.debug(dto);
		return api.cancelPurchasePlan(dto, request, response);
	}
	

	@RequestMapping(value = "/pinganLoan", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String pinganLoan(PinganLoanDto dto, HttpServletRequest request,HttpServletResponse response){
		M.logger.debug(dto);
		return api.pinganLoan(dto, request, response);
	}
	


	@RequestMapping(value = "/queryRepayResultAndCostResult", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String queryRepayResultAndCostResult( HttpServletRequest request, HttpServletResponse response) {
		return api.queryRepayResultAndCostResult(request, response);
	}


	@RequestMapping(value = "/queryRepayResult", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String queryRepayResult( HttpServletRequest request, HttpServletResponse response) {
		return api.queryRepayResult(request, response);
	}


	@RequestMapping(value = "/getRegion", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getRegion(PhoneDto dto, HttpServletRequest request,
				  HttpServletResponse response) {
		return api.getRegion(request, response);
	}


	@RequestMapping(value = "/getRepayRecord", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getRepayRecord(PhoneDto dto, HttpServletRequest request,
					 HttpServletResponse response) {
		return api.getRepayRecord(request, response);
	}

	@RequestMapping(value = "/getRepayFee", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getRepayFee(PhoneDto dto, HttpServletRequest request,
						  HttpServletResponse response) {
		return api.getRepayFee(request, response);
	}


	@RequestMapping(value = "/xfPay", produces = "text/html; charset=utf-8")
	public 
	String xfPay(IdentityDto dto, HttpServletRequest request,
						  HttpServletResponse response,String type) {
		try {
			String result = api.XrPayQuickzxEPay(dto, request, response,type);
			StringUtils.responseStr(response,result );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
 
	
	@RequestMapping(value = "/jwPayH5", produces = "text/html; charset=utf-8")
	public 
	String jwH5(IdentityDto dto, HttpServletRequest request,
						  HttpServletResponse response,String type) {
		try {
			String result = api.jwH5Pay(dto, request, response);
			StringUtils.responseStr(response,result );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	@RequestMapping(value = "/jyxPay", produces = "text/html; charset=utf-8")
	public 
	String jyxPay(IdentityDto dto, HttpServletRequest request,
						  HttpServletResponse response,String type) {
		String result=""	;
		try {
			 result=api.ytPay(dto, request, response,type);
			StringUtils.responseStr(response,result );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/kjPay", produces = "text/html; charset=utf-8")
	public 
	String kjPay(IdentityDto dto, HttpServletRequest request,
						  HttpServletResponse response,String type) {
		try {
			String result = api.KjPayQuickzxEPay(dto, request, response);
			StringUtils.responseStr(response,result );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/validateBankCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String validateBankCard(IdentityDto dto, HttpServletRequest request,
	                   HttpServletResponse response) {
		M.logger.debug(dto);
		return api.validateBankCard(dto, request, response);
	}

	@RequestMapping(value = "/validateIdCard", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String validateIdCard(IdentityDto dto, HttpServletRequest request,
	                   HttpServletResponse response) {
		M.logger.debug(dto);
		return api.validateIdCard(dto, request, response);
	}
 
}
