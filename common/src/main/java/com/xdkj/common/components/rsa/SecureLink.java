package com.xdkj.common.components.rsa;


import com.xdkj.common.components.exception.ParseKeyFileErrorException;
import com.xdkj.common.components.exception.ValidSignErrorException;
import com.xdkj.common.components.rsa.exception.AddSignErrorException;
import com.xdkj.common.util.Base64Utils;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecureLink {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String KEY_FILE_ENCODE = "UTF-8";
    private static final String CHKVALUE_ENCODE = "UTF-8";

    public SecureLink() {
    }

    public static String sign(String chkValue, String priFileName) throws AddSignErrorException {
        String sign = "";

        try {
            if (chkValue != null && !"".equals(chkValue)) {
                String privateKey = getKeyFromFile(priFileName);
                byte[] data = chkValue.getBytes("UTF-8");
                byte[] keyBytes = Base64Utils.decode(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                Signature signature = Signature.getInstance("MD5withRSA");
                signature.initSign(privateK);
                signature.update(data);
                sign = Base64Utils.encode(signature.sign());
                return sign;
            } else {
                throw new AddSignErrorException("数字签名时参数：chkValue不能为空");
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | FileNotFoundException var10) {
            throw new AddSignErrorException("数字签名失败", var10);
        } catch (Exception var11) {
            throw new AddSignErrorException("数字签名失败", var11);
        }
    }

    public static boolean verify(String chkValue, String pubFileName, String sign) throws ValidSignErrorException {
        boolean flag = false;

        try {
            if (chkValue != null && !"".equals(chkValue)) {
                String publicKey = getKeyFromFile(pubFileName);
                byte[] data = chkValue.getBytes("UTF-8");
                byte[] keyBytes = Base64Utils.decode(publicKey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicK = keyFactory.generatePublic(keySpec);
                Signature signature = Signature.getInstance("MD5withRSA");
                signature.initVerify(publicK);
                signature.update(data);
                flag = signature.verify(Base64Utils.decode(sign));
                return flag;
            } else {
                throw new ValidSignErrorException("验证签名时参数：chkValue不能为空");
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | ParseKeyFileErrorException var11) {
            throw new ValidSignErrorException("验证签名失败", var11);
        } catch (Exception var12) {
            throw new ValidSignErrorException("验证签名失败", var12);
        }
    }

    public static boolean verify(String repData, String pubFileName) throws ValidSignErrorException {
        String data = "";
        String check = "";
        Pattern mPData = Pattern.compile("\"(data)\":(\\{([^\\}]*)\\})");

        for (Matcher mMData = mPData.matcher(repData); mMData.find(); data = mMData.group(2)) {
            ;
        }

        Pattern mPCheck = Pattern.compile("\"(check)\":\"(.*?)\"");

        for (Matcher mMCheck = mPCheck.matcher(repData); mMCheck.find(); check = mMCheck.group(2)) {
            ;
        }

        return verify(data, pubFileName, check);
    }

    public static String getKeyFromFile(String filePath) throws ParseKeyFileErrorException {
        String result = "";
        FileInputStream fis = null;
        byte[] buff = new byte[512];
        boolean var4 = false;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new ParseKeyFileErrorException("解析密钥文件:" + filePath + "不存在");
            }

            int len;
            for (fis = new FileInputStream(file); (len = fis.read(buff)) != -1; result = result + new String(buff, 0, len, "UTF-8")) {
                ;
            }
        } catch (UnsupportedEncodingException var14) {
            throw new ParseKeyFileErrorException("解析密钥文件:" + filePath + "时不支持字符编码：" + "UTF-8", var14);
        } catch (IOException var15) {
            throw new ParseKeyFileErrorException("解析密钥文件:" + filePath + "时IO异常", var15);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var13) {
                    throw new ParseKeyFileErrorException("解析密钥文件:" + filePath + "时关闭文件流异常", var13);
                }
            }

        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        String jsonStr = "{\"name\":\"张三\",\"sex\":\"a\"}";
        String signStr = sign(jsonStr, "C:\\Users\\Administrator\\Desktop\\keysManage\\vjinke\\priKey.cer");
        jsonStr = "{\"check\":\"" + signStr + "\",\"data\":{\"name\":\"张三\",\"sex\":\"a\"}}";
        boolean flag = verify(jsonStr, "C:\\Users\\Administrator\\Desktop\\keysManage\\vjinke\\pubKey.pfx");
        System.out.println("signStr==" + signStr);
        System.out.println("flag==" + flag);
    }
}
