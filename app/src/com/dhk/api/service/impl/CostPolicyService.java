package com.dhk.api.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dhk.api.dao.ICostPolicyDao;
import com.dhk.api.core.impl.CostPolicyDisp;
import com.dhk.api.dto.CostPlanDto;
import com.dhk.api.dto.DelCreditCarDto;
import org.springframework.stereotype.Service;

import com.dhk.api.dto.QResponse;
import com.dhk.api.entity.CostPlan;
import com.dhk.api.entity.CostPolicy;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.service.ICostPlanService;
import com.dhk.api.service.ICostPolicyService;
import com.dhk.api.service.IParamFeeService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.tool.M;

@Service("CostPolicyService")
public class CostPolicyService implements ICostPolicyService {

	@Resource(name = "CostPolicyDao")
	private ICostPolicyDao CostPolicyDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "CostPlanService")
	private ICostPlanService costPlanService;

	@Resource(name = "ParamFeeService")
	private IParamFeeService paramFeeService;

	@Override
	public QResponse insertCostPolicy(CostPlanDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			try {
				double transa = Double.parseDouble(dto.getTrans_amount());
				if (transa < 0) {
					return new QResponse(false, "消费金额错误");
				}
			} catch (NumberFormatException e2) {
				e2.printStackTrace();
				return new QResponse(false, "消费金额错误");
			}
			try {
				if (!checkTimeValueable(dto)) {
					return QResponse.newInstance(false, "开始时间必须从今天开始");
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
				return QResponse.newInstance(false, "时间格式错误");
			}
			CostPolicy o = new CostPolicy();
			o.setCard_no(dto.getCardNo());
			o.setUser_id(dto.getUserId());
			o.setTrans_amount(dto.getTrans_amount());
			o.setTrans_count(dto.getTrans_count());
			o.setPolicy_type(dto.getPolicy_type());
			o.setRepeat_bengin_date(dto.getRepeat_begindate());
			o.setRepeat_end_date(dto.getRepeat_enddate());
			o.setRepeat_mode(dto.getRepeat_mode());
			o.setRepeat_detail(dto.getRepeat_detail());
			String type = dto.getType();
			if ("1".equals(type)) {// 插入数据库类型
				String sql = M.getInsertSqlWhenFilesIsNotNull(o);
				Long pid = CostPolicyDao.insert(sql, o);
				o.setId(Long.parseLong("" + pid));
				ParamFee fee = paramFeeService.getUseParamByCode("purchase");
				CostPolicyDisp dis = new CostPolicyDisp(fee, o);
				List<CostPlan> l = null;
				try {
					l = dis.genDetailPolicy();
					double fees = dis.genExpense();
					QResponse s = costPlanService.insertCostPlan(l);
					Map<String, Object> map = new HashMap<String, Object>();
					if (fees >= 0) {
						map.put("fees", String.format("%.2f", fees));
						map.put("messageList", l);
					} else {
						map.put("fees", "0.00");
						map.put("messageList", l);
					}
					s.data = map;
					return s;
				} catch (ParseException e) {
					M.logger.debug(e);
					e.printStackTrace();
					return new QResponse(false, "时间格式错误");
				}
			} else {//解析消费计划
				ParamFee fee = paramFeeService.getUseParamByCode("purchase");
				CostPolicyDisp dis = new CostPolicyDisp(fee, o);
				List<CostPlan> l = null;
				try {
					l = dis.genDetailPolicy();
					double fees = dis.genExpense();
					Map<String, Object> map = new HashMap<String, Object>();
					if (fees >= 0) {
						map.put("fees", String.format("%.2f", fees));
						map.put("messageList", l);
					} else {
						map.put("fees", "0.00");
						map.put("messageList", l);
					}
					QResponse s = new QResponse();
					s.data = map;
					return s;
				} catch (ParseException e) {
					M.logger.debug(e);
					e.printStackTrace();
					return new QResponse(false, "时间格式错误");
				}
			}

		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

	/**
	 * 检查时间是否有效
	 * 
	 * @param dto
	 * @return
	 * @throws ParseException
	 */
	private boolean checkTimeValueable(CostPlanDto dto) throws ParseException {
		Calendar now = Calendar.getInstance();
		now.setTime(M.dformat.parse(M.dformat.format(new Date())));
		String start = dto.getRepeat_begindate();
		Calendar sta = Calendar.getInstance();
		sta.setTime(M.dformat.parse(start));
		if (now.compareTo(sta) <= 0) {
			return true;
		}
		return false;
	}

	@Override
	public QResponse getCostPlanList(DelCreditCarDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			return costPlanService.getCostPlanList(dto);
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

}
