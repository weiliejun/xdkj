package com.xdkj.common.components.verifycode;

import java.awt.*;

/**
 * ttf字体文件
 */
public class ImgFontByte {
    public Font getFont(int fontHeight) {
        try {
            return new Font(Font.SANS_SERIF, Font.ITALIC, fontHeight);
        } catch (Exception e) {
            return new Font(Font.SANS_SERIF, Font.BOLD, fontHeight);
        }
    }
}