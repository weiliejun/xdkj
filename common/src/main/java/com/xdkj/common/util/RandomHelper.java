package com.xdkj.common.util;

import java.util.Random;

public class RandomHelper {

    /**
     * 生成大于100000 小于 999999的随机数
     *
     * @return
     */
    public static int getRandomNum6() {
        Random random = new Random();
        int number = random.nextInt(899999);
        return number + 100000;
    }


    public static int nextInt(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    /**
     * 获取指定长度的随机数
     *
     * @param length
     */
    public static String getRandomNumber(int length) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
