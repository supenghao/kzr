package com.dhk.api.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dhk.api.core.impl.CreditBillLogic;
import com.dhk.api.core.impl.PlanTool;
import com.dhk.api.core.impl.RepayPolicyDisp;
import com.dhk.api.dao.IRepayPlanDao;
import com.dhk.api.dto.IdentityDto;
import com.dhk.api.dto.QResponse;
import com.dhk.api.dto.RepayPlanDto;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.entity.CreditcardBill;
import com.dhk.api.entity.ParamFee;
import com.dhk.api.entity.RepayCost;
import com.dhk.api.entity.RepayPlan;
import com.dhk.api.service.IAccountService;
import com.dhk.api.service.ICreditCardService;
import com.dhk.api.service.ICreditcardBillService;
import com.dhk.api.service.IParamFeeService;
import com.dhk.api.service.IPayService;
import com.dhk.api.service.IRepayCostService;
import com.dhk.api.service.IRepayPlanService;
import com.dhk.api.service.IRepayPlanTemService;
import com.dhk.api.service.IRepayRecordService;
import com.dhk.api.service.ISystemParamService;
import com.dhk.api.service.ITokenService;
import com.dhk.api.service.IUserService;
import com.dhk.api.service.impl.RepayPlanTemService.FeeReturnBeen;
import com.dhk.api.tool.M;
import com.dhk.api.tool.OrderNoUtil;
import com.dhk.redis.RedisUtils;
import com.xdream.kernel.util.StringUtil;

import redis.clients.jedis.Jedis;

@Service("RepayPlanService")
public class RepayPlanService implements IRepayPlanService {

	@Resource(name = "RepayPlanDao")
	private IRepayPlanDao repayPlanDao;

	@Resource(name = "CreditCardService")
	private ICreditCardService creditCarService;

	@Resource(name = "TokenService")
	private ITokenService tokenService;

	@Resource(name = "CreditcardBillService")
	private ICreditcardBillService creditcardBillService;

	@Resource(name = "AccountService")
	private IAccountService accountService;

	@Resource(name = "RepayPlanTemService")
	private IRepayPlanTemService repayPlanTemService;

	@Resource(name = "RepayRecordService")
	private IRepayRecordService repayRecordService;

	@Resource(name = "UserService")
	private IUserService userService;

	@Resource(name = "PayService")
	private IPayService payService;

	@Resource(name = "systemParamService")
	private ISystemParamService systemParamService;
	protected static Logger logger = Logger.getLogger(RepayPlanService.class);
	@Autowired
	RedisUtils redisUtils;

	@Resource(name = "RepayCostService")
	private IRepayCostService repayCostService;

	/**
	 * 引入取费率的
	 */
	@Resource(name = "ParamFeeService")
	private IParamFeeService paramFeeService;

	/*
	 * 解析还款计划并增加到临时表
	 */
	@Override
	public QResponse genRepayPlansNew(RepayPlanDto dto, HttpServletRequest request) throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			repayPlanTemService.clear(dto.getUserId(),dto.getCardNo());
			CreditCard card = creditCarService.getCreditCarInfo(userId, cardNo);
			if (card == null) {
				return new QResponse(false, "未绑定该卡");
			}
			if(StringUtils.isEmpty(dto.getTotal_amount())){
				return new QResponse(false, "还款金额不能为空");
			}
			if(StringUtils.isEmpty(dto.getPay_amount())){
				return new QResponse(false, "保证金不能为空");
			}



			int totalAmount=Integer.parseInt(dto.getTotal_amount());  //总金额
			int payAmount=new BigDecimal(dto.getPay_amount()).intValue();  //启动金额
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");



			if(Integer.parseInt(startDate)<Integer.parseInt(StringUtil.getCurrentDateTime("yyyyMMdd"))){
				return new QResponse(false, "开始日期不能小于当天");
			}


			if(StringUtil.getCurrentDateTime("yyyyMMdd").equals(startDate)){
				String exec_repay_end_time=systemParamService.findParam("exec_repay_end_time");  //计划最晚执行时间
				String submit_repay_end_time = PlanTool.computeTime(exec_repay_end_time,-3600*6-600);  //提前6个小时  保证任务有充分的时间执行
				String currentTime = StringUtil.getCurrentDateTime("HH:mm:ss");
				if(currentTime.compareTo(submit_repay_end_time)>=0){
					return new QResponse(false, "当天的计划请在"+submit_repay_end_time+"点前提交");
				}
			}


			if(payAmount*10<totalAmount){
				return new QResponse(false, "启动金额至少为还款总额的十分之一");
			}

			if(payAmount>totalAmount-10){
				return new QResponse(false, "启动金额不能大于"+(totalAmount-10));
			}

			if(payAmount<500){
				return new QResponse(false, "启动金额不得小于500");
			}
			if(payAmount>20000){
				return new QResponse(false, "启动金额不得大于20000");
			}


			//计算还款笔数
			int count= (int)Math.round((((double)totalAmount/(double)payAmount)*1.5));


			SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.setTime(sdf.parse(startDate));
			Calendar calendarEnd = Calendar.getInstance();
			calendarEnd.setTime(sdf.parse(endDate));


			//获取两个日期相隔多少天
			long between_days=(calendarEnd.getTimeInMillis()-calendarStart.getTimeInMillis())/(1000*3600*24)+1;
			if(between_days>count||between_days<(Math.ceil((double) count/3))){
				return new QResponse(false, "还款周期请设置到"+((int)Math.ceil((double) count/3))+"到"+count+"天");
			}

			//获取随机分布的金额
			double[] randomMoney = getRandomMoney(totalAmount,payAmount,count);


			List<RepayPlan> listd  = new ArrayList();
			//获得要还款的日期

			for (int i=0;i<count;i++){
				if(calendarStart.compareTo(calendarEnd)>0){
					calendarStart.setTime(sdf.parse(startDate));
				}
				RepayPlan repayPlan=new RepayPlan();
				repayPlan.setRepay_amount(randomMoney[i]);
				repayPlan.setRepay_day(sdf.format(calendarStart.getTime()));
				repayPlan.setCredit_card_no(cardNo);
				repayPlan.setStatus("0");
				repayPlan.setUser_id(userId);
				listd.add(repayPlan);

				calendarStart.add(Calendar.DAY_OF_YEAR, 1);
			}
			// 按日期排序
			Collections.sort(listd, new Comparator<RepayPlan>() {
				@Override
				public int compare(RepayPlan o1, RepayPlan o2) {
					return o1.getRepay_day().compareTo(o2.getRepay_day());
				}
			});

			genRepayTime(listd );
			repayPlanTemService.clear(userId,cardNo);
			repayPlanTemService.insertRepayPlanList(listd);
			// 插入还款计划的消费计划列表

			return new QResponse(listd);
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

	/**
	 * 生成还款时间
	 * @param listd
	 * @return
	 * @throws Exception
	 */
	private List genRepayTime(List<RepayPlan> listd ) throws Exception{

		String exec_repay_begin_time = systemParamService.findParam("exec_repay_begin_time");  //07:00:00
		String exec_repay_end_time = systemParamService.findParam("exec_repay_end_time");  //23:00:00
		String currentDate = StringUtil.getCurrentDateTime("yyyyMMdd");
		Map<String,List<String>> execTimes2Day = new HashMap<String,List<String>>();

		for (RepayPlan rep : listd) {
			String exetime = "";
			String repayDate = rep.getRepay_day();   //还款时间
			int count = getrepayPlanCountByDay(listd,repayDate);  //获取当天还款总比数
			List<String>  execTimeList = execTimes2Day.get(repayDate);//已经提交的还款时间
			String currentTime =  StringUtil.getCurrentDateTime("HH:mm:ss");
			String beginTime = getBeginTime(execTimeList,exec_repay_begin_time,currentDate.equals(repayDate),currentTime);
			String endTime = getEndTime(count,execTimeList ,exec_repay_end_time);

			if(Integer.parseInt(beginTime.replaceAll(":",""))>Integer.parseInt(endTime.replaceAll(":",""))){//接近临界值的时候  开始时间可能会比结束时间大   ，因为开始时间有加了5分钟  ，防止zset那边处理订单的时候把订单误删除了
				endTime= beginTime;
			}
			//System.out.println("beginTime:"+beginTime+"|endTime："+endTime);
			exetime=PlanTool.genRandomTime(beginTime, endTime);

			rep.setExec_time(exetime);// 设置执行时间

			if (execTimes2Day.get(repayDate)!=null){
				List<String> execTimes = execTimes2Day.get(repayDate);
				execTimes.add(exetime);
			}else{
				List<String> execTimes = new ArrayList<String>();
				execTimes.add(exetime);
				execTimes2Day.put(repayDate, execTimes);
			}
		}

		genRepayCostMoney(listd);

		return listd;
	}
	
	private List genRepayTimeOnDate(List<RepayPlan> listd) throws Exception  {

		ParamFee hkfee = paramFeeService.findBy("quickl_proxy_pay");
		String feehk = String.valueOf(hkfee.getFee()); //还款费率
		ParamFee xffee = paramFeeService.findBy("purchase");
		String  feexf = String.valueOf(xffee.getFee()); //消费费率
				
		String exec_repay_begin_time = "08:30:00";  //08:00:00
		String exec_repay_end_time = "21:30:00";  //23:00:00
		String currentDate = StringUtil.getCurrentDateTime("yyyyMMdd");
		Map<String,List<String>> execTimes2Day = new HashMap<String,List<String>>();

		for (RepayPlan rep : listd) {
			String exetime = "";
			String repayDate = rep.getRepay_day();   //还款时间
			int count = getrepayPlanCountByDay(listd,repayDate);  //获取当天还款总比数
			List<String>  execTimeList = execTimes2Day.get(repayDate);//已经提交的还款时间
			String currentTime =  StringUtil.getCurrentDateTime("HH:mm:ss");
			String beginTime = getBeginTime(execTimeList,exec_repay_begin_time,currentDate.equals(repayDate),currentTime);
			String endTime = getEndTime(count,execTimeList ,exec_repay_end_time);

			if(Integer.parseInt(beginTime.replaceAll(":",""))>Integer.parseInt(endTime.replaceAll(":",""))){//接近临界值的时候  开始时间可能会比结束时间大   ，因为开始时间有加了5分钟  ，防止zset那边处理订单的时候把订单误删除了
				endTime= beginTime;
			}
			//System.out.println("beginTime:"+beginTime+"|endTime："+endTime);
			exetime=PlanTool.genRandomTime(beginTime, endTime);
			rep.setExec_time(exetime);// 设置执行时间
			
			if (execTimes2Day.get(repayDate)!=null){
				List<String> execTimes = execTimes2Day.get(repayDate);
				execTimes.add(exetime);
			}else{
				List<String> execTimes = new ArrayList<String>();
				execTimes.add(exetime);
				execTimes2Day.put(repayDate, execTimes);
			}
		}
       
		//计算消费对应的还款金额			
		for(int i=0;i<listd.size();i++){
			RepayPlan rep = listd.get(i);

			double hkAmt = rep.getRepay_amount(); //还款金额
			ArrayList<Double> listTmp = new ArrayList<Double>();
      /*  	if(hkAmt>2000){//大于2000的拆成2单
        		int percentage = getRandomInt(40,80);
        		double tmp = hkAmt*percentage/100;
        		listTmp.add(tmp);
        		listTmp.add(hkAmt-tmp);
        	}else*/
        		listTmp.add(hkAmt);
        	String hkTime = rep.getExec_time(); //还款时间
        	String hkDate =  rep.getRepay_day(); //还款日期
        	String defultTime = "08:03:00";
        	String begin = hkDate +" "+defultTime;
        	if(currentDate.equals(hkDate)){ //当天的话
        		
        		String currentTime =  StringUtil.getCurrentDateTime("HH:mm:ss");
        	     currentTime= PlanTool.compareTime(currentTime,defultTime)>0?currentTime:defultTime; //超过7点就取当前时间
        		 begin = hkDate +" "+currentTime;
        	}
        	//hkTime = dateBefore(hkTime, 600000);
        	if(i>0){ //第二次之后 要判断跟上一次是否是同一天
        		RepayPlan repBef = listd.get(i-1);
        		String hkTimeBef = repBef.getExec_time();//上次还款时间
	        	String hkDateBef =  repBef.getRepay_day(); //上次还款日期
	        	if(hkDateBef.equals(hkDate))
	        		begin = hkDate + " "+hkTimeBef;
        	}
        	List<RepayCost> list = new ArrayList<RepayCost>();
        	for(int j=0;j<listTmp.size();j++){
        		double xftmp = listTmp.get(j);
        		//System.out.println(begin);
        		//System.out.println(hkDate+" "+hkTime);
        		//begin = dateAfter(begin, 600000);
        		hkTime = PlanTool. computeTime(hkTime,-1800);//随机时间范围在还款时间前30分钟 因为通道交易结果可能将近30分钟才返回
         		String xfTime = randomDate(begin, hkDate+" "+hkTime);
        		BigDecimal xftmp_b = new BigDecimal(xftmp);
        		if(j==0) 
        			xftmp_b = xftmp_b.add(new BigDecimal(feehk)); //加上还款费率
        		//BigDecimal y = new BigDecimal(1).subtract(new BigDecimal(feexf));
        		xftmp_b = xftmp_b.divide((new BigDecimal(1).subtract(new BigDecimal(feexf).divide(new BigDecimal(100)))),2,BigDecimal.ROUND_UP);//.setScale(2,BigDecimal.ROUND_UP);//保留两位小数直接进位
        		RepayCost cost = new RepayCost();
        		begin = xfTime;
    			cost.setExec_time(xfTime.substring(9));
    			//System.out.println("消费金额为:"+xftmp_b.toPlainString());
    			Double tmp =Double.parseDouble(xftmp_b.toPlainString());
    			//System.out.println("消费金额为2:"+tmp);
    			cost.setCost_amount(tmp);
    			list.add(cost);
        	}
        	rep.setRepayCostList(list);
		
		}
		
		
		//genRepayCostMoney(listd);
		System.out.println(listd);
		return listd;
	}


	/**
	 * 生成还款消费金额
	 * @param listd
	 * @return
	 * @throws Exception
	 */
	private List genRepayCostMoney(List<RepayPlan> listd ) throws Exception{


		//1.消费总额不能大于还款总额
		//2.消费总额不能低于（还款总额+保证金）

		int repaySum=0;
		int repayCostSum=listd.get(0).getRepay_amount().intValue();  //第一笔还款金额等于保证金
		int differenceMoney=0;//总还款跟总消费的差值
		

		for (int i=0;i<listd.size();i++){
			RepayPlan repayPlan=listd.get(i);

			String nextExecTime = null;

			if(i!=listd.size()-1) {
				RepayPlan nextPlan = listd.get(i+1);
				if(repayPlan.getRepay_day().equals(nextPlan.getRepay_day())){
					nextExecTime = nextPlan.getExec_time();
				}

			}
			int costMoney =0;

			repaySum+=repayPlan.getRepay_amount();


			if(i==listd.size()-1){ //最后一笔
				costMoney=differenceMoney+ repayPlan.getRepay_amount().intValue();
			}else{
				double temMax= repayPlan.getRepay_amount().intValue()+differenceMoney;  //消费总额不能大于还款总额（如果大于此数，信用卡的额度可能不够）
				double temMin= repaySum+listd.get(i+1).getRepay_amount()-repayCostSum;   //x+保证金+ repayCostSum>= repaySum+下一笔还款金额    (如果小于此数 ，保证金的金额会不够)
				//todo   temMin不得小鱼最大金额的百分70
				if(temMin<temMax*0.7){
					temMin=temMax*0.7;
				}
				Random random =	new Random();
				costMoney =random.nextInt((int)temMax - (int)temMin+1) + (int)temMin;

				differenceMoney=  (int)temMax- costMoney ;
			}
			repayCostSum+=costMoney;

			List<RepayCost> repayCostList = genRepayCostTime(repayPlan.getExec_time(),nextExecTime,costMoney);
			repayPlan.setRepayCostList(repayCostList);
		}
		return listd;
	}

	/**
	 * 生成还款消费时间
	 * @return
	 * @throws Exception
	 */
	private List<RepayCost> genRepayCostTime(String repayExecTime, String nextRepayExecTime, int costMoney){
		long beginTimeStamp = PlanTool.getTimeStamp(repayExecTime,"HH:mm:ss",600000);   //1000*60*10  10分钟
		long endTimeStamp;
		if(nextRepayExecTime==null){
			endTimeStamp =  PlanTool.getTimeStamp("22:25:00","HH:mm:ss",0);
		}else{
			endTimeStamp =  PlanTool.getTimeStamp(nextRepayExecTime,"HH:mm:ss",-600000);  //1000*60*10  10分钟
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		List<RepayCost>list=new ArrayList<>();
		//拆分金额
		Integer caiMon = Integer.parseInt(systemParamService.findParam("api_repay_slip_money"));
		if (costMoney >= caiMon) {  //要拆分成2笔    2笔之间要间隔半小时
			//获取开始时间 和结束时间的中间值
			long middle = beginTimeStamp+(endTimeStamp- beginTimeStamp)/2;  //比如 beginTimeStamp=11：00：00   endTimeStamp=14：00：00    middle就等于12：30
			//前后加上15分钟，就是间隔半小时了
			String firstExecTime = sdf.format(ThreadLocalRandom.current().nextLong(beginTimeStamp,middle-900000));      //1000*60*15
			String secondExecTime = sdf.format(ThreadLocalRandom.current().nextLong(middle+900000,endTimeStamp));      //1000*60*15

			Random random = new Random();
			int i =random.nextInt(7)+2;
			int firstAmount = costMoney/10*i;
			int secondAmount = costMoney-firstAmount;
			RepayCost cost = new RepayCost();
			cost.setExec_time(firstExecTime);
			cost.setCost_amount((double)firstAmount);
			list.add(cost);
			RepayCost cost2 = new RepayCost();
			cost2.setExec_time(secondExecTime);
			cost2.setCost_amount((double)secondAmount);
			list.add(cost2);

		}else{
			String execTime = sdf.format(ThreadLocalRandom.current().nextLong(beginTimeStamp,endTimeStamp));
			RepayCost cost = new RepayCost();
			cost.setExec_time(execTime);
			cost.setCost_amount((double)costMoney);
			list.add(cost);
		}
		return list;
	}


	/**
	 * 1.第一笔等于保证金
	 * 2.获取平均金额，其余的在平均金额到保证金之间浮动
	 * @param totalMoney
	 * @param payAmount
	 * @param count
	 * @return
	 */
	private double[] getRandomMoney(int totalMoney,int payAmount,int count){


		double [] dou = new double[count];
		dou[0]= payAmount;//第一笔要等于保证金
		int  average= (totalMoney-payAmount)/(count-1);
		int syje= totalMoney-payAmount- average*(count-1);


		//随大一笔金额
		int  maxMoney=  syje +average;
		//计算随机数
		Random random = new Random();

		int randomInt = 0;
		boolean flag=true;
		for (int i=1;i<count;i++){
			if(i==count-1&&count%2==0){    //如果是最后一笔，并且总数是偶数
				dou[i] = average;
			}else if(i%2!=0){
				randomInt=random.nextInt(payAmount- maxMoney+1);
				dou[i] = average+randomInt;
			}else{
				dou[i] = average-randomInt;
			}

			if(flag&&dou[i]+syje<=payAmount){
				dou[i]+=syje;
				flag=false;
			}
		}
		return  dou;
	}
	



	private final String geterSql = "select REPAY_DAY,POLICY_TYPE,REPAY_MONTH from t_n_repay_plan where (credit_card_no=:credit_card_no and USER_ID=:user_id and repay_month=:repay_month and status='0')";

	private List<RepayPlan> getRepayPlans(RepayPlanDto dto, CreditCard card) {
		CreditcardBill bill = CreditBillLogic.disposeBillDate(card);
		String repay_month = bill.getBill_day().substring(0, 6);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("repay_month", repay_month);
		map.put("user_id", dto.getUserId());
		map.put("credit_card_no", card.getCard_no());
		List<RepayPlan> ret = repayPlanDao.find(geterSql, map);
		return ret;
	}

	public List<RepayPlan> findByCardNo4User(String cardNo,String userId){
		String sql = "select * from t_n_repay_plan where credit_card_no=:credit_card_no and USER_ID=:user_id and (status='0' or status='1')";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", userId);
		map.put("credit_card_no", cardNo);
		List<RepayPlan> ret = repayPlanDao.find(sql, map);

		return ret;
	}

	/*
	 * cardNo,userId,token
	 */
	@Transactional
	public QResponse insertRepayPlans(RepayPlanDto dto)  throws Exception{
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			if (cardNo == null) {
				return new QResponse(false, "卡号不能为空");
			}
			CreditCard card = creditCarService.getCreditCarInfo(userId, cardNo);
			if (card == null) {
				return new QResponse(false, "未绑定该卡");
			}


			QResponse li = repayPlanTemService.getRepayPlanTemfee(dto);
			String require = "0";
			String fee = "0";// 手续费
			double totalPay ;// 手续费
			if (li.state) {
				FeeReturnBeen tbe = (FeeReturnBeen) li.data;
				require = tbe.getRequire(); //保证金
				fee=  tbe.getFee();  //手续费
				totalPay=  Double.parseDouble(require)+Double.parseDouble(fee);
			} else {
				M.logger.error("费率计算失败");
				return new QResponse(false, "支付失败");
			}
			JSONArray jsonArray = repayPlanTemService.txgetValueableTemList(dto);
			List<RepayPlan> ilist = new LinkedList<RepayPlan>();
			String time = StringUtil.getCurrentDateTime("HHmmssS");
			String orderNo = OrderNoUtil.genOrderNo(time,32);


			boolean subBalance = accountService.subBalance(userId, totalPay);
			if (!subBalance) {
				M.logger.error("ERROR:subBalance 余额不足!!!");
				throw new RuntimeException("余额不足");
			}
			//插入t_n_repay_record
			String currentMonth = StringUtil.getCurrentDateTime("yyyyMM");

			String startDate=jsonArray.getJSONObject(0).getString("repay_day");
			String endDate= jsonArray.getJSONObject(jsonArray.size()-1).getString("repay_day");

			int repay_money=0;

			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				repay_money+=jsonObject.getDouble("repay_amount");
			}

			long recordId = repayRecordService.doReRepayRecode(userId,
					currentMonth, jsonArray.size() + "", card.getCard_no(), totalPay+"",fee,startDate,endDate,repay_money+"",orderNo);

			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				jsonObject.put("record_id",recordId);
			}
			insertRepayPlan(jsonArray.toJavaList(RepayPlan.class));
			repayPlanTemService.clear(dto.getUserId(),dto.getCardNo());
			return new QResponse();
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}





	/**
	 * 获得还款逻辑对象
	 *
	 * @param dto
	 * @param card
	 * @return
	 */
	private RepayPolicyDisp getRepayPolicyDisp(RepayPlanDto dto, CreditCard card) {

		RepayPolicyDisp dis = null;
		try {
			dis = new RepayPolicyDisp(card, dto);
		} catch (ParseException e) {
			e.printStackTrace();
			M.logger.error("账单时间解析发生错误", e);
		}
		return dis;
	}

	@Override
	public QResponse getRepayPlanList(IdentityDto dto, String repayRecordId) {
		String userId = dto.getUserId();
		String token = dto.getToken();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {

			String sql = "select * from t_n_repay_plan where record_id=:record_id and user_id=:user_id";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", userId);
			map.put("record_id", repayRecordId);
			List<RepayPlan> repayPlanList = repayPlanDao.find(sql, map);
			List<RepayCost> repayCostList = repayCostService.findByRecordId(Long.parseLong(repayRecordId));
			for (RepayPlan repayPlan : repayPlanList ) {
				long repayId = repayPlan.getId();
				List<RepayCost> list=repayPlan.getRepayCostList();
				if(list==null){
					list=new ArrayList<>();
				}
				for (RepayCost repayCost:repayCostList) {
					 if(repayId==repayCost.getRepay_plan_id().longValue()){
						 list.add(repayCost);
					 }
				}
				repayPlan.setRepayCostList(list);
			}

			return new QResponse(repayPlanList);
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}

	@Override
	public List<RepayPlan> findUserRepayPlan(String cardNo, String isRun,Long userId) {
		String sql = "select * from t_n_repay_plan where credit_card_no=:card_no and user_id=:user_id and IS_RUN=:IS_RUN";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_no", cardNo);
		map.put("IS_RUN", isRun);
		map.put("user_id", userId);
		List<RepayPlan> ret = repayPlanDao.find(sql, map);
		return ret;
	}


	/**
	 * 插入还款计划
	 *
	 * @param listd
	 */
	@Transactional
	private void insertRepayPlan(List<RepayPlan> listd) throws Exception{
		Jedis jedis = null;
		try{
			jedis = redisUtils.getJedis();;
			for (RepayPlan rep : listd) {
				String repayDate = rep.getRepay_day();   //还款时间
				String insertsql = "insert into t_n_repay_plan(credit_card_no,status,USER_ID,REPAY_AMOUNT,POLICY_TYPE,REPAY_DAY,repay_month,exec_time,orderNo,record_id) values(:credit_card_no,:status,:user_id,:repay_amount,:policy_type,:repay_day,:repay_month,:exec_time,:orderNo,:record_id)";
				long id = repayPlanDao.insert(insertsql, rep);
				rep.setId(id);
				if(id>0){
					jedis.zadd("repayPlanZset_"+repayDate, Long.parseLong(rep.getExec_time().replaceAll(":","")),  JSONObject.toJSON(rep).toString());
				}
				List<RepayCost> repayCostList = rep.getRepayCostList();
				for (RepayCost repayCost:repayCostList ) {
					repayCostService.insertRepayCost(repayCost.getCost_amount(),repayCost.getExec_time(),id,repayDate);
				}
			}
			 /**
			int repaySum=0;//排除第一笔还款
			int repayCostSum=0;
			int differenceMoney=0;

			for (int i=0;i<listd.size();i++){
				RepayPlan repayPlan=listd.get(i);

				String nextExecTime = null;

				if(i!=listd.size()-1) {
					RepayPlan nextPlan = listd.get(i+1);
					if(repayPlan.getRepay_day().equals(nextPlan.getRepay_day())){
						nextExecTime = nextPlan.getExec_time();
					}

				}
				int costMoney =0;
				if(i==listd.size()-1){ //最后一笔
					costMoney=differenceMoney+ repayPlan.getRepay_amount().intValue();
				}else{
					double temMax= repayPlan.getRepay_amount().intValue()+differenceMoney;
					double temMin= repaySum+listd.get(i+1).getRepay_amount()-repayCostSum;
					//todo   temMin不得小鱼最大金额的百分70
					if(temMin<temMax*0.7){
						temMin=temMax*0.7;
					}


					Random random =	new Random();
					costMoney =random.nextInt((int)temMax - (int)temMin+1) + (int)temMin;
					differenceMoney=  (int)temMax- costMoney ;
				}
				repayCostSum+=costMoney;
				if(i!=0){
					repaySum+=repayPlan.getRepay_amount();
				}
				repayCostService.insertRepayCost(repayPlan.getId(),repayPlan.getExec_time(), nextExecTime,costMoney,repayPlan.getRepay_day());
			}
			System.out.println("111111111111:"+repayCostSum);
			**/
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (jedis!=null){
				jedis.close();
			}
		}

	}



	private int getrepayPlanCountByDay(List<RepayPlan> listd,String repayDay){
		int count = 0;
		for (RepayPlan rep : listd) {
			if(rep.getRepay_day().equals(repayDay)){
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取开始时间
	 * 1.要考虑上一个任务的时间
	 * 2.尽量平均分布时间
	 * isCurrentDay   是否当天
	 *
	 * @return
	 */
	private String getBeginTime(List<String> execTimes,String defultBeginTime,boolean isCurrentDay,String currentTime) throws ParseException {


		String lastExetime;
		if(execTimes==null){  //代表是第一笔
			if(isCurrentDay){
				String  beginTime= PlanTool.compareTime(currentTime,defultBeginTime)>0?currentTime:defultBeginTime; //超过7点就取当前时间
				return  PlanTool.computeTime(beginTime,300);  //往后推迟5分钟，保证zset那边处理订单的时候不会把订单误删除了
			}else{
				return defultBeginTime;
			}
		}else{
			lastExetime= execTimes.get(execTimes.size()-1);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = sdf.parse(lastExetime);
		long l=date.getTime()+7200000;    //离上一个任务至少要间隔2个小时
		return sdf.format(l);
	}
	/**
	 * 获取结束时间 2个任务时间至少要间隔2个小时
	 * count 当天所有任务数量 （最少一个，最多3个）
	 * index  当前第几个任务，1开始
	 * lastEndTime  s上一个任务结束时间
	 * endTime      set_plan_end_time的时间  23：00：00
	 * @throws ParseException
	 */
	private String getEndTime(int count,List<String> execTimes,String endTime) throws ParseException {
		int index;
		if(execTimes==null){
			index=1;
		}else {
			index=execTimes.size()+1;
		}

		if(count==1){
			return endTime;
		}else if(count==2){
			if(index==1){
				endTime = "18:29:29";
			}else{
				return endTime;
			}
		}else if(count==3){
			if(index==1){
				//endTime = PlanTool.computeTime(endTime,-3600*6-1+10); // 16:59:59 因为后面还有2个任务，所有至少要16:59:59点前结束,提交当天订单最迟时间是16：59：59，所以要+10防止开始时间比结束时间大
				endTime = "15:29:29";
			}else if(index==2){
				endTime = "18:29:29";
			}else{
				return endTime;
			}
		}
		return endTime;
	}



	/**
	 * 根据redis中存的对象来获得平均分布的执行时间
	 * @param date
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private String randomTime(Jedis jedis,String date, String beginTime, String endTime,boolean isCurrentDay,String currentTime) throws Exception {
		String redisKey="randomhour_"+date;
		Map<String,String> map = jedis.hgetAll(redisKey);
		if(map.size()==0){//如果不存在 就順便給他初始化
			Map temMap = new HashMap();
			for(int i=7;i<22;i++){
				if(i<10){
					temMap.put("0"+i,"0");
				}else{
					temMap.put(i+"","0");
				}

			}
			jedis.hmset(redisKey,temMap);
			map = jedis.hgetAll(redisKey);
		}

		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		if(isCurrentDay){//如果是当天，就给他加上权重
			//7:00-16:59  的毫秒数是 是35999000         把他分成60份   每份599983，权重递减
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			long weight = (sdf.parse(currentTime).getTime() -sdf.parse("07:00:00").getTime())/ 599983;
			for(Map.Entry<String, String> entry:list){
				Integer key=Integer.parseInt(entry.getKey());
				Integer value = Integer.parseInt(entry.getValue());
				entry.setValue(value-(23-key)*80-weight+"");
			}

		}

		//排序
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				if(o1.getValue().equals(o2.getValue())) {
					return Integer.parseInt(o1.getKey())-Integer.parseInt(o2.getKey());
				}else{
					return Integer.parseInt(o1.getValue())-Integer.parseInt(o2.getValue());
				}
			}
		});

		String beginHour = beginTime.substring(0,2);
		String endHour =  endTime.substring(0,2);
		List<String> temList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			String redisHour =list.get(i).getKey();  //最小的小时
			if(Integer.parseInt(beginHour)<=Integer.parseInt(redisHour)&&
					Integer.parseInt(redisHour)<=Integer.parseInt(endHour)){//取符合条件的前三名，然后在根据这个前五名进行随机，要不然每次取第一名会出现很规律的时间   比如  12：15   14：32   16：33
				if(temList.size()<3){
					temList.add(redisHour);
				} else{
					break;
				}
			}
		}
		if(temList.size()==0){
			throw new RuntimeException("list中没数据，这不科学");
		}

		String redisHour =  temList.get(ThreadLocalRandom.current().nextInt(0, temList.size()));

		if(Integer.parseInt(beginTime.replaceAll(":",""))>Integer.parseInt(beginTime.replaceAll(":",""))){//接近临界值的时候  开始时间可能会比结束时间大   ，因为开始时间有加了5分钟  ，防止zset那边处理订单的时候把订单误删除了
			endTime= beginTime;
		}

		if(beginHour.equals(endHour)){
			return PlanTool.genRandomTime(beginTime, endTime);
		}else if(beginHour.equals(redisHour)){
			return PlanTool.genRandomTime(beginTime, redisHour+":59:59");
		}else if(endHour.equals(redisHour)){
			return PlanTool.genRandomTime(redisHour+":00:00",endTime);
		}else{
			return PlanTool.genRandomTime(redisHour+":00:00",redisHour+":59:59");
		}

	}

	public static void main(String[]args){
	/*	List<RepayPlan> listd = new ArrayList<>();
		RepayPlan repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(840.00);
		repayPlan.setRepay_day("20180101");
		listd.add(repayPlan);
		repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(780.00);
		repayPlan.setRepay_day("20180101");
		listd.add(repayPlan);
		repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(458.00);
		repayPlan.setRepay_day("20180101");
		listd.add(repayPlan);

		repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(792.00);
		repayPlan.setRepay_day("20180102");
		listd.add(repayPlan);

		repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(300.00);
		repayPlan.setRepay_day("20180102");
		listd.add(repayPlan);

		repayPlan = new RepayPlan();
		repayPlan.setRepay_amount(590.00);
		repayPlan.setRepay_day("20180102");
		listd.add(repayPlan);*/

		 RepayPlanService repayPlanService=new RepayPlanService();
		 System.out.println(repayPlanService.getRandomMoneyNew(10000, 1000,15));
		/*try {
			repayPlanService.genRepayCostMoney(listd);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * 资金不过夜模式
	 */
	@Override
	public QResponse genRepayPlansOneDate(RepayPlanDto dto, HttpServletRequest request) throws Exception {

		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			repayPlanTemService.clear(dto.getUserId(),dto.getCardNo());
			CreditCard card = creditCarService.getCreditCarInfo(userId, cardNo);
			if (card == null) {
				return new QResponse(false, "未绑定该卡");
			}
			if(StringUtils.isEmpty(dto.getTotal_amount())){
				return new QResponse(false, "还款金额不能为空");
			}
			/*if(StringUtils.isEmpty(dto.getPay_amount())){
				return new QResponse(false, "保证金不能为空");
			}
*/
			Map<String,String> limit = PlanTool.getBankLimitMap(card.getBank_name());
			double singleLimit = 0;//单笔限额
			double singleDayLimit = 0;//每日限额
			logger.info(card.getBank_name()+"获取限额："+limit);
            if(limit!=null){
            	singleLimit = NumberUtils.toDouble(limit.get("singleLimit"), 0) ;
            	singleDayLimit = NumberUtils.toDouble(limit.get("singleDayLimit"), 0) ;
            }
		
			int totalAmount=Integer.parseInt(dto.getTotal_amount());  //总金额
			int payAmount=new BigDecimal(dto.getPay_amount()).intValue();  //启动金额
			if(singleLimit!=0 && payAmount>singleLimit){
				return new QResponse(false, "启动金额超出 "+card.getBank_name()+" 单笔限额消费"+singleLimit);
			}
			if(singleDayLimit!=0 && payAmount>singleDayLimit){
				return new QResponse(false, "启动金额超出 "+card.getBank_name()+" 每日限额消费"+singleLimit);
			}
			if(payAmount==0)
				payAmount = (int) (totalAmount*0.1);//启动金额为空取还款金额百分10
			if(payAmount<500)
				payAmount = 500;//启动金额不足500取500
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");



			if(Integer.parseInt(startDate)<Integer.parseInt(StringUtil.getCurrentDateTime("yyyyMMdd"))){
				return new QResponse(false, "开始日期不能小于当天");
			}


			if(StringUtil.getCurrentDateTime("yyyyMMdd").equals(startDate)){
				String exec_repay_end_time=systemParamService.findParam("exec_repay_end_time");  //计划最晚执行时间
				String submit_repay_end_time = PlanTool.computeTime(exec_repay_end_time,-3600*6-600);  //提前6个小时  保证任务有充分的时间执行
				String currentTime = StringUtil.getCurrentDateTime("HH:mm:ss");
				if(currentTime.compareTo(submit_repay_end_time)>=0){
					return new QResponse(false, "当天的计划请在"+submit_repay_end_time+"点前提交");
				}
			}


			/*if(payAmount*10<totalAmount){
				return new QResponse(false, "启动金额至少为还款总额的十分之一");
			}

			if(payAmount>totalAmount-10){
				return new QResponse(false, "启动金额不能大于"+(totalAmount-10));
			}

			if(payAmount<500){
				return new QResponse(false, "启动金额不得小于500");
			}
			if(payAmount>20000){
				return new QResponse(false, "启动金额不得大于20000");
			}
			*/

			//计算还款笔数
			int count= (int)Math.round((((double)totalAmount/(double)payAmount)*1.5));


			SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.setTime(sdf.parse(startDate));
			Calendar calendarEnd = Calendar.getInstance();
			calendarEnd.setTime(sdf.parse(endDate));


			//获取两个日期相隔多少天
			long between_days=(calendarEnd.getTimeInMillis()-calendarStart.getTimeInMillis())/(1000*3600*24)+1;
			if(between_days>count||between_days<(Math.ceil((double) count/3))){
				return new QResponse(false, "还款周期请设置到"+((int)Math.ceil((double) count/3))+"到"+count+"天");
			}

			//获取随机分布的金额
			//double[] randomMoney = getRandomMoney(totalAmount,payAmount,count);
			ParamFee xffee = paramFeeService.findBy("purchase");
			String  feexf = String.valueOf(xffee.getFee()); //消费费率
			ParamFee hkfee = paramFeeService.findBy("quickl_proxy_pay");
			String feehk = String.valueOf(hkfee.getFee()); //还款费率
			BigDecimal syje_b = new BigDecimal(0); //首笔金额
			syje_b = new BigDecimal(payAmount).subtract(new BigDecimal(payAmount).multiply(new BigDecimal(feexf).divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_UP)).subtract(new BigDecimal(feehk));
			List<Double> listintnew = getRandomMoneyNew(totalAmount,syje_b.intValue(),count);

			List<RepayPlan> listd  = new ArrayList();
			//获得要还款的日期
           Map<String,Double> dayTotalAmount = new HashMap<String, Double>();
			for (int i=0;i<count;i++){
				if(calendarStart.compareTo(calendarEnd)>0){
					calendarStart.setTime(sdf.parse(startDate));
				}
				if(singleLimit>0 && singleLimit<listintnew.get(i)){
					return new QResponse(false, "该计划会存在超出 "+card.getBank_name()+" 单笔限额消费"+singleLimit+"，请重新设置");
				}
				String repay_day = sdf.format(calendarStart.getTime());
				double daytotalamt = dayTotalAmount.get(repay_day)==null ? listintnew.get(i) : dayTotalAmount.get(repay_day)+listintnew.get(i);
				if(singleDayLimit>0 && daytotalamt>singleDayLimit){
					return new QResponse(false, "该计划会存在超出 "+card.getBank_name()+" 每日限额消费"+singleDayLimit+"，请重新设置");
				}else{
					dayTotalAmount.put(repay_day, daytotalamt);
				}
				RepayPlan repayPlan=new RepayPlan();
				repayPlan.setRepay_amount(listintnew.get(i));
				repayPlan.setRepay_day(repay_day);
				repayPlan.setCredit_card_no(cardNo);
				repayPlan.setStatus("0");
				repayPlan.setUser_id(userId);
				listd.add(repayPlan);
				calendarStart.add(Calendar.DAY_OF_YEAR, 1);
			}
			// 按日期排序
			Collections.sort(listd, new Comparator<RepayPlan>() {
				@Override
				public int compare(RepayPlan o1, RepayPlan o2) {
					return o1.getRepay_day().compareTo(o2.getRepay_day());
				}
			});

			//genRepayTime(listd );
			genRepayTimeOnDate(listd );
			repayPlanTemService.clear(userId,cardNo);
			repayPlanTemService.insertRepayPlanList(listd);
			// 插入还款计划的消费计划列表

			return new QResponse(listd);
		} else {
			return QResponse.ERROR_SECURITY;
		}
	
	}


	private List<Double> getRandomMoneyNew(int allMoney,int payAmount, int allCount) {

		int subMoney = allMoney-payAmount;
		int subCount = allCount-1;
		int average = subMoney/subCount;//平均每笔还款
        Integer randomInt = 0;
        int allMoneyTmp = 0;
        int randomMoney = 0;
        List<Integer> listint = new ArrayList<Integer>();
        for(int i=1;i<=subCount;i++){//循环所有笔数-1次  统计金额 
        	 randomInt = getRandomInt(20,80);
             randomMoney = average*randomInt/100;//平均金额随机百分20 -百分80
             listint.add(randomMoney);
             allMoneyTmp = allMoneyTmp + randomMoney;//统计已经计算的金额
        }
        int subMoneyTmp = subMoney -allMoneyTmp;//最后一笔金额
        int averageTmp = subMoneyTmp/subCount;//最后一笔整数部分平均到每笔里面
		int ysTmp = subMoneyTmp%subCount;//最后一笔尾数部分平局后余数算到最后一笔里
		List<Double> listintnew  = new ArrayList<Double>();
		double all = 0;
		all = all +payAmount;
		listintnew.add(Double.parseDouble(payAmount+""));
		for(int i=0;i<listint.size();i++){
			double tmp  = listint.get(i);
			tmp = tmp + averageTmp;
			if(i==listint.size()-1)
				tmp  = tmp  + ysTmp;
			listintnew.add(tmp);
			all = all + tmp;
		}
		System.out.println("总数为:"+all);
		
		/*if(i==count-1&&count%2==0){    //如果是最后一笔，并且总数是偶数
			dou[i] = average;
		}else if(i%2!=0){
			randomInt=random.nextInt(payAmount- maxMoney+1);
			dou[i] = average+randomInt;
		}else{
			dou[i] = average-randomInt;
		}*/
		
		return listintnew;
	}

	public static int getRandomInt(int min,int max){
        Random random = new Random();
		int  randomInt = random.nextInt(max)%(max-min+1) + min;
		return randomInt;

	}
	
	private static String randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date start = format.parse(beginDate);// 开始日期
            Date end = format.parse(endDate);// 结束日期
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	 private static long random(long begin, long end) {
	        long rtnn = begin + (long) (Math.random() * (end - begin));
	        if (rtnn == begin || rtnn == end) {
	            return random(begin, end);
	        }
	        return rtnn;
	 }

	@Override
	public QResponse insertRepayDatePlans(RepayPlanDto dto) throws Exception {
		String userId = dto.getUserId();
		String token = dto.getToken();
		String cardNo = dto.getCardNo();
		boolean c = tokenService.checkToken(userId, token);
		if (c) {
			if (cardNo == null) {
				return new QResponse(false, "卡号不能为空");
			}
			CreditCard card = creditCarService.getCreditCarInfo(userId, cardNo);
			if (card == null) {
				return new QResponse(false, "未绑定该卡");
			}


			QResponse li = repayPlanTemService.getRepayPlanTemfee(dto);
			String require = "0";
			String fee = "0";// 手续费
			double totalPay ;// 手续费
			if (li.state) {
				FeeReturnBeen tbe = (FeeReturnBeen) li.data;
				require = tbe.getRequire(); //保证金
				fee=  tbe.getFee();  //手续费
				totalPay=  Double.parseDouble(require)+Double.parseDouble(fee);
			} else {
				M.logger.error("费率计算失败");
				return new QResponse(false, "支付失败");
			}
			JSONArray jsonArray = repayPlanTemService.txgetValueableTemList(dto);
			List<RepayPlan> ilist = new LinkedList<RepayPlan>();
			String time = StringUtil.getCurrentDateTime("HHmmssS");
			String orderNo = OrderNoUtil.genOrderNo(time,32);

            //无需判断账户余额
			/*boolean subBalance = accountService.subBalance(userId, totalPay);
			if (!subBalance) {
				M.logger.error("ERROR:subBalance 余额不足!!!");
				throw new RuntimeException("余额不足");
			}*/
			//插入t_n_repay_record
			String currentMonth = StringUtil.getCurrentDateTime("yyyyMM");

			String startDate=jsonArray.getJSONObject(0).getString("repay_day");
			String endDate= jsonArray.getJSONObject(jsonArray.size()-1).getString("repay_day");

			int repay_money=0;

			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				repay_money+=jsonObject.getDouble("repay_amount");
			}

			long recordId = repayRecordService.doReRepayRecode(userId,
					currentMonth, jsonArray.size() + "", card.getCard_no(), totalPay+"",fee,startDate,endDate,repay_money+"",orderNo);

			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				jsonObject.put("record_id",recordId);
			}
			insertRepayPlan(jsonArray.toJavaList(RepayPlan.class));
			repayPlanTemService.clear(dto.getUserId(),dto.getCardNo());
			return new QResponse();
		} else {
			return QResponse.ERROR_SECURITY;
		}
	}
 
	
}