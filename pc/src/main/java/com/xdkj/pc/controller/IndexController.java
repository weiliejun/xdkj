package com.xdkj.pc.controller;

import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.websiteAdvertise.bean.WebsiteAdvertise;
import com.xdkj.common.model.websiteCase.bean.WebsiteCase;
import com.xdkj.pc.service.infoPublishManage.WebsiteAdvertiseService;
import com.xdkj.pc.service.infoPublishManage.WebsiteCaseService;
import com.xdkj.pc.service.mbigerServiceManage.MbigerService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController extends AbstractBaseController {

    @Value("${resource.pictureServerURL}")
    private String pictureServerAccessURL;
    @Autowired
    private WebsiteAdvertiseService advertiseService;
    @Autowired
    private MbigerService mbigerService;

    @Autowired
    private WebsiteCaseService websiteCaseService;

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        //轮播图
        // /upload/advertise/picture/18042015112506716443/1524208285139_1872130543935674524.jpg
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "carousel");
        List<WebsiteAdvertise> advertiseList = advertiseService.listAdvertisesByParams(params);
        model.addAttribute("advertiseList", advertiseList);

        // 查询 案例list
        params.clear();
        params.put("isHomePageShow", "0");
        List<WebsiteCase> caseList = websiteCaseService.listCasesByParams(params);
        model.addAttribute("caseList", caseList);

        model.addAttribute("pictureServerAccessURL", pictureServerAccessURL);

        return "index";
    }


    @RequestMapping("/saas")
    // 云计算SAAS
    public String saas() {
        return "saas/index";
    }

    @RequestMapping("/iaas")
    // 云计算IAAS
    public String iaasIndex(Model model) {
        List<ServiceInfo> iaasServiceList = mbigerService.listServiceInfosByServiceModule("IAAS");
        model.addAttribute("iaasServiceList", iaasServiceList);
        return "home/iaas";
    }

    @RequestMapping("/paas")
    // 云计算PAAS
    public String paas(Model model) {
        List<ServiceInfo> paasServiceList = mbigerService.listServiceInfosByServiceModule("PAAS");
        model.addAttribute("paasServiceList", paasServiceList);
        return "home/paas";
    }

    @RequestMapping("/customization")
    public String customizationSystem(Model model) {
        // 查询 案例list
        Map<String, String> params = new HashMap<String, String>();
        List<WebsiteCase> caseList = websiteCaseService.listCasesByParams(params);
        model.addAttribute("caseList", caseList);

        return "home/customization";
    }

    @RequestMapping("/aboutUs")
    // 关于我们
    public String aboutUs() {
        return "home/aboutUs";
    }
}
