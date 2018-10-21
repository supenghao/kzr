package com.dhk.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.PayResult;
import com.dhk.api.dao.ICreditCardDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.entity.User;
import com.dhk.api.service.IApplyCashService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.tool.M;
import com.dhk.api.controller.Api;
import com.dhk.api.controller.JsonCreater;
import com.dhk.api.core.impl.PayRequest;
import com.dhk.api.dao.IAccountDao;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RechargeDto;
import com.dhk.api.entity.Account;
import com.dhk.api.entity.ApplyCash;
import com.dhk.api.entity.CreditCard;
import org.springframework.stereotype.Service;

import com.dhk.api.service.IAccountService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.IUserService;

import static com.dhk.api.controller.JsonCreater.getJsonOk;

@Service("AccountService")
public class AccountService implements IAccountService {

	@Resource(name = "AccountDao")
	private IAccountDao accountDao;

	@Resource(name = "CreditCardDao")
	private ICreditCardDao creditCardDao;

	@Resource(name = "ApplyCashService")
	private IApplyCashService applyCashService;
	@Resource(name = "api")
	private Api api;

	@Override
	public void insertAaccount(int userid) {
		Date nowdate = new Date();
		String update_date = M.dformat.format(nowdate);
		String update_time = M.tformat.format(nowdate);
		String sql = "insert into t_s_user_account(user_id,cur_balance,UPDATE_DATE,UPDATE_TIME) values(:user_id,0,:update_date,:update_time)";
		Account t = new Account();
		t.setUser_id(userid);
		t.setUpdate_date(update_date);
		t.setUpdate_time(update_time);
		accountDao.insert(sql, t);
	}

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public QResponse getUserAccount(IdentityDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			return new QResponse(getUserAccount(dto.getUserId()));
		}
		return QResponse.ERROR_SECURITY;
	}

	public Account getUserAccount(String userid) {
		String sql = "select * from t_s_user_account where user_id=:user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userid);
		List<Account> l = accountDao.find(sql, map);
		return l.get(0);
	}

//	@Resource(name = "RechargeService")
//	private IRechargeService rechargeService;

	@Resource(name = "PayService")
	private IPayService payService;

	@Resource(name = "UserService")
	private IUserService userService;

	@Override
	public QResponse txRecharge(RechargeDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			try {
				double am = Double.parseDouble(dto.getAmount());
				if (am <= 0) {
					return QResponse.newInstance(false, "充值金额不得小于0元");
				}
				if (am > 20000) {
					return QResponse.newInstance(false, "充值金额不得大于20000元");
				}
				
				User u = userService.getUserById(dto.getUserId());
				if (u == null) {
					return QResponse.newInstance(false, "充值系统内部错误,请联系客服");
				}
				// /调用支付接口
				PayRequest req = new PayRequest();
				req.setUserId(dto.getUserId());
				req.setAcctNo(dto.getCreditCardNo());
				req.setTransAmt((long) (am * 100));
				req.setPhoneNo(dto.getCreditCardPhone());
				req.setCustomerName(u.getRealname());
				req.setCerdType("01");
				req.setCerdId(u.getId_number());
				
				req.setCvn2(dto.getCvn2());
				req.setExpDate(dto.getExpdate());
				try {
					PayResult result = payService.debitPurchase_recharge(req);
					if (!"0000".equals(result.getCode())) {
						return QResponse
								.newInstance(false, result.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
					return QResponse.newInstance(false, e.getMessage());
				}
				return QResponse.OK;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return QResponse.newInstance(false, "充值金额格式错误");
			}
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

	@Override
	public JSONObject rechargeWithBindCard(RechargeDto dto, HttpServletRequest request) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			try {
				double am = Double.parseDouble(dto.getAmount());
				if (am < 10) {
					return JsonCreater.getJson(0,1001,"充值金额不得小于10元");
				}
				if (am > 20000) {
					return JsonCreater.getJson(0,1001,"充值金额不得大于20000元");
				}

				User u = userService.getUserById(dto.getUserId());
				if (u == null) {
					return JsonCreater.getJson(0,1001,"充值系统内部错误,请联系客服");
				}
				String sql = "select * from t_s_user_creditcard where card_no=:card_no and userid=:userid and card_status='1'";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("card_no", dto.getCreditCardNo());
				map.put("userid", u.getId());
				List<CreditCard>  list = creditCardDao.find_Personal(sql, map);
				CreditCard creditCard  = null;
				PayRequest req = new PayRequest();
				if(!list.isEmpty()){    // 如果已经绑定该卡 取卡数据
					creditCard=list.get(0);
					if (!api.checkShortMsg(creditCard.getPhoneno(), dto.getCheckCode(), request)) {
						return JsonCreater.getJson(0,1001,"验证码输入错误");
					}
					req.setUserId(dto.getUserId());
					req.setAcctNo(creditCard.getCard_no());
					req.setTransAmt((long) (am * 100));
					req.setPhoneNo(creditCard.getPhoneno());
					req.setCustomerName(u.getRealname());
					req.setCerdType("01");
					req.setCerdId(u.getId_number());
					req.setCvn2(creditCard.getCvn2());
					req.setExpDate(creditCard.getExpdate());
				}else {       //取前端传过来的数据
					if (!api.checkShortMsg(dto.getPhoneNo(), dto.getCheckCode(), request)) {
						return JsonCreater.getJson(0,1001,"验证码输入错误");
					}
					req.setUserId(dto.getUserId());
					req.setAcctNo(dto.getCreditCardNo());
					req.setTransAmt((long) (am * 100));
					req.setPhoneNo(dto.getPhoneNo());
					req.setCustomerName(u.getRealname());
					req.setCerdType("01");
					req.setCerdId(u.getId_number());
					req.setCvn2(dto.getCvn2());
					req.setExpDate(dto.getExpdate());
				}
				// /调用支付接口
				PayResult result = payService.debitPurchase_recharge(req);
				if("P000".equals(result.getCode())
						|| "9997".equals(result.getCode())
						|| "9999".equals(result.getCode())){
					return JsonCreater.getJson(0,9997,"订单已提交，请留意余额变动");
				}else if (!"0000".equals(result.getCode())) {
					return JsonCreater.getJson(0,1002,"错误："+result.getMessage());
				}
				if (creditCard==null) {   //判断卡片是否被绑定 如果 绑过了就不给他绑定
					dto.setRealname(u.getRealname());
					dto.setCerdId(u.getId_number());
					creditCard = new CreditCard(dto);
					sql = M.getInsertSqlWhenFilesIsNotNull(creditCard);
					creditCardDao.insert_Personal(sql, creditCard);
				}
				return JsonCreater.getJsonOk();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return JsonCreater.getJson(0,1001,"充值金额格式错误");
			}catch (Exception ex) {
				ex.printStackTrace();
				return JsonCreater.getJson(0,1001,ex.toString());
			}
		} else {
			return JsonCreater.getJson(0,1001,"登录状态异常,请重新登录");
		}
	}

	@Override
	public QResponse seriWithdrawCash(RechargeDto dto,HttpServletRequest request) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			try {
				double am = Double.parseDouble(dto.getAmount());
				if (am <= 0) {
					return QResponse.newInstance(false, "申请提现金额不得小于0元");
				}
				if (am > 50000) {
					return QResponse.newInstance(false, "申请提现金额不得大于50000元");
				}
				Account a = getUserAccount(dto.getUserId());
				double ba = Double.parseDouble(a.getCur_balance());
				if (ba < am) {
					return QResponse.newInstance(false, "余额不足");
				} else {
					applyCashService.insertApplyCash(dto.getUserId(),
							dto.getAmount());
					seriTransToRecashFreeze(dto.getUserId(), am);
					return new QResponse();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return QResponse.newInstance(false, "申请提现金额格式错误");
			}
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

	/**
	 * 将提现申请金额冻结
	 * 
	 * @param userId
	 * @param am
	 * @return
	 */
	private void seriTransToRecashFreeze(String userId, double am) {
		String sql = "update t_s_user_account set cur_balance=(cur_balance-:am),recash_freeze=(IFNULL(recash_freeze,0)+:am) where user_id=:user_id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("am", am);
		map.put("user_id", userId);
		accountDao.update(sql, map);
	}

	@Override
	public QResponse getWithdrawCash(IdentityDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			List<ApplyCash> list = applyCashService.getApplyCashByUserid(dto
					.getUserId());
			return new QResponse(list);
		}
		return QResponse.ERROR_SECURITY;
	}

	/*
	 * 将用户账户减去一个值
	 */
	@Override
	public boolean subBalance(String userId, double am) {
		String sql = "update t_s_user_account set cur_balance=(cur_balance-:am) where (user_id=:user_id and cur_balance>=:am)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("am", am);
		map.put("user_id", userId);
		int i = accountDao.update(sql, map);
		if (i == 1)
			return true;
		return false;
	}
	
	/*
	 * 将用户账户加上一个值
	 */
	@Override
	public boolean addBalance(String userId, double am) {
		String sql = "update t_s_user_account set cur_balance=(cur_balance+:am) where user_id=:user_id ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("am", am);
		map.put("user_id", userId);
		int i = accountDao.update(sql, map);
		if (i == 1)
			return true;
		return false;
	}
}
