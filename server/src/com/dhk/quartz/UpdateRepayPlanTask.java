package com.dhk.quartz;

import com.dhk.entity.APPUser;
import com.dhk.entity.RepayPlanDetail;
import com.dhk.payment.PayResult;
import com.dhk.service.IAPPUserService;
import com.dhk.service.IRepayPlanDetailService;
import com.dhk.service.ITransWaterService;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 更新还款计划状态，将今天之前的未还款的还款计划更改为失败
 * @author y1iag
 *
 */
public class UpdateRepayPlanTask {
	
	@Resource(name = "RepayPlanDetailService")
	private IRepayPlanDetailService repayPlanDetailService;
	@Resource(name = "TransWaterService")
	private ITransWaterService transWaterService;
	@Resource(name = "APPUserService")
	private IAPPUserService appUserService;

	public void runTask(){
		List<RepayPlanDetail> rpds=	repayPlanDetailService.unexecuteRepayBeforeToday();
		PayResult pr= PayResult.genCustomFailPayResult("还款失败-自动");
		BigDecimal zero=new BigDecimal("0");
		for(RepayPlanDetail rpd:rpds){
			long userId=rpd.getUserId();
			String cardNo=rpd.getCreditCardNo();

				BigDecimal amount=rpd.getRepayAmount();
				APPUser appUser=appUserService.findById(userId);
				rpd.setStatus("2");
				repayPlanDetailService.doUpdateStatus(rpd);
				String orderNo=appUserService.getOrderNo(appUser.getId(), appUser.getMobilephone());
				transWaterService.writeFastRepayTransWater(orderNo,appUser, cardNo,
						amount, zero, zero, rpd.getId(), null, null, pr);
		}
	}
	
	
}
