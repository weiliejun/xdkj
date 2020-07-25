package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.SysMessageTmplService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysMessageTmpl.bean.SysMessageTmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName SysMessageTmplController
 * @Description 系统消息模板功能类
 * @Author 徐赛平
 * @UpdateDate 2019/3/11 10:43
 */
@Controller
public class SysMessageTmplController extends AbstractBaseController {

    @Autowired
    private SysMessageTmplService sysMessageTmplService;

    @RequestMapping(value = "/sysMessageTmpl/list", method = RequestMethod.GET)
    public String list() {
        return "/system/sysMessageTmpl/list";
    }

    @RequestMapping(value = "/sysMessageTmpl/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listSysMessageTmpls(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        Page<SysMessageTmpl> results = (Page<SysMessageTmpl>) sysMessageTmplService.listSysMessageTmplsByParam(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }

    @RequestMapping("/sysMessageTmpl/toAdd")
    public String addIndex(Model model, @RequestParam(value = "id", required = false) Integer id, HttpServletRequest request) {
        if (id != null) {
            SysMessageTmpl sysMessageTmpl = sysMessageTmplService.getSysMessageTmplById(id);
            model.addAttribute("sysMessageTmpl", sysMessageTmpl);
        } else {
            model.addAttribute("sysMessageTmpl", new SysMessageTmpl());
        }
        return "/system/sysMessageTmpl/add";
    }

    @RequestMapping("/sysMessageTmpl/addOrUpdate")
    @ResponseBody
    public Map<String, Object> addOrUpdate(SysMessageTmpl sysMessageTmpl) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String msg;

        if (sysMessageTmpl.getId() == null) {
            // 新增
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("busiType", sysMessageTmpl.getBusiType());
            params.put("type", sysMessageTmpl.getType());
            List<SysMessageTmpl> sysMessageTmpls = sysMessageTmplService.listSysMessageTmplsByParam(params);
            if (sysMessageTmpls != null && sysMessageTmpls.size() > 0) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "此业务类型已存在");
                return resultMap;
            }
            sysMessageTmpl.setDataStatus(GlobalConstant.DATA_VALID);
            sysMessageTmpl.setCreatorId(getSessionSysManager().getId());
            sysMessageTmpl.setCreatorName(getSessionSysManager().getName());
            sysMessageTmpl.setCreateTime(new Date());
            sysMessageTmpl.setStatus(GlobalConstant.DATA_VALID);
            sysMessageTmplService.addSysMessageTmpl(sysMessageTmpl);
            msg = "模板保存成功";
        } else {
            // 修改
            sysMessageTmplService.updateSysMessageTmpl(sysMessageTmpl);
            msg = "模板修改成功";
        }
        resultMap.put("flag", "true");
        resultMap.put("msg", msg);
        return resultMap;
    }

    @RequestMapping(value = "/sysMessageTmpl/{operateType}")
    @ResponseBody
    public Map<String, Object> operate(@PathVariable String operateType, Integer id) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String msg = "成功";
        SysMessageTmpl sysMessageTmpl = sysMessageTmplService.getSysMessageTmplById(id);
        if (sysMessageTmpl == null) {
            resultMap.put("flag", "true");
            resultMap.put("msg", "该模板不存在");
            return resultMap;
        }
        SysMessageTmpl temp = new SysMessageTmpl();
        temp.setId(id);
        // 删除
        if ("delete".equals(operateType)) {
            temp.setDataStatus(GlobalConstant.STATUS_INVALID);
            msg = "删除成功";
        }
        // 启用
        if ("enable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_VALID);
            msg = "启用成功";
        }
        // 禁用
        if ("disable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_INVALID);
            msg = "禁用成功";
        }
        sysMessageTmplService.updateSysMessageTmpl(temp);
        resultMap.put("flag", "true");
        resultMap.put("msg", msg);
        return resultMap;
    }

}
