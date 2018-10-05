package com.dhk.payment.yilian;

import java.security.Key;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESedeUtil {
    public static final String ALGORITHM_DESede = "desede/CBC/PKCS7Padding";

//    private static byte[] miv = { 1, 2, 3, 4, 5, 6, 7, 8 };
    private static byte[] miv = "01234567".getBytes();

    /**
     * 3DES算法，加密
     *
     * @param data 待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception
     */
    public static String encode(String data,String key){
    	
        if(data == null){
            return null;
        }
        if(key == null){
            return data;
        }
        try{
        	
        	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        	
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DESede);
            IvParameterSpec iv = new IvParameterSpec(miv);
            AlgorithmParameterSpec zeroIv = iv;
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,zeroIv);
            byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.encode(bytes);
//            return byte2HexString(bytes);
        }catch(Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 3DES算法，解密
     *
     * @param data 待解密字符串
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String decode(String data,String key){
        if(data == null){
            return null;
        }
        if(key == null){
            return data;
        }
        try{
        	
        	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        	
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DESede);
            IvParameterSpec iv = new IvParameterSpec(miv);
            AlgorithmParameterSpec zeroIv = iv;
            cipher.init(Cipher.DECRYPT_MODE,secretKey,zeroIv);
            return new String(cipher.doFinal(Base64.decode(data)));
//            return new String(cipher.doFinal(byte2hex(data.getBytes())));
        }catch(Exception e){
            e.printStackTrace();
            return data;
        }
    }

    /**
     * 二行制转16进制字符串
     *
     * @param b
     * @return String
     */
    public static String byte2HexString(byte[] b){
        StringBuilder hs = new StringBuilder();
        String stmp;
        for(int n = 0 ; b != null && n < b.length ; n++){
            stmp = Integer.toHexString(b[n] & 0XFF);
            if(stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 二进制转化成16进制
     *
     * @param b
     * @return byte
     */
    private static byte[] byte2hex(byte[] b){
        if((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for(int n = 0 ; n < b.length ; n += 2){
            String item = new String(b,n,2);
            b2[n / 2] = (byte)Integer.parseInt(item,16);
        }
        return b2;
    }
    
    public static void main(String[] args) throws Exception{
    	String temp = DESedeUtil.encode("111111", "123456789012345678901234");
    	
    	System.out.println("temp:"+temp);
    }
}
