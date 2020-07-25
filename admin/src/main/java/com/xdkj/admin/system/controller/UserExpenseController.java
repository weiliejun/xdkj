package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.ServiceInfoService;
import com.xdkj.admin.system.service.UserExpenseService;
import com.xdkj.admin.system.service.UserInfoService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userExpense.bean.UserExpense;
import com.xdkj.common.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : UserExpenseController
 * @Description 用户消费列表
 * @auther: zhangkele
 * @UpadteDate: 2019/2/21 14:35
 */
@Controller
@RequestMapping("/userExpense")
public class UserExpenseController extends AbstractBaseController {

    @Autowired
    private UserExpenseService userExpenseService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ServiceInfoService serviceInfoService;

    /**
     * @Description 新增用户消费页面
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:37
     */
    @RequestMapping(value = {"/toadd"}, method = RequestMethod.GET)
    public String toAddUserExpense() {
        return "/system/userExpense/add";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String toListUserExpenses(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/system/userExpense/list";
    }

    /**
     * @Description 分页查询用户消费信息
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:42
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listUserExpenses(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        PageHelper.orderBy("CREATE_TIME desc");
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<Map<String, Object>> results = (Page<Map<String, Object>>) userExpenseService.listUserExpensesMapByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 保存用户消费信息（新增或修改）
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateUserExpense(UserExpense userExpense) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        UserInfo userInfo = userInfoService.getUserById(userExpense.getUserId());
        //新增
        if (userExpense.getId() == null) {
            //设置初始化值
            userExpense.setDataStatus(GlobalConstant.DATA_VALID);
            userExpense.setAppkey(userInfo.getAppkey());
            userExpense.setOrdId(RandomUtil.getSerialNumber());//随机生成的策略
            userExpense.setStatus("0");//扣款成功是0；
            userExpenseService.addUserExpense(userExpense);
            resultMap.put("flag", "true");
            resultMap.put("msg", "消费信息保存成功");
            return resultMap;
        } else {//编辑
            userExpense.setUpdateTime(new Date());
            userExpenseService.updateUserExpense(userExpense);
            resultMap.put("flag", "true");
            resultMap.put("msg", "消费信息修改成功");
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
        UserExpense userExpense = userExpenseService.getUserExpenseById(id);
        if (userExpense == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该消费记录不存在");
            return resultMap;
        }
        userExpense.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            userExpense.setDataStatus(GlobalConstant.DATA_INVALID);
            userExpenseService.updateUserExpense(userExpense);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //启用
        if ("enable".equals(operateType)) {
            userExpense.setStatus("0");
            userExpenseService.updateUserExpense(userExpense);
            resultMap.put("flag", "true");
            resultMap.put("msg", "启用成功");
            return resultMap;
        }
        //禁用
        if ("disable".equals(operateType)) {
            userExpense.setStatus("1");
            userExpenseService.updateUserExpense(userExpense);
            resultMap.put("flag", "true");
            resultMap.put("msg", "禁用成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }


    /**
     * @Description 分页查询用户信息
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:42
     */
    @PostMapping(value = "/tochooseUser/list")
    @ResponseBody
    public Map<String, Object> tochooseUserlistUserExpenses(HttpServletRequest request) {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<UserInfo> results = (Page<UserInfo>) userInfoService.listUserInfosByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 分页查询所有服务信息
     * @auther: cyp
     * @UpadteDate: 2019/2/21 14:42
     */
    @PostMapping(value = "/toChooseServiceInfo/list")
    @ResponseBody
    public Map<String, Object> toChooseServiceInfo(HttpServletRequest request) {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<ServiceInfo> results = (Page<ServiceInfo>) serviceInfoService.listServiceInfosByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }
}
