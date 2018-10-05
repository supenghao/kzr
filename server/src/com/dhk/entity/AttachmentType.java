package com.dhk.entity;

public enum AttachmentType {
//	//身份证正面
//		PositiveIdCard,
//		//身份证反面
//		OppsiteIdCard,
//		//营业执照
//		BusinessLicense,
//		//银行卡
//		BankCard,
//		//"合同文件"
//		ContractDoc;
	PositiveIdCard("身份证正面"),OppsiteIdCard("身份证反面"),BusinessLicense("营业执照"),BankCard("银行卡"),ContractDoc("合同文件");
	private String desc;
	private AttachmentType(String desc){
		this.desc=desc;
	}
	
	@Override
	public String toString() {
		return desc;
	}
	
//	public static void main(String[] args) {
//		
//		try {
//			File file=null;
//			
//				file=new File("//www.dhk.com:8080/org/tomcat.txt");
//		if(!file.exists()){
//			file.mkdirs();
//		}
//			FileOutputStream fo=new FileOutputStream(file);
//			try {
//				fo.write("test".getBytes());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				fo.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
////		String type="身份证正面";
////		Calendar c=Calendar.getInstance();
////		
////		long mm=System.currentTimeMillis();
////		SimpleDateFormat sdFormat=new SimpleDateFormat("yyyyMMddHHmmssS");
////		
////		System.out.println(sdFormat.format(new Date(mm)));;
////		switch (AttachmentType.valueOf(type)) {
////		case PositiveIdCard:
////			System.out.println("身份证正面");
////			break;
////
////		default:
////			break;
////		}
//		//System.out.println(AttachmentType("身份证正面"));
//	}
}
