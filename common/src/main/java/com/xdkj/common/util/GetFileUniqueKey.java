package com.xdkj.common.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GetFileUniqueKey {

    /**
     * @description 生成22位编码
     * @version 1.0
     * @update 2017-6-1 15:01:02
     */
    public static String generateIdentifier() {

        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String uuid = format.format(new Date().getTime()) + new Double(Math.random() * 100000).intValue();
        while (uuid.length() < 22) {
            uuid = uuid + "0";
        }
        uuid = uuid.substring(2);

        return uuid;
    }

    /**
     * @description 生成6位密码
     * @version 1.0
     * @update 2017-6-1 15:01:02
     */
    public static String generatePwd() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 6; i++)
            str = str.append(String.valueOf(array[i]));
        return str.toString();
    }

    /**
     * @description 解析图片path
     *              {“id”:””,“name”:””,“path”:””,”thumbnail”:””,”type”:””}
     * @version 1.0
     * @update 2017-6-12 15:01:02
     */


    /**
     * @param width
     * @param height
     * @param wMax
     * @param hMax
     * @return
     * @description 圈子评论展示小图，设置最大150 * 100
     * @version 1.0
     * @author 冯艳敏
     * @update 2018年12月6日 上午11:03:10
     */
    public static Map<String, Object> resizeImageWH(int width, int height, int wMax, int hMax) {
        Map<String, Object> setMap = new HashMap<String, Object>();
        int w0 = width, h0 = height;
        float scale = width / height;
        if (scale < 0.5) {
            w0 = hMax; //100
            h0 = wMax; // 150
        } else if (scale > 2) {
            w0 = wMax; //150
            h0 = hMax; // 100
        } else {
            if (width > height) {
                h0 = hMax;
                w0 = (int) (h0 * scale);
            } else {
                w0 = hMax;
                h0 = (int) (w0 / scale);
            }
        }
        setMap.put("w0", w0);
        setMap.put("h0", h0);
        return setMap;
    }

    public static void main(String[] args) {
        Map<String, Object> mm = resizeImageWH(102, 360, 150, 100);
        Map<String, Object> mm2 = resizeImageWH(100, 100, 150, 100);
        System.out.println(mm.toString());
        System.out.println(mm2.toString());
		/*String aa = "{'path':'/upload/rulaibao/appimage/commentPhoto/tesssssst/','photoNmae':'18112311153485771776.jpg','smallPhotoNmae':'small_18112311153485771776.jpg','width':'990','height':'2840'}";
		System.out.println(GetFileUniqueKey.getPicturePath("o_o", aa,"path")+GetFileUniqueKey.getListPath( aa,"photoNmae"));
		System.out.println(GetFileUniqueKey.getListPath( aa,"33"));
		*/
		/*String tt = "[{'id':'18042410140387157491'},{'id':'180424101403999999'}]";
		JSONArray jsonArr = JSONArray.fromObject(tt);
		tt = tt.replace("[", "").replace("]", "");
		JSONObject photoJson = JSONObject.fromObject(tt);
		System.out.println("========"+photoJson);
		*/
	/*	JSONArray jsonArr = JSONArray.fromObject(tt);
		for (int a = 0; a < jsonArr.size(); a++) {
			System.out.println(jsonArr.getJSONObject(1));
		}
		System.out.println(jsonArr);
		String path = null;*/
		
		
		/*UploadFile uf = new UploadFile();
		uf.setId("001");
		uf.setName("feng");
		uf.setPath("aa/opps.jpg");
		uf.setType("jpg");
		uf.setThumbnail("aa/smapll_opps.jpg");
		JSONObject pathObject = JSONObject.fromObject(uf);
		System.out.println(pathObject);
		String birth = GetFileUniqueKey.getPicturePath("", pathObject.toString(), "thumbnail");
		System.out.println("Thumbnail=============="+birth);*/
    }
}
