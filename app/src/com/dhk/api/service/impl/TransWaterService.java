package com.dhk.api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhk.api.dao.ITransWaterDao;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.TransWaterDto;
import com.dhk.api.entity.APPUser;
import com.dhk.api.entity.TransWater;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.ITransWaterService;
import com.dhk.api.tool.DateTimeUtil;
import com.xdream.kernel.dao.Pager;
import com.xdream.kernel.sql.SQLConf;

@Service("TransWaterService")
public class TransWaterService implements ITransWaterService {

	@Resource(name = "TransWaterDao")
	private ITransWaterDao TransWaterDao;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Override
	public QResponse getTransWaterList(TransWaterDto dto) {
		boolean c = tokenService.checkToken(dto);
		if (c) {
			String keywordtype = dto.getKeywordsType();
			String pageSize = dto.getPageSize();
			String pageNum = dto.getPageNumber();
			if (keywordtype != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("USER_ID", dto.getUserId());
				String keyword = dto.getKeywords();
				keyword = (keyword == null) ? "else" : keyword;
				if (keyword != null) {
					String sql = "select * from t_s_trans_water where USER_ID=:USER_ID and RESP_CODE<>'init' ";
					if (keywordtype.equals("0")) {// 按卡类型:借记卡,信用卡
						sql += " and CARD_TYPE=:CARD_TYPE";
						map.put("CARD_TYPE", keyword);
						Pager<TransWater> pager = new Pager<TransWater>();
						int pageS = 2, pageN = 1;
						try {
							pageS = Integer.parseInt(pageSize);
							pageN = Integer.parseInt(pageNum);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return new QResponse(false,
									"pageSize or pageNum ERROR");
						}
						pager.setPageSize(pageS);
						pager.setCurPageNum(pageN);
						List<TransWater> ret = TransWaterDao
								.findForPage2OrderBy(sql, map, pager,
										"ORDER BY id desc").getLists();
						return new QResponse(ret);
					} else if (keywordtype.equals("1")) {// 按交易类型
						sql += " and TRANS_TYPE=:TRANS_TYPE";
						map.put("TRANS_TYPE", keyword);
						Pager<TransWater> pager = new Pager<TransWater>();
						int pageS = 2, pageN = 1;
						try {
							pageS = Integer.parseInt(pageSize);
							pageN = Integer.parseInt(pageNum);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return new QResponse(false,
									"pageSize or pageNum ERROR");
						}
						pager.setPageSize(pageS);
						pager.setCurPageNum(pageN);
						List<TransWater> ret = TransWaterDao
								.findForPage2OrderBy(sql, map, pager,
										"ORDER BY id desc").getLists();
						System.out.println(ret);
						return new QResponse(ret);
					} else if (keywordtype.equals("2")) {// 按全部交易
						Pager<TransWater> pager = new Pager<TransWater>();
						int pageS = 2, pageN = 1;
						try {
							pageS = Integer.parseInt(pageSize);
							pageN = Integer.parseInt(pageNum);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							return new QResponse(false,
									"pageSize or pageNum ERROR");
						}
						pager.setPageSize(pageS);
						pager.setCurPageNum(pageN);
						List<TransWater> ret = TransWaterDao
								.findForPage2OrderBy(sql, map, pager,
										"ORDER BY id desc").getLists();
						return new QResponse(ret);
					} else {
						return new QResponse(false, "未知keywordtype类型");
					}
				}
			} else {
				return new QResponse(false, "缺少keywordtype");
			}
		}
		return QResponse.ERROR_SECURITY;
	}
	
	
	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType){
		TransWater tw = new TransWater();
		tw.setResp_code("init");
		tw.setResp_res("待支付");
		tw.setCard_no(cardNo);
		tw.setOrg_id(user.getOrgId());
		tw.setUser_id(user.getId());
		tw.setTrans_amount(amount.setScale(2, RoundingMode.HALF_UP)+"");
		tw.setFee(fee+"");
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP)+"");
		tw.setQrcode_id(user.getQrcodeId());
		tw.setRelatioin_no(user.getRelationNo());
		tw.setIs_org_recah("0");
		tw.setTrans_type(transType);//快速还款
		tw.setCard_type("1");//信用卡
		tw.setPlan_id(planId);
		tw.setCost_id(costId);
		tw.setProxy_pay_channel("1");
		tw.setProxy_pay_type("");
		tw.setTrans_no(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHost_trans_no(transNo);
		tw.setHost_trans_date(date);
		tw.setHost_trans_time(time);
		tw.setTrans_date(date);
		tw.setTrans_time(time);
		
		return this.doInsert(tw);
	}
	
	public long addTransls(String transNo,APPUser user,String cardNo,BigDecimal amount,
			BigDecimal fee,BigDecimal external,Long planId,Long costId,String transType,String Resp_res){
		TransWater tw = new TransWater();
		tw.setResp_code("init");
		tw.setResp_res(Resp_res);
		tw.setCard_no(cardNo);
		tw.setOrg_id(user.getOrgId());
		tw.setUser_id(user.getId());
		tw.setTrans_amount(amount.setScale(2, RoundingMode.HALF_UP)+"");
		tw.setFee(fee+"");
		tw.setExternal(external.setScale(2, RoundingMode.HALF_UP)+"");
		tw.setQrcode_id(user.getQrcodeId());
		tw.setRelatioin_no(user.getRelationNo());
		tw.setIs_org_recah("0");
		tw.setTrans_type(transType);//境外交易
		tw.setCard_type("1");//信用卡
		tw.setPlan_id(planId);
		tw.setCost_id(costId);
		tw.setProxy_pay_channel("1");
		tw.setProxy_pay_type("");
		tw.setTrans_no(transNo);
		String date = DateTimeUtil.getNowDateTime("yyyyMMdd");
		String time = DateTimeUtil.getNowDateTime("HHmmss");
		tw.setHost_trans_no(transNo);
		tw.setHost_trans_date(date);
		tw.setHost_trans_time(time);
		tw.setTrans_date(date);
		tw.setTrans_time(time);
		
		return this.doInsert(tw);
	}
	
	public long doInsert(TransWater tw) {
		
		String sql=SQLConf.getSql("transwater", "insert");
		return TransWaterDao.insert(sql, tw);
	}
}
