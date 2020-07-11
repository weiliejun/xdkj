package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class BigDecimalHelper {
    public static BigDecimal HUNDRED = new BigDecimal("100");
    private static Logger logger = Logger.getLogger(BigDecimalHelper.class);

    public static BigDecimal getScale2Up(BigDecimal big) {
        return big.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保留4为小数
     */
    public static BigDecimal getScale4Up(BigDecimal big) {
        return big.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 保留8为小数
     */
    public static BigDecimal getScale8Up(BigDecimal big) {
        return big.setScale(8, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 利率/100 * 金额 ，保留两位小数
     *
     * @param rate  利率
     * @param mount 金额
     * @return rate/100 * mount
     */
    public static BigDecimal movePointLeftScale2UpMul(BigDecimal rate, BigDecimal mount) {
        return getScale2Up(rate.multiply(mount).divide(new BigDecimal(100)));
    }


    /**
     * 利率/100 * 金额 ,保留四位小数
     *
     * @param rate  利率
     * @param mount 金额
     * @return rate/100 * mount
     */

    public static BigDecimal movePointLeftScale4UpMul(BigDecimal rate, BigDecimal mount) {
        // return rate.movePointLeft(2).multiply(mount).setScale(4,
        // BigDecimal.ROUND_HALF_UP);
        return rate.multiply(mount).divide(new BigDecimal(100), 4);
    }

    /**
     * 字符串转换成BigDecimal
     */

    public static BigDecimal StringToBigdecimal(String str) {
        if (str == null || str.length() < 1) {
            return BigDecimal.ZERO;
        }
        str = str.replaceAll(",", "");
        return new BigDecimal(str);
    }

    /***
     * 两个值相加
     *
     * @param sum
     * @param repayedAmount
     * @return
     * @author yanminfeng
     */
    public static BigDecimal getBigDecimalValue(BigDecimal sum, BigDecimal repayedAmount) {
        if (repayedAmount != null) {
            if (sum != null && sum.compareTo(new BigDecimal(0.00)) == 1) {
                sum = sum.add(repayedAmount);
            } else {
                sum = repayedAmount;
            }
        }
        return sum == null ? new BigDecimal("0.00") : sum;
    }

    /**
     * -------返回0.00数据
     *
     * @param value
     * @return
     */
    public static String getStringXXValue(BigDecimal value) {
        DecimalFormat myformat = new DecimalFormat("0.00");
        String str = myformat.format(value);
        return str;
    }

    /*
     * 计算公式1 相减求百分比（保留2位小数） XXXX={XXX1-XXX2}÷XXX2×100
     */
    public static BigDecimal computational(BigDecimal BigDecimal1, BigDecimal BigDecimal2) {
        BigDecimal bd3 = new BigDecimal(100);
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal d = BigDecimal1.subtract(BigDecimal2);
        BigDecimal e = d.divide(BigDecimal2, 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal d2ToBigDeWithPre = new BigDecimal(df.format(e.multiply(bd3).doubleValue()));
        return d2ToBigDeWithPre;
    }

    /*
     * 计算公式1 相减求百分比 ,取整 XXXX={XXX1-XXX2}÷XXX2×100
     */
    public static BigDecimal computationalTrunc(BigDecimal BigDecimal1, BigDecimal BigDecimal2) {
        BigDecimal bd3 = new BigDecimal(100);
        BigDecimal d = BigDecimal1.subtract(BigDecimal2).multiply(bd3);
        BigDecimal d2ToBigDeWithPre = d.divide(BigDecimal2, 0, BigDecimal.ROUND_HALF_UP);
        return d2ToBigDeWithPre;
    }

    /**
     * 计算公式2 相除求百分比（保留2位小数，金额、利息计算） XXXX=XXX1÷XXX2×100 （四舍五入）
     */
    public static BigDecimal calculatePercent(BigDecimal dividend, BigDecimal divisor) {
        BigDecimal percent = new BigDecimal("0.00");
        if (dividend == null || divisor == null) {
            return percent;
        }
        if (divisor.compareTo(new BigDecimal("0.00")) == 0) {
            return percent;
        }
        BigDecimal hundred = new BigDecimal(100);
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal e = dividend.divide(divisor, 6, BigDecimal.ROUND_HALF_UP);
        percent = new BigDecimal(df.format(e.multiply(hundred).doubleValue()));
        return percent;
    }

    /**
     * 计算公式2 相除求百分比 XXXX=XXX1÷XXX2×100 应用：我的账户-金额百分比值显示，避免出现0.000X取2位小数为0.00
     *
     * @param dividend
     * @param divisor
     * @param df       格式化
     * @return （保留2位小数，舍去）
     */
    public static BigDecimal calculatePercentXXXX(BigDecimal dividend, BigDecimal divisor, DecimalFormat df) {
        BigDecimal percent = new BigDecimal("0.00");
        if (dividend == null || divisor == null) {
            return percent;
        }
        if (divisor.compareTo(new BigDecimal("0.00")) == 0) {
            return percent;
        }
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal e = dividend.divide(divisor, 6, BigDecimal.ROUND_HALF_DOWN);
        percent = new BigDecimal(df.format(e.multiply(hundred).doubleValue()));
        return percent;
    }

    /**
     * 计算公式2 相除求百分比,取整 XXXX=XXX1÷XXX2×100
     */
    public static BigDecimal calculatePercentTrunc(BigDecimal dividend, BigDecimal divisor) {
        BigDecimal percent = new BigDecimal("0");
        if (dividend == null || divisor == null) {
            return percent;
        }
        if (divisor.compareTo(new BigDecimal("0")) == 0) {
            return percent;
        }
        BigDecimal hundred = new BigDecimal(100);
        // BigDecimal e = dividend.divide(divisor,6,BigDecimal.ROUND_HALF_UP);
        percent = dividend.multiply(hundred).divide(divisor, 0, BigDecimal.ROUND_HALF_UP);
        ;
        return percent;
    }

    /**
     * 获取最小值
     */
    public static BigDecimal getMin(List<BigDecimal> list) {
        BigDecimal min = null;
        if (list != null && list.size() > 0) {
            min = new BigDecimal(0);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    min = list.get(i);
                } else {
                    if (list.get(i).compareTo(min) < 0) {
                        min = list.get(i);
                    }
                }
            }
        }

        return min;
    }

    /**
     * 获取最大值
     */
    public static BigDecimal getMax(List<BigDecimal> list) {
        BigDecimal max = null;
        if (list != null && list.size() > 0) {
            max = new BigDecimal(0);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    max = list.get(i);
                } else {
                    if (list.get(i).compareTo(max) > 0) {
                        max = list.get(i);
                    }
                }
            }
        }
        return max;
    }

    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 格式化为0.00格式
     */
    public static String format(BigDecimal decimal) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(decimal).toString();
    }

    /**
     * 格式化为pattern格式
     */
    public static String formatByPattern(BigDecimal decimal, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(decimal).toString();
    }

    /**
     * 以万为单位，格式化为#.##,最多包含两位小数
     */
    public static String countInTenThousands(Object decimal) {
        BigDecimal result = BigDecimal.ZERO;
        if (decimal instanceof String) {
            if (decimal != null && !"".equals(decimal)) {
                result = new BigDecimal((String) decimal);
            }
        }
        if (decimal instanceof BigDecimal) {
            if (decimal != null) {
                result = (BigDecimal) decimal;
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        result = result.multiply(new BigDecimal("0.0001"));
        return df.format(result).toString();
    }

    /**
     * 金额格式化
     *
     * @param s   金额
     * @param len 小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,##0");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
     * 是否是整数倍校验
     */
    public static boolean isIntegralMultiple(BigDecimal decimal, BigDecimal divisor) {
        return decimal.multiply(HUNDRED).remainder(divisor.multiply(HUNDRED)).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * @param totalLimit 银行卡单日限额
     * @return
     * @description 银行卡单日限额格式化
     * @version 1.0
     * @author 徐赛平
     * @update 2018年9月28日 下午2:58:27
     */
    public static String formatTotalLimit(String totalLimit) {
        String totalLimitStr = "";
        BigDecimal totalLimitDec = new BigDecimal("0.00");
        DecimalFormat df1 = new DecimalFormat("#.##");
        if (totalLimit == null || totalLimit.length() < 1) {
            return "";
        }
        totalLimitDec = new BigDecimal(totalLimit.replace(",", ""));
        if (totalLimitDec.compareTo(new BigDecimal("10000")) > 0) {
            totalLimitDec = totalLimitDec.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
            totalLimitStr = df1.format(totalLimitDec) + "万";
        } else {
            totalLimitStr = totalLimitDec + "";
        }
        return totalLimitStr;
    }


    public static void main(String[] args) {

        System.out.println("getScale8Up================" + getScale8Up(new BigDecimal(100.00009)));
        System.out.println(StringHelper.toUpperCase("unRepayPrincipal"));
        System.out.println(StringHelper.toUpperCase("unRepayInterest"));
    }
}
