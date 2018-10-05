package com.dhk.service;

import com.dhk.entity.OrgAgentApplyCash;

import java.math.BigDecimal;

/**
 * t_s_org_apply_cash service 接口<br/>
 * 2017-02-14 07:32:17 bianzk
 */
public interface IOrgAgentApplyCashService {


 public boolean updateStatus(String status,String id);



 /**
  *
  * @Title orgByIdUpdateStatus
  * @Description TODO //更新代理商状态
  * @param orgId
  * @param Status
  * @param date
  * @param time
  * @return  Integer
  * @author jaysonQiu
  */
 public Integer orgByIdUpdateStatus(Long orgId, String Status, String date, String time);

 /**
  * 机构ID、状态3、相同金额
  * @param orgId
  * @param status
  * @param money
  * @return
  */
 public OrgAgentApplyCash findApplyId(Long orgId, String status, BigDecimal money);

 public OrgAgentApplyCash findApplyId(String applyId);
}

