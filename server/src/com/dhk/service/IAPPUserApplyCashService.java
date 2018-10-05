package com.dhk.service;

import com.dhk.entity.APPUserApplyCash;
import com.sunnada.kernel.dao.Pager;

import java.math.BigDecimal;
import java.util.Map;

/**
 * t_s_user_apply_cash service 接口<br/>
 * 2017-02-14 07:34:53 bianzk
 */
public interface IAPPUserApplyCashService {
 public Pager<APPUserApplyCash> findByPager(int curPage, int pages, String param, Map<String, Object> paramMap) throws Exception;

 public APPUserApplyCash findById(Long id);

 public boolean updateStatus(APPUserApplyCash appUserApplyCash);
 public boolean updateStatus(Long id, String status );

 /**
  *
  * @param orgId
  * @param status
  * @return APPUserApplyCash
  * @author 邱灵杰
  * @comment 统计提现成功总额
  */
 public APPUserApplyCash countAgentApplyCash(Long orgId, String status);



 /**
  *
  * @Title upById
  * @Description TODO // 更新用户状态
  * @param CashId
  * @param Status
  * @param date
  * @param time
  * @param operId
  * @return  Integer
  * @author jaysonQiu
  */
 public void renewStatus(Long CashId, String Status, String date, String time, Long operId);
 public void renewStatus(Long CashId, String Status, String date, String time);

 /**
  * 根据用户ID、状态为3 金额相同的
  * @param userId
  * @param status
  * @param money
  * @return
  */
 public APPUserApplyCash findApplyId(Long userId, String status, BigDecimal money);
}

