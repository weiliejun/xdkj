package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.admin.system.service.SysMessageService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysMessage.bean.SysMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description 系统消息管理
 * @auther: cyp
 * @UpadteDate: 2019-02-25 13:23
 */
@Controller
@RequestMapping("/sysMessage")
public class SysMessageController extends AbstractBaseController {

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private SysManagerService sysManagerService;


    /**
     * @Description 新增系统消息
     * @auther: cyp
     * @UpadteDate: 2019-02-25 13:23
     */
    @RequestMapping(value = {"/toadd"}, method = RequestMethod.GET)
    public String toAddSysManager() {
        return "/system/sysMessage/add";
    }

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String toListSysManagers(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        model.addAttribute("cxmk", request.getParameter("cxmk"));
        return "/system/sysMessage/list";
    }

    /**
     * @Description 分页查询系统消息
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 14:42
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listSysManagers(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<SysMessage> results = (Page<SysMessage>) sysMessageService.listSysMessagesByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 删除
     * @auther: zhangkele
     * @UpadteDate: 2019/2/22 15:57
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Map<String, Object> operate(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysMessage sysMessage = sysMessageService.getSysMessageById(new Integer(id));
        if (sysMessage == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该消息不存在");
            return resultMap;
        }
        SysMessage sysMessageTemp = new SysMessage();
        sysMessageTemp.setId(new Integer(id));
        //删除
        sysMessageTemp.setDataStatus(GlobalConstant.DATA_INVALID);
        sysMessageService.updateSysMessage(sysMessageTemp);
        resultMap.put("flag", "true");
        resultMap.put("msg", "删除成功");
        return resultMap;

    }


}
