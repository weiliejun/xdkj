package com.xdkj.common.util;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;

public class AESUtil {
    public static final String VIPARA = "0102030405060708";
    public static final String bm = "UTF-8";
    private static Logger logger = Logger.getLogger(AESUtil.class);

    /**
     * 加密(GBK)
     */
    public static String encryptGBK(String input, String key) {
        return encrypt(input, key, "GBK");
    }

    /**
     * 解密(GBK)
     */
    public static String decryptGBK(String input, String key) {
        return decrypt(input, key, "GBK");
    }

    /**
     * 加密
     */
    public static String encrypt(String input, String key, String charset) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            //用GBK解决中文乱码
            crypted = cipher.doFinal(input.getBytes(charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.encode(crypted));
    }


    /**
     * 解密
     */
    public static String decrypt(String input, String key, String charset) {
        byte[] decrypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            decrypted = cipher.doFinal(Base64.decode(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //用GBK解决中文乱码
        return new String(decrypted, Charset.forName(charset));
    }

    /**
     * 加密
     *
     * @param text 要加密的字符串
     * @param key  私钥 AES固定格式为128/192/256bits.即：16/24/32bytes。DES固定格式为128bits，
     *             即8bytes。
     * @return
     * @throws Exception
     */
    public static String encrypt(String text, String key) throws Exception {

        String iv = "aabbccddeeffgghh";// 初始化向量参数，AES 为16bytes. DES 为8bytes.
        Key keySpec = new SecretKeySpec(key.getBytes(), "AES"); // 两个参数，第一个为私钥字节数组，
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");// 实例化加密类，参数为加密方式，要写全
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// 初始化，此方法可以采用三种方式，按服务器要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom
        // random = new
        // SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // SecureRandom random = new SecureRandom();
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec, random);
        byte[] b = cipher.doFinal(text.getBytes());// 加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64,
        // HEX,
        // UUE,7bit等等。此处看服务器需要什么编码方式
        String ret = BASE64Util.encryptBASE64(b); // Base64、HEX等编解码
        return ret;
    }

    public static String decrypt(String dataPassword, String encrypted) throws Exception {
        byte[] byteMi = Base64.decode(encrypted);
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
        SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);

        return new String(decryptedData, bm);
    }

    public static void main(String[] args) throws Exception {
        String content = "{'check':c81161d84b591d2edde5d7423409884b ,'data' : 'userName':'vghhhhh','password':'ffvvgvg','validateCode':''}";
        String password = "KSDFRGYNJKUHNKKV";

        byte[] p = password.getBytes();

        logger.info(p.length);
        // 加密
        logger.info("加密前：" + content);
        String encryptResult = encrypt(content, password);
        logger.info("加密后：" + encryptResult);
        // //解密
        String decryptResult = decrypt(password, encryptResult);
        logger.info("解密后：" + decryptResult);

    }
}