package com.dhk.entity;
import java.math.BigDecimal;

import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

 /**
 * t_s_trans_water 实体类<br/>
 * 2017-01-19 01:21:54 Gnaily
 */ 
@SuppressWarnings("serial")
@Table(name="t_s_trans_water")
public class TransWater  extends Entity {
	
	@Column(name="TRANS_DATE")
	private String transDate;

	@Column(name="TRANS_TIME")
	private String transTime;

	@Column(name="HOST_TRANS_DATE")
	private String hostTransDate;

	@Column(name="Host_TRANS_TIME")
	private String hostTransTime;

	@Column(name="TRANS_NO")
	private String transNo;

	@Column(name="CARD_NO")
	private String cardNo;

	@Column(name="ORG_ID")
	private Long orgId;

	@Column(name="USER_ID")
	private Long userId;

	@Column(name="TRANS_AMOUNT")
	private BigDecimal transAmount;

	@Column(name="TRANS_TYPE")
	private String transType;

	@Column(name="RESP_CODE")
	private String respCode;

	@Column(name="FEE")
	private BigDecimal fee;

	@Column(name="CARD_TYPE")
	private String cardType;

	@Column(name="RESP_RES")
	private String respRes;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="HOST_TRANS_NO")
	private String hostTransNo;

	@Column(name="EXTERNAL")
	private BigDecimal external;
	
	@Column(name="IS_ORG_RECAH")
	private String isOrgRecah;
	//附加字段
	@Column(name="QRCODE_ID")
	private Long qrcodeId;
		
	@Column(name="RELATION_NO")
	private String relationNo;
	
	@Column(name="PLAN_ID")
	private Long planId;

	 @Column(name="COST_ID")
	 private Long costId;
	
	@Column(name="PROXY_PAY_TYPE")
	private String proxyPayType;
	
	@Column(name="PROXY_PAY_CHANNEL")
	private String proxyPayChannel;

	private String again_order_no;
	private String history_resp_res;
	
	private String transactionType;

	private String userName;
	private String orgName;
	private String cardTypeText;
	private String trans_type;
	private String transTypeText;
	private String statusText;
	
	//附加字段
	@Column(name="TRANS_COUNT")
	private Integer transCount;
	
	private BigDecimal allTransAmount;

	 public String getTransactionType() {
		 return transactionType;
	 }

	 public void setTransactionType(String transactionType) {
		 this.transactionType = transactionType;
	 }

	 public Long getCostId() {
		 return costId;
	 }

	 public void setCostId(Long costId) {
		 this.costId = costId;
	 }

	 public Integer getTransCount() {
		return transCount;
	}
	public void setTransCount(Integer transCount) {
		this.transCount = transCount;
	}
	public BigDecimal getExternal() {
		return external;
	}
	public void setExternal(BigDecimal external) {
		this.external = external;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getRelationNo() {
		return relationNo;
	}
	public void setRelationNo(String relationNo) {
		this.relationNo = relationNo;
	}
	public Long getQrcodeId() {
		return qrcodeId;
	}
	public void setQrcodeId(Long qrcodeId) {
		this.qrcodeId = qrcodeId;
	}
	public String getRespRes() {
		return respRes;
	}
	public void setRespRes(String respRes) {
		this.respRes = respRes;
	}
	
	public void setTransDate(String transDate){
		this.transDate=transDate;
	}
	public String getTransDate(){
		return transDate;
	}
	
	public void setTransTime(String transTime){
		this.transTime=transTime;
	}
	public String getTransTime(){
		return transTime;
	}
	public void setHostTransDate(String hostTransDate){
		this.hostTransDate=hostTransDate;
	}
	public String getHostTransDate(){
		return hostTransDate;
	}
	public void setHostTransTime(String hostTransTime){
		this.hostTransTime=hostTransTime;
	}
	public String getHostTransTime(){
		return hostTransTime;
	}
	public void setTransNo(String transNo){
		this.transNo=transNo;
	}
	public String getTransNo(){
		return transNo;
	}
	public void setHostTransNo(String hostTransNo){
		this.hostTransNo=hostTransNo;
	}
	public String getHostTransNo(){
		return hostTransNo;
	}
	
	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}
	public String getCardNo(){
		return cardNo;
	}
	public void setOrgId(Long orgId){
		this.orgId=orgId;
	}
	public Long getOrgId(){
		return orgId;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public Long getUserId(){
		return userId;
	}
	public void setTransAmount(BigDecimal transAmount){
		this.transAmount=transAmount;
	}
	public BigDecimal getTransAmount(){
		return transAmount;
	}
	public void setTransType(String transType){
		this.transType=transType;
	}
	public String getTransType(){
		return transType;
	}
	public void setRespCode(String respCode){
		this.respCode=respCode;
	}
	public String getRespCode(){
		return respCode;
	}
	public void setFee(BigDecimal fee){
		this.fee=fee;
	}
	public BigDecimal getFee(){
		return fee;
	}
	public void setCardType(String cardType){
		this.cardType=cardType;
	}
	public String getCardType(){
		return cardType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCardTypeText() {
		return cardTypeText;
	}
	public void setCardTypeText(String cardTypeText) {
		this.cardTypeText = cardTypeText;
	}
	public String getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}
	public String getTransTypeText() {
		return transTypeText;
	}
	public void setTransTypeText(String transTypeText) {
		this.transTypeText = transTypeText;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public BigDecimal getAllTransAmount() {
		return allTransAmount;
	}
	public void setAllTransAmount(BigDecimal allTransAmount) {
		this.allTransAmount = allTransAmount;
	}
	public Long getPlanId() {
		return planId;
	}
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	public String getProxyPayType() {
		return proxyPayType;
	}
	public void setProxyPayType(String proxyPayType) {
		this.proxyPayType = proxyPayType;
	}
	public String getProxyPayChannel() {
		return proxyPayChannel;
	}
	public void setProxyPayChannel(String proxyPayChannel) {
		this.proxyPayChannel = proxyPayChannel;
	}

	 public String getIsOrgRecah() {
		 return isOrgRecah;
	 }

	 public void setIsOrgRecah(String isOrgRecah) {
		 this.isOrgRecah = isOrgRecah;
	 }

	public String getAgain_order_no() {
		return again_order_no;
	}

	public void setAgain_order_no(String again_order_no) {
		this.again_order_no = again_order_no;
	}

	public String getHistory_resp_res() {
		return history_resp_res;
	}

	public void setHistory_resp_res(String history_resp_res) {
		this.history_resp_res = history_resp_res;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	 
	 
 }

