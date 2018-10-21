package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.core.impl.PlanTool;
import org.springframework.stereotype.Service;

import com.dhk.api.dao.IPinganLoanDao;
import com.dhk.api.dto.PinganLoanDto;
import com.dhk.api.entity.PinganLoan;
import com.dhk.api.service.IPinganLoanService;

@Service("pinganLoanService")
public class PinganLoanService implements IPinganLoanService{

	@Resource(name = "pinganLoanDao")
	private IPinganLoanDao pinganLoanDao;
	
	@Override
	public void loan2e(PinganLoanDto dto){
		String sql = "insert into t_s_pingan_loan (loanType,userId,userName,sex,phone,houseAddr,houseArea,userMemo,landNature,applyDate,applyTime,cstatus)"
				+ " values "
				+ "(:loanType,:userId,:userName,:sex,:phone,:houseAddr,:houseArea,:userMemo,:landNature,:applyDate,:applyTime,:cstatus)";
		PinganLoan loan = new PinganLoan();
		loan.setUserId(Long.parseLong(dto.getUserId()));
		loan.setLoanType("E");
		loan.setUserName(dto.getUserName());
		loan.setPhone(dto.getPhone());
		loan.setSex(dto.getSex());
		loan.setHouseAddr(dto.getHouseAddr());
		loan.setHouseArea(dto.getHouseArea());
		loan.setUserMemo(dto.getUserMemo());
		loan.setLandNature(dto.getLandNature());		
		loan.setApplyDate(PlanTool.getCurrentDateTime("yyyy-MM-dd"));
		loan.setApplyTime(PlanTool.getCurrentDateTime("HH:mm:ss"));
		loan.setCstatus("0");
		pinganLoanDao.insert(sql, loan);
	}
	@Override
	public void loan2i(PinganLoanDto dto){
		String sql = "insert into t_s_pingan_loan (loanType,userId,userName,sex,phone,workAddr,insuranceAmount,years,yearsPayed,applyDate,applyTime,cstatus)"
				+ " values "
				+ "(:loanType,:userId,:userName,:sex,:phone,:workAddr,:insuranceAmount,:years,:yearsPayed,:applyDate,:applyTime,:cstatus)";
		PinganLoan loan = new PinganLoan();
		loan.setUserId(Long.parseLong(dto.getUserId()));
		loan.setLoanType("I");
		loan.setUserName(dto.getUserName());
		loan.setPhone(dto.getPhone());
		loan.setSex(dto.getSex());
		loan.setWorkAddr(dto.getWorkAddr());
		loan.setInsuranceAmount(dto.getInsuranceAmount());
		loan.setYears(dto.getYears());
		loan.setYearsPayed(dto.getYearsPayed());
		loan.setApplyDate(PlanTool.getCurrentDateTime("yyyy-MM-dd"));
		loan.setApplyTime(PlanTool.getCurrentDateTime("HH:mm:ss"));
		loan.setCstatus("0");
		pinganLoanDao.insert(sql, loan);
	}
}
