package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dengjianping on 2015/8/18.
 */
public class RandomProbability {
    private static Logger logger = Logger.getLogger(RandomProbability.class);

    private int[] pool = null;
    private int[][] arrys = null;

    private AtomicInteger poolLen = new AtomicInteger();

    private synchronized int[][] initData(int min, int max, int length) {
        if (arrys != null) {
            return arrys;
        }
        if (length > (max - min))
            return null;
        int min_ = min;
        int MIN = min;
        int y_length = 1 + (int) Math.ceil(((float) ((max - min) / length)));
        arrys = new int[length][y_length];
        int index_x = 0;
        int index_y = 0;
        while (index_x < arrys.length) {
            if (MIN < min_ + ((float) (max - min)) / length && MIN < max) {
                arrys[index_x][index_y] = MIN;
                index_y++;
                MIN++;
            } else {
                min_ = MIN;
                index_x++;
                index_y = 0;
            }
        }
        return arrys;
    }

    private void print(int[][] arrays) {
        for (int x = 0; x < arrays.length; x++) {
            for (int y = 0; y < arrays[x].length; y++) {
                // System.out.print(" "+arrays[x][y]);
            }
            // logger.info("\n");
        }
    }

    public int randomValue(int min, int max, int length, int[][] weight) {
        if (max <= min)
            return -1;
        if (length != weight[0].length)
            return -2;
        if (max - min < length)
            return -3;
        int[][] arrys = initData(min, max, length);
        int poosize = initPool(weight[0]);
        int index_x = randomIndex(poosize, weight);
        int index_y = (int) (Math.random() * arrys[0].length);
        // System.out.print(" index_x:" + index_x + " index_y:" + index_y);
        return arrys[index_x][index_y];

    }

    private int randomIndex(int poolsize, int[][] weight) {
        int y = (int) (Math.random() * poolsize);
        int result = -1;
        result = getPosition(y, weight);
        // System.out.print("========result:" + result + " randomY:" + y+"
        // pool["+y+"]:"+pool[y]);
        if (pool[y] > 0) {
            // 找和该位置等同的位置放置
            result = setValueToBestPosition(result, weight);
        } else {
            setValueToPool(y, weight);
        }
        if (result == -1) {
            result = randomIndex(poolsize, weight);
        }
        return result;
    }

    private synchronized void setValueToPool(int index, int[][] weight) {
        pool[index] = 1;
        int hasSize = poolLen.incrementAndGet();
        if (hasSize == pool.length) {
            pool = null;
            initPool(weight[0]);
        }
    }

    private int setValueToBestPosition(int position, int[][] weight) {
        int skip = 0;
        int scop = 0;
        int result = -1;
        Boolean resultflag = true;
        for (int index = 0; index < weight[0].length; index++) {

            if (position == weight[1][index]) {
                scop = weight[0][index];
                break;
            } else {
                skip += weight[0][index];
            }

        }
        for (int index = skip; index < skip + scop - 1; index++) {
            synchronized (pool) {
                if (pool[index] == 0) {
                    setValueToPool(index, weight);
                    resultflag = false;
                    result = position;
                    // System.out.print(" resltFalg11:"+resultflag+" ");
                    break;
                }
            }
        }
        synchronized (pool) {
            for (int index = 0; resultflag && index < pool.length; index++) {
                if (pool[index] == 0) {
                    setValueToPool(index, weight);
                    result = getPosition(index, weight);
                    resultflag = false;
                    // System.out.print(" resltFalg22:"+resultflag+" ");
                    break;
                }
            }
        }

        if (resultflag) {
            pool = null;
            initPool(weight[0]);
            // System.out.print(" resltFalg33:"+resultflag+" ");
        }
        return result;
    }

    private int getPosition(int index, int[][] weight) {
        int inc = 0;
        int result = -1;
        for (int y = 0; y < weight[0].length; y++) {
            if (index < inc + weight[0][y]) {
                result = weight[1][y];
                break;
            }
            inc += weight[0][y];
        }

        return result;
    }

    private synchronized int initPool(int[] weigth) {
        if (pool != null) {
            return pool.length;
        }
        int key = 0;
        for (int index = 0; index < weigth.length; index++) {
            key += weigth[index];
        }
        pool = new int[key];
        return key;
    }
}
