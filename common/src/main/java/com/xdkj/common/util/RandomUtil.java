package com.xdkj.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by dengjianping on 2015/8/24.
 */
public class RandomUtil {
    private static RandomProbability rp1 = new RandomProbability();
    private static RandomProbability rp2 = new RandomProbability();
    private static RandomProbability rp3 = new RandomProbability();
    private static RandomProbability rp4 = new RandomProbability();

    public static RandomProbability getPool_1_5_Instence() {
        return rp1;
    }

    public static RandomProbability getPool_10_120_Instence() {
        return rp2;
    }

    public static RandomProbability getPool_10_188_Instence() {
        return rp3;
    }

    public static RandomProbability getPool_10_30_Instence() {
        return rp4;
    }

    /**
     * 获取指定位数的随机数字串
     *
     * @param digit 随机数的长度
     * @return
     */
    public static String getRandomString(int digit) {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            randomString.append(random.nextInt(10));
        }
        return randomString.toString();
    }

    /**
     * 获取6位数字验证码
     *
     * @return
     */
    public static int getRandomVerifyCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x + 100000;
        return x;
    }


    /**
     * 生成流水号
     */
    public static String getSerialNumber() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String serialNumber = format.format(new Date().getTime()) + new Double(Math.random() * 100000).intValue();
        while (serialNumber.length() < 22) {
            serialNumber = serialNumber + "0";
        }
        serialNumber = serialNumber.substring(2);
        return serialNumber;
    }

}
