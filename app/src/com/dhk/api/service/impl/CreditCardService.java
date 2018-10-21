package com.dhk.api.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhk.api.dao.ICardBinDao;
import com.dhk.api.dao.ICreditCardDao;
import com.dhk.api.dto.AddCreditCarDto;
import com.dhk.api.dto.DelCreditCarDto;
import com.dhk.api.dto.EditCreditCarDto;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.BankCss;
import com.dhk.api.entity.CardBin;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.entity.Token;
import com.dhk.api.entity.User;
import com.dhk.api.entity.param.HXPayBindParam;
import com.dhk.api.service.IBankCssService;
import com.dhk.api.service.ICardBinService;
import com.dhk.api.service.ICreditCardService;
import com.dhk.api.service.ICreditcardBillService;
import com.dhk.api.service.IHXPayService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.IRepayRecordService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.IUserService;
import com.dhk.api.third.BankAuthUtil;
import com.dhk.api.tool.M;
import com.dhk.init.Constant;
import com.dhk.redis.RedisUtils;
import com.fast.pay.FastPay;
import com.fast.pay.core.FastPayResponse;
import com.fast.pay.core.product.CreditCardRefundRequest;
import com.xdream.kernel.util.StringUtil;

@Service("CreditCardService")
public class CreditCardService implements ICreditCardService {
	private Logger logger = Logger.getLogger(CreditCardService.class);

	@Resource(name = "CreditCardDao")
	private ICreditCardDao creditCardDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "PayService")
	private IPayService payService;
	
	@Resource(name = "RepayRecordService")
	private IRepayRecordService repayRecordService;
	@Resource(name = "HXPayService")
	IHXPayService   hxpayService;
	
	@Resource(name = "UserService")
	private IUserService userService;
	@Resource(name = "CardBinDao")
	private ICardBinDao cardBinDao;
	@Autowired
	RedisUtils redisUtils;
	
	
	@Override
	public QResponse addCreditCarSendMsg(AddCreditCarDto dto) {
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			boolean isVerify = false;
			try {
				
				User u = userService.getUserById(dto.getUserId());
				if(u==null){
					return new QResponse(false, "用户信息异常");
				}
//				return HlbBankCardUtil.HlbPayAuthBandCardSMS(dto);
				HXPayBindParam bindParam = new HXPayBindParam();
				bindParam.setClient_trans_id(generateTransId());
				bindParam.setCvv(dto.getCvn2());
				String expire_date = "";
				if(dto.getExpDate()!=null){
					expire_date = dto.getExpDate().substring(2,4)+dto.getExpDate().substring(0,2);
				}
				if(u.getMerchantid()==null){
					return new QResponse(false, "绑卡失败-请先报户");
				}
				bindParam.setThird_merchant_code(u.getMerchantid());
				bindParam.setExpire_date(expire_date);
				bindParam.setFamily_name(dto.getRealname());
				bindParam.setId_card(dto.getCerdId());
				bindParam.setMobile(dto.getPhoneNo());
				bindParam.setPay_bank_no(dto.getCardNo());
				Map resultMap = hxpayService.HXBind(bindParam);
				if (!"000000".equals(resultMap.get("respCode") != null ? resultMap.get("respCode").toString() : "")) {
						return new QResponse(false, "发送验证码请求失败:"+resultMap.get("respMsg")+"");
				}else{
					QResponse response = new QResponse(true, "短信发送成功"); 
					String status = resultMap.get("status") != null ? resultMap.get("status").toString() : "";
					if(!"SUCCESS".equals(status)){
						return new QResponse(false, "发送验证码:"+resultMap.get("err_msg")+"");
					}
					response.data = resultMap.get("bind_apply_code") != null ? resultMap.get("bind_apply_code").toString() : "";
					return response; 
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("短信发送失败", e);
				return new QResponse(false, "短信发送失败");
			}

		}
		return new QResponse(false, "非法请求");
	}
	
	/**
	 * 快捷支付发送短信
	 */
	@Override
	public QResponse addCreditCarSendMsgKj(AddCreditCarDto dto) {
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			 
			try {
				boolean isVerify = false;

				User u = userService.getUserById(dto.getUserId());
				if(u==null){
					return new QResponse(false, "用户信息异常");
				}
				if(StringUtil.isEmpty(u.getKj_merno())  || StringUtil.isEmpty(u.getKj_key())){
                	return QResponse.newInstance(false, "请先进行报户！");
                }
				  FastPay pay = new FastPay(Constant.kjpostUrl,u.getKj_merno(),
							u.getKj_key());
				  try{
						isVerify = BankAuthUtil.bankCard4Verify(u.getRealname(),u.getId_number(), dto.getCardNo(), dto.getPhoneNo());
					}catch(Exception ee){
						ee.printStackTrace();
						isVerify = false;
					}
					if (!isVerify){
						return new QResponse(false, "身份信息校验失败");
					}
					dto.setRealname(u.getRealname()); //防止前端乱传数据
					dto.setCerdId(u.getId_number());
					String sql = "select * from t_s_user_creditcard where card_no=:card_no and card_status='1'";
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("card_no", dto.getCardNo());
						if (!creditCardDao.find_Personal(sql, map).isEmpty()) {
							return QResponse.newInstance(false, "卡号已被绑定");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					int bday = 0;
					int rday = 0;
					try {
						bday = Integer.parseInt(dto.getBill_date());
						rday = Integer.parseInt(dto.getRepay_date());
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
						return QResponse.newInstance(false, "账单日还款日格式错误");
					}
					if (bday == rday) {
						return QResponse.newInstance(false, "账单日和还款日不能是同一天");
					}
					String expire_date = "";
					if(dto.getExpDate()!=null){
						expire_date = dto.getExpDate().substring(2,4)+dto.getExpDate().substring(0,2);
						//expire_date = dto.getExpDate();
					}


				//创建信用卡还款请求对象
				CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
				creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_GVC);
				creditCardRefundRequest.put("phone", dto.getPhoneNo());
				creditCardRefundRequest.put("card_number", dto.getCardNo());
				creditCardRefundRequest.put("id_card_number", dto.getCerdId());
				creditCardRefundRequest.put("card_month_year", expire_date);
				creditCardRefundRequest.put("car_cvn", dto.getCvn2());
				creditCardRefundRequest.put("user_name", dto.getRealname());
				creditCardRefundRequest.put("timestamp", System.currentTimeMillis());
				//提交请求
				logger.info("发送验证码请求："+creditCardRefundRequest);
				FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
				logger.info("返回："+fastPayResponse);
				if (!"A0000".equals(fastPayResponse.getError() != null ? fastPayResponse.getError().toString() : "")) {
						return new QResponse(false, "发送验证码请求失败:"+fastPayResponse.getMessage()+"");
				}else{
					QResponse response = new QResponse(true, "短信发送成功"); 
					 
					return response; 
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("短信发送失败", e);
				return new QResponse(false, "短信发送失败");
			}

		}
		return new QResponse(false, "非法请求");
	}

	@Override
	public QResponse addCreditCar(AddCreditCarDto dto) throws Exception {
		boolean c = tokenService.checkToken(dto.getUserId(), dto.getToken());
		if (c) {
			boolean isVerify = false;

			User u = userService.getUserById(dto.getUserId());
			if(u==null){
				return new QResponse(false, "用户信息异常");
			}
			 
			try{
				isVerify = BankAuthUtil.bankCard4Verify(u.getRealname(),u.getId_number(), dto.getCardNo(), dto.getPhoneNo());
			}catch(Exception ee){
				ee.printStackTrace();
				isVerify = false;
			}
			if (!isVerify){
				return new QResponse(false, "身份信息校验失败");
			}
			dto.setRealname(u.getRealname()); //防止前端乱传数据
			dto.setCerdId(u.getId_number());
			String sql = "select * from t_s_user_creditcard where card_no=:card_no and card_status='1'";
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("card_no", dto.getCardNo());
				if (!creditCardDao.find_Personal(sql, map).isEmpty()) {
					return QResponse.newInstance(false, "卡号已被绑定");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			int bday = 0;
			int rday = 0;
			try {
				bday = Integer.parseInt(dto.getBill_date());
				rday = Integer.parseInt(dto.getRepay_date());
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
				return QResponse.newInstance(false, "账单日还款日格式错误");
			}
			if (bday == rday) {
				return QResponse.newInstance(false, "账单日和还款日不能是同一天");
			}
			String expire_date = "";
		/*	QResponse ret = HlbBankCardUtil.HlbPayAuthBandCardSubmit(dto);
			if(!ret.state)
				return ret;*/
//			HXPayBindConfirmParam bindConfirmParam = new HXPayBindConfirmParam();
//			bindConfirmParam.setBind_apply_code(dto.getOrderId());
//			bindConfirmParam.setClient_trans_id(generateTransId());
//			bindConfirmParam.setCvv(dto.getCvn2());
//			String expire_date = "";
//			if(dto.getExpDate()!=null){
//				expire_date = dto.getExpDate().substring(2,4)+dto.getExpDate().substring(0,2);
//			}
//			bindConfirmParam.setExpire_date(expire_date);
//			bindConfirmParam.setFamily_name(dto.getRealname());
//			bindConfirmParam.setId_card(dto.getCerdId());
//			bindConfirmParam.setMobile(dto.getPhoneNo());
//			bindConfirmParam.setPay_bank_no(dto.getCardNo());
//			bindConfirmParam.setVerify_code(dto.getCheckCode());
//			bindConfirmParam.setThird_merchant_code(u.getMerchantid());
//			Map resultMap = hxpayService.HXBindConfirm(bindConfirmParam);
//			if (!"000000".equals(resultMap.get("respCode") != null ? resultMap.get("respCode").toString() : "")) {
//					return new QResponse(false, "确认绑定卡请求失败："+resultMap.get("respMsg")+"");
//			}
//			String status = resultMap.get("status") != null ? resultMap.get("status").toString() : "";
//			if(!"SUCCESS".equals(status)){
//				return new QResponse(false, "确认绑定卡:"+resultMap.get("err_msg")+"");
//			}
//			if(dto.getCardNo().length()>6){
//				String sqlCardBin = "select * from t_card_bin where cardBin like '" + dto.getCardNo().substring(0,6)  + "%'  and (cardType='贷记卡' or cardType='准贷记卡' or cardType is null or cardType='')";
//				List<CardBin> li = cardBinDao.find(sqlCardBin, null);
//				if (li == null || li.size() == 0) {
//					 return new QResponse(false, "未识别到该银行，请换卡再试");
//				} 
//			}
			if(dto.getExpDate()!=null){
				expire_date = dto.getExpDate().substring(2,4)+dto.getExpDate().substring(0,2);
				//expire_date = dto.getExpDate();
		    }
 
			if("kj".equals(Constant.bktdtype)){
                        if(StringUtil.isEmpty(u.getKj_merno())  || StringUtil.isEmpty(u.getKj_key())){
                        	return QResponse.newInstance(false, "请先进行报户！");
                        }
				        FastPay pay = new FastPay(Constant.kjpostUrl,u.getKj_merno(),
						u.getKj_key());

						//创建信用卡还款请求对象
						CreditCardRefundRequest creditCardRefundRequest = new CreditCardRefundRequest();
						creditCardRefundRequest.setAction(CreditCardRefundRequest.Action.CCR_BDC);
						creditCardRefundRequest.put("phone", dto.getPhoneNo());
						creditCardRefundRequest.put("user_name", dto.getRealname());
						creditCardRefundRequest.put("card_number", dto.getCardNo());
						creditCardRefundRequest.put("id_card_number", dto.getCerdId());
						creditCardRefundRequest.put("card_month_year", expire_date);
						creditCardRefundRequest.put("car_cvn", dto.getCvn2());
						creditCardRefundRequest.put("validate_code",dto.getCheckCode());
						creditCardRefundRequest.put("bank_name", dto.getBank_name());
						
						logger.info("绑卡请求："+creditCardRefundRequest);
						//提交请求
						FastPayResponse fastPayResponse = pay.execute(creditCardRefundRequest);
						
						logger.info("绑卡返回："+fastPayResponse);
						
						if (!"A0000".equals(fastPayResponse.getError() != null ? fastPayResponse.getError().toString() : "")) {
							return new QResponse(false, "确认绑定卡请求失败:"+fastPayResponse.getMessage()+"");
					    }else{
						QResponse response = new QResponse(true, "确认绑定卡成功"); 
						 
					}
			}
			CreditCard car = new CreditCard(dto);
	/*		String bindId = (String) ret.data;
			car.setBindId(bindId);*/
			sql = M.getInsertSqlWhenFilesIsNotNull(car);
			creditCardDao.insert_Personal(sql, car);
			return QResponse.newInstance(true, "");

		}
		return new QResponse(false, "非法请求");
	}

	@Override
	public Token editCreditCar(EditCreditCarDto dto)  throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		// String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			CreditCard car = new CreditCard(dto);
			String where = " where userid=:userid and card_no=:card_no and card_status='1'";
			String carno = car.getCard_no();
			String userid = car.getUserid();
			car.setCard_no(null);
			car.setUserid(null);
			String sql2 = M.getUpdateSqlWhenFilesIsNotNull(car, "");
			car.setCard_no(carno);
			car.setUserid(userid);
			System.out.println(car);
			logger.debug("editCreditCar sql:" + sql2 + where);
			int i = creditCardDao.updateBy_Personal(sql2 + where, car);
			if (i == 1) {
				return new Token();
			}
		}
		return null;
	}

	@Override
	public Token deleteCreditCar(DelCreditCarDto dto)  throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			String sql = "delete from t_s_cost_plan where status='0' and card_no=:card_no and user_id=:userid";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("card_no", cardNo);
			map.put("userid", userId);
			creditCardDao.delete_Personal(sql, map);
			sql = "update t_s_user_creditcard set card_status ='0' where card_no=:card_no and userid =:userid";
			creditCardDao.update_Personal(sql, map);
			//解冻
			//repayRecordService.unfreeze(Long.parseLong(userId), cardNo);
			
			return new Token();
		}
		return null;
	}
	
	@Override
	public Token txUnfreeze(DelCreditCarDto dto)  throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			String sql = "update t_n_repay_plan set status='2' where status='0' and credit_card_no=:card_no and user_id=:user_id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("card_no", cardNo);
			map.put("user_id", userId);
			creditCardDao.update_Personal(sql, map);
			//解冻
			//repayRecordService.unfreeze(Long.parseLong(userId), cardNo);
			return new Token();
		}
		return null;
	}

	@Resource(name = "CreditcardBillService")
	private ICreditcardBillService creditcardBillService;

	@Resource(name = "BankCssService")
	private IBankCssService bankCssService;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Resource(name = "CardBinService")
	private ICardBinService cardBinService;

	@Override
	public QResponse getCreditCarList(IdentityDto dto)  throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		boolean c1 = tokenService.checkToken(userId, token);
		if (c1) {
			String sql = "select * from t_s_user_creditcard where userId=:userId and card_status='1'";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			List<CreditCard> t = creditCardDao.find_Personal(sql, map);
			List<CreditCardListBeen> ret = new LinkedList<CreditCardListBeen>();
			for (CreditCard c : t) {
				CardBin b = cardBinService.getCarbinByCardNo(c.getCard_no().substring(0, 6) + "%'");// 获取cardbin表
				String code = "";
				if (b != null) {
					 code = b.getBankCode().substring(0, 4);
				} else {
					 code = "0000";
				}
				BankCss bankCss = bankCssService.getBankcssByBank4code(code);
				bankCss.setLogoimgname(bankCss.getLogoimgname());
				CreditCardListBeen creditCardListBeen=new CreditCardListBeen(c,bankCss);
				ret.add(creditCardListBeen);// 返回数据格式转换
			}
			return new QResponse(ret);
		}
		return QResponse.ERROR_SECURITY;
	}

	@Override
	public CreditCard getCreditCarInfo(String USERID, String card_no) {
		String sql = "select * from t_s_user_creditcard where card_no=:card_no and USERID=:USERID and card_status='1'";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", card_no);
		map.put("USERID", USERID);
		try {
			List<CreditCard> l = creditCardDao.find_Personal(sql, map);
			if (l != null && l.size() != 0) {
				return l.get(0);
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public QResponse getCreditCardDetail(DelCreditCarDto dto)   throws Exception{
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			String sql = "select * from t_s_user_creditcard where (userId=:userId and card_no=:card_no)";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("card_no", cardNo);
			List<CreditCard> t = creditCardDao.find_Personal(sql, map);
			List<CreditCardBillBeen> re = new LinkedList<CreditCardBillBeen>();
			if (t != null && t.size() > 0) {
				List<CreditcardBill> ret = creditcardBillService.getBillList(t
						.get(0).getId().toString());
				for (CreditcardBill bill : ret) {
					re.add(new CreditCardBillBeen(bill));
				}
			}
			return new QResponse(re);
		}
		return QResponse.ERROR_SECURITY;
	}

	public class CreditCardBillBeen {

		private String card_bill_id, repay_date, bill_date, bill_month,
				bill_amount, bill_status;



		public CreditCardBillBeen() {
		}

		public CreditCardBillBeen(CreditcardBill c) {
			this.card_bill_id = c.getId() + "";
			this.repay_date = c.getRepay_day();
			this.bill_date = c.getBill_day();
			this.bill_amount = c.getBill_amount() + "";
			this.bill_status = c.getStatus();
		}



		public String getCard_bill_id() {
			return card_bill_id;
		}

		public void setCard_bill_id(String card_bill_id) {
			this.card_bill_id = card_bill_id;
		}

		public String getRepay_date() {
			return repay_date;
		}

		public void setRepay_date(String repay_date) {
			this.repay_date = repay_date;
		}

		public String getBill_date() {
			return bill_date;
		}

		public void setBill_date(String bill_date) {
			this.bill_date = bill_date;
		}

		public String getBill_month() {
			return bill_month;
		}

		public void setBill_month(String bill_month) {
			this.bill_month = bill_month;
		}

		public String getBill_amount() {
			return bill_amount;
		}

		public void setBill_amount(String bill_amount) {
			this.bill_amount = bill_amount;
		}

		public String getBill_status() {
			return bill_status;
		}

		public void setBill_status(String bill_status) {
			this.bill_status = bill_status;
		}
	}

	public class CreditCardListBeen {

		private String logurl, bacColor;
		/**
		 * 卡号
		 */
		private String card_no;
		private String phoneno;

		/**
		 * 银行名称
		 */
		private String bankName;
		/**
		 * 持卡人
		 */
		private String realName;
		/**
		 * 未出账
		 */
		private String nobill;

		/**
		 * 账单日
		 */
		private String bill_date;
		/**
		 * 还款日
		 */
		private String repay_day;

		/**
		 * 还款日(YYYYMMDD)
		 */
		private String repay_date;

		/**
		 * 账单金额
		 */
		private String bill_amount;
		/**
		 * 状态
		 */
		private String status;

		/**
		 * 还款日,比如30,31,1,2,3
		 */
		private String repaydays = "";

		private String mail_addr;
		private String maxmon;

		private String mail_auth_code;

		private String  hasRepay;  //已还
		private String  unRepay;   //未还
		private String  repayStatus;   //未还
		private Long  repayRecordId;
		private boolean  hasRepayPlanTemp;
		private String hasRepayCurMonth;

		public CreditCardListBeen() {

		}

		public CreditCardListBeen(CreditCard c,  BankCss bc) {
			bacColor = bc.getBaccolor();
			logurl = bc.getLogoimgname();
			card_no = c.getCard_no();
			bankName = c.getBank_name();
			phoneno = c.getPhoneno();
			maxmon = c.getMaxmon();
			realName = c.getRealname();
			nobill = "";
			mail_addr = c.getMail_addr();
			mail_auth_code = c.getMail_auth_code();
			bill_date = c.getBill_date();
			repay_day = c.getRepay_date();
//
			this.hasRepayPlanTemp =  hasRepayPlanTemp;
		}

		public boolean chaeckReapyAble(int bday, int rday) {
			Calendar cnow = Calendar.getInstance();
			int now = cnow.get(Calendar.DAY_OF_MONTH);
			if (bday < rday) {// 同月
				if (bday < now && now < rday) {
					return true;
				}
			} else {// 不同月
				if ((now > rday && now > bday) || (now < rday && now < bday)) {
					return true;
				}
			}
			return false;
		}

		public String getMaxmon() {
			return maxmon;
		}

		public void setMaxmon(String maxmon) {
			this.maxmon = maxmon;
		}

		public String getHasRepayCurMonth() {
			return hasRepayCurMonth;
		}

		public void setHasRepayCurMonth(String hasRepayCurMonth) {
			this.hasRepayCurMonth = hasRepayCurMonth;
		}

		public String getPhoneno() {
			return phoneno;
		}

		public void setPhoneno(String phoneno) {
			this.phoneno = phoneno;
		}

		public boolean isHasRepayPlanTemp() {
			return hasRepayPlanTemp;
		}

		public void setHasRepayPlanTemp(boolean hasRepayPlanTemp) {
			this.hasRepayPlanTemp = hasRepayPlanTemp;
		}

		public Long getRepayRecordId() {
			return repayRecordId;
		}

		public void setRepayRecordId(Long repayRecordId) {
			this.repayRecordId = repayRecordId;
		}

		public String getRepayStatus() {
			return repayStatus;
		}

		public void setRepayStatus(String repayStatus) {
			this.repayStatus = repayStatus;
		}

		public String getHasRepay() {
			return hasRepay;
		}

		public void setHasRepay(String hasRepay) {
			this.hasRepay = hasRepay;
		}

		public String getUnRepay() {
			return unRepay;
		}

		public void setUnRepay(String unRepay) {
			this.unRepay = unRepay;
		}
		public String getRepay_date() {
			return repay_date;
		}

		public void setRepay_date(String repay_date) {
			this.repay_date = repay_date;
		}

		public String getLogurl() {
			return logurl;
		}

		public void setLogurl(String logurl) {
			this.logurl = logurl;
		}

		public String getBacColor() {
			return bacColor;
		}

		public void setBacColor(String bacColor) {
			this.bacColor = bacColor;
		}

		public String getMail_addr() {
			return mail_addr;
		}

		public void setMail_addr(String mail_addr) {
			this.mail_addr = mail_addr;
		}

		public String getMail_auth_code() {
			return mail_auth_code;
		}

		public void setMail_auth_code(String mail_auth_code) {
			this.mail_auth_code = mail_auth_code;
		}

		public String getRepaydays() {
			return repaydays;
		}

		public void setRepaydays(String repaydays) {
			this.repaydays = repaydays;
		}

		public String getCard_no() {
			return card_no;
		}

		public void setCard_no(String card_no) {
			this.card_no = card_no;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}

		public String getNobill() {
			return nobill;
		}

		public void setNobill(String nobill) {
			this.nobill = nobill;
		}

		public String getBill_date() {
			return bill_date;
		}

		public void setBill_date(String bill_date) {
			this.bill_date = bill_date;
		}

		public String getRepay_day() {
			return repay_day;
		}

		public void setRepay_day(String repay_day) {
			this.repay_day = repay_day;
		}

		public String getBill_amount() {
			return bill_amount;
		}

		public void setBill_amount(String bill_amount) {
			this.bill_amount = bill_amount;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "CreditCardListBeen [repay_day=" + repay_day + "]";
		}

	}

	/**
	 * 按还款日距当前时间的间隔程度升序排列
	 * 
	 */
	public class RepayDateDescComparator implements
			Comparator<CreditCardListBeen> {
		private long now = new Date().getTime();

		@Override
		public int compare(CreditCardListBeen o1, CreditCardListBeen o2) {
			String d1 = o1.getRepay_date();
			String d2 = o2.getRepay_date();
			Long da1 = 0l, da2 = 0l;
			try {
				da1 = M.dformat.parse(d1).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				da2 = M.dformat.parse(d2).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long ds1 = Math.abs(da1 - now);
			long ds2 = Math.abs(da2 - now);
			return (int) (ds1 - ds2);
		}
	}

	public static void main(String[] args) {

	}
	static String generateTransId() {
		String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String nanoTime = System.nanoTime() + "";
		return "K" + String.format("%s%s", time, nanoTime.substring(nanoTime.length() - 6));
	}
	
}
