package com.xdkj.pc.controller;

import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName PAASCenterController
 * @Description 账户中心-云计算PAAS（申请试用）
 * @Version 1.0
 * @Author Feng.yanmin
 * @UpdateDate 2019-1-21 16:43:54
 */
@Controller
@RequestMapping("/account")
public class PAASCenterController extends AbstractBaseController {
    private static Logger logger = org.apache.log4j.Logger.getLogger(PAASCenterController.class);
    @Autowired
    private MbigerService mbigerService;


    /**
     * @Description 账户中心---PAAS服务页面
     * @auther: FENG.yanmin
     * @UpadteDate: 2019/1/28 16:15
     */
    @RequestMapping(value = {"/paasCenter/{serviceType}"})
    public String paasCenterIndex(HttpServletRequest request, Model model, @PathVariable String serviceType) {
        // 查询 oa 服务信息
        ServiceInfo serviceInfo = mbigerService.getServiceInfoByCode(serviceType);

        model.addAttribute("serviceInfo", serviceInfo);
        model.addAttribute("serviceType", serviceType);
        return "userAccount/paasCenter/applyforUsing";
    }


}
