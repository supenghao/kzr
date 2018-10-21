package com.dhk.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aimi.bean.MerchantRegisterRequest;
import com.aimi.bean.MerchantRegisterResponse;
import com.aimi.bean.MerchantSettingFeeRequest;
import com.aimi.bean.MerchantSettingFeeResponse;
import com.aimi.service.AimiMerchantManagerService;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.dao.ICardBinDao;
import com.dhk.api.dao.IParamFeeDao;
import com.dhk.api.dao.IUserDao;
import com.dhk.api.dto.AddUserCarDto;
import com.dhk.api.dto.ForgetPwdDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RegistDto;
import com.dhk.api.dto.UpdatePwdDto;
import com.dhk.api.dto.loginOutDto;
import com.dhk.api.entity.CardBin;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.entity.Qrcode;
import com.dhk.api.entity.Token;
import com.dhk.api.entity.User;
import com.dhk.api.entity.param.HXRegisterParam;
import com.dhk.api.service.IAccountService;
import com.dhk.api.service.IBeneficiaryService;
import com.dhk.api.service.IHXPayService;
import com.dhk.api.service.IInsuranceLsService;
import com.dhk.api.service.IInsurancedService;
import com.dhk.api.service.INoticesService;
import com.dhk.api.service.IOrgrateService;
import com.dhk.api.service.IParamFeeService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.IQrcodeService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.IUserNoticeService;
import com.dhk.api.service.IUserService;
import com.dhk.api.third.BankAuthUtil;
import com.dhk.api.tool.M;
import com.xdream.kernel.sql.SQLConf;
import com.xdream.kernel.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("UserService")
public class UserService implements IUserService {

	private final String TABLE_NAME = " t_s_user ";
	static Logger logger = Logger.getLogger(UserService.class);
	@Resource(name = "UserDao")
	private IUserDao UserDao;

	@Resource(name = "CardBinDao")
	private ICardBinDao cardBinDao;
	
	@Resource(name = "ParamFeeDao")
	private IParamFeeDao paramFeeDao;
	
	@Resource(name = "HXPayService")
	private IHXPayService hxpayService;
	
	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "QrcodeService")
	private IQrcodeService qrcodeService;

	@Resource(name = "AccountService")
	private IAccountService accountService;

	@Resource(name = "OrgrateService")
	private IOrgrateService orgrateService;

	@Resource(name = "ParamFeeService")
	private IParamFeeService paramFeeService;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Resource(name = "PayService")
	private IPayService payService;

	@Resource(name = "InsuranceLsService")
	private IInsuranceLsService insuranceLsService;

	@Resource(name = "InsurancedService")
	private IInsurancedService insurancedService;

	@Resource(name = "BeneficiaryService")
	private IBeneficiaryService beneficiaryService;
	
	@Resource(name = "userNoticeService")
	private IUserNoticeService userNoticeService;
	
	@Resource(name = "NoticesService")
	protected INoticesService noticesService;

	@Resource(name = "aimiMerchantManagerService")
	AimiMerchantManagerService aimiMerchantManagerService;

	@Autowired
	JedisPool jedisPool;

	@Override
	@Transactional
	public QResponse txInsertUser(RegistDto dto) {
		Qrcode q = null;
		try {
			q = qrcodeService.getQrcodeByInvitation(dto.getInvitationCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new QResponse(false, "邀请码不存在");
		}
		if (q==null){
			return new QResponse(false, "邀请码不存在或状态异常");
		}
		ParamFee fee = paramFeeService.getUseParamByCode("purchase");// 取出还款手续费
		boolean reteb = orgrateService.updateDiffRateInfo(q.getOrg_id()
				.toString(), q.getId().toString(), fee.getFee());
		if (!reteb) {
			M.logger.debug("insertUser() t_s_org_rate 修改失败");
			throw new RuntimeException("insertUser t_s_org_rate 修改失败");
		}
		User u = new User();
		u.setPassword(dto.getLoginPwd());
		u.setOrg_id(q.getOrg_id().toString());
		u.setRelation_no(q.getRelation_no());
		u.setUsername(dto.getLoginName());
		u.setNick_name(dto.getLoginName());// 昵称默认是登录手机号
		u.setMobilephone(dto.getLoginName());// 手机号就是登录名
		// u.setStatus("1");// 设置账户成为未启用(未实名)状态
		u.setQrcode_id(q.getId());// 设置二维码的id
		u.setReg_date(PlanTool.getCurrentDateTime("yyyyMMdd"));
		String sql = M.getInsertSqlWhenFilesIsNotNull(u);
		Long userid = UserDao.insert(sql, u);
		accountService.insertAaccount(userid.intValue());// 插入账户
		qrcodeService.changeQrcodeState(q.getId().toString(), "3");
		Token t = new Token();
		t.setToken(M.createUUID());
		t.setUserid(userid + "");
		tokenService.insertToken(t);
		//增加相应的公告ID
		Integer maxid = noticesService.getMaxNoticesId();
		Long maxNoticeId = 0L;
		if (maxid!=null){
			maxNoticeId = maxid.longValue();
		}
		userNoticeService.updateMaxNoticeId(new Long(userid), maxNoticeId);
		
		
		return new QResponse(t);
	}

	@Override
	@Transactional
	public QResponse txInsertUserNew(RegistDto dto) {
		User u = new User();
		u.setPassword(dto.getLoginPwd());
		u.setOrg_id("0");
		u.setUsername(dto.getLoginName());
		u.setNick_name(dto.getLoginName());// 昵称默认是登录手机号
		u.setMobilephone(dto.getLoginName());// 手机号就是登录名
		// u.setStatus("1");// 设置账户成为未启用(未实名)状态
//		u.setQrcode_id(q.getId());// 设置二维码的id
		u.setReg_date(PlanTool.getCurrentDateTime("yyyyMMdd"));
		String sql = M.getInsertSqlWhenFilesIsNotNull(u);
		Long userid = UserDao.insert(sql, u);
		accountService.insertAaccount(userid.intValue());// 插入账户
//		qrcodeService.changeQrcodeState(q.getId().toString(), "3");
		Token t = new Token();
		t.setToken(M.createUUID());
		t.setUserid(userid + "");
		tokenService.insertToken(t);
		//增加相应的公告ID
		Integer maxid = noticesService.getMaxNoticesId();
		Long maxNoticeId = 0L;
		if (maxid!=null){
			maxNoticeId = maxid.longValue();
		}
		userNoticeService.updateMaxNoticeId(new Long(userid), maxNoticeId);
		return new QResponse(t);
	}
	@Override
	public QResponse login(String username, String pwd) {
		String sql = SQLConf.getSql("user", "findUserByName");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		User u = null; 
		try{
			u = UserDao.findBy(sql, map);
		}catch(Exception e){
			u = null;
		}
		if (u==null){
			return QResponse.newInstance(false, "用户名或密码错误");
		}
		boolean state = checkUserState(u);
		if (!state)
			return QResponse.newInstance(false, "账户异常,请联系客服");
		if (u.getPassword().equals(pwd)) {
			Token t = new Token();
			t.setToken(M.createUUID());
			t.setUserid(u.getId() + "");
			tokenService.updateToken(t);
			return new QResponse(new LoginRetBeen(u, t));
		}
		return QResponse.newInstance(false, "用户名或密码错误");
	}

	public class LoginRetBeen extends Token {
		private static final long serialVersionUID = 8192008184714866762L;
		private String userName, sex;
		private String nick_name;

		LoginRetBeen(User u, Token t) {
			this.userName = u.getUsername();
			this.sex = u.getSex();
			this.nick_name = u.getRealname();
			this.token = t.getToken();
			this.userid = t.getUserid();
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getNick_name() {
			return nick_name;
		}

		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}

	}

	@Override
	public QResponse updatePwd(UpdatePwdDto dto) {
		boolean t = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (t) {
			String sql = SQLConf.getSql("user", "findUserById");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dto.getUserId());
			//User u = UserDao.findBy(sql, map);
			List<User> users = UserDao.find(sql, map);
			if (users==null || users.isEmpty() || users.size()<=0){
				return QResponse.newInstance(false, "无此用户信息");
			}
			User u = users.get(0);
			
			if (u.getPassword().equals(dto.getLoginPwd())) {
				sql = SQLConf.getSql("user", "changePwd");
				map.put("password", dto.getNewPwd());
				UserDao.update(sql, map);
				Token to = new Token();
				to.setToken(M.createUUID());
				to.setUserid(dto.getUserId());
				tokenService.updateToken(to);
				return new QResponse(to);
			} else {
				return QResponse.newInstance(false, "密码校验错误");
			}
		}
		return QResponse.ERROR_SECURITY;
	}

	@Override
	public Token updatePwd(ForgetPwdDto dto) {
		String pwd = dto.getNewPwd();
		if (pwd == null || pwd.trim().isEmpty()) {
			return null;
		}
		String sql = "select id from t_s_user where USERNAME=:USERNAME";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USERNAME", dto.getLoginName());// 登录名就是手机号码
		
		List<User> users = UserDao.find(sql, map);
		if (users==null || users.isEmpty() || users.size()<=0){
			return null;
		}
		User u = users.get(0);
		//User u = UserDao.findBy(sql, map);
		String id = u.getId() + "";
		map.clear();
		sql = "update t_s_user set password=:password where USERNAME=:USERNAME";
		map = new HashMap<String, Object>();
		map.put("password", pwd);
		map.put("USERNAME", dto.getLoginName());
		UserDao.update(sql, map);
		Token to = new Token();
		to.setToken(M.createUUID());
		to.setUserid(id);
		tokenService.updateToken(to);
		return to;
	}

	@Override
	public boolean loginOut(loginOutDto dto) {
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			Token to = new Token();
			to.setToken(M.createUUID());
			to.setUserid(dto.getUserId());
			tokenService.updateToken(to);
			return true;
		}
		return false;
	}

	@Override
	public QResponse getBaseInfo(IdentityDto dto) {
		boolean t = tokenService.checkToken(dto);
		if (t) {
			String sql = "select * from" + TABLE_NAME + "where id=:userId";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", dto.getUserId());
			List<User> li = UserDao.find(sql, map);
			if (li == null || li.size() == 0) {
				return new QResponse(false, "为找到该用户信息");
			} else {
				User u = li.get(0);
				String auth = u.getIs_auth();
				if (auth == null || auth.isEmpty()) {
					return new QResponse(false, "账户异常");
				} else if (auth.equals("1")) {
					User us = new User();
					us.setRealname(u.getRealname());
					us.setId_number(u.getId_number());
					us.setBank_name(u.getBank_name());
					us.setCard_no(u.getCard_no());
					us.setMobilephone(u.getMobilephone());
					us.setNick_name(u.getNick_name());
					us.setSex(u.getSex());
					us.setIs_auth("1");
					us.setUpfileurl(systemParamService.findParam("app_url_o")+"/upload/uploadImage");
					return new QResponse(us);
				} else if (auth.equals("0")||auth.equals("2")) {
					User us = new User();
					us.setIs_auth(auth);
					us.setQrcode_id(u.getQrcode_id());
					us.setUpfileurl(systemParamService.findParam("app_url_o")+"/upload/uploadImage");
					return new QResponse(us);
				} else {
					return new QResponse(false, "账户已经失效");
				}
			}
		}
		return QResponse.ERROR_SECURITY;
	}

	@Override
	public QResponse addUserCard(AddUserCarDto dto, HttpServletRequest request) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			boolean isVerify = BankAuthUtil.bankCard4Verify(dto.getRealname(), dto.getId_number(), dto.getCardNo(), dto.getPhoneNo());
			if (isVerify){
				HXRegisterParam registerParam = new HXRegisterParam();
					registerParam.setClient_trans_id(generateTransId());
					registerParam.setMerchant_oper_flag("A");
					registerParam.setCounter_fee_t0("0");//单笔消费交易手续费
					String provinceCode = "";
					String cityCode = "";
					String districtCode = "";
				if (StringUtils.isBlank(dto.getPhoneNo())) {
					return new QResponse(false, "电话号码不能为空");
				} else {
					registerParam.setMerchant_code("HX"+dto.getPhoneNo());
				}
					
				if (StringUtils.isBlank(dto.getRealname())) {
						return new QResponse(false, "商户名称不能为空");
				}else {
					registerParam.setMerchant_name(dto.getRealname());
				}
				if (StringUtils.isBlank(dto.getBankProvince())) {
					return new QResponse(false,  "商户所在省份不能为空");
				} else{
					registerParam.setMerchant_province(dto.getBankProvince());
					provinceCode= dto.getBankProvince();
				}
				if (StringUtils.isBlank(dto.getBankCity())) {
					return new QResponse(false,  "商户所在城市不能为空");
				}else{
					registerParam.setMerchant_city(dto.getBankCity());
					cityCode = dto.getBankCity();
				}
				if (StringUtils.isBlank(dto.getBankArea())) {
					return new QResponse(false,  "商户地址不能为空");
				} else{
					registerParam.setMerchant_address(dto.getBankArea());
					districtCode = dto.getBankArea();
				}
					
				if (StringUtils.isBlank(dto.getRealname())) {
					return new QResponse(false,  "姓名不能为空");
				} else{
					registerParam.setFamily_name(dto.getRealname());
				}
					
				if (StringUtils.isBlank(dto.getId_number())) {
					return new QResponse(false,  "证件号不能为空");
				}else{
					registerParam.setId_card(dto.getId_number());
				}
				if (StringUtils.isBlank(dto.getPhoneNo())) {
						return new QResponse(false, "手机号不能为空");
				} else{
					registerParam.setMobile(dto.getPhoneNo());
				}
				
				if (StringUtils.isBlank(dto.getBankName())) {
					return new QResponse(false, "银行名称不能为空");
				}
				
				 if (StringUtils.isBlank(dto.getCardNo())) {
					 return new QResponse(false, "结算账号不能为空");
				} else{
					
					if(dto.getCardNo().length()>6){
						String sql = "select * from t_card_bin where cardBin like  '%" + dto.getCardNo().substring(0,6)  + "%'  and (cardType='借记卡' or cardType is null)";
						List<CardBin> li = cardBinDao.find(sql, null);
						if (li == null || li.size() == 0) {
							 return new QResponse(false, "结算卡请用借记卡");
						} 
						if(li.get(0)!=null && li.get(0).getBankName()!=null && (li.get(0).getBankName().contains("邮政") || li.get(0).getBankName().contains("邮储"))){
							 return new QResponse(false, "结算卡暂不支持该银行");
						}
					}
					registerParam.setPayee_bank_no(dto.getCardNo());
				}
				 
				 String findFeeSql = "select FEE from  t_param_fee  where CODE = :purchase";
					Map<String, Object> findFeeMap = new HashMap<String, Object>();
					findFeeMap.put("purchase", "purchase");
				ParamFee paramFee =	paramFeeDao.findBy(findFeeSql, findFeeMap);
				 
			    if (paramFee!=null&&paramFee.getFee()!=null) {
			    	registerParam.setRate_t0(String.format("%.2f",paramFee.getFee()));
				} else{
					 return new QResponse(false, "获取费率异常");
				}
			    
			    String findFeeSql2 = "select FEE from  t_param_fee  where CODE = :quickl_proxy_pay";
				Map<String, Object> findFeeMap2 = new HashMap<String, Object>();
				findFeeMap2.put("quickl_proxy_pay", "quickl_proxy_pay");
			ParamFee paramFee2 =	paramFeeDao.findBy(findFeeSql2, findFeeMap2);
			 
		    if (paramFee2!=null&&paramFee2.getFee()!=null) {
		    	registerParam.setCounter_fee_t1(String.format("%.2f",paramFee2.getFee()));//单笔提现手续费
			} else{
				 return new QResponse(false, "获取提现手续费异常");
			}
			    //调用汇享报户
			    String merchantid = "";
			    //报送户的先注视掉
			/* try {
				Map resultMap =   hxpayService.HXRegister(registerParam);
				if ("000000".equals(resultMap.get("respCode")!=null ?resultMap.get("respCode").toString() : "")) {
					merchantid = resultMap.get("third_merchant_code")!=null ? resultMap.get("third_merchant_code").toString() : "";
				} else {
					if(resultMap.get("respMsg")!=null && resultMap.get("respMsg").toString().indexOf("merchant_code")>0){
						String merchantcode =  resultMap.get("respMsg").toString();
						merchantid = merchantcode.substring(merchantcode.indexOf("=")+1, merchantcode.length()-1);
					}else{
						 return new QResponse(false, resultMap.get("respCode")+"-"+resultMap.get("respMsg"));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				 return new QResponse(false, "报户异常");
			}*/
				String id = dto.getUserId();
				String realName = dto.getRealname();
				String idNumber = dto.getId_number();
				String cardNo = dto.getCardNo();
				String bankName = dto.getBankName();
				String mobilephone = dto.getPhoneNo();
				String sql = "update t_s_user set PROVINCE_CODE =:PROVINCE_CODE,CITY_CODE=:CITY_CODE,DISTRICT_CODE=:DISTRICT_CODE, mobilephone=:mobilephone,is_auth =:is_auth,realname=:realName,id_number=:idNumber,bank_name=:bankName,card_no=:cardNo,auth_time=now() where id=:id";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("realName", realName);
				map.put("idNumber", idNumber);
				map.put("cardNo", cardNo);
				map.put("bankName", bankName);
				map.put("is_auth", "2");
				map.put("mobilephone", mobilephone);
				map.put("PROVINCE_CODE", provinceCode);
				map.put("CITY_CODE", cityCode);
				map.put("DISTRICT_CODE", districtCode);
				map.put("id", id);
				int change = UserDao.update(sql, map);
				return new QResponse();
			} else {
				return new QResponse(false, "身份信息校验失败");
			}
		}
		return QResponse.ERROR_SECURITY;
	}

	@Override
	public QResponse addUserCardNew(AddUserCarDto dto, HttpServletRequest request) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			boolean isVerify = BankAuthUtil.bankCard4Verify(dto.getRealname(), dto.getId_number(), dto.getCardNo(), dto.getPhoneNo());
			if (isVerify){
				//二维码判断
				Qrcode q = null;
				try {
					q = qrcodeService.getQrcodeByInvitation(dto.getInvitationCode());
				} catch (Exception e) {
					e.printStackTrace();
					return new QResponse(false, "邀请码不存在");
				}
				if (q==null){
					return new QResponse(false, "邀请码不存在或状态异常");
				}
				ParamFee fee = paramFeeService.getUseParamByCode("purchase");// 取出还款手续费
				boolean reteb = orgrateService.updateDiffRateInfo(q.getOrg_id()
						.toString(), q.getId().toString(), fee.getFee());
				if (!reteb) {
					M.logger.debug("insertUser() t_s_org_rate 修改失败");
					throw new RuntimeException("insertUser t_s_org_rate 修改失败");
				}
				
				
					String provinceCode = "";
					String cityCode = "";
					String districtCode = "";
				if (StringUtils.isBlank(dto.getPhoneNo())) {
					return new QResponse(false, "电话号码不能为空");
				}
					
				if (StringUtils.isBlank(dto.getRealname())) {
						return new QResponse(false, "商户名称不能为空");
				}
				if (StringUtils.isBlank(dto.getBankProvince())) {
					return new QResponse(false,  "商户所在省份不能为空");
				} else{
					provinceCode= dto.getBankProvince();
				}
				if (StringUtils.isBlank(dto.getBankCity())) {
					return new QResponse(false,  "商户所在城市不能为空");
				}else{
					cityCode = dto.getBankCity();
				}
				if (StringUtils.isBlank(dto.getBankArea())) {
					return new QResponse(false,  "商户地址不能为空");
				} else{
					districtCode = dto.getBankArea();
				}
					
				if (StringUtils.isBlank(dto.getRealname())) {
					return new QResponse(false,  "姓名不能为空");
				}
					
				if (StringUtils.isBlank(dto.getId_number())) {
					return new QResponse(false,  "证件号不能为空");
				}
				if (StringUtils.isBlank(dto.getPhoneNo())) {
						return new QResponse(false, "手机号不能为空");
				} 
				
				if (StringUtils.isBlank(dto.getBankName())) {
					return new QResponse(false, "银行名称不能为空");
				}
				
				 if (StringUtils.isBlank(dto.getCardNo())) {
					 return new QResponse(false, "结算账号不能为空");
				} 
					if(dto.getCardNo().length()>6){
						String sql = "select * from t_card_bin where cardBin like '%" + dto.getCardNo().substring(0,6)  + "%'  and (cardType='借记卡' or cardType is null)";
						List<CardBin> li = cardBinDao.find(sql, null);
						if (li == null || li.size() == 0) {
							 return new QResponse(false, "结算卡请用借记卡");
						} 
						if(li.get(0)!=null && li.get(0).getBankName()!=null && (li.get(0).getBankName().contains("邮政") || li.get(0).getBankName().contains("邮储"))){
							 return new QResponse(false, "结算卡暂不支持该银行");
						}
					}
			    String merchantid = "";
				String id = dto.getUserId();
				String realName = dto.getRealname();
				String idNumber = dto.getId_number();
				String cardNo = dto.getCardNo();
				String bankName = dto.getBankName();
				String mobilephone = dto.getPhoneNo();
				String sql = "update t_s_user set PROVINCE_CODE =:PROVINCE_CODE,CITY_CODE=:CITY_CODE,DISTRICT_CODE=:DISTRICT_CODE, "
						+ " mobilephone=:mobilephone,is_auth =:is_auth,realname=:realName,id_number=:idNumber,"
						+ "bank_name=:bankName,card_no=:cardNo,org_id=:org_id,relation_no=:relation_no,qrcode_id=:qrcode_id,auth_time=now() where id=:id";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("realName", realName);
				map.put("idNumber", idNumber);
				map.put("cardNo", cardNo);
				map.put("org_id", q.getOrg_id().toString());
				map.put("relation_no", q.getRelation_no());
				map.put("qrcode_id", q.getId());
				map.put("bankName", bankName);
				map.put("is_auth", "2");
				map.put("mobilephone", mobilephone);
				map.put("PROVINCE_CODE", provinceCode);
				map.put("CITY_CODE", cityCode);
				map.put("DISTRICT_CODE", districtCode);
				map.put("id", id);
				int change = UserDao.update(sql, map);
				//修改二维码状态
				qrcodeService.changeQrcodeState(q.getId().toString(), "3");
				
				return new QResponse();
			} else {
				return new QResponse(false, "身份信息校验失败");
			}
		}
		return QResponse.ERROR_SECURITY;
	}
	
	private MerchantRegisterResponse merchantRegister(HttpServletRequest httpServletRequest){
		String realname =httpServletRequest.getParameter("realname");
		String phoneNo =httpServletRequest.getParameter("phoneNo");
		String id_number =httpServletRequest.getParameter("id_number");
		String cardNo =httpServletRequest.getParameter("cardNo");
		String bankHeadOfficeName =httpServletRequest.getParameter("bankHeadOfficeName");
		String bankName =httpServletRequest.getParameter("bankName");
		String bankProvince =httpServletRequest.getParameter("bankProvince");
		String bankCity =httpServletRequest.getParameter("bankCity");
		String bankArea =httpServletRequest.getParameter("bankArea");



		MerchantRegisterRequest request = new MerchantRegisterRequest();
		request.setRequestId(getOrderNo());
		request.setCustomerType("PERSON");//PERSON ：个人 ENTERPRISE：企业
		request.setSignedName(realname);//个人时，填写姓名；企业时：填企业名称
		request.setBindMobile(httpServletRequest.getParameter("phoneNo"));//手机号
		request.setLinkman(realname);//联系人
		request.setIdcard(id_number);//结算银行卡身份证号码
		request.setMinSettleAmount("1");//商户起始结算金额，大于0的整数，单位：分。
		request.setBankAccountType("PrivateCash");//PrivateCash：对私，PublicCash：对公
		request.setBankAccountNumber(cardNo);
		request.setBankHeadOfficeName(bankHeadOfficeName);//结算银行卡开户行，总行名称
		request.setBankName(bankName);//结算银行卡开户所在支行
		request.setAccountName(realname);//结算银行卡 bankaccountnumber 对应的开户名
		request.setBankProvince(bankProvince);//开户省
		request.setBankCity(bankCity);//开户市
		request.setBankArea(bankArea);//开户区
		request.setDeposit("0");//单位：分   保证金仅会影响到商户的出款金额：可用于出款的金额 = 账户余额 – 保证金   默认为0
		request.setCallbackUrl(systemParamService.findParam("app_url_o")+"/merchantRegisterCallBack");//子商户审核通结果  回调通知地址
		try {
			MerchantRegisterResponse response = aimiMerchantManagerService.merchantRegister(request);
			System.out.println("子商户注册返回:"+ JSONObject.toJSONString(response));    //{"code":"0","merchantId":"000000088","msg":"成功","sign":"9808CF21EB7EFC8A5E23256CDEFD2FF7"}
			return response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MerchantRegisterResponse();
	}

	/**
	 *子商户费率设置
	 */
	private MerchantSettingFeeResponse merchantSettingFee(String merchantId){
		MerchantSettingFeeRequest request = new MerchantSettingFeeRequest();
		request.setMerchantId(merchantId);//子商户号
		request.setFee("0");//D0 每笔交易手续费   单元分
		request.setT0Rate("0.0080");//t0交易费率  万分比   t0实际交易费率  = T0Rate + T1Rate =0.0038
		request.setT1Rate("0.0080");//t1交易费率
		request.setTradeType("UNIONPAYPORT");//交易类型 参照文档附录4
		try {
			MerchantSettingFeeResponse response = aimiMerchantManagerService.merchantSettingFee(request);
			System.out.println("子商户费率设置返回:"+JSONObject.toJSONString(response));
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MerchantSettingFeeResponse();
	}



	private String getOrderNo() {
		String time= StringUtil.getCurrentDateTime("yyMMddHHmmss");
		long uniqueId=0;
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			uniqueId = jedis.incr("uniqueId");
			long temp= uniqueId;
			if(uniqueId<=50){//有可能出现并发的时候会产生重复的 uniqueId    ，比如0001有多个
				uniqueId = jedis.incr("uniqueId2");
			}
			if(temp>89999){
				jedis.set("uniqueId","0");
			}
			if(45000<temp&&temp<45010){
				jedis.set("uniqueId2","0");
			}
		}catch (Exception e){
			uniqueId=new Random().nextInt(89999);
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}

		return "A"+time+String.format("%05d", uniqueId);

	}


	@Override
	public String getUserPhoneNo(String loginName) {
		String sql = "select MOBILEPHONE from t_s_user where USERNAME=:USERNAME";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USERNAME", loginName);
		try {
			List<User> li = UserDao.find(sql, map);
			if (li == null || li.isEmpty()) {
				return null;
			}
			return li.get(0).getMobilephone();
		} catch (Exception e) {
			e.printStackTrace();
			M.logger.debug("getUserPhoneNo() ERROR!", e);
		}
		return null;
	}

	@Override
	public boolean checkUserState(String userid){
		try{
		String sql = "select status from" + TABLE_NAME + "where id=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userid);
		List<User> li = UserDao.find(sql, map);
		if (li == null || li.size() == 0) {
			return false;
		} else {
			User u = li.get(0);
			String state = u.getStatus();
			if ("1".equals(state)) {
				return true;
			} else {
				return false;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getUserById(String  userid) {
		String sql = "select *from" + TABLE_NAME + "where id=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userid);
		List<User> li = UserDao.find(sql, map);
		if (li.size() != 0) {
			return li.get(0);
		}
		return null;
	}

	/**
	 * 检查账户状态
	 * 
	 * @param u
	 * @return
	 */
	public boolean checkUserState(User u) {
		if (u == null)
			return false;
		String state = u.getStatus();
		if ("1".equals(state)) {
			return true;
		} else {
			return false;
		}
	}
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
}
