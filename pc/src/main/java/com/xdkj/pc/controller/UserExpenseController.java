package com.xdkj.pc.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userExpense.bean.UserExpense;
import com.xdkj.pc.service.userManage.UserExpenseService;
import com.xdkj.pc.service.userManage.UserTopupService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户消费相关功能类
 * @Author 徐赛平
 * @UpdateDate 2019/1/21 15:35
 */
@Controller
@RequestMapping("/account")
public class UserExpenseController extends AbstractBaseController {

    @Autowired
    private UserExpenseService userExpenseService;

    @Autowired
    private UserTopupService userTopupService;

    /**
     * @Description 用户消费记录列表
     * @auther: xsp
     * @UpadteDate: 2019/1/21 16:14
     */
    @RequestMapping("/userExpenseRecord")
    public String topUpRecordIndex(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String startDate, String endDate, String expenseType, String loadingType, Model model) {
        UserInfo userInfo = getUserInfoBySid(request);
        Map<String, Object> params = new HashMap<String, Object>();
        Integer userId = userInfo.getId();
        PageHelper.startPage(pageNum, pageSize);
        // 用户消费记录
        params.put("userId", userId);
        params.put("createTimeStart", startDate);
        params.put("createTimeEnd", endDate);
        params.put("expenseType", expenseType);
        List<UserExpense> userExpenses = userExpenseService.listUserExpensesByParams(params);
        PageInfo pageInfo = new PageInfo<UserExpense>(userExpenses);
        // 用户充值总金额
        BigDecimal totalAmount = userTopupService.countUserTopupTotalAmount(userId);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("expenseType", expenseType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        if ("partLoad".equals(loadingType)) {
            return "userAccount/expenseRecord::userExpenseList";
        } else {
            return "userAccount/expenseRecord";
        }
    }
}
