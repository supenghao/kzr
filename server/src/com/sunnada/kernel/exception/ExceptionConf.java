package com.sunnada.kernel.exception;


public class ExceptionConf {

//	private final String CONF_DIR = "";
//	private static Map<String,String> codeMap = new HashMap<String,String>();
//	static{
//		codeMap.put("E0001", "系统异常");
//		codeMap.put("E0002", "记录重复");
//	}
	public static String getExceptionText(String code) throws Exception{
		String text = "";
//		if (codeMap.get(code)!=null && !codeMap.get(code).trim().equals("")){
//			text = codeMap.get(code).trim();
//		}else{
//			text = "系统异常";
//		}
		
		
		
//		ErrorcodeFacade codeFacade = new ErrorcodeFacade();
//		ArrayList<String> arrayList = new ArrayList<String>();
//		arrayList.add("error_code,string,"+code);
//		List list = codeFacade.findByHql("errorcode", "findByCode", arrayList);
//		if (list.size()>0){
//			Errorcode errorcode = (Errorcode)list.get(0);
//			text = errorcode.getError_text();
//		}else{
//			text = "系统异常";
//		}
		
		return text;
	}
//	public Map<String,String> getCodeMap(){
//		return codeMap;
//	}
	
	public static void main(String[] args) throws Exception{
		try{
			throw new BaseException("E0001");
		}catch(BaseException e){
			System.out.println("aaa:"+e.getMessage());
			e.printStackTrace();
		}
	}
}
