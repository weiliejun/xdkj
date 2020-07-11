package com.xdkj.common.util;

/**
 * @Description: redis 工具类
 */
public class RedisUtil {
    /**
     * 系统标识
     */
    public static final String INVEST_PREFIX = "invest";//投资端

    public static final String LOAN_PREFIX = "loan";//融资端

    public static final String CONSOLE_PREFIX = "console";//管理后台

    public static final String GLOBAL_PREFIX = "global";//全局范围

    /**
     * 分割字符，默认[:]，使用[:]分隔符，在rdm管理工具中，可分组查看
     */
    private static final String KEY_SPLIT_CHAR = ":";

    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String module, String func, String... args) {
        return keyBuilder(null, module, func, args);
    }

    /**
     * redis的key键规则定义
     *
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String module, String func, String objStr) {
        return keyBuilder(null, module, func, new String[]{objStr});
    }

    /**
     * redis的key键规则定义
     *
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param objStr 对象.toString()
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, String objStr) {
        return keyBuilder(prefix, module, func, new String[]{objStr});
    }

    /**
     * redis的key键规则定义
     *
     * @param prefix 项目前缀
     * @param module 模块名称
     * @param func   方法名称
     * @param args   参数..
     * @return key
     */
    public static String keyBuilder(String prefix, String module, String func, String... args) {
        // 项目前缀
        if (prefix == null) {
            prefix = GLOBAL_PREFIX;
        }
        StringBuilder key = new StringBuilder(prefix);

        if (module != null) {
            key.append(KEY_SPLIT_CHAR).append(module);
        }

        if (func != null) {
            key.append(KEY_SPLIT_CHAR).append(func);
        }

        for (String arg : args) {
            if (arg != null) {
                key.append(KEY_SPLIT_CHAR).append(arg);
            }
        }
        return key.toString();
    }

    /**
     * redis的key键规则定义
     *
     * @param redisEnum 枚举对象
     * @param objStr    对象.toString()
     * @return key
     */
    public static String keyBuilder(RedisEnum redisEnum, String objStr) {
        return keyBuilder(redisEnum.getKeyPrefix(), redisEnum.getModule(), redisEnum.getFunc(), objStr);
    }

    /**
     * redis的key键规则定义
     *
     * @param redisEnum 枚举对象
     * @param args      参数..
     * @return key
     */
    public static String keyBuilder(RedisEnum redisEnum, String... args) {
        return keyBuilder(redisEnum.getKeyPrefix(), redisEnum.getModule(), redisEnum.getFunc(), args);
    }
}