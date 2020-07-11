package com.xdkj.admin.portal.controller;

import com.xdkj.common.components.verifycode.VerifyCodeImageGenarator;
import com.xdkj.common.constant.ApplicationSessionKeys;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成验证码
 */
@Controller
public class ImageCodeController {

    public static final int WIDTH = 120;
    public static final int HEIGHT = 30;

    /**
     * @Description 生成图形验证码
     */
    @RequestMapping(value = {"/get/imgcode"}, method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        VerifyCodeImageGenarator vCode = new VerifyCodeImageGenarator(WIDTH, HEIGHT, 4, 0);
        //session存放图形验证码
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute(ApplicationSessionKeys.LOGIN_VERIFYCODE, vCode.getCode());
        vCode.write(response.getOutputStream());
    }

}
