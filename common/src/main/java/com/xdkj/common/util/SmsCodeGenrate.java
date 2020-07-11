package com.xdkj.common.util;

import java.util.Random;

/**
 * @Description 生成手机验证码随机字符串6位
 * @auther: cyp
 * @UpadteDate: 2019-01-22 14:03
 */
public class SmsCodeGenrate {

    public static int getRandomVerifyCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x + 100000;
        return x;
    }
}

