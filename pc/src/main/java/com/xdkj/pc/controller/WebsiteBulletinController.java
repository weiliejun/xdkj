package com.xdkj.pc.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xdkj.common.constant.ApplicationSessionKeys;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.userWebsiteBulletinRead.bean.UserWebsiteBulletinRead;
import com.xdkj.common.model.websiteBulletin.bean.WebsiteBulletin;
import com.xdkj.common.util.StringHelper;
import com.xdkj.pc.service.infoPublishManage.WebsiteBulletinService;
import com.xdkj.pc.service.userManage.UserWebsiteBulletinReadService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class WebsiteBulletinController extends AbstractBaseController {

    @Autowired
    private WebsiteBulletinService websiteBulletinService;
    @Autowired
    private UserWebsiteBulletinReadService userWebsiteBulletinReadService;

    /**
     * @Description 网站公告列表
     * @auther: cyp
     * @UpadteDate: 2019-01-22 17:04
     */
    @RequestMapping(value = {"/account/websiteBulletin/list"}, method = RequestMethod.GET)
    public String sysMessageIndex(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);
        Map<String, Object> params = new HashMap<String, Object>();

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(pageNum, pageSize);

        //startPage后紧跟的这个查询就是分页查询
        params.put("status", "0");
        params.put("publishStatus", "0");
        List<Map<String, Object>> websiteBulletinsList = websiteBulletinService.listWebsiteBulletinsByParams(params);
        if (websiteBulletinsList != null && websiteBulletinsList.size() > 0) {
            //循环遍历数据，判断已读未读进行添加
            int readStatus = 1;//默认未读
            for (int i = 0; i < websiteBulletinsList.size(); i++) {
                int bulletinId = Integer.parseInt(websiteBulletinsList.get(i).get("id").toString());
                UserWebsiteBulletinRead userWebsiteBulletinRead = userWebsiteBulletinReadService.getUserWebsiteBulletinReadByBulletinId(bulletinId, userInfo.getId());
                if (userWebsiteBulletinRead != null) {
                    readStatus = 0;
                } else {
                    readStatus = 1;
                }
                websiteBulletinsList.get(i).put("readStatus", readStatus);
            }
        }

        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<Map<String, Object>>(websiteBulletinsList);

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        if ("partLoad".equals(loadingType)) {
            return "userAccount/websiteBulletin::websiteBulletinList";
        } else {
            return "userAccount/websiteBulletin";
        }
    }

    @RequestMapping("/account/websiteBulletin/detail/{id}")
    public String details(HttpServletRequest request, Model model, @PathVariable("id") String id) {
        UserInfo userInfo = getUserInfoBySid(request);
        int idInt = Integer.parseInt(id);
        WebsiteBulletin websiteBulletin = null;
        if (StringHelper.isNotEmpty(id)) {
            websiteBulletin = websiteBulletinService.getWebsiteBulletinById(idInt);
        }
        String publishStatus = websiteBulletin.getPublishStatus();
        String dataStatus = websiteBulletin.getDataStatus();
        //更新点击次数(数据有效且发布)
        if (publishStatus.equals("0") && dataStatus.equals("0")) {
            int clicks = websiteBulletin.getClicks();
            clicks = clicks + 1;
            websiteBulletin.setClicks(clicks);
            websiteBulletinService.updateWebsiteBulletin(websiteBulletin);
        }
        //插入一条用户网站公告已查阅的数据
        if (StringHelper.isNotEmpty(String.valueOf(userInfo.getId()))) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userInfo.getId());
            param.put("bulletinId", idInt);
            //用户网站已读消息表中数据<=0,说明未读，新增一条数据
            int countBulletinReadNum = userWebsiteBulletinReadService.countUserWebsiteBulletinReadByParam(param);
            if (countBulletinReadNum <= 0) {
                UserWebsiteBulletinRead userWebsiteBulletinRead = new UserWebsiteBulletinRead();
                userWebsiteBulletinRead.setBulletinId(idInt);
                userWebsiteBulletinRead.setUserId(userInfo.getId());
                userWebsiteBulletinRead.setDataStatus("0");
                userWebsiteBulletinRead.setCreateTime(new Date());
                userWebsiteBulletinReadService.addUserWebsiteBulletinRead(userWebsiteBulletinRead);
                //再次查询公告已发布的有效消息表
                param.put("publishStatus", "0");
                countBulletinReadNum = userWebsiteBulletinReadService.countUserWebsiteBulletinReadByParam(param);
                //session取出系统消息的数量 - countBulletinReadNum；两数之差放入到session
                int messageNums = (Integer) request.getSession().getAttribute(ApplicationSessionKeys.SYS_MESSAGE_NUMS_COUNT);
                int countSysMessages = messageNums - countBulletinReadNum;
                if (countSysMessages <= 0) {
                    countSysMessages = 0;
                }
                request.getSession().setAttribute(ApplicationSessionKeys.SYS_MESSAGE_NUMS_COUNT, countSysMessages);
            }
        }
        model.addAttribute("websiteBulletin", websiteBulletin);
        return "userAccount/websiteBulletinDetail";
    }

}
