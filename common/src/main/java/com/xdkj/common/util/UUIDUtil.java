package com.xdkj.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDUtil {

    /**
     * 获取一个UUID值
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取一个UUID集合
     */
    public static List<String> listUUIDs(int size) {
        List<String> uuids = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            uuids.add(getUUID());
        }
        return uuids;
    }

    public static void main(String[] args) {
        List<String> uuids = listUUIDs(10);
        for (String uuid : uuids) {
            System.out.println(uuid);
        }
    }


}
