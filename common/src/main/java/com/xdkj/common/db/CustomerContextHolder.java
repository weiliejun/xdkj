package com.xdkj.common.db;

import java.util.ArrayList;
import java.util.List;


public class CustomerContextHolder {
    public static final String DATA_SOURCE_X = "x_master";
    public static final String DATA_SOURCE_X_SLAVE = "x_slave";
    private static final ThreadLocal<List<String>> serviceDataSourceHolder = new ThreadLocal<List<String>>();

    /**
     * 将指定的service绑定到指定的数据源上
     *
     * @param
     * @param datasourcce
     */
    public static void setServiceDataSource(String datasourcce) {
        List<String> list = serviceDataSourceHolder.get();
        if (list == null) {
            list = new ArrayList<String>();
        }
        if (list.add(datasourcce)) {
            serviceDataSourceHolder.set(list);
        }
    }

    public static String getServiceDataSource() {
        List<String> list = serviceDataSourceHolder.get();
        if (list != null && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        //默认数据源
        return DATA_SOURCE_X;
    }

    public static void removeServiceDataSource() {
        List<String> list = serviceDataSourceHolder.get();
        if (list != null && list.size() > 0) {
            list.remove(list.size() - 1);
        }
    }
}