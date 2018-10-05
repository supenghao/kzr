package com.dhk.quartz;

import com.dhk.FeeInfo;
import com.dhk.entity.OrgRate;
import com.dhk.entity.TransWater;
import com.dhk.init.Constant;
import com.dhk.service.*;
import com.sunnada.kernel.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 运营商结算
 * @author L
 *
 */
public class SettleOperatorTask {

	@Resource(name="FeeParamService")
	private IFeeParamService feeParamService;
	
	@Resource(name = "SettleService")
	private ISettleService settleService;
	
	@Resource(name = "TransWaterService") 
	private ITransWaterService transWaterService;
	
	@Resource(name = "OrgRateService") 
	private IOrgRateService orgRateService;
	
	@Resource(name = "OrgService") 
	private IOrgService orgService;
	private static final Logger log= LogManager.getLogger();
	public void runTask() throws Exception  {
		try{
			String systemTime = StringUtil.getCurrentTime();
			String[] time = systemTime.split(" ");
			String nowDate = time[0];// 日期
			String[] date=nowDate.split("-");
			nowDate=date[0]+date[1]+date[2];
			BigDecimal profit = new BigDecimal("0");
			List<TransWater> transls=transWaterService.findByIsBusAndDate("F", nowDate);
			//交易类型(0:纯消费，1：还款消费，2：快速还款，3：普通还款,4:充值，5,：提现)
			BigDecimal allAmt = new BigDecimal("0");
			for (TransWater transWater : transls) {
				//取出交易类型
				String transType=transWater.getTrans_type();
				//取出交易金额
				BigDecimal amount=transWater.getTransAmount();
				//判断TRANS_TYPE
				if("1".equals(transType)){//0:纯消费，1：还款消费
					//拿出QRCODE_ID查询
					Long qrcodeId=transWater.getQrcodeId();
					//查询出一级代理商
					OrgRate orgRates=orgRateService.findByQrcodeIdAndMinId(qrcodeId);
					//取出代理商成本
					BigDecimal rate=orgRates.getRate();
					//计算成本
					FeeInfo feeInfo=feeParamService.computeFeeInfo(amount, "purchase", "purchase_cost");
					//取出运营商成本
					BigDecimal cost=feeInfo.getCost();

					//BigDecimal resultOper=Rate.subtract(cost);
					//计算最终结果（赚了多少钱）
					profit = amount.multiply((rate.divide(new BigDecimal("100")))).setScale(2, RoundingMode.HALF_UP);
					log.info("消费---代理商:"+orgRates.getOrgId()+ "消费："+amount +" 代理商成本价："+profit +" 平台商成本价："+cost +" 平台商赚："+profit.subtract(cost));
					profit=profit.subtract(cost);
				}else if("0".equals(transType)){
					//取出一级代理商费率
					BigDecimal rate=Constant.feeMaps.get("org_only_purchase_cost_1").getFee();
					//计算额外成本
					BigDecimal rate_external=Constant.feeMaps.get("org_only_purchase_cost_1").getExternal();
					//取出运营商费率
					BigDecimal cost=Constant.feeMaps.get("org_only_purchase_cost_0").getFee();
					//计算额外成本
					BigDecimal cost_external=Constant.feeMaps.get("org_only_purchase_cost_0").getExternal();
					//计算代理商成本
					BigDecimal	rateprofit = amount.multiply((rate.divide(new BigDecimal("100")))).setScale(2, RoundingMode.HALF_UP).add(rate_external);
					
					//计算运营商成本
					BigDecimal	costprofit = amount.multiply((cost.divide(new BigDecimal("100")))).setScale(2, RoundingMode.HALF_UP).add(cost_external);
					log.info("消费---代理商:"+transWater.getOrgId()+ "消费："+amount +" 代理商成本价："+rateprofit +" 平台商成本价："+costprofit +" 平台商赚："+rateprofit.subtract(costprofit));
					profit=rateprofit.subtract(costprofit);
				}else if("2".equals(transType)||"5".equals(transType)){
//					FeeInfo feeInfo;
//					if("2".equals(transType)||"5".equals(transType)){//2：快速还款  5：提现
						//计算成本
					FeeInfo feeInfo=feeParamService.computeFeeInfo(amount, "quickl_proxy_pay", "credit_quickl_proxy_pay_cost");
//					}else if("3".equals(transType)){//3：普通还款
//						//计算成本
//						feeInfo=feeParamService.computeFeeInfo(amount, "normal_proxy_pay", "credit_normal_proxy_pay_cost");
//					}else{//4:充值
//						//计算成本
//						feeInfo=feeParamService.computeFeeInfo(amount, "czhd", "recharge_cost");
//					}
					//取出成本
					BigDecimal cost=feeInfo.getCost();
					//额外手续费
					//BigDecimal external=feeInfo.getExternal();
					//手续费
					BigDecimal fee=feeInfo.getFee();
					log.info("还款---代理商:"+transWater.getOrgId()+ "还款："+amount +" 代理商成本价（固定手续费）："+fee +" 平台商成本价："+cost +" 平台商赚："+fee.subtract(cost));
					profit =fee.subtract(cost);
				}
				allAmt = allAmt.add(profit);
				/*
				//更新到运营商账户
				orgService.seriUpdateBalance(1L, profit);

				//将交易流水的运营商结算状态改为T
				transWaterService.updateIsBus(transWater.getId(), "T");
				*/
				settleService.txOperatoerSett(profit, transWater.getId());
				log.info("执行运营商结算成功");
			}
			log.info("本次结算："+allAmt);
		}catch (Exception e){
			log.error("执行运营商结算错误："+e.getMessage());
			log.error(e.toString());
			  e.printStackTrace();
		}

		
	}
	
}
