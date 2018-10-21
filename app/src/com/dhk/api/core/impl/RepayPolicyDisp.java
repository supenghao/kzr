package com.dhk.api.core.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.dhk.api.core.IRepayPolicy;
import com.dhk.api.entity.CreditCard;
import com.dhk.api.entity.RepayPlan;
import com.dhk.api.tool.M;
import com.dhk.api.dto.RepayPlanDto;

public class RepayPolicyDisp implements IRepayPolicy {

	private RepayPlanDto dto;
	// private CreditcardBill bill;
	private List<String> details;
	private CreditCard card;
	// public double bill_amount;// 账单金额
	private Calendar billdate = Calendar.getInstance();
	private Calendar repaydate = Calendar.getInstance();

	// private List<ParamFee> feelist;

	public RepayPolicyDisp(CreditCard card, RepayPlanDto dto
			) throws ParseException {
		this.dto = dto;
		this.card = card;
		// bill_amount = Double.parseDouble(bill.getBill_amount());
		// this.feelist = feelist;
//		billdate.setTime(M.dformat.parse(bill.getBill_day()));
//		repaydate.setTime(M.dformat.parse(bill.getRepay_day()));
		if (card.getId() == null) {
			throw new RuntimeException("信用卡id不能为null");
		}
	}

	/**
	 * 检查当前是否还款时间
	 * 
	 * @return
	 * @throws ParseException
	 */
	public boolean testRepaytime() {
		Calendar now = Calendar.getInstance();
		if (now.compareTo(billdate) >= 0 && now.compareTo(repaydate) < 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<String> detailDays() {
		if (details != null)
			return details;
		String detail = dto.getRepeat_detail();
		String policy_type = dto.getPolicy_type();
		String trans_count = dto.getTrans_count();// 还款笔数
		List<String> tem = Arrays.asList(detail.split(","));
		List<String> ds = new LinkedList<String>();
		ds.addAll(tem);
		details = new LinkedList<String>();
		int count = 0;
		try {
			if (trans_count != null)
				count = Integer.parseInt(trans_count);
			count = count <= 0 ? 1 : count;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return details;
		}
		return getCountDays(count, ds);
	}

	private List<RepayPlan> retRepayPlan;

	/**
	 * 根据(次数和选择的日期)生成具体的计划日期
	 * 
	 * @param count
	 * @param ds
	 * @return
	 */
	private List<String> getCountDays(int count, List<String> ds) {
		int size = ds.size();
		int hcount = 0;
		Calendar calendar = Calendar.getInstance();

		List<String> temp=new ArrayList<String>();
		for (int i=0;i<31;i++){
			String day = calendar.get(Calendar.DAY_OF_MONTH)+ "";
			if (ds.contains(day)&&!temp.contains(day)) {
				temp.add(day);
				details.add(M.dformat.format(calendar.getTime()));
				ds.remove(ds);
				hcount++;
			}

			if (ds.size() == 0 || hcount >= count) {
				break;
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (count <= size) {
			return details;
		} else {       //把不足还款笔数的补齐
			int elcon = count - size;
			List<String> sour = new LinkedList<String>();
			sour.addAll(details);
			while (true) {
				for (int i = 0; i < sour.size(); i++) {
					elcon--;
					details.add(sour.get(i));
					if (elcon <= 0) {
						return details;
					}
				}
			}
		}
	}

	// private double expense;

	@Override
	public List<RepayPlan> genDetailPolicy() {
		if (retRepayPlan != null)
			return retRepayPlan;
		List<String> days = detailDays();
		retRepayPlan = new LinkedList<RepayPlan>();
		RepayPlan tem = null;
		double total = Double.parseDouble(dto.getTrans_amount());
		int dsize = days.size();
		if (dsize <= 0)
			return retRepayPlan;
		double[] ranmon = getMoney(total, dsize);
		tem = new RepayPlan();
		tem.setCredit_card_no(card.getCard_no());
		tem.setPolicy_type(dto.getPolicy_type());
		tem.setRepay_amount(ranmon[0]);
		tem.setRepay_day(days.remove(0));
		tem.setStatus("0");
		tem.setUser_id(dto.getUserId());
		retRepayPlan.add(tem);
		if (dsize == 1) {
			return retRepayPlan;
		} else {
			for (int i = 0; i < days.size(); i++) {
				tem = new RepayPlan();
				tem.setCredit_card_no(card.getCard_no());
				tem.setPolicy_type(dto.getPolicy_type());
				tem.setRepay_amount(ranmon[i + 1]);
				tem.setRepay_day(days.get(i));
				tem.setStatus("0");
				tem.setUser_id(dto.getUserId());
				retRepayPlan.add(tem);
			}
			// 按日期排序
			Collections.sort(retRepayPlan, new Comparator<RepayPlan>() {
				@Override
				public int compare(RepayPlan o1, RepayPlan o2) {
					return o1.getRepay_day().compareTo(o2.getRepay_day());
				}
			});
			return retRepayPlan;
		}
	}

	/*public static double[] getMoney(double money, int num) {
		Random r = new Random();
		DecimalFormat format = new DecimalFormat(".##");
		double middle = Double.parseDouble(format.format(money / num));
		double[] dou = new double[num];
		double redMoney = 0;
		double nextMoney = money;
		double sum = 0;
		int index = 0;
		for (int i = num; i > 0; i--) {
			if (i == 1) {
				dou[index] = nextMoney;
			} else {
				while (true) {
					String str = format.format(r.nextDouble() * nextMoney);
					redMoney = Double.parseDouble(str);
					if (redMoney > 0 && redMoney < middle) {
						break;
					}
				}
				nextMoney = Double.parseDouble(format.format(nextMoney
						- redMoney));
				sum = sum + redMoney;
				dou[index] = redMoney;
				middle = Double.parseDouble(format.format(nextMoney / (i - 1)));
				index++;
			}
		}
		return dou;
	}*/
	public static double [] getMoney(double money, int num){
		int ddint = (int) money;
	    double ddAfterDot = money-ddint;
	 
	    double [] dou = new double[num];
	    
	    if (num==1){
	    	dou[0] = money;
	    	return dou;
	    }
	    
	    double average = ddint/num;
	    int averageInt = (int)average;
	    double averageDot = average - averageInt;
	    
	    int surplus = ddint - averageInt * num;
	    
	    
	    //System.out.println("averageInt:"+averageInt+"=averageDot:"+averageDot+"==surplus:"+surplus);
	    
	    for (int i=0;i<num;i++){
	    	dou[i] = average;
	    }
	    //判断num奇偶数
	    int n = 0;
	    if (num%2==0){
	    	n = num;
	    }else{
	    	n = num -1;
	    }
	    
	    int temp = (int)(dou[0]/num);//5
	    //System.out.println("temp:"+temp);
	    dou[0] = dou[0] + temp;
	    
	    for (int j=1;j<n;j++){
	    	if (j%2==0){
	    		dou[j] = dou[j] + temp;	 
	    	}else{
	    		dou[j] = dou[j] - temp;	 
	    		temp = (int)(dou[j] /num);//5	    		
	    	}  	
	    	
	    }
	    BigDecimal t = new BigDecimal(dou[num-1]+ddAfterDot+surplus+averageDot);
        dou[num-1] = t.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        
	    //System.out.println("n:"+n+"=="+(ddint/10));
	    
		return dou;
	}



	public static void  main(String[]args){

		int totalMoney=10000;
		int bzj =  (int)(totalMoney*0.1)-(int)(totalMoney*(0.85/100));
		int count=13;

		int  average= totalMoney/count;

		int syje= totalMoney- average*count;


		double [] dou = new double[count];
		//随大一笔金额
		int  maxMoney=  syje +average;
		//计算随机数
		Random random = new Random();


		int randomInt = 0;
		for (int i=0;i<count;i++){
			if(i==count-1&&count%2!=0){    //如果是最后一笔，并且总数是奇数
				dou[i] = average;
			}else if(i%2==0){
				randomInt=random.nextInt(bzj- maxMoney+1);
				dou[i] = average+randomInt;
			}else{
				dou[i] = average-randomInt;
			}
		}
		dou[0] =dou[0]+ syje;

		System.out.println(dou);
	}




}
