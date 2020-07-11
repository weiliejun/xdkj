package com.xdkj.admin.business.user.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.business.user.service.UserTopupService;
import com.xdkj.admin.system.service.UserInfoService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userTopup.bean.UserTopup;
import com.xdkj.common.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName UserTopUpController
 * @Description 用一句话说明这个方法做什么
 * @Version 1.0
 * @Author 徐赛平
 * @UpdateDate 2019/3/5 10:22
 */
@Controller
public class TopUpManageController extends AbstractBaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserTopupService userTopupService;

    @RequestMapping(value = "/userTopUp", method = RequestMethod.GET)
    public String topUpIndex() {

        return "/business/user/topUp/index";
    }

    @RequestMapping(value = "/userTopUp", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addTopUp(Integer userId, String topupType, BigDecimal topupAmount) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        if (userId == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "您选择的用户不存在");
            return resultMap;
        }
        UserInfo userInfo = userInfoService.getUserById(userId);
        if (userInfo == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "您选择的用户不存在");
            return resultMap;
        }
        UserTopup userTopup = new UserTopup();
        userTopup.setUserId(userId);
        userTopup.setUserName(userInfo.getUserName());
        userTopup.setTopupAmount(topupAmount);
        userTopup.setTopupType(topupType);
        userTopup.setStatus("success");
        userTopup.setUserMobile(userInfo.getMobile());
        userTopup.setDataStatus("0");
        userTopup.setCreateTime(new Date());
        userTopup.setUpdateTime(new Date());
        userTopup.setOrdId(RandomUtil.getSerialNumber());
        userTopupService.addUserTopup(userTopup);
        resultMap.put("flag", "true");
        resultMap.put("msg", "充值成功");
        return resultMap;
    }

    @RequestMapping(value = {"/userTopUp/list"}, method = RequestMethod.GET)
    public String toListUserTopUps(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/business/user/topUp/list";
    }

    /**
     * @Description 分页查询用户充值信息
     * @auther: xsp
     * @UpadteDate: 2019/3/6 10:42
     */
    @PostMapping(value = "/userTopUp/list")
    @ResponseBody
    public Map<String, Object> listUserTopUps(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        Page<UserTopup> results = (Page<UserTopup>) userTopupService.listUserTopupsByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }

    @RequestMapping("/userTopUp/editStatus")
    @ResponseBody
    public Map<String, Object> editStatus(@RequestParam(value = "userTopupId", required = true) Integer userTopupId) {
        Map<String, Object> resultMap = userTopupService.updateTopUpStatus(userTopupId);
        return resultMap;
    }

}
