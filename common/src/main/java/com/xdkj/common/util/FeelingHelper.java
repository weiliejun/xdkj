package com.xdkj.common.util;

import org.apache.log4j.Logger;

import java.io.File;

public final class FeelingHelper {
    private static Logger logger = Logger.getLogger(FeelingHelper.class);

    /**
     * @author zhangyf
     * @date 2014-04-05 将内容中对应的标签转为图片
     */
    public static String replaceContent(String content, String basePath) {
        basePath = basePath + "/assets/ui/themes/base/img/forum/feeling";
        String[] feelingStr = {":\\)", ":-\\)", ":D", ":\\(", ":mrgreen:", ":-o", ":shock:", ":\\?:", "8\\)", ":lol:", ":x", ":P", ":oops:", ":cry:", ":evil:", ":twisted:", ":roll:", ":wink:", ":\\!:", ":\\?"};
        String[] imagesStr = {"3b63d1616c5dfcf29f8a7a031aaa7cad.gif", "3b63d1616c5dfcf29f8a7a031aaa7cad.gif", "283a16da79f3aa23fe1025c96295f04f.gif", "9d71f0541cff0a302a0309c5079e8dee.gif", "feeling/ed515dbff23a0ee3241dcc0a601c9ed6.gif", "47941865eb7bbc2a777305b46cc059a2.gif", "385970365b8ed7503b4294502a458efa.gif", "0a4d7238daa496a758252d0a2b1a1384.gif", "b2eb59423fbf5fa39342041237025880.gif", "97ada74b88049a6d50a6ed40898a03d7.gif", "1069449046bcd664c21db15b1dfedaee.gif", "69934afc394145350659cd7add244ca9.gif", "499fd50bc713bfcdf2ab5a23c00c2d62.gif", "c30b4198e0907b23b8246bdd52aa1c3c.gif", "2e207fad049d4d292f60607f80f05768.gif", "908627bbe5e9f6a080977db8c365caff.gif", "2786c5c8e1a8be796fb2f726cca5a0fe.gif", "8a80c6485cd926be453217d59a84a888.gif", "9293feeb0183c67ea1ea8c52f0dbaf8c.gif",
                "136dd33cba83140c7ce38db096d05aed.gif"};
        for (int a = 0; a < feelingStr.length; a++) {
            String imageUrl = basePath + File.separator + imagesStr[a];
            String url = "<img src='" + imageUrl + "' />";
            content = content.replaceAll(feelingStr[a], url);
        }
        return content;
    }

    public static void main(String[] str) {
        String ss = ":\\?";
        String aa = "[b]好复杂[/b] :?: :( :D :shock:";
        aa = aa.replaceAll(ss, "ewewewe");
        logger.info(aa);
    }

    /**
     * @author zhangyf
     * @date 2014-04-05 编辑器字符串处理
     */
    public static String replaceHtml(String content) {
        content = content.replaceAll("\\[color=", "<font color=").replaceAll("\\[/color]", "</font>");
        content = content.replaceAll("\\[size=", "<font size=").replaceAll("\\[/size]", "</font>");
        content = content.replaceAll("\\[list]", "<li>").replaceAll("\\[/list]", "</li>");
        content = content.replaceAll("\\[", "<").replaceAll("\\]", ">");
        return content;
    }

}
