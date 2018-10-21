package com.dhk.service;
import com.dhk.entity.UserAccount;

import java.math.BigDecimal;

/**
    * t_s_user_account service 接口<br/>
    * 2017-01-09 09:04:57 Gnaily
    */ 
public interface IUserAccountService {
	
	/**
	 * 更新用户账户余额
	 * @param ua
	 * @return
	 */
	public  boolean  seriUpdateBalance(UserAccount ua);

	public  boolean  seriUpdateRecashFreeze(UserAccount ua);
	
	public  boolean seriUpdateBalanceAndRecashFreeze(UserAccount ua);
	
	public UserAccount findByUserId(Long userId);

	/**
	 * 添加用户金额
	 * @param freeze 金额
	 * @param date 日期
	 * @param time 时间
	 * @param userId 用户id
	 * @return
	 */
	public void upUserAccount(BigDecimal freeze, String date, String time, Long userId);

	/**
	 *
	 * @Title deductionRecashFreezeMoney
	 * @Description TODO // 扣除冻结金额
	 * @param freeze
	 * @param date
	 * @param time
	 * @param userId
	 * @return  Integer
	 * @author jaysonQiu
	 */
	public void doDuctionRecashFreezeMoney(BigDecimal freeze,String date,String time,Long userId);

	/**
	 *
	 * @Title disposeUserMoney
	 * @Description TODO //增加余额扣除冻结金额
	 * @param freeze
	 * @param date
	 * @param time
	 * @param userId
	 * @return  Integer
	 * @author jaysonQiu
	 */
	public void disposeUserMoney(BigDecimal freeze,String date,String time,Long userId);


}

