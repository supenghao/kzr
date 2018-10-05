package com.dhk.entity;
import com.sunnada.kernel.dao.jdbc.Column;
import com.sunnada.kernel.dao.jdbc.Table;
import com.sunnada.kernel.entity.Entity;

   /**
    * t_s_user 实体类<br/>
    * 2017-02-10 10:45:16 Gnaily
    */ 
@SuppressWarnings("serial")
@Table(name="t_s_user")
public class APPUser  extends Entity {
	

	@Column(name="ORG_ID")
	private Long orgId;

	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	@Column(name="MOBILEPHONE")
	private String mobilephone;

	@Column(name="ID_NUMBER")
	private String idNumber;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="REALNAME")
	private String realname;

	@Column(name="SIGNATURE")
	private String signature;

	@Column(name="STATUS")
	private String status;

	@Column(name="USERNAME")
	private String username;

	@Column(name="ID_FRONT_URL")
	private String idFrontUrl;

	@Column(name="ID_OPPOSITE_URL")
	private String idOppositeUrl;

	@Column(name="BANK_PIC_URL")
	private String bankPicUrl;

	@Column(name="USER_LEVEL")
	private String userLevel;

	@Column(name="CARD_NO")
	private String cardNo;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="USER_EMAIL")
	private String userEmail;

	@Column(name="TERMINAL_CODE")
	private String terminalCode;

	@Column(name="SEX")
	private String sex;

	@Column(name="PROVINCE_CODE")
	private String provinceCode;

	@Column(name="CITY_CODE")
	private String cityCode;

	@Column(name="DISTRICT_CODE")
	private String districtCode;

	@Column(name="USER_STREET")
	private String userStreet;

	@Column(name="HEAD_PIC_URL")
	private String headPicUrl;

	@Column(name="NICK_NAME")
	private String nickName;

	@Column(name="IS_AUTH")
	private String isAuth;

	@Column(name="QRCODE_ID")
	private Long qrcodeId;

	@Column(name="RELATION_NO")
	private String relationNo;

    @Column(name="MERCHANID")
	private String merchantid;

    @Column(name="ybl_merchantid")
	private String ybl_merchantid;

    @Column(name="kj_merno")
    private String kjMerno;
    
    @Column(name="kj_key")
    private String kjKey;
	//认证状态
	private String authStatus;
	private String statusText;
	//邀请码
	private String invitationCode;

	   public String getMerchantid() {
		   return merchantid;
	   }

	   public void setMerchantid(String merchantid) {
		   this.merchantid = merchantid;
	   }

	   public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	

	public void setMobilephone(String mobilephone){
		this.mobilephone=mobilephone;
	}
	public String getMobilephone(){
		return mobilephone;
	}
	public void setIdNumber(String idNumber){
		this.idNumber=idNumber;
	}
	public String getIdNumber(){
		return idNumber;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public String getPassword(){
		return password;
	}
	public void setRealname(String realname){
		this.realname=realname;
	}
	public String getRealname(){
		return realname;
	}
	public void setSignature(String signature){
		this.signature=signature;
	}
	public String getSignature(){
		return signature;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setUsername(String username){
		this.username=username;
	}
	public String getUsername(){
		return username;
	}
	public void setIdFrontUrl(String idFrontUrl){
		this.idFrontUrl=idFrontUrl;
	}
	public String getIdFrontUrl(){
		return idFrontUrl;
	}
	public void setIdOppositeUrl(String idOppositeUrl){
		this.idOppositeUrl=idOppositeUrl;
	}
	public String getIdOppositeUrl(){
		return idOppositeUrl;
	}
	public void setBankPicUrl(String bankPicUrl){
		this.bankPicUrl=bankPicUrl;
	}
	public String getBankPicUrl(){
		return bankPicUrl;
	}
	public void setUserLevel(String userLevel){
		this.userLevel=userLevel;
	}
	public String getUserLevel(){
		return userLevel;
	}
	public void setCardNo(String cardNo){
		this.cardNo=cardNo;
	}
	public String getCardNo(){
		return cardNo;
	}
	public void setBankName(String bankName){
		this.bankName=bankName;
	}
	public String getBankName(){
		return bankName;
	}
	public void setUserEmail(String userEmail){
		this.userEmail=userEmail;
	}
	public String getUserEmail(){
		return userEmail;
	}
	public void setTerminalCode(String terminalCode){
		this.terminalCode=terminalCode;
	}
	public String getTerminalCode(){
		return terminalCode;
	}
	public void setSex(String sex){
		this.sex=sex;
	}
	public String getSex(){
		return sex;
	}
	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}
	public String getProvinceCode(){
		return provinceCode;
	}
	public void setCityCode(String cityCode){
		this.cityCode=cityCode;
	}
	public String getCityCode(){
		return cityCode;
	}
	public void setDistrictCode(String districtCode){
		this.districtCode=districtCode;
	}
	public String getDistrictCode(){
		return districtCode;
	}
	public void setUserStreet(String userStreet){
		this.userStreet=userStreet;
	}
	public String getUserStreet(){
		return userStreet;
	}
	public void setHeadPicUrl(String headPicUrl){
		this.headPicUrl=headPicUrl;
	}
	public String getHeadPicUrl(){
		return headPicUrl;
	}
	public void setNickName(String nickName){
		this.nickName=nickName;
	}
	public String getNickName(){
		return nickName;
	}
	public void setIsAuth(String isAuth){
		this.isAuth=isAuth;
	}
	public String getIsAuth(){
		return isAuth;
	}
	public void setQrcodeId(Long qrcodeId){
		this.qrcodeId=qrcodeId;
	}
	public Long getQrcodeId(){
		return qrcodeId;
	}
	public void setRelationNo(String relationNo){
		this.relationNo=relationNo;
	}
	public String getRelationNo(){
		return relationNo;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getYbl_merchantid() {
		return ybl_merchantid;
	}
	public void setYbl_merchantid(String ybl_merchantid) {
		this.ybl_merchantid = ybl_merchantid;
	}
	public String getKjMerno() {
		return kjMerno;
	}
	public void setKjMerno(String kjMerno) {
		this.kjMerno = kjMerno;
	}
	public String getKjKey() {
		return kjKey;
	}
	public void setKjKey(String kjKey) {
		this.kjKey = kjKey;
	}
	@Override
	public String toString() {
		return "APPUser [orgId=" + orgId + ", mobilephone=" + mobilephone
				+ ", idNumber=" + idNumber + ", password=" + password
				+ ", realname=" + realname + ", signature=" + signature
				+ ", status=" + status + ", username=" + username
				+ ", idFrontUrl=" + idFrontUrl + ", idOppositeUrl="
				+ idOppositeUrl + ", bankPicUrl=" + bankPicUrl + ", userLevel="
				+ userLevel + ", cardNo=" + cardNo + ", bankName=" + bankName
				+ ", userEmail=" + userEmail + ", terminalCode=" + terminalCode
				+ ", sex=" + sex + ", provinceCode=" + provinceCode
				+ ", cityCode=" + cityCode + ", districtCode=" + districtCode
				+ ", userStreet=" + userStreet + ", headPicUrl=" + headPicUrl
				+ ", nickName=" + nickName + ", isAuth=" + isAuth
				+ ", qrcodeId=" + qrcodeId + ", relationNo=" + relationNo
				+ ", merchantid=" + merchantid + ", ybl_merchantid="
				+ ybl_merchantid + ", kjMerno=" + kjMerno + ", kjKey=" + kjKey
				+ ", authStatus=" + authStatus + ", statusText=" + statusText
				+ ", invitationCode=" + invitationCode + "]";
	}

	
}

