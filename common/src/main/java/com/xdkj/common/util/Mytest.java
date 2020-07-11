package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by dengjianping on 2015/8/14.
 */
public class Mytest implements Runnable {
    private static Logger logger = Logger.getLogger(Mytest.class);

    private RandomProbability randomUtil = new RandomProbability();
    // private RandomProbability randomUtil2 = new RandomProbability();
    private int[][] weigth = {{45, 29, 15, 5, 3, 2, 1}, {0, 1, 2, 3, 4, 5, 6}};

    // private int[][] weigth2 = {{75,25},{0,1}};
    public static void main(String[] args) {
        Mytest test = new Mytest();

        for (int i = 0; i < 10000; i++) {
            test.run();
        }

    }

    public void run() {
        /*
         * int result = randomUtil.randomValue(1,5,2,weigth2);
         * System.out.print(" value:"+result+"\n");
         */

        logger.info(MYRandom(new BigDecimal(100000)));

        // RandomUtil.getPool_1_5_Instence().randomValue(1,5,2,weigth2);
    }

    private int MYRandom(BigDecimal investAmounts) {
        int rand = 0;
        if (investAmounts.compareTo(new BigDecimal(100)) <= 0) {// 出借金额小于100元的
            int[][] weigth = {{71, 29}, {0, 1}};
            while (rand == 0) {
                try {
                    rand = RandomUtil.getPool_1_5_Instence().randomValue(1, 5, 2, weigth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (investAmounts.compareTo(new BigDecimal(100)) > 0 && investAmounts.compareTo(new BigDecimal(10000)) < 0) {
            int[][] weigth = {{70, 20, 5, 2, 1, 1, 1}, {0, 1, 2, 3, 4, 5, 6}};
            while (rand == 0) {
                try {
                    rand = RandomUtil.getPool_10_120_Instence().randomValue(10, 120, 7, weigth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (investAmounts.compareTo(new BigDecimal(10000)) >= 0) {
            int[][] weigth = {{55, 25, 15, 2, 1, 1, 1}, {0, 1, 2, 3, 4, 5, 6}};
            while (rand == 0) {
                try {
                    rand = RandomUtil.getPool_10_188_Instence().randomValue(10, 188, 7, weigth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rand;

    }

}