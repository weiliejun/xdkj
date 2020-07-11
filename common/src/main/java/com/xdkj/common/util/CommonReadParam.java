/**
 * 文件名: CommonReadParam.java
 *
 * @Package com.phhc.sys.common.util
 * @Description: TODO
 * @author 罗顺锋
 * @date 2015-3-17 下午3:44:30
 * @version V1.0
 */
package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>
 * Title: CommonReadParam
 * </p>
 * <p>
 * Description: 读取系统配置文件类
 * </p>
 * <p>
 * Company: capinfo
 * </p>
 *
 * @author luoshunfeng
 * @date 2012-2-17
 */
public class CommonReadParam {
    private static Logger logger = Logger.getLogger(CommonReadParam.class);
    // private String propertyFileName;//文件名（不比输入后缀）
    private static ResourceBundle bundle;// 这个类的作用就是读取资源属性文件（properties），然后根据.properties文件的名称信息（本地化信息），匹配当前系统的国别语言信息（也可以程序指定），然后获取相应的properties文件的内容

    public CommonReadParam(String FileName) {
        bundle = ResourceBundle.getBundle(FileName);
    }

    public static String getString(String key) {
        if (key == null || key.equals("") || key.equals("null")) {
            return "";
        }
        String result = "";
        try {
            result = bundle.getString(key);
        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
        try {
            return new String(result.getBytes(EncodeType.ISO.toString()), EncodeType.UTF.toString()).toString();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * Title: main
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param args
     * @author luoshunfeng
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CommonReadParam param = new CommonReadParam("conf/jdbc");
        logger.info(param.getString("QuatotionTemplate"));
    }

}
