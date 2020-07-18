package com.xdkj.pc.controller;

import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IAASCenterController
 * @Description 账户中心-云计算IAAS（预约咨询）
 * @Version 1.0
 * @Author Feng.yanmin
 * @UpdateDate 2019-1-21 16:43:54
 */
@Controller
@RequestMapping("/account")
public class GXKFCenterController extends AbstractBaseController {
    @Autowired
    private MbigerService mbigerService;

    /**
     * 账户用心-gxkf服务页面
     *
     * @param request
     * @param model
     * @param serviceType
     * @return
     */
    @RequestMapping("/gxkfCenter/{serviceType}")
    public String gxkfCenterIndex(HttpServletRequest request, Model model, @PathVariable String serviceType) {
        // 查询  服务信息
        ServiceInfo serviceInfo = mbigerService.getServiceInfoByCode(serviceType);
        model.addAttribute("serviceInfo", serviceInfo);
        model.addAttribute("serviceType", serviceType);
        return "userAccount/gxkfCenter/consulting";
    }


}
