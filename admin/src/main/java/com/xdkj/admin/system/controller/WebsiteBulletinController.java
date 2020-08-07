package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.WebsiteBulletinService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.websiteBulletin.bean.WebsiteBulletin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description 网站公告
 * @auther: cyp
 * @UpadteDate: 2019/2/27 11:08
 */
@Controller
@RequestMapping("/websiteBulletin")
public class WebsiteBulletinController extends AbstractBaseController {

    @Autowired
    private WebsiteBulletinService websiteBulletinService;

    /**
     * @Description 新增公告页面
     * @auther: cyp
     * @UpadteDate: 2019/2/27 11:08
     */
    @RequestMapping(value = {"/system/toAdd"}, method = RequestMethod.GET)
    public String toAdd(Model model, @RequestParam(value = "id", required = false) String id, HttpServletRequest request) {
        if (id != null) {
            WebsiteBulletin websiteBulletin = websiteBulletinService.getWebsiteBulletinById(new Integer(id));
            model.addAttribute("websiteBulletin", websiteBulletin);
        } else {
            model.addAttribute("websiteBulletin", new WebsiteBulletin());
        }
        return "/system/websiteBulletin/add";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String toListWebsiteBulletins(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/system/websiteBulletin/list";
    }

    /**
     * @Description 分页查询公告信息
     * @auther: cyp
     * @UpadteDate: 2019/2/27 11:08
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWebsiteBulletins(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<WebsiteBulletin> results = (Page<WebsiteBulletin>) websiteBulletinService.listWebsiteBulletinByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());

        //记录业务日志
        saveBusinessLog("网站公告管理", "查询网站公告信息", params);

        return resultMap;
    }


    /**
     * @Description 保存公告信息（新增或修改）
     * @auther: cyp
     * @UpadteDate: 2019/2/27 14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateWebsiteBulletin(WebsiteBulletin websiteBulletin) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysManager currentManager = getSessionSysManager();
        //新增
        if (websiteBulletin.getId() == null) {
            //设置初始化值
            websiteBulletin.setStatus(GlobalConstant.STATUS_INVALID);//使用
            websiteBulletin.setDataStatus(GlobalConstant.DATA_VALID);
            websiteBulletin.setPublisherId(currentManager.getId());
            websiteBulletin.setPublisherName(currentManager.getName());
            websiteBulletin.setPublishStatus("1");//默认发布
            websiteBulletin.setClicks(0);
            websiteBulletin.setDataStatus("1");
            websiteBulletin.setCreateTime(new Date());
            websiteBulletinService.addWebsiteBulletin(websiteBulletin);
            resultMap.put("flag", "true");
            resultMap.put("msg", "公告保存成功");

            //记录业务日志
            saveBusinessLog("网站公告管理", "新增公告信息", websiteBulletin);

            return resultMap;
        } else {//编辑
            websiteBulletin.setPublisherId(currentManager.getId());
            websiteBulletin.setPublisherName(currentManager.getName());
            websiteBulletin.setUpdateTime(new Date());
            websiteBulletinService.updateWebsiteBulletin(websiteBulletin);
            resultMap.put("flag", "true");
            resultMap.put("msg", "公告修改成功");

            //记录业务日志
            saveBusinessLog("网站公告管理", "修改公告信息", websiteBulletin);

            return resultMap;
        }
    }

    /**
     * @Description 删除、启用、禁用
     * @auther: zhangkele
     * @UpadteDate: 2019/2/22 15:57
     */
    @RequestMapping(value = "/{operateType}")
    @ResponseBody
    public Map<String, Object> operate(@PathVariable String operateType, Integer id) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        WebsiteBulletin websiteBulletin = websiteBulletinService.getWebsiteBulletinById(id);
        if (websiteBulletin == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该公告不存在");
            return resultMap;
        }
        WebsiteBulletin websiteBulletinTemp = new WebsiteBulletin();
        websiteBulletinTemp.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            websiteBulletinTemp.setDataStatus(GlobalConstant.DATA_INVALID);
            websiteBulletinService.updateWebsiteBulletin(websiteBulletinTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //启用（发布）
        if ("enable".equals(operateType)) {
            websiteBulletinTemp.setStatus("1");
            websiteBulletinService.updateWebsiteBulletin(websiteBulletinTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "发布成功");
            return resultMap;
        }
        //禁用（停用）
        if ("disable".equals(operateType)) {
            websiteBulletinTemp.setStatus("0");
            websiteBulletinService.updateWebsiteBulletin(websiteBulletinTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "停用成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }
}
