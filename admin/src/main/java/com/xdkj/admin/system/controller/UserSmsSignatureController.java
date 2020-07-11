package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.userSmsTemplateApplication.bean.UserSmsTemplateApplication;
import com.xdkj.admin.system.service.UserExpenseService;
import com.xdkj.admin.system.service.UserInfoService;
import com.xdkj.admin.system.service.UserSmsTemplateApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : UserSmsSignatureController
 * @Description 用户短信签名列表
 * @auther: cyp
 * @UpadteDate: 2019/2/21 14:35
 */
@Controller
@RequestMapping("/userSmsSignature")
public class UserSmsSignatureController extends AbstractBaseController {

    @Autowired
    private UserExpenseService userExpenseService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserSmsTemplateApplicationService userSmsTemplateApplicationService;

    /**
     * @Description 新增用户短信签名页面
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:37
     */
    @RequestMapping(value = {"/toadd"}, method = RequestMethod.GET)
    public String toAddUserExpense() {
        return "/system/userSmsSignature/add";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String toListUserSmsSignature(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/system/userSmsSignature/list";
    }

    /**
     * @Description 分页查询用户消费信息
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:42
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listUserSmsSignature(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<UserSmsTemplateApplication> results = (Page<UserSmsTemplateApplication>) userSmsTemplateApplicationService.listUserSmsTemplateApplicationsByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 保存用户短信信息签名（修改）
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateUserSmsSignature(UserSmsTemplateApplication userSmsTemplateApplication) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        //新增
        if (userSmsTemplateApplication.getId() == null) {
            //设置初始化值
            userSmsTemplateApplication.setDataStatus(GlobalConstant.DATA_VALID);
            userSmsTemplateApplication.setCreateTime(new Date());
            userSmsTemplateApplicationService.addUserSmsTemplateApplication(userSmsTemplateApplication);
            resultMap.put("flag", "true");
            resultMap.put("msg", "短信签名保存成功");
            return resultMap;
        } else {//编辑
            userSmsTemplateApplicationService.updateUserSmsTemplateApplication(userSmsTemplateApplication);
            resultMap.put("flag", "true");
            resultMap.put("msg", "短信签名修改成功");
            return resultMap;
        }
    }

    /**
     * @Description 删除、启用、禁用
     * @auther: cyp
     * @UpadteDate: 2019/2/22 15:57
     */
    @RequestMapping(value = "/{operateType}")
    @ResponseBody
    public Map<String, Object> operate(@PathVariable String operateType, Integer id) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        UserSmsTemplateApplication userSmsTemplateApplication = userSmsTemplateApplicationService.getUserSmsTemplateApplicationById(id);
        if (userSmsTemplateApplication == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该短信签名不存在");
            return resultMap;
        }
        userSmsTemplateApplication.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            userSmsTemplateApplication.setDataStatus(GlobalConstant.DATA_INVALID);
            userSmsTemplateApplicationService.updateUserSmsTemplateApplication(userSmsTemplateApplication);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //通过
        if ("enable".equals(operateType)) {
            userSmsTemplateApplication.setReviewStatus("0");
            userSmsTemplateApplicationService.updateUserSmsTemplateApplication(userSmsTemplateApplication);
            resultMap.put("flag", "true");
            resultMap.put("msg", "审核通过");
            return resultMap;
        }
        //拒绝
        if ("disable".equals(operateType)) {
            userSmsTemplateApplication.setReviewStatus("1");
            userSmsTemplateApplicationService.updateUserSmsTemplateApplication(userSmsTemplateApplication);
            resultMap.put("flag", "true");
            resultMap.put("msg", "审核拒绝成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }

}
