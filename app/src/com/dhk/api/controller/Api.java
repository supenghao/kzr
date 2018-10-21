package com.dhk.api.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.NetShortMsg;
import com.dhk.api.core.impl.PayResult;
import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.AddUserCarDto;
import com.dhk.api.dto.AgentIdentifyDto;
import com.dhk.api.dto.AppVersionDto;
import com.dhk.api.dto.CardInfo;
import com.dhk.api.dto.CostPlanDto;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.EditCreditCarDto;
import com.dhk.api.dto.ForgetPwdDto;
import com.dhk.api.dto.GetBankInfoDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.LoanDto;
import com.dhk.api.dto.LoginDto;
import com.dhk.api.dto.MemberInfoDto;
import com.dhk.api.dto.MessageGDto;
import com.dhk.api.dto.PhoneDto;
import com.dhk.api.dto.PinganLoanDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RechargeDto;
import com.dhk.api.dto.RegistDto;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.dto.SuperDto;
import com.dhk.api.dto.TransWaterDto;
import com.dhk.api.dto.UpdatePwdDto;
import com.dhk.api.dto.UserInfo;
import com.dhk.api.dto.UserMsgDto;
import com.dhk.api.dto.loginOutDto;
import com.dhk.api.entity.CardBin;
import com.dhk.api.entity.Notices;
import com.dhk.api.entity.RepayPlanTem;
import com.dhk.api.entity.RepayRecord;
import com.dhk.api.entity.Token;
import com.dhk.api.entity.User;
import com.dhk.api.entity.UserNotice;
import com.dhk.api.service.IAccountService;
import com.dhk.api.service.IAppVersionService;
import com.dhk.api.service.ICardBinService;
import com.dhk.api.service.ICostPlanService;
import com.dhk.api.service.ICostPolicyService;
import com.dhk.api.service.ICreditCardService;
import com.dhk.api.service.IHomeAdService;
import com.dhk.api.service.IHomeButService;
import com.dhk.api.service.IInsuranceLsService;
import com.dhk.api.service.ILinkFaceService;
import com.dhk.api.service.ILoanService;
import com.dhk.api.service.IMemberinfoService;
import com.dhk.api.service.INoticesService;
import com.dhk.api.service.IORGService;
import com.dhk.api.service.IParamFeeService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.IPinganLoanService;
import com.dhk.api.service.IRepayCostService;
import com.dhk.api.service.IRepayPlanService;
import com.dhk.api.service.IRepayPlanTemService;
import com.dhk.api.service.IRepayRecordService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.ITransWaterService;
import com.dhk.api.service.IUserMsgService;
import com.dhk.api.service.IUserNoticeService;
import com.dhk.api.service.IUserService;
import com.dhk.api.third.YunPainSmsMsg;
import com.dhk.api.third.YunPainSmsObj;
import com.dhk.api.tool.M;
import com.dhk.init.Constant;
import com.dhk.redis.RedisUtils;
import com.fast.pay.FastPay;
import com.fast.pay.core.FastPayResponse;
import com.fast.pay.core.product.CreditCardRefundRequest;
import com.fast.pay.core.product.MerchantRequest;
import com.xdream.kernel.util.StringUtil;

@Component("api")
@Scope("singleton")
public class Api {

	protected static Logger logger = Logger.getLogger(Api.class);

	@Resource(name = "UserService")
	protected IUserService userService;

	@Resource(name = "MemberinfoService")
	protected IMemberinfoService memberServer;

	@Resource(name = "NoticesService")
	protected INoticesService noticesService;

	@Resource(name = "CreditCardService")
	protected ICreditCardService creditCarService;

	@Resource(name = "systemParamService")
	protected ISystemParamService systemParamService;

	@Resource(name = "ParamFeeService")
	protected IParamFeeService paramFeeService;
	
	@Resource(name = "CostPlanService")
	private ICostPlanService costPlanService;
	
	@Resource(name = "loanService")
	private ILoanService loanService;
	
	@Resource(name = "pinganLoanService")
	private IPinganLoanService pinganLoanService;
	
	@Resource(name = "RepayCostService")
	private IRepayCostService repayCostService;
	
	@Resource(name = "RepayPlanService")
	private IRepayPlanService repayPlanService;
	@Resource(name = "PayService")
	private IPayService payService;

	@Resource(name = "TokenService")
	private ITokenService tokenService;
	
	@Resource(name = "LinkFaceService")
	private ILinkFaceService linkFaceService;

	@Autowired
	 RedisUtils redisUtils;
	

	private final NetShortMsg msgServer = new NetShortMsg();

	/**
	 * 检查Session中的验证码
	 * 
	 * @param phone
	 * @param code
	 * @param request
	 * @return
	 */
	public  boolean checkShortMsg(String phone, String code,
			HttpServletRequest request) {
		logger.info("短信验证："+phone+" code:"+code);
		HttpSession session = request.getSession();
		String shortMsg = (String) session.getAttribute(phone);
		shortMsg = shortMsg ==null ? redisUtils.get(phone) : shortMsg;
		if (code == null || phone == null || shortMsg == null
				|| !shortMsg.equals(code)) {
			logger.info("短信验证失败："+code+"  phone:"+phone+" shortMsg:"+shortMsg);
			return false;
		}
		logger.info("短信验证成功："+code+"  phone:"+phone+" shortMsg:"+shortMsg);
		session.removeAttribute(phone);
		session.removeAttribute(phone + "time");
		return true;
	}

	public String getYzm(PhoneDto dto, HttpServletRequest request,
						 HttpServletResponse response) {
		String phone = dto.getPhone();
		JSONObject ret = null;
		if (phone == null || phone.isEmpty()) {
			ret = JsonCreater.getJsonError(1001, "手机号码不能为空");
			return JsonCreater.resultString(ret);
		}
		HttpSession session = request.getSession();
		Long time = (Long) session.getAttribute(phone + "time");
		if (time == null) {
			time = NumberUtils.toLong(redisUtils.get(phone + "time"),0l);
		}
		long now = System.currentTimeMillis();
		long cot = Math.abs(now - time.longValue());
		// 防止利用接口反复发送短信
		if (time == null || cot > 56000) {
			String r = getRandomInt() + "";
			YunPainSmsObj obj = YunPainSmsMsg.sendCode(r, phone);
			if (!"0".equals(obj.getCode())) {
				ret = JsonCreater.getJsonError(1001, "验证码发送失败!");
			} else {
				session.setAttribute(phone, r);
				session.setAttribute(phone + "time", System.currentTimeMillis());
				session.setMaxInactiveInterval(300);// session有效期是60秒
				redisUtils.set(phone, r, 300);
				redisUtils.set(phone + "time", System.currentTimeMillis()+"", 300);
				ret = JsonCreater.getJsonOk();
			}
		} else {
			ret = JsonCreater.getJsonError(1002, "请" + ((56000-cot) / 1000) + "秒后再获取");
		}
		return JsonCreater.resultString(ret);
	}

	private final Random r = new Random();

	private final int BASENUMBER = 100000;
	
	public int getRandomInt() {
		int tem = BASENUMBER;
		int i = r.nextInt(900000);
		int res = tem + i;
		return res;
	}

	public String registUser(RegistDto dto, HttpServletRequest request,
							 HttpServletResponse response) {
		JSONObject ret = null;
//		logger.debug("暂不支持此功能，如有疑问请联系客服热线");
//		ret = getJsonError(1003, "暂不支持此功能，如有疑问请联系客服热线");
//		return resultString(ret);
		if (!checkShortMsg(dto.getLoginName(), dto.getCheckCode(), request)) {
			ret = JsonCreater.getJsonError(1001, "验证码输入错误");
			return JsonCreater.resultString(ret);
		}
		if (dto.getInvitationCode() == null) {
			ret = JsonCreater.getJsonError(1001, "邀请码不能为空!");
			return JsonCreater.resultString(ret);
		}
		try {
			QResponse t = userService.txInsertUser(dto);
			if (!t.state) {
				ret = JsonCreater.getJsonError(1001, t.msg);
			} else {
				ret = JsonCreater.getJsonOk(t.data);
			}
		} catch (DuplicateKeyException e) {
			ret = JsonCreater.getJsonError(1002, "用户名已经存在");
			logger.debug("插入用户名已存在:" + M.getTrace(e));
		} catch (Exception e) {
			logger.debug("注册异常2" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1003, "注册异常");
		}
		return JsonCreater.resultString(ret);
	}

	public String registUserNew(RegistDto dto, HttpServletRequest request,
			 HttpServletResponse response) {
		JSONObject ret = null;
		// logger.debug("暂不支持此功能，如有疑问请联系客服热线");
		// ret = getJsonError(1003, "暂不支持此功能，如有疑问请联系客服热线");
		// return resultString(ret);
		if (!checkShortMsg(dto.getLoginName(), dto.getCheckCode(), request)) {
			ret = JsonCreater.getJsonError(1001, "验证码输入错误");
			return JsonCreater.resultString(ret);
		}
		/*
		 * if (dto.getInvitationCode() == null) { ret =
		 * JsonCreater.getJsonError(1001, "邀请码不能为空!"); return
		 * JsonCreater.resultString(ret); }
		 */
		try {
			QResponse t = userService.txInsertUserNew(dto);
			if (!t.state) {
				ret = JsonCreater.getJsonError(1001, t.msg);
			} else {
				ret = JsonCreater.getJsonOk(t.data);
			}
		} catch (DuplicateKeyException e) {
			ret = JsonCreater.getJsonError(1002, "用户名已经存在");
			logger.debug("插入用户名已存在:" + M.getTrace(e));
		} catch (Exception e) {
			logger.debug("注册异常2" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1003, "注册异常");
		}
		return JsonCreater.resultString(ret);
	}
	
	public String forgetPwd(ForgetPwdDto dto, HttpServletRequest request,
                            HttpServletResponse response) {
		JSONObject ret = null;
		String type = dto.getType();
		if (type != null && !type.isEmpty()) {
			if ("1".equals(type)) {// 获取验证码
				HttpSession session = request.getSession();
				String phone = dto.getLoginName();
				if (phone == null) {
					ret = JsonCreater.getJsonError(1003, "请输入正确的用户名");
				} else {
					Long time = (Long) session.getAttribute(phone + "time");
					if (time == null) {
						time = 0l;
					}
					long now = System.currentTimeMillis();
					long cot = Math.abs(now - time.longValue());
					if (cot < 5600) {
						ret = JsonCreater.getJsonError(1006, "请" + (cot / 1000) + "秒后再获取");
					} else {
						String url = systemParamService.findParam("payment_url_i")+"/payment/yunpain/sms/sendCheckCode";
						String code = msgServer.sendCheckCode(phone, url);
						if (code == null) {
							ret = JsonCreater.getJsonError(1003, "验证码发送失败!");
							M.logger.error("发送验证码到" + phone + "失败!");
						} else {
							session.setAttribute(phone, code);
							session.setAttribute(phone + "time",
									System.currentTimeMillis());
							session.setMaxInactiveInterval(60);// session有效期是60秒
							ret = JsonCreater.getJsonOk();
						}
					}
				}
			} else if ("2".equals(type)) {// 修改密码
				String phone = dto.getLoginName();
				if (!checkShortMsg(phone, dto.getCheckCode(), request)) {
					ret = JsonCreater.getJsonError(1004, "验证码输入错误");
					return JsonCreater.resultString(ret);
				} else {
					Token t = userService.updatePwd(dto);
					if (t == null) {
						ret = JsonCreater.getJsonError(1005, "密码修改失败");
					} else
						ret = JsonCreater.getJsonOk();
				}
			} else {
				ret = JsonCreater.getJsonError(1002, "unkonw type");
			}
		} else {
			ret = JsonCreater.getJsonError(1001, "unkonw type");
		}
		return JsonCreater.resultString(ret);
	}

	public String login(LoginDto dto, HttpServletRequest request,
						HttpServletResponse response) {
		JSONObject ret = null;
		QResponse t = null;
		try {
			String userName = dto.getLoginName();
			String pwd = dto.getLoginPwd();
			logger.debug("userName:" + userName + "-" + pwd);
			t = userService.login(userName, pwd);
			if (t.state) {
				ret = JsonCreater.getJsonOk(t.data);
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			logger.debug("登录失败!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "用户名或密码错误");
		}
		return JsonCreater.resultString(ret);
	}

	public String addUserCard(AddUserCarDto dto, HttpServletRequest request,
                              HttpServletResponse response) {
		JSONObject ret = null;

		
		try {
			if (!checkShortMsg(dto.getPhoneNo(), dto.getCheckCode(), request)) {
			ret = JsonCreater.getJsonError(1001, "验证码输入错误");
			return JsonCreater.resultString(ret);
			}
			QResponse t = userService.addUserCard(dto, request);
			if (t.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("addUserCard ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "内部错误");
		}
		return JsonCreater.resultString(ret);
	}

	public String addUserCardNew(AddUserCarDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			if (!checkShortMsg(dto.getPhoneNo(), dto.getCheckCode(), request)) {
				ret = JsonCreater.getJsonError(1001, "验证码输入错误");
				return JsonCreater.resultString(ret);
			}
			QResponse t = null;
			User user= userService.getUserById(dto.getUserId());
            if(user!=null&& user.getOrg_id()!=null&&!"0".equals(user.getOrg_id()) && user.getQrcode_id()!=null){
        		 t = userService.addUserCard(dto, request);
            }else{
			if (dto.getInvitationCode() == null) {
				ret = JsonCreater.getJsonError(1001, "邀请码不能为空!");
				return JsonCreater.resultString(ret);
			}
			 t = userService.addUserCardNew(dto, request);
            }
			
			if (t.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("addUserCard ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "内部错误");
		}
		return JsonCreater.resultString(ret);
	}
	
	
	@Resource(name = "CardBinService")
	protected ICardBinService cardBinService;

	public String getBankInfo(GetBankInfoDto dto, HttpServletRequest request,
							  HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = cardBinService.getInfo(dto);
			if (t.state) {
				CardBin bin = (CardBin) t.data;
				bin.setCardbin(null);
				ret = JsonCreater.getJsonOk(bin);
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			logger.debug("getBankInfo ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}

	public String getBaseInfo(IdentityDto dto, HttpServletRequest request,
							  HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = userService.getBaseInfo(dto);
			if (t.state) {
				ret = JsonCreater.getJsonOk(t.data);
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			logger.debug("getBaseInfo ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}

	public String updatePwd(UpdatePwdDto dto, HttpServletRequest request,
                            HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = userService.updatePwd(dto);
			if (!t.state) {
				ret = JsonCreater.getJsonError(1002, t.msg);
			} else {
				ret = JsonCreater.getJsonOk(t.data);
			}
		} catch (Exception e) {
			logger.debug("更新密码失败!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1003, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}

	public String loginOut(loginOutDto dto, HttpServletRequest request,
                           HttpServletResponse response) {
		boolean t = userService.loginOut(dto);
		JSONObject ret = null;
		if (t) {
			ret = JsonCreater.getJsonOk();
		} else {
			ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}



	public String editMemberInfo(MemberInfoDto dto, HttpServletRequest request,
                                 HttpServletResponse response) {
		Boolean t = memberServer.editMemberInfo(dto);
		JSONObject ret = null;
		if (t != null) {
			ret = JsonCreater.getJsonOk();
		} else {
			ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}


	public String getMessageList(MessageGDto dto, HttpServletRequest request,
                                 HttpServletResponse response) {
		List<Notices> l = noticesService.getMessageList(dto);
		JSONObject ret = null;
		if (l != null) {
			ret = JsonCreater.getJsonOkWithArrays(l);
		} else {
			ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}


	public String getMessageDetail(MessageGDto dto, HttpServletRequest request,
                                   HttpServletResponse response) {
		Notices m = noticesService.getMessageDetail(dto);
		JSONObject ret = null;
		if (m != null) {
			ret = JsonCreater.getJsonOk(m);
		} else {
			ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);
	}


	public String addCreditCar(AddCreditCarDto dto, HttpServletRequest request,
                               HttpServletResponse response) {
		
		JSONObject ret = null;
		
		try {
/*			if (!checkShortMsg(dto.getPhoneNo(), dto.getCheckCode(), request)) {
				ret = JsonCreater.getJsonError(1000, "验证码输入错误");
				return JsonCreater.resultString(ret);
			}*/
			QResponse t = creditCarService.addCreditCar(dto);
			if (t.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("addCreditCar() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
		}
		return JsonCreater.resultString(ret);
	}
	
	public String addCreditCardNew(AddCreditCarDto dto, HttpServletRequest request,
            HttpServletResponse response) {
		JSONObject ret = null;
		
		try {
		/*if (!checkShortMsg(dto.getPhoneNo(), dto.getCheckCode(), request)) {
		ret = JsonCreater.getJsonError(1000, "验证码输入错误");
		return JsonCreater.resultString(ret);
		}*/
		QResponse t = creditCarService.addCreditCar(dto);
		if (t.state) {
		ret = JsonCreater.getJsonOk();
		} else {
		ret = JsonCreater.getJsonError(1001, t.msg);
		}
		} catch (Exception e) {
		e.printStackTrace();
		logger.debug("addCreditCar() Exceptions!" + M.getTrace(e));
		ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
		}
		return JsonCreater.resultString(ret);
	}


	public String editCreditCar(EditCreditCarDto dto,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			Token t = creditCarService.editCreditCar(dto);
			if (t != null) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
			}
		} catch (Exception e) {
			logger.debug("editCreditCar() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
		}
		return JsonCreater.resultString(ret);
	}


	public String deleteCreditCar(DelCreditCarDto dto,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			String userId = dto.getUserId();
			String cardNo = dto.getCardNo();
			
			if (repayRecordService.hasFreeze(userId,cardNo)){
				ret = JsonCreater.getJsonError(1001, "该卡还有未执行完计划或有未解冻的余额，不能删除");
			}else{		
				Token t = creditCarService.deleteCreditCar(dto);
				if (t != null) {
					ret = JsonCreater.getJsonOk();
					// ret.put("cardNo", dto.getCardNo());
				} else {
					ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
					// ret.put("cardNo", dto.getCardNo());
				}
			}
		} catch (Exception e) {
			logger.debug("deleteCreditCar() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
			ret.put("cardNo", dto.getCardNo());
		}
		return JsonCreater.resultString(ret);
	}


	public String getCreditCarList(IdentityDto dto, HttpServletRequest request,
                                   HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = creditCarService.getCreditCarList(dto);
			if (t.state) {
				ret = JsonCreater.getJsonOkWithArrays(t.data);
			} else {
				ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
			}
			User user= userService.getUserById(dto.getUserId());
			if(user!=null){
				ret.put("is_auth", user.getIs_auth());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("getCreditCarList() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
		}
		return JsonCreater.resultString(ret);
	}


	public String getCreditCardDetail(DelCreditCarDto dto,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = creditCarService.getCreditCardDetail(dto);
			if (t.state) {
				ret = JsonCreater.getJsonOkWithArrays(t.data);
			} else {
				ret = JsonCreater.getJsonError(1001, "登录状态异常,请重新登录");
			}
		} catch (Exception e) {
			logger.debug("getCreditCardDetail() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
		}
		return JsonCreater.resultString(ret);
	}

	


	public String setRepayPlan(RepayPlanDto dto, HttpServletRequest request,
                               HttpServletResponse response) {
		JSONObject ret = null;
		
		if("4".equals(dto.getType())||"2".equals(dto.getType())){
			JSONArray  jsonArray = repayPlanTemService.txgetValueableTemList(dto);
			for (int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				String currentDate = StringUtil.getCurrentDateTime("yyyyMMdd");
				if(currentDate.equals(jsonObject.getString("repay_day"))){
					String exec_repay_end_time=systemParamService.findParam("exec_repay_end_time");  //计划最晚执行时间
					String submit_repay_end_time = PlanTool.computeTime(exec_repay_end_time,-3600*6-600);  //提前6个小时  保证任务有充分的时间执行
					String currentTime = StringUtil.getCurrentDateTime("HH:mm:ss");
					if(currentTime.compareTo(submit_repay_end_time)>=0){
						ret = JsonCreater.getJsonError(1001, "当天的计划请在"+submit_repay_end_time+"点前提交");
						return JsonCreater.resultString(ret);
					}
					break;
				}
			}
		}
		if (!"3".equals(dto.getType())){
			String userId = dto.getUserId();
			String cardNo = dto.getCardNo();
			
			/*if (repayRecordService.hasFreeze(userId,cardNo)){
				ret = JsonCreater.getJsonError(1001, "该卡还有未执行完计划或有未解冻的余额，不能再增加计划");
				return JsonCreater.resultString(ret);
			}*/
			User user= userService.getUserById(userId);

			if(user!=null && user.getIs_auth()!=null&&!"1".equals(user.getIs_auth())){
				ret = JsonCreater.getJsonError(1001, "您的账号处于未认证状态，请先认证");
				return JsonCreater.resultString(ret);
			}
			
			if (repayRecordService.hasExecRepayRecord(userId,cardNo)){ //判断是否在执行的任务
				ret = JsonCreater.getJsonError(1001, "该卡还有未执行完计划，不能再增加计划");
				return JsonCreater.resultString(ret);
			}
			
		}

		if ("2".equals(dto.getType())) {// 插入数据
			try {
				//QResponse t = repayPlanService.insertRepayPlans(dto);
				QResponse t = repayPlanService.insertRepayDatePlans(dto); //资金不过夜模式

				if (t.state) {
					ret = JsonCreater.getJsonOk();
				} else {
					ret = JsonCreater.getJsonError(1001, t.msg);
				}
			} catch (Exception e) {
				logger.error(M.getTrace(e));
				ret = JsonCreater.getJsonError(1001, e.getMessage());
			}
		} else if ("3".equals(dto.getType())) {// 获取临时表列表
			JSONArray jsonArray = repayPlanTemService
					.txgetValueableTemList(dto);
			ret = JsonCreater.getJsonOkWithArrays(jsonArray);
		} else if ("4".equals(dto.getType())) {// 计算费率
			QResponse re = repayPlanTemService.getRepayPlanTemfee(dto);
			if (re.state) {
				ret = JsonCreater.getJsonOk(re.data);
			} else {
				ret = JsonCreater.getJsonError(3000, re.msg);
			}
		} else if ("5".equals(dto.getType())) {// 删除临时表数据
			QResponse te = repayPlanTemService.txRemoveRepayTemByid(dto);
			if (te.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(2000, te.msg);
			}
		}else if ("6".equals(dto.getType())) {// 解析并获取临时表列表
			QResponse t = null;
			try {
				//t = repayPlanService.genRepayPlansNew(dto,request);
				t = repayPlanService.genRepayPlansOneDate(dto,request);
				if (t.state) {
					ret = JsonCreater.getJsonOkWithArrays(t.data);
				} else {
					ret = JsonCreater.getJsonError(1001, t.msg);
				}
			} catch (Exception e) {
				logger.error("setRepayPlanNew:" + M.getTrace(e));
				ret = JsonCreater.getJsonError(2000, e.getMessage());
			}
	    }else {
			ret = JsonCreater.getJsonError(1001, "type类型错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "RepayPlanTemService")
	private IRepayPlanTemService repayPlanTemService;


	public String getRepayPlanList(IdentityDto dto,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		QResponse t = repayPlanService.getRepayPlanList(dto,request.getParameter("recordId"));
		if (t.state) {
			ret = JsonCreater.getJsonOkWithArrays(t.data);
		} else {
			ret = JsonCreater.getJsonError(1001, t.msg);
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "CostPolicyService")
	private ICostPolicyService costPolicyService;


	public String setCostPlan(CostPlanDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		
		
		String beginTime=systemParamService.findParam("set_cost_begin_time");
		String endTime=systemParamService.findParam("set_cost_end_time");
		String currentTime = StringUtil.getCurrentDateTime("HH:mm:ss");
		if(currentTime.compareTo(beginTime)<=0 || currentTime.compareTo(endTime)>=0){
			ret = JsonCreater.getJsonError(1001, "请在每天" + beginTime + "到" + endTime + "提交消费计划");
			return JsonCreater.resultString(ret);
		}
		
		QResponse t = costPolicyService.insertCostPolicy(dto);
		if (t.state) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) t.data;
			ret = JsonCreater.getJsonOkWithArrays(map.get("messageList"));
			ret.put("fees", map.get("fees"));
		} else {
			ret = JsonCreater.getJsonError(1001, t.msg);
		}
		return JsonCreater.resultString(ret);
	}


	public String getCostPlanList(DelCreditCarDto dto,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		QResponse t = costPolicyService.getCostPlanList(dto);
		if (t.state) {
			ret = JsonCreater.getJsonOkWithArrays(t.data);
		} else {
			ret = JsonCreater.getJsonError(1001, t.msg);
		}
		return JsonCreater.resultString(ret);
	}


	public String genRepayPlan(DelCreditCarDto dto, HttpServletRequest request,
                               HttpServletResponse response) {
		JSONObject ret = null;
		ret = JsonCreater.getJsonError(1001, "废弃接口");
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;


	public String getTransWater(TransWaterDto dto, HttpServletRequest request,
                                HttpServletResponse response) {
		JSONObject ret = null;
		QResponse t = transWaterService.getTransWaterList(dto);
		if (t.state) {
			ret = JsonCreater.getJsonOkWithArrays(t.data);
		} else {
			ret = JsonCreater.getJsonError(1001, t.msg);
		}
		return JsonCreater.resultString(ret);
	}


	public String getTransTotal(TransWaterDto dto, HttpServletRequest request,
                                HttpServletResponse response) {
		JSONObject ret = null;
		ret = JsonCreater.getJsonError(1001, "接口待完善");
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "HomeAdService")
	private IHomeAdService homeadService;


	public String getHomeAd(SuperDto dto, HttpServletRequest request,
                            HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		try {
			res = homeadService.getHomeAd(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOkWithArrays(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "HomeButService")
	private IHomeButService homeButService;


	public String getFunBtnList(SuperDto dto, HttpServletRequest request,
                                HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		try {
			res = homeButService.getFunBtnList(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOkWithArrays(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}


	@Resource(name = "ORGService")
	private IORGService orgService;


	public String agentLogin(LoginDto dto, HttpServletRequest request,
                             HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		try {
			res = orgService.login(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk((Token) res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}


	public String agentTotal(AgentIdentifyDto dto, HttpServletRequest request,
                             HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		try {
			res = orgService.agentTotal(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "AppVersionService")
	private IAppVersionService appversionService;


	public String update(AppVersionDto dto, HttpServletRequest request,
                         HttpServletResponse response) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = appversionService.update(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	/**
	 * 作废
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 */

	public String recharge(RechargeDto dto, HttpServletRequest request, HttpServletResponse response) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			
			if (!checkShortMsg(dto.getCreditCardPhone(), dto.getCheckCode(), request)) {
				ret = JsonCreater.getJsonError(1001, "验证码输入错误");
				return JsonCreater.resultString(ret);
			}
			
			res = accountService.txRecharge(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	public String rechargeWithBindCard(RechargeDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			ret = accountService.rechargeWithBindCard(dto,request);

		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}



	public String withdrawCash(RechargeDto dto, HttpServletRequest request) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = accountService.seriWithdrawCash(dto,request);
			if (res.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, e.getMessage());
		}
		return JsonCreater.resultString(ret);
	}


	public String getwithdrawCash(IdentityDto dto) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = accountService.getWithdrawCash(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOkWithArrays(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "UserMsgService")
	private IUserMsgService userMsgService;


	public String getShortMsg(UserMsgDto dto) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = userMsgService.getShortMsg(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOkWithArrays(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "AccountService")
	private IAccountService accountService;

	@Resource(name = "RepayRecordService")
	private IRepayRecordService repayRecordService;


	public String getUserAccount(IdentityDto dto) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = accountService.getUserAccount(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
			/*ParamFee fee = paramFeeService.getUseParamByCode("recharge");
			BankFeeCule cu = new BankFeeCule("10000", fee);
			ret.put("bankfee", cu.bankPayFee());*/
			ret.put("bankfee", 0);
			String fes = repayRecordService.getUserRecodeValue(dto.getUserId());
			ret.put("recodevalue", fes);
			ret.put("customertel", Constant.customertel);
			
			User user= userService.getUserById(dto.getUserId());
			if(user!=null){
				ret.put("is_auth", user.getIs_auth());
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}


	public String upFile(MultipartFile file, IdentityDto dto,
                         HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		if (file == null) {
			ret = JsonCreater.getJsonError(1001, "文件类型未知");
		} else {
			try {
				file.transferTo(new File("E:/temp/" + ""
						+ file.getOriginalFilename()));
			} catch (IllegalStateException e) {
				logger.error(M.getTrace(e));
			} catch (IOException e) {
				logger.error(M.getTrace(e));
			}
			ret = JsonCreater.getJsonOk();
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "InsuranceLsService")
	private IInsuranceLsService insuranceLsService;


	public String getInsuressList(IdentityDto dto, HttpServletRequest request,
                                  HttpServletResponse response) {
		QResponse res = null;
		JSONObject ret = null;
		try {
			res = insuranceLsService.getUserinsurances(dto);
			if (res.state) {
				ret = JsonCreater.getJsonOk(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}
		} catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	@Resource(name = "userNoticeService")
	private IUserNoticeService userNoticeService;
	
	

	public String getNoticInfo(IdentityDto dto, HttpServletRequest request, HttpServletResponse response){
		//QResponse res = null;
		JSONObject ret = null;
		try {
			UserNotice un = userNoticeService.findByUserId(Long.parseLong(dto.getUserId()));
			
			Long userNoticeId = 0l;
			if (un!=null){
				userNoticeId = un.getMaxNoticeId();
			}
			userNoticeId=10000L;
			List<Notices> notices = noticesService.getNotices2id(userNoticeId);
//			Integer maxid = noticesService.getMaxNoticesId();
//			Long maxNoticeId = 0L;
//			if (maxid!=null){
//				maxNoticeId = maxid.longValue();
//			}
//			userNoticeService.updateMaxNoticeId(Long.parseLong(dto.getUserId()), maxNoticeId);
			
			ret = JsonCreater.getJsonOkWithArrays(notices);

			
		}catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		
		String resutl = JsonCreater.resultString(ret);
		 return resutl;
	}

	public String unFreezePlan(DelCreditCarDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
				ret = repayRecordService.unfreeze(dto.getUserId(),dto.getCardNo(),dto.getToken());
		} catch (Exception e) {
			logger.debug("unFreezePlan() Exceptions!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
			ret.put("cardNo", dto.getCardNo());
		}
		return JsonCreater.resultString(ret);
	}
	

	public String cancelRepayPlanTem(RepayPlanDto dto, HttpServletRequest request, HttpServletResponse response){
		JSONObject ret = null;
		try{
			repayPlanTemService.clear(dto.getUserId(),dto.getCardNo());
			ret = JsonCreater.getJsonOk();
		}catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		
		return JsonCreater.resultString(ret);
	}
	

	public String cancelPurchasePlan(DelCreditCarDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try{
			costPlanService.cancelCostPlan(dto);
			ret = JsonCreater.getJsonOk();
		}catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		
		return JsonCreater.resultString(ret);
	}

	public String cpicLoan(LoanDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try{
			loanService.cipcLoan(dto);
			ret = JsonCreater.getJsonOk();
		}catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		
		return JsonCreater.resultString(ret);
	}
	

	public String pinganLoan(PinganLoanDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try{
			if (dto.getLoanType().equals("E")){
				pinganLoanService.loan2e(dto);
			}else if (dto.getLoanType().equals("I")){
				pinganLoanService.loan2i(dto);
			}else{
				ret = JsonCreater.getJsonError(1002, "无此贷款类型");
				return JsonCreater.resultString(ret);
			}
			ret = JsonCreater.getJsonOk();
		}catch (Exception e) {
			logger.error(M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		
		return JsonCreater.resultString(ret);
	}
	


	public String queryRepayResultAndCostResult(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		result.put("result", "1");
		String userId=request.getParameter("userId");
		String cardNo=request.getParameter("cardNo");
		String token=request.getParameter("token");
		boolean c = tokenService.checkToken(userId, token);
		if(!c){
			return JsonCreater.resultString(JsonCreater.getJsonError(1001, "登录状态异常,请重新登录"));
		}
		try{
			RepayRecord repayRecord = repayRecordService.queryRepayResult(userId,cardNo);
			result.put("repayRecord",repayRecord);
			result.put("hasRepayCurMonth",repayRecordService.hasRepayCurMonth(userId,cardNo));
			result.put("costPlanCount",costPlanService.getCostPlanCount(userId,cardNo));
			String key =  "repayplan_tem_"+userId+"_"+cardNo;
			List<RepayPlanTem> list =	JSONArray.parseArray(redisUtils.get(key),RepayPlanTem.class);
			result.put("hasTemp",list!=null);
			return JsonCreater.resultString(result);
		} catch (Exception e){
			e.printStackTrace();
			return JsonCreater.getJsonError(1001,e.getMessage()).toJSONString();
		}
	}

	
	public String queryRepayResult(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		String userId=request.getParameter("userId");
		String cardNo=request.getParameter("cardNo");
		String token=request.getParameter("token");
		boolean c = tokenService.checkToken(userId, token);
		if(!c){
			return JsonCreater.resultString(JsonCreater.getJsonError(1001, "登录状态异常,请重新登录"));
		}
		try{
			RepayRecord repayRecord = repayRecordService.queryRepayResult(userId,cardNo);
			res =new QResponse(repayRecord);
			if (res.state) {
				ret = JsonCreater.getJsonOk(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}

		} catch (Exception e){
			e.printStackTrace();
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	public String getRegion(HttpServletRequest request, HttpServletResponse response) {
		return JsonCreater.resultString(systemParamService.getRegion());
	}

	public String getRepayRecord(HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		QResponse res = null;
		String userId=request.getParameter("userId");
		String cardNo=request.getParameter("cardNo");
		String token=request.getParameter("token");
		boolean c = tokenService.checkToken(userId, token);
		if(!c){
			return JsonCreater.resultString(JsonCreater.getJsonError(1001, "登录状态异常,请重新登录"));
		}
		try{
			List<RepayRecord> repayRecordList = repayRecordService.getRepayRecord(userId,cardNo);
			res =new QResponse(repayRecordList);
			if (res.state) {
				ret = JsonCreater.getJsonOkWithArrays(res.data);
			} else {
				ret = JsonCreater.getJsonError(1001, res.msg);
			}

		} catch (Exception e){
			e.printStackTrace();
			ret = JsonCreater.getJsonError(1002, "系统错误");
		}
		return JsonCreater.resultString(ret);
	}

	public String getRepayFee(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		result.put("result", "1");
		try{
			result.put("fee",paramFeeService.getRepayFeeStr());
			return JsonCreater.resultString(result);
		} catch (Exception e){
			e.printStackTrace();
			return JsonCreater.getJsonError(1001,e.getMessage()).toJSONString();
		}
	}

	/**
	 * 调用合利宝绑卡短信
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 */
	public String addCreditCarSendMsg(AddCreditCarDto dto, HttpServletRequest request,
            HttpServletResponse response) {
		JSONObject ret = null;
		if("kj".equals(Constant.bktdtype)){
			try {
				QResponse t = creditCarService.addCreditCarSendMsgKj(dto);
				if (t.state) {
				ret = JsonCreater.getJsonOk();
				ret.put("orderId", t.data);
				} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
				}
				} catch (Exception e) {
				e.printStackTrace();
				logger.debug("addCreditCar() Exceptions!" + M.getTrace(e));
				ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
				}
		}else{
			try {
				QResponse t = creditCarService.addCreditCarSendMsg(dto);
				if (t.state) {
				ret = JsonCreater.getJsonOk();
				ret.put("orderId", t.data);
				} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
				}
				} catch (Exception e) {
				e.printStackTrace();
				logger.debug("addCreditCar() Exceptions!" + M.getTrace(e));
				ret = JsonCreater.getJsonError(1002, "参数缺失或错误");
				}
		}
		
		return JsonCreater.resultString(ret);
	}
	
	/**
	 * 
		* @Title: XrPayQuickzxEPay 
		* @Description: 消费
		* @param @param dto
		* @param @param request
		* @param @param response
		* @param @return    设定文件 
		* @return String    返回类型 
		* @throws
	 */
	public String XrPayQuickzxEPay(IdentityDto dto, HttpServletRequest request, HttpServletResponse response,String type) {
		JSONObject ret = null;
		String amt = request.getParameter("total_amount");
		String cardNo = request.getParameter("cardNo");
		PayResult t=new PayResult();
		if("yt".equals(type)){
			 t = payService.YtPayQuickzxEPay(amt, dto, cardNo);
		}else{
			 t = payService.XrPayQuickzxEPay(amt, dto, cardNo);
		}
	 
//		if (t!=null && "0000".equals(t.getCode())) {
//			ret = JsonCreater.getJsonOk();
//			ret.put("html",HtmlUtils.htmlEscape(t.getMessage()) );
//		} else {
//			ret = JsonCreater.getJsonError(1001, t.getMessage());
//		}
//		return JsonCreater.resultString(ret);
		return t.getMessage();
	}
	
	
	/**
	 * 
		* @Title: 快捷支付
		* @Description: 消费
		* @param @param dto
		* @param @param request
		* @param @param response
		* @param @return    设定文件 
		* @return String    返回类型 
		* @throws
	 */
	public String KjPayQuickzxEPay(IdentityDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		String amt = request.getParameter("total_amount");
		String cardNo = request.getParameter("cardNo");
		String payCode = request.getParameter("payCode");
		PayResult t=new PayResult();
		t = payService.KjPayQuickzxEPay(amt, dto, cardNo,payCode);
		 
        return t.getMessage();
	}
	
	public String validateIdCard(IdentityDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			String realPath=request.getServletContext().getRealPath("/");
			QResponse t = linkFaceService.validateIdCard(dto,realPath);
			if (t.state) {
				UserInfo bin = (UserInfo) t.data;
				ret = JsonCreater.getJsonOk(bin);
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			logger.debug("getBankInfo ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);	
	}
	
 
	/**
	 * 
		* @Title: XrPayQuickzxEPay 
		* @Description: 消费
		* @param @param dto
		* @param @param request
		* @param @param response
		* @param @return    设定文件 
		* @return String    返回类型 
		* @throws
	 */
	public String jwH5Pay(IdentityDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		String amt = request.getParameter("total_amount");
		String cardNo = request.getParameter("cardNo");
		PayResult t=new PayResult();
	 
		t = payService.YtPayQuickzxEPay(amt, dto, cardNo);
		 
 
		return t.getMessage();
	}
	/**
	 * 樱桃精选、樱桃优选
	 * @param dto
	 * @param request
	 * @param response
	 * @return
	 */
	public String ytPay(IdentityDto dto, HttpServletRequest request, HttpServletResponse response,String type) {
		JSONObject ret = null;
		String amt = request.getParameter("total_amount");
		String cardNo = request.getParameter("cardNo");
		
		PayResult t=new PayResult();
	    if("jx".equals(type)){
		t = payService.YtjxPayQuickzxEPay(amt, dto, cardNo);
	    }else if("yx".equals(type)){
	    t=payService.YtyxPayQuickzxEPay(amt, dto, cardNo);
	    } 
 
		return t.getMessage();
	}
	
	
	
 
	public String validateBankCard(IdentityDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			String realPath=request.getServletContext().getRealPath("/");
			QResponse t = linkFaceService.validateBankCard(dto,realPath);
			if (t.state) {
				CardInfo bin = (CardInfo) t.data;
				ret = JsonCreater.getJsonOk(bin);
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			logger.debug("getBankInfo ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "登录状态异常,请重新登录");
		}
		return JsonCreater.resultString(ret);	
		}
	
	
	
	public String addCardInfo(AddUserCarDto dto, HttpServletRequest request, HttpServletResponse response) {
		JSONObject ret = null;
		try {
			QResponse t = null;
			User user= userService.getUserById(dto.getUserId());
            if(user!=null&& user.getOrg_id()!=null&&!"0".equals(user.getOrg_id()) && user.getQrcode_id()!=null){
        		 t = userService.addUserCard(dto, request);
            }else{
			 t = userService.addUserCardNew(dto, request);
            }
			
			if (t.state) {
				ret = JsonCreater.getJsonOk();
			} else {
				ret = JsonCreater.getJsonError(1001, t.msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("addUserCard ERROR!" + M.getTrace(e));
			ret = JsonCreater.getJsonError(1002, "内部错误");
		}
		return JsonCreater.resultString(ret);
	}
	
 
	public static void main(String[] args) {
		FastPay pay = new FastPay("http://debug.hgesy.com:8080/FastPay/","cgh",
				 "15bf5a58e73a5864a0b0f1565cb4f236");

		//创建信用卡还款请求对象
		//创建信用卡还款请求对象
//		CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYI);
//		creditCardRefundRequest.put("card_number", "438******16495");
//		creditCardRefundRequest.put("order_code", "TST1234567890");
//		creditCardRefundRequest.put("order_money", "100");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
		//创建信用卡还款请求对象
		CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_GVC);
		creditCardRefundRequest.put("phone", "13799776360");
		creditCardRefundRequest.put("card_number", "6217001930004911609");
//		
//		
//		CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_BDC);
//		creditCardRefundRequest.put("phone", "13799776360");
//		creditCardRefundRequest.put("user_name", "章云祥");
//		creditCardRefundRequest.put("card_number", "6217001930004911609");
//		creditCardRefundRequest.put("id_card_number", "350524198904026030");
//		creditCardRefundRequest.put("card_month_year", "1121");
//		creditCardRefundRequest.put("car_cvn", "023");
//		creditCardRefundRequest.put("validate_code", "556190");
////		
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYI);
//		creditCardRefundRequest.put("card_number", "6217001930004911609");
//		creditCardRefundRequest.put("order_code", "TST1234567899");
//		creditCardRefundRequest.put("order_money", "3");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
		 
//		creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCT_PYO);
//		creditCardRefundRequest.put("card_number", "6217001930004911609");
//		creditCardRefundRequest.put("order_code", "TST1234567895");
//		creditCardRefundRequest.put("order_money", "3.1");
//		creditCardRefundRequest.put("pay_code", "HeLiBao-CreditCardRefund");
		
//		MerchantRequest merchantRequest = new MerchantRequest();
//		merchantRequest.setAction(MerchantRequest.Action.MCT_RST);
//		merchantRequest.put("parent_mct_number", "qlqw_test");
//		merchantRequest.put("mct_number", "cgh");
//		merchantRequest.put("mct_name", "陈根辉");
//		merchantRequest.put("phone", "13779952024");
//		merchantRequest.put("id_card_number", "350206198810210014");
//		merchantRequest.put("id_card_img_a", new File("图片路径"));
//		merchantRequest.put("id_card_img_b", new File("图片路径"));
//		merchantRequest.put("img_face", new File("图片路径"));
		
		//提交请求
//		FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
 
		
		//创建商户请求对象
		MerchantRequest merchantRequest = new MerchantRequest();
		merchantRequest.setAction(MerchantRequest.Action.MCT_BDC_A);
		merchantRequest.put("card_number", "6226661500305762");
		merchantRequest.put("user_name", "陈根辉");
		merchantRequest.put("phone", "13779952024");
		merchantRequest.put("bank_img",new File("1.jpg"));
		merchantRequest.put("bank_name", "光大银行");
		merchantRequest.put("bank_branch_name", "光大银行");
		merchantRequest.put("id_card_number", "350206198810210014");

		//提交请求
		FastPayResponse fastPayResponse = pay.execute(merchantRequest);
	 
		System.out.println(fastPayResponse);
	}
}