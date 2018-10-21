package com.dhk.api.service.impl;

import javax.annotation.Resource;

import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.dao.ILoanDao;
import com.dhk.api.dto.LoanDto;
import com.dhk.api.service.ILoanService;
import com.dhk.api.entity.Loan;
import org.springframework.stereotype.Service;

@Service("loanService")
public class LoanService implements ILoanService {

	@Resource(name = "loanDao")
	private ILoanDao loanDao;
	
	@Override
	public void cipcLoan(LoanDto dto){
		String sql = "insert into t_s_loan (loanType,userId,userName,idNo,phone,creditItem,assetsItem,applyDate,applyTime,cstatus,applyArea)"
				+ " values "
				+ "(:loanType,:userId,:userName,:idNo,:phone,:creditItem,:assetsItem,:applyDate,:applyTime,:cstatus,:applyArea)";
		Loan loan = new Loan();
		loan.setUserId(Long.parseLong(dto.getUserId()));
		loan.setLoanType("cipc");
		loan.setUserName(dto.getUserName());
		loan.setIdNo(dto.getIdNo());
		loan.setPhone(dto.getPhone());
		loan.setCreditItem(dto.getCreditItem());
		loan.setAssetsItem(dto.getAssetsItem());
		loan.setApplyDate(PlanTool.getCurrentDateTime("yyyy-MM-dd"));
		loan.setApplyTime(PlanTool.getCurrentDateTime("HH:mm:ss"));
		loan.setCstatus("0");
		loan.setApplyArea(dto.getApplyArea());
		// Map<String, Object> map = new HashMap<String, Object>();
		loanDao.insert(sql, loan);
	}
	
}
