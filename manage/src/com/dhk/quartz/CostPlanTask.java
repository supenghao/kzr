package com.dhk.quartz;

import com.dhk.entity.CostPlanDetail;
import com.dhk.entity.CreditCard;
import com.dhk.service.ICostPlanDetailService;
import com.dhk.service.ICreditCardCostService;
import com.dhk.service.ICreditCardService;
import com.sunnada.kernel.util.StringUtil;
import com.sunnada.uaas.service.ISystemParamService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 纯消费任务
 * @author y1iag
 *
 */
public class CostPlanTask {

	@Resource(name="CostPlanDetailService")
	private ICostPlanDetailService costPlanDetailService;

	@Resource(name="CreditCardService")
	private ICreditCardService creditCardService;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;

	@Resource(name = "CreditCardCostService")
	private ICreditCardCostService creditCardCostService;

	private final String  isCreditCardValid="1";

	public void runTask()  {

			String beginTime=systemParamService.findByParamName("trans_begin_time").getParam_text();
		String endTime=systemParamService.findByParamName("trasn_end_time").getParam_text();
		String currentTime = StringUtil.getCurrentDateTime("HH:mm:ss");
		if(currentTime.compareTo(beginTime)<=0 || currentTime.compareTo(endTime)>=0){
			return ;
		}
		List<CostPlanDetail> costPlanDetails=costPlanDetailService.getUnexecuteCostPlan();

		if(costPlanDetails==null || costPlanDetails.isEmpty()){
			return;
		}
		//执行消费任务
		for(CostPlanDetail cpd:costPlanDetails){

			String cardNo=cpd.getCardNo();
			CreditCard card= creditCardService.findByCardNo(cardNo);
			String curTime = StringUtil.getCurrentDateTime("HHmmss");
			if(card!=null && isCreditCardValid.equals(card.getCardStatus())
					&& cpd.getExecTime().compareTo(curTime)<=0){
				creditCardCostService.creditCost(cpd);
			}
		}
	}
	
	
	
}
