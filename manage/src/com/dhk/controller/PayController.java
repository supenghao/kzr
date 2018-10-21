package com.dhk.controller;

import com.dhk.entity.APPUser;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.payment.PayRequest;
import com.dhk.payment.PayResult;
import com.dhk.service.*;
import com.sunnada.kernel.util.JsonUtil;
import com.sunnada.kernel.util.ResponseUtil;
import com.sunnada.kernel.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 操作员相关
 * @author bian
 *
 */
@SuppressWarnings("serial")
@Controller

public class PayController {
	
	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;
	
	@Resource(name = "OrgService")
	private IOrgService orgService;
	
	@Resource(name = "RechargeService")
	private IRechargeService rechargeService;
	
	@Resource(name="ReviewApplyCashService")
	private IReviewApplyCashService reviewApplyCashService;
	
	private final String commodityName = "妥妥商品";
    @Resource(name="CreditCardRepayService")
    private ICreditCardRepayService creditCardRepayService;
	
    @Resource(name="RepayPlanDetailService")
    private IRepayPlanDetailService repayPlanDetailService;
    
	//用户提现审核
	@RequestMapping(value="/reviewUserApplyCash")
	public void reviewUserApplyCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PayResult payResult = null;
		try{
			String applyId = request.getParameter("applyId");
			String isPass = request.getParameter("isPass");
			if (StringUtils.isBlank(applyId)){
				ResponseUtil.sendFailJson(response, "error","参数applyId为空");
				return ;
			}

			if (StringUtils.isBlank(isPass)){
				ResponseUtil.sendFailJson(response, "error","参数isPass为空");
				return ;
			}
			boolean isPassBoolean = true;
			if (isPass.equals("T")){
				isPassBoolean = true;
			}else{
				isPassBoolean = false;
			}

			payResult=reviewApplyCashService.applyCash(applyId,isPassBoolean);
			ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
		}catch(Exception e){
			e.printStackTrace();
			if (payResult==null){
				ResponseUtil.sendFailJson(response, "error",e.getMessage());
			}else{
				ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
			}
			return;
		}
		
	}


	
	@RequestMapping(value="/recharge")
	public void recharge(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String userId = request.getParameter("userId");
		PayRequest payRequest = new PayRequest();
		long id;
		if(StringUtils.isBlank(userId)){
			ResponseUtil.sendFailJson(response, "userId为空");
			return;
		}else{
			try {
				id=Long.parseLong(userId);
			} catch (Exception e) {
				ResponseUtil.sendFailJson(response, "userId不正确");
				return ;
			}
		}
		String oderDate = StringUtil.getCurrentDate();
		payRequest.setOrderDate(oderDate);
		String phoneNum = request.getParameter("phoneNo");
		if(StringUtils.isBlank(phoneNum)){
			ResponseUtil.sendFailJson(response, "phoneNum不正确");
			return ;
		}
		payRequest.setPhoneNo(phoneNum);
		String orderNo=appUserService.getOrderNo(id, phoneNum);
		payRequest.setOrderNo(orderNo);
		String customerName = request.getParameter("customerName");
		if(StringUtils.isBlank(customerName)){
			ResponseUtil.sendFailJson(response, "customerName不正确");
			return ;
		}
		payRequest.setCustomerName(customerName);
		String transAmt = request.getParameter("transAmt");
		long cash;
		if(StringUtils.isBlank(transAmt)){
			ResponseUtil.sendFailJson(response, "transAmt为空");
			return ;
		}else{
			try {
				cash=Long.parseLong(transAmt);
				payRequest.setTransAmt(cash);
			} catch (Exception e) {
				ResponseUtil.sendFailJson(response, "transAmt不正确");
				return ;
			}
			
		}
		String cerdType = request.getParameter("cerdType");
		if(StringUtils.isBlank(cerdType)){
			ResponseUtil.sendFailJson(response, "cerdType不正确");
			return ;
		}
		payRequest.setCerdType(cerdType);
		String cerdId = request.getParameter("cerdId");
		if(StringUtils.isBlank(cerdId)){
			ResponseUtil.sendFailJson(response, "cerdId不正确");
			return ;
		}
		payRequest.setCerdId(cerdId);
		String acctNo = request.getParameter("acctNo");
		if(StringUtils.isBlank(acctNo)){
			ResponseUtil.sendFailJson(response, "acctNo不正确");
			return ;
		}
		payRequest.setAcctNo(acctNo);
		
		String cvn2 = request.getParameter("cvn2");
		if(StringUtils.isBlank(cvn2)){
			ResponseUtil.sendFailJson(response, "cvn2为空");
			return ;
		}
		payRequest.setCvn2(cvn2);
		
		String expDate = request.getParameter("expDate");
		if(StringUtils.isBlank(expDate)){
			ResponseUtil.sendFailJson(response, "expDate为空");
			return ;
		}
		payRequest.setExpDate(expDate);
		
		payRequest.setCommodityName(commodityName);
		APPUser user = appUserService.findById(id);
		payRequest.setMerchantId(user.getMerchantid());
		PayResult payResult=rechargeService.userRecharge(payRequest, user);
		ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
	}

	/**
	 * 代理商提现审核
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/reviewOrgApplyCash")
	public void reviewOrgApplyCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PayResult payResult = null;
		try{

			String isPass = request.getParameter("isPass");
			String applyId = request.getParameter("applyId");

			if (StringUtils.isBlank(applyId)){
				ResponseUtil.sendFailJson(response, "error","参数applyId为空");
				return ;
			}


			if (StringUtils.isBlank(isPass)){
				ResponseUtil.sendFailJson(response, "error","参数isPass为空");
				return ;
			}
			boolean isPassBoolean = true;
			if (isPass.equals("T")){
				isPassBoolean = true;
			}else{
				isPassBoolean = false;
			}
			payResult=reviewApplyCashService.applyCashForOrg(applyId, isPassBoolean);
			ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
		}catch(Exception e){
			e.printStackTrace();
			if (payResult==null){
				ResponseUtil.sendFailJson(response, "error","server-agent系统出错,操作失败");
			}else{
				ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
			}
			return;
		}
	}



	/**
	 * 运营商提现
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/operatorsCash")
	public void operatorsCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		/**PayResult payResult = null;
		try{
			System.out.println("运营商提现....");
			String transAmt = request.getParameter("transAmt");
			//long cash;
			if(StringUtils.isBlank(transAmt)){
				ResponseUtil.sendFailJson(response, "transAmt为空");
				return ;
			}
			payResult=reviewApplyCashService.operatorsCash(new BigDecimal(transAmt));
			ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
		}catch(Exception e){			
			e.printStackTrace();
			if (payResult==null){
				ResponseUtil.sendFailJson(response, "error","server-agent系统出错,操作失败");
			}else{
				ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
			}
			return;
		}
		 **/
		ResponseUtil.sendFailJson(response, "error","server-agent系统出错,操作失败");
	}

	/**
	 * 重新提现
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/repayPlanAgain")
	public void repayPlanAgain(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String orderNo = request.getParameter("orderNo");
		String transNo = request.getParameter("transNo");
		if (StringUtils.isBlank(orderNo)){
			ResponseUtil.sendFailJson(response, "error","参数orderNo为空");
			return ;
		}
		if (StringUtils.isBlank(transNo)){
			ResponseUtil.sendFailJson(response, "error","参数transNo为空");
			return ;
		}
		PayResult payResult = creditCardRepayService.repayPlanAgain(orderNo,transNo);
		ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
	}
	
	/**
	 * 补单重新提现
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/repayPlanAgainBD")
	public void repayPlanAgainBD(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String planId = request.getParameter("planId");
		if (StringUtils.isBlank(planId)){
			ResponseUtil.sendFailJson(response, "error","参数planId为空");
			return ;
		}
		RepayPlanDetail repayPlanDetail = repayPlanDetailService.findRepayPlanById(Long.valueOf(planId));
		PayResult payResult = creditCardRepayService.reCreatOrder(repayPlanDetail); //资金不过夜模式
		ResponseUtil.responseJson(response, JsonUtil.toJson(payResult));
	}
	
}
