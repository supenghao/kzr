package com.dhk.payment.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtil {

	
	/** 
     * JavaBean转换成xml 
     * 默认编码UTF-8 
     * @param obj 
     * @param writer 
     * @return  
     */  
    public static String convertToXml(Object obj,boolean format) {  
        return convertToXml(obj, "GBK",format);  
    }  
    
  
    /** 
     * JavaBean转换成xml 
     * @param obj 
     * @param encoding  
     * @return  
     */  
    public static String convertToXml(Object obj, String encoding,boolean format) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller(); 
            if (format){
            	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            }
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
  
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
    
  
    /** 
     * xml转换成JavaBean 
     * @param xml 
     * @param c 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T converyToJavaBean(String xml, Class<T> c) {  
        T t = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(c);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            t = (T) unmarshaller.unmarshal(new StringReader(xml));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return t;  
    }  
    
    public static String demoObj2Xml(){
    	FundeRequest request = new FundeRequest();
        request.setPARTNER_ID("pcis_xyz");
        request.setTRAN_CODE("Issue");
        request.setUserCode("leqizuqiu@chinacriagent.com");
        request.setPassWord("Sinolife2008");
        request.setInsuredAmount("10000");
        request.setProductCode("1718");
        request.setPremium("3.5");
        request.setQuoteSchemeId("D000033242");
        request.setInsuranceEndDate("2018-03-29");
        request.setInsuranceBeginDate("2017-03-30");
        request.setProvinceCode("130000");
        request.setCityCode("130100");
        request.setAreaCode("130103");
        request.setAddressInDetail("去哪儿网");
        request.setKindCountMap("");
        ApplyRelationParamDomain arpd = new ApplyRelationParamDomain();
        arpd.setAppliRelation("01");
        arpd.setUwAddress("");
        arpd.setUwBirthday("1993-04-02");
        arpd.setUwCertificateNo("12345678922");
        arpd.setUwCertificateType("02");
        arpd.setUwCreateDate("");
        arpd.setUwCreatedUser("");
        arpd.setUwEmail("651782981@qq.com");
        arpd.setUwMobileTelephone("13585101922");
        arpd.setUwPersonnelName("王辰阳0209");
        arpd.setUwPersonnelSex("1");
        arpd.setUwOccupationCode("");
        arpd.setUwPostCode("");
        arpd.setUwUpdatedUser("");
        request.setUwApplyRelationParamDomain(arpd);
        List<ApplyBeneficiary> listUwApplyBeneficiary = new ArrayList<ApplyBeneficiary>();
        ApplyBeneficiary abf = new ApplyBeneficiary();
        abf.setAppliRelation("01");
        abf.setUwAddress("");
        abf.setUwBirthday("1993-04-02");
        abf.setUwCertificateNo("12345678922");
        abf.setUwCertificateType("02");
        abf.setUwCreateDate("");
        abf.setUwCreatedUser("");
        abf.setUwEmail("651782981@qq.com");
        abf.setUwMobileTelephone("13585101922");
        abf.setUwPersonnelName("王辰阳0209");
        abf.setUwPersonnelSex("1");
        abf.setUwOccupationCode("");
        abf.setUwPostCode("");
        abf.setUwUpdatedUser("");
        listUwApplyBeneficiary.add(abf);
        request.setListUwApplyBeneficiary(listUwApplyBeneficiary);
        List<ApplyInsured> listUwApplyInsured = new ArrayList<ApplyInsured>();
        ApplyInsured ai = new ApplyInsured();
        ai.setAppliRelation("01");
        ai.setUwAddress("");
        ai.setUwBirthday("1993-04-02");
        ai.setUwCertificateNo("12345678922");
        ai.setUwCertificateType("02");
        ai.setUwCreateDate("");
        ai.setUwCreatedUser("");
        ai.setUwEmail("651782981@qq.com");
        ai.setUwMobileTelephone("13585101922");
        ai.setUwPersonnelName("王辰阳0209");
        ai.setUwPersonnelSex("1");
        ai.setUwOccupationCode("");
        ai.setUwPostCode("");
        ai.setUwUpdatedUser("");
        listUwApplyInsured.add(ai);
        request.setListUwApplyInsured(listUwApplyInsured);
        List<ApplyBidPropertyDomain> listUwApplyBidPropertyDomain = new ArrayList<ApplyBidPropertyDomain>();
        ApplyBidPropertyDomain abpd = new ApplyBidPropertyDomain();
        abpd.setFieldaj("账户ID simpleqiu");
        listUwApplyBidPropertyDomain.add(abpd);
        request.setListUwApplyBidPropertyDomain(listUwApplyBidPropertyDomain);
        List<Kind> kindList = new ArrayList<Kind>();
        Kind k = new Kind();
        k.setKindCode("1718001");
        kindList.add(k);
        request.setKindList(kindList);
        
        
        
        String xml = JAXBUtil.convertToXml(request,true);
        
        return xml;
    }
	
    public static void main(String[] args) throws Exception{
		
		
		String xml="<?xml version='1.0' encoding='GBK'?><RESPONSE><policyNo>602019917182017E086941</policyNo><applyNo>002019917182017E087391</applyNo><insuranceBeginDate>2017-03-30</insuranceBeginDate><insuranceEndDate>2018-03-29</insuranceEndDate><SL_RSLT_CODE>999</SL_RSLT_CODE><SL_RSLT_MESG>投保成功！保单号：602019917182017E086941</SL_RSLT_MESG></RESPONSE>";
		
		FundeResult bsObj = (FundeResult)JAXBUtil.converyToJavaBean(xml, FundeResult.class);
        
        System.out.println("policyNo:"+bsObj.getApplyNo());
        System.out.println("SL_RSLT_CODE:"+bsObj.getSL_RSLT_CODE());
        System.out.println("SL_RSLT_MESG:"+bsObj.getSL_RSLT_MESG());
        
        
        System.out.println("xml:"+JAXBUtil.demoObj2Xml());
	}
}
