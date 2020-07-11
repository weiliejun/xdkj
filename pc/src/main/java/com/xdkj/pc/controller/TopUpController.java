package com.xdkj.pc.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userTopup.bean.UserTopup;
import com.xdkj.common.web.SessionUser;
import com.xdkj.pc.service.userManage.UserTopupService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TopUpController
 * @Description 充值相关功能类
 * @Version 1.0
 * @Author 徐赛平
 * @UpdateDate 2019/1/21 10:43
 */
@Controller
@RequestMapping("/account")
public class TopUpController extends AbstractBaseController {

    @Autowired
    private UserTopupService userTopupService;

    @Value("${resource.pictureServerURL}")
    private String pictureServerAccessURL;

    /**
     * @Description 用户充值页面
     * @auther: xsp
     * @UpadteDate: 2019/1/21 16:14
     */
    @RequestMapping("/topUp")
    public String topUpIndex(HttpServletRequest request, Model model, String topUpType) {
        UserInfo userInfo = getUserInfoBySid(request);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("topUpType", topUpType);
        return "userAccount/topUp";
    }

    /**
     * @Description 用户充值记录
     * @auther: xsp
     * @UpadteDate: 2019/1/23 17:22
     */
    @RequestMapping("/topUpRecord")
    public String topUpRecord(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String startDate, String endDate, String loadingType, Model model) {
        UserInfo userInfo = getUserInfoBySid(request);
        Map<String, Object> params = new HashMap<String, Object>();
        Integer userId = userInfo.getId();
        PageHelper.startPage(pageNum, pageSize);
        // 用户充值记录
        params.put("userId", userId);
        params.put("createTimeStart", startDate);
        params.put("createTimeEnd", endDate);
        List<UserTopup> userTopups = userTopupService.listUserTopupsByParams(params);
        PageInfo pageInfo = new PageInfo<UserTopup>(userTopups);
        // 用户充值总金额
        BigDecimal totalAmount = userTopupService.countUserTopupTotalAmount(userId);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        if ("partLoad".equals(loadingType)) {
            return "userAccount/topUpRecord::userTopUpList";
        } else {
            return "userAccount/topUpRecord";
        }
    }

    /**
     * @Description 线下转账 提交
     * @auther: xsp
     * @UpadteDate: 2019/1/24 13:59
     */
    @RequestMapping("/offLineTransfer")
    @ResponseBody
    public Map<String, ?> offLineTransfer(HttpServletRequest request, String mobile, String transAmount, String topUpType, @RequestParam(value = "transferFile") MultipartFile transferFile) {
        Map result = new HashMap();
        SessionUser sessionUser = getSessionUserBySid(request);
        if (sessionUser == null) {
            result.put("flag", "false");
            result.put("msg", "您已下线，请重新登录");
            return result;
        }
        if (transferFile.isEmpty()) {
            result.put("flag", "false");
            result.put("msg", "提交失败，请选择文件");
            return result;
        }
        try {
            UserInfo userInfo = sessionUser.getUserInfo();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userInfo", userInfo);
            params.put("transAmount", transAmount);
            params.put("topUpType", topUpType);
            params.put("mobile", mobile);
            params.put("transferFile", transferFile);
            result = userTopupService.offLineTransfer(params);
        } catch (IOException e) {
            result.put("flag", "false");
            result.put("msg", "转账失败！");
        }
        return result;
    }

}
