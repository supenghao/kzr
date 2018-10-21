package com.dhk.api.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.RepaytemPolicyFee;
import com.dhk.api.dao.IRepayPlanTemDao;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.Account;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.entity.RepayPlan;
import com.dhk.api.entity.RepayPlanTem;
import com.dhk.api.service.IAccountService;
import com.dhk.api.service.IParamFeeService;
import com.dhk.api.service.IRepayPlanTemService;
import com.dhk.api.service.ITokenService;
import com.dhk.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RepayPlanTemService")
public class RepayPlanTemService implements IRepayPlanTemService {

	@Resource(name = "RepayPlanTemDao")
	private IRepayPlanTemDao repayPlanTemDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "ParamFeeService")
	private IParamFeeService paramFeeService;

	@Resource(name = "AccountService")
	private IAccountService accountService;

	@Autowired
	RedisUtils redisUtils;
	/**
	 * 获取有效的临时表数据
	 * */
	@Override
	public JSONArray txgetValueableTemList(RepayPlanDto dto) {
		if (tokenService.checkToken(dto)) {
			String key =  "repayplan_tem_"+dto.getUserId()+"_"+dto.getCardNo();
			JSONArray jSONArray =JSONArray.parseArray(redisUtils.get(key));
			return jSONArray!=null?jSONArray:new JSONArray();
		}
		return new JSONArray();
	}

	/*
	 * 清除已经插入到还款计划的临时表
	 */
	@Override
	public void clear(String userId,String creditCarNo) {
		redisUtils.del("repayplan_tem_"+userId+"_"+creditCarNo);
	}

	@Override
	public void insertRepayPlanList(List<RepayPlan> ps) {
		/**
		String userid = "";
		String creditCarNo = "";
		JSONArray ret = new JSONArray();
		Long currentTimeMillis = System.currentTimeMillis(); //用时间戳当id
		int i = 0;
		for (RepayPlan p : ps) {
			RepayPlanTem e = new RepayPlanTem();
			e.setCard_no(p.getCredit_card_no());
			userid = p.getUser_id();
			creditCarNo = p.getCredit_card_no();
			e.setUser_id(userid);
			e.setPolicy_type(p.getPolicy_type());
			e.setRepay_amount(p.getRepay_amount());
			e.setRepay_day(p.getRepay_day());
			e.setId(currentTimeMillis+i);
			i++;
			ret.add(e);
		}

		String key =  "repayplan_tem_"+userid+"_"+creditCarNo;

		redisUtils.set(key,ret.toString(),7200);   //2个小时
		 **/
		String userid =ps.get(0).getUser_id();
		String creditCarNo = ps.get(0).getCredit_card_no();


		String key =  "repayplan_tem_"+userid+"_"+creditCarNo;

		redisUtils.set(key,(JSONArray.toJSON(ps)).toString(),7200);   //2个小时
	}

	@Override
	public QResponse txRemoveRepayTemByid(RepayPlanDto dto) {
		if (tokenService.checkToken(dto)) {
			if (dto.getRepaytemid() == null) {
				return QResponse.newInstance(false, "ID不能为空");
			}
			String key= "repayplan_tem_"+dto.getUserId()+"_"+dto.getCardNo();
			JSONArray jsonArray= JSONArray.parseArray(redisUtils.get(key));

			for(int i=0;i<jsonArray.size();i++){
				JSONObject tem =jsonArray.getJSONObject(i);
				if(tem.getString("id").equals(dto.getRepaytemid())){
					jsonArray.remove(i);
					break;
				}
			}

			redisUtils.set(key,jsonArray.toJSONString(),7200);   //2个小时
			return QResponse.OK;
		}
		return QResponse.ERROR_SECURITY;
	}

	/*
	 * 计算用户临时表中计划的费率等信息
	 */
	@Override
	public QResponse getRepayPlanTemfee(RepayPlanDto dto) {
		if (tokenService.checkToken(dto)) {
			FeeReturnBeen ret = new FeeReturnBeen();
			JSONArray li = txgetValueableTemList(dto);
			List<ParamFee> fees = paramFeeService.getAllUseParam("0");
			RepaytemPolicyFee ca = new RepaytemPolicyFee(li, fees);
			//ret.setFee(ca.getfees() + "");
			ret.setFee(ca.getDatefees() + "");  //资金不过夜模式
			ret.setPaybill(ca.getTotalAmount() + "");
			//ret.setRequire(ca.getRequire() + "");
			ret.setRequire(ca.getDateRequire() + ""); //资金不过夜模式

			Account t = accountService.getUserAccount(dto.getUserId());
			ret.setBalance(t.getCur_balance());
			double bac = Double.parseDouble(t.getCur_balance());
			double requ = ca.getRequire();
			String diff = "0";
			if (bac < requ) {
				diff = (requ - bac) + "";
			}
			ret.setDiff(diff);
			ret.setRepayPlanArray(li);
			return new QResponse(ret);
		}
		return QResponse.ERROR_SECURITY;
	}

	public class FeeReturnBeen {

		String balance;
		String fee;// 手续费
		String paybill;// 本次计划总还款金额
		String require;// 需要金额的最低比率
		String diff;// 还差的余额
		String bankPayFee;
		JSONArray repayPlanArray;

		public JSONArray getRepayPlanArray() {
			return repayPlanArray;
		}

		public void setRepayPlanArray(JSONArray repayPlanArray) {
			this.repayPlanArray = repayPlanArray;
		}

		public String getBankPayFee() {
			return bankPayFee;
		}

		public void setBankPayFee(String bankPayFee) {
			this.bankPayFee = bankPayFee;
		}

		public String getBalance() {
			return balance;
		}

		public void setBalance(String balance) {
			this.balance = balance;
		}

		public String getDiff() {
			return diff;
		}

		public void setDiff(String diff) {
			this.diff = diff;
		}

		public String getFee() {
			return fee;
		}

		public void setFee(String fee) {
			this.fee = fee;
		}

		public String getPaybill() {
			return paybill;
		}

		public void setPaybill(String paybill) {
			this.paybill = paybill;
		}

		public String getRequire() {
			return require;
		}

		public void setRequire(String require) {
			this.require = require;
		}

	}

}
