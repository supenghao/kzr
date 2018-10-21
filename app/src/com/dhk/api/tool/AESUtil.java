package com.dhk.api.tool;

/**
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 *	SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 *	-- 解密
 *	SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 * @author 836508
 *
 */
public class AESUtil {

    private static String key = "chenshuai1234567";
    // 加密
    public static String Encrypt(String sSrc) throws Exception {
        return   sSrc;

//        if(StringUtils.isEmpty(sSrc))return "";
//
//        byte[] raw = key.getBytes("utf-8");
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
//        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc) {
        return   sSrc;
//        if(StringUtils.isEmpty(sSrc))return "";
//        try {
//            byte[] raw = key.getBytes("utf-8");
//            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
//            try {
//                byte[] original = cipher.doFinal(encrypted1);
//                String originalString = new String(original,"utf-8");
//                return originalString;
//            } catch (Exception e) {
//                System.out.println(e.toString());
//                return null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "1234567891234567";
        // 需要加密的字串
        String cSrc = "www.gowhere.so";
        System.out.println(cSrc);
        // 加密
        String enString = AESUtil.Encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AESUtil.Decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
    }
}