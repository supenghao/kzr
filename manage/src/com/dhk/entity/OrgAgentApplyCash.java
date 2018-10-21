package com.dhk.entity;

import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

import java.math.BigDecimal;

/**
 * t_s_org_apply_cash 实体类<br/>
 * 2017-02-14 07:32:17 bianzk
 */
@SuppressWarnings("serial")
@Table(name="t_s_org_apply_cash")
public class OrgAgentApplyCash extends Entity {
 private Long org_id;
 private String apply_date;
 private String apply_time;
 private String auth_date;
 private String auth_time;

 //统计成功金额
 private BigDecimal countUserSuccessAmount;

 public String getApply_time() {
     return apply_time;
 }
 public void setApply_time(String apply_time) {
     this.apply_time = apply_time;
 }
 public String getAuth_date() {
     return auth_date;
 }
 public void setAuth_date(String auth_date) {
     this.auth_date = auth_date;
 }
 public String getAuth_time() {
     return auth_time;
 }
 public void setAuth_time(String auth_time) {
     this.auth_time = auth_time;
 }
 private BigDecimal amount;
 private String status;
 private Long oper_id;
 private String refuse;
 private String orgName;
 private String operName;
 private String applyCashStatus;
 public Long getOrg_id() {
     return org_id;
 }
 public void setOrg_id(Long org_id) {
     this.org_id = org_id;
 }
 public String getApply_date() {
     return apply_date;
 }
 public void setApply_date(String apply_date) {
     this.apply_date = apply_date;
 }

 public BigDecimal getAmount() {
     return amount;
 }
 public void setAmount(BigDecimal amount) {
     this.amount = amount;
 }
 public String getStatus() {
     return status;
 }
 public void setStatus(String status) {
     this.status = status;
 }
 public Long getOper_id() {
     return oper_id;
 }
 public void setOper_id(Long oper_id) {
     this.oper_id = oper_id;
 }
 public String getRefuse() {
     return refuse;
 }
 public void setRefuse(String refuse) {
     this.refuse = refuse;
 }
 public String getOrgName() {
     return orgName;
 }
 public void setOrgName(String orgName) {
     this.orgName = orgName;
 }
 public String getOperName() {
     return operName;
 }
 public void setOperName(String operName) {
     this.operName = operName;
 }
 public String getApplyCashStatus() {
     return applyCashStatus;
 }
 public void setApplyCashStatus(String applyCashStatus) {
     this.applyCashStatus = applyCashStatus;
 }
 public BigDecimal getCountUserSuccessAmount() {
     return countUserSuccessAmount;
 }
 public void setCountUserSuccessAmount(BigDecimal countUserSuccessAmount) {
     this.countUserSuccessAmount = countUserSuccessAmount;
 }

}

