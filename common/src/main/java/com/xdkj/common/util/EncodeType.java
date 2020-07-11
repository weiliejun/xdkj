/**
 * 文件名: EncodeType.java
 *
 * @Package com.phhc.sys.common.util
 * @Description: TODO
 * @author 罗顺锋
 * @date 2014-9-18 下午3:30:18
 * @version V1.0
 */
package com.xdkj.common.util;

/**
 * @author 罗顺锋
 * @ClassName: EncodeType
 * @Description: TODO(编码格式枚举)
 * @date 2014-9-18 下午3:30:18
 */
public enum EncodeType {
    UTF, GBK, ISO, ASCII;

    private static int number = EncodeType.values().length;
    /*
     * public static EncodeType getRandomColor(){ long random =
     * System.currentTimeMillis() % number; switch ((int) random){ case 0:
     * return EncodeType.UTF; case 1: return EncodeType.GBK; case 2: return
     * EncodeType.ISO; default : return EncodeType.UTF; } }
     */

    /**
     * 重写toString方法，返回自己想要的字符串
     */
    public String toString() {
        switch (this) {
            case UTF:
                return "UTF-8";
            case GBK:
                return "GBK";
            case ISO:
                return "ISO-8859-1";
            case ASCII:
                return "US-ASCII";
            default:
                return "UTF-8";
        }
    }
}
