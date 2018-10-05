package com.dhk.payment.util;

import com.antgroup.zmxy.openplatform.api.DefaultZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditAntifraudScoreGetRequest;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditAntifraudScoreGetResponse;

public class TestZhimaCreditAntifraudScoreGet {

	//芝麻开放平台地址
    private String gatewayUrl     = "https://zmopenapi.zmxy.com.cn/openapi.do";
    //商户应用 Id
    //private String appId          = "1001702";//"";
    private String appId          = "1002355";//"1001702";
    //商户 RSA 私钥
    private String privateKey     = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMd5GBhvxT2yfyizw7jjyzXEXin2NSA+iFFlvS1s06QlfsBJKcsjJQ4qPcuBErT4xUi9egw2kfPPnWARMQALFlV0o2xS4wtPe1twoEduAAFWP6YN03o1MZAP875xuSqnytU+CLrJ5ezuWJD29D52NE/Q8HERAl7wq+IH+JePKZ7FAgMBAAECgYEAqwuOet3U72vRGBDVwwQqK2fAKxFBSu30LIAmMP6BmN7CCpZyLQts6fGU3Z1HckFjwlUfg7uDeeLMmyUCtHvNYGKCdpcUjJrRB1/C/DPLJqNI8DP3Ki4j/ZpMTkUPu4f3/T/XsKaHCA1pumEsZ5GV5E31OR9CPJu0HonOii+Wu30CQQD4c4KVvgIJjpxBFf7LWw0bZItVT38siM2ML9gxwSOzWd2LRSf5TaNSPiWkfvx1OEUtK1OXHJZmRPOdnR/9zRALAkEAzYif4AQBpU64EQpEmH9ztNHSovGsjnbccVf2CrKtdMmS39OHYTik6o/Zb43sBUmMNbIDk5V44pdj/aIUlTc+bwJAOpE4MkZlQo18phciRhvDnjZ4ZfbkesIQ+j/5DdmCqe/AOullf+5eVq/RvdI1fwhxvjEddlzF03vjlb8JYJHrewJAOB5DuMCfSmlx7IUnMZQrHYXkw0TFXqipi/D+ZvFeVvLqvV5eKndy3/Ci58iSVuc79zVBsLwxI30+efQVNqC81QJAENixVROvPviN5opCo2XYrPckUUpiRHMcG3czRw3Dbv5/GRI5eIV6uCg4WQim8GciIah0peTxjhOdXt71hzN+rQ==";
    //芝麻 RSA 公钥
    private String zhimaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6gx4eVasoPK4KvyueGJ5jX6wDFfUXQ3MuvI10SUS+/Ww94QvkFCUJkxlKcxAByBOLx/tvWQ6x8m9dUcriHR8evh6XeCfEik8fDM8O7XRuFuqlb/B+5McEFSne98gBGz8Didl+HC3/3KMP8Dq+kDSv9G1JPzl8eBHVxVMNbEncywIDAQAB";//"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCewNlGqbM62oqw2/pQp5Uakn1JEK9Ojy3DsBTdF6vffEcSzD8BPb1uzzFFMaHPVCpvVF65lxtbCnCXGISlJ/W7/RT8xd6WtOKg4gymuK1jFOpvjrEpOFbegaevAlbEoxijuHaE5/n54bopi7lfvygYDY7EUkM0DwODYV/58Mn6WQIDAQAB";
    //private String zhimaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCewNlGqbM62oqw2/pQp5Uakn1JEK9Ojy3DsBTdF6vffEcSzD8BPb1uzzFFMaHPVCpvVF65lxtbCnCXGISlJ/W7/RT8xd6WtOKg4gymuK1jFOpvjrEpOFbegaevAlbEoxijuHaE5/n54bopi7lfvygYDY7EUkM0DwODYV/58Mn6WQIDAQAB";
    
    private String platform = "zmop";
    private String productCode = "w1010100003000001100";//"w1010100000000000103";
 
    public void  testZhimaCreditAntifraudScoreGet() {
        ZhimaCreditAntifraudScoreGetRequest req = new ZhimaCreditAntifraudScoreGetRequest();
        req.setPlatform("zmop");
        req.setProductCode("w1010100003000001100");// 必要参数         
        req.setTransactionId("20151127233347987676212309251");// 必要参数         
        req.setCertType("IDENTITY_CARD");// 必要参数         
        req.setCertNo("350121198110045212");// 必要参数         
        req.setName("卞智堃");// 必要参数         
        req.setMobile("18650330856");//         
//        req.setEmail("jnlxhy@alitest.com");//         
//        req.setBankCard("20110602436748024138");//         
//        req.setAddress("杭州市西湖区天目山路266号");//         
//        req.setIp("101.247.161.1");//         
//        req.setMac("44-45-53-54-00-00");//         
//        req.setWifimac("00-00-00-00-00-E0");//         
//        req.setImei("868331011992179");//         
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey,
            zhimaPublicKey);
        try {
            ZhimaCreditAntifraudScoreGetResponse response =(ZhimaCreditAntifraudScoreGetResponse)client.execute(req);
            System.out.println(response.isSuccess());
            System.out.println(response.getErrorCode());
            System.out.println(response.getErrorMessage());
            System.out.println(response.getScore());
        } catch (ZhimaApiException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        TestZhimaCreditAntifraudScoreGet result = new  TestZhimaCreditAntifraudScoreGet();
        result.testZhimaCreditAntifraudScoreGet();
    }
}
