package com.dhk.api.tool;

import io.bestpay.sdk.ca.CaGenerator;
import io.bestpay.sdk.sign.RsaDataEncrypt;
import io.bestpay.sdk.sign.RsaSignBase64DataEncrypt;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Map;


/**
 * Created by Administrator on 2016/12/14.
 */
public class RsaDataEncryptUtil {
    //私钥-加密工具
    public static RsaDataEncrypt rsaDataEncryptPri;
    //公钥-解密工具
    public static RsaDataEncrypt rsaDataEncryptPub;

    static {
        String keyPassword = "123456";
        //客户私钥
        InputStream storeIn = RsaDataEncryptUtil.class.getResourceAsStream("/client.pfx");
        //服务端公钥
        InputStream cerIn = RsaDataEncryptUtil.class.getResourceAsStream("/server.cer");
        ByteArrayOutputStream storeOut = new ByteArrayOutputStream();
        try {
            IOUtils.copy(storeIn, storeOut);
            KeyStore ks = CaGenerator.getKeyStore(CaGenerator.KEYSTORE_PKCS12, new ByteArrayInputStream(Base64.decodeBase64(storeOut.toByteArray())), keyPassword.toCharArray());
            PrivateKey privateKey = (PrivateKey) CaGenerator.getPrivateKey(ks, keyPassword);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(cerIn, out);
            byte[] cert = Base64.decodeBase64(out.toByteArray());
            rsaDataEncryptPri = new RsaSignBase64DataEncrypt(null, privateKey, null);
            rsaDataEncryptPub = new RsaSignBase64DataEncrypt(Map.class, CaGenerator.getCertificate(cert));
        } catch (Exception e) {//公钥或私钥加载失败
            e.printStackTrace();
        }
    }
}
