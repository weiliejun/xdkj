package com.xdkj.admin.components.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Description shrio登录密码加密
 * @auther: zhangkele
 * @UpadteDate: 2019/3/4 9:14
 */
public class PasswordHelper {
    public static final String ALGORITHM_NAME = "md5"; // 基础散列算法
    public static final int HASH_ITERATIONS = 2; // 自定义散列次数

    public static String encryptPassword(String salt, String password) {
        String newPassword = new SimpleHash(ALGORITHM_NAME, password,
                ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    public static void main(String[] args) {
        System.out.println("-----------" + encryptPassword("sysadmin", "123456"));
    }
}