package com.xdkj.common.util;

import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 3DES加密工具类
 */
public class DES3 {
    // 密钥
    private final static String secretKey = "QAZXSWEDCVFDFGFDRTGBNHHGC";
    // 向量
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";
    private static Logger logger = Logger.getLogger(DES3.class);

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText) {
        Key deskey = null;
        DESedeKeySpec spec;
        SecretKeyFactory keyfactory;
        Cipher cipher;
        byte[] encryptData;
        try {
            spec = new DESedeKeySpec(secretKey.getBytes());
            keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            encryptData = cipher.doFinal(plainText.getBytes(encoding));

            return URLEncoder.encode(Base64.encode(encryptData), "UTF-8");//指定返回URLEncode编码

        } catch (InvalidKeyException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("DES加密文本失败", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("DES加密文本失败", e);
        }
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws InvalidKeyException
     * @throws Exception
     */
    public static String decrypt(String encryptText) {
        Key deskey = null;
        DESedeKeySpec spec;
        SecretKeyFactory keyfactory;
        Cipher cipher;
        byte[] decryptData;
        try {
            spec = new DESedeKeySpec(secretKey.getBytes());
            keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            decryptData = cipher.doFinal(Base64.decode(encryptText));
            return new String(decryptData, encoding);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (BadPaddingException e) {
            throw new RuntimeException("DES解密文本失败", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("DES解密文本失败", e);
        }
    }

    public static void main(String[] args) throws Exception {
        String content = "{'check':c81161d84b591d2edde5d7423409884b ,'data' : 'userName':'vghhhhh','password':'ffvvgvg','validateCode':''}";
        String password = "KSDFRGYNJKUHNKKV";

        byte[] p = password.getBytes();

        logger.info(p.length);
        // 加密
        logger.info("加密前：" + content);
        String encryptResult = encrypt(content);
        logger.info("加密后：" + encryptResult);
        // //解密
        String decryptResult = decrypt(encryptResult);
        logger.info("解密后：" + decryptResult);
    }
}
