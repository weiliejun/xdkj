package com.xdkj.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成公钥和私钥
 *
 * @version 1.0
 */
public class KeyManager {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成公钥私钥文件的编码
     */
    private static final String KEY_FILE_ENCODE = "UTF-8";

    /**
     * 生成公钥私钥文件的路径
     */
    private static final String KEY_FILE_PATH = "C:\\Users\\Administrator\\Desktop\\";

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static void genKeyPair(Map<String, String> params) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        String seed = params.get("seed");
        String ptrId = params.get("ptrId");
        String url = params.get("url");
        String shortName = params.get("shortName");
        SecureRandom secrand = new SecureRandom();
        // 初始化随机产生器
        secrand.setSeed(seed.getBytes());
        keyPairGen.initialize(1024, secrand);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        if (url.indexOf("vjinke.com") >= 0) {
            keyToFile(Base64Utils.encode(publicKey.getEncoded()), shortName, "pubKey.key");
            keyToFile(Base64Utils.encode(privateKey.getEncoded()), shortName, "priKey.key");
            // keyToFile(
            // Base64Utils.encode(publicKey.getEncoded()),"pubKey.key");
            // keyToFile(
            // Base64Utils.encode(privateKey.getEncoded()),"priKey.key");
        } else {
            keyToFile(Base64Utils.encode(publicKey.getEncoded()), shortName, "pubKey" + ptrId + ".key");
            keyToFile(Base64Utils.encode(privateKey.getEncoded()), shortName, "priKey" + ptrId + ".key");
            // keyToFile(
            // Base64Utils.encode(publicKey.getEncoded()),"pubKey"+ptrId+".key");
            // keyToFile(
            // Base64Utils.encode(privateKey.getEncoded()),"priKey"+ptrId+".key");
        }

    }

    /**
     * 从文件中获取公钥或私钥
     */
    public static String getKeyFromFile(String filePath) throws Exception {
        String result = "";
        if (!StringHelper.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buff = new byte[512];
                int len = 0;
                while ((len = fis.read(buff)) != -1) {
                    result += new String(buff, 0, len, KEY_FILE_ENCODE);
                }
                fis.close();
            }
        }
        return result;
    }

    /**
     * 公钥或私钥写入文件
     */
    public static void keyToFile(String key, String keyFileName) throws Exception {
        if (key != null && !"".equals(key)) {
            byte[] keyBytes = new byte[512];
            keyBytes = key.getBytes(KEY_FILE_ENCODE);
            int b = key.length();
            // 获取项目应用的路径
            String filePath = ApplicationPath.getRootPath();
            filePath = filePath + File.separator + "keysManage";
            System.out.println(filePath);
            File priFileDir = new File(filePath);
            if (!priFileDir.exists()) {
                priFileDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(priFileDir, keyFileName));
            fos.write(keyBytes, 0, b);
            fos.close();
        }
    }

    /**
     * 公钥或私钥写入文件
     */
    public static void keyToFile(String key, String shortName, String keyFileName) throws Exception {
        if (key != null && !"".equals(key)) {
            byte[] keyBytes = new byte[512];
            keyBytes = key.getBytes(KEY_FILE_ENCODE);
            int b = key.length();
            // 获取项目应用的路径
            String filePath = ApplicationPath.getRootPath();
            filePath = filePath + File.separator + "keysManage";
            //
            if (shortName != null && !"".equals(shortName)) {
                filePath += File.separator + shortName;
            }
            System.out.println(filePath);
            File priFileDir = new File(filePath);
            if (!priFileDir.exists()) {
                priFileDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(priFileDir, keyFileName));
            fos.write(keyBytes, 0, b);
            fos.close();
        }
    }

    public static void main(String[] args) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        map.put("seed", "dfasdfasd");
        map.put("ptrId", "wefadfa");
        genKeyPair(map);
    }

}