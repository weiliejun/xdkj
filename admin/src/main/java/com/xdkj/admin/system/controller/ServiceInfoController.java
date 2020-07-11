package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.ServiceInfoService;
import com.xdkj.admin.system.service.SysDictionaryService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.serviceinfo.bean.ServiceInfo;
import com.xdkj.common.model.sysdictionary.bean.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : ServiceInfoController
 * @Description 系统 自定义服务管理
 * @auther: FENG.yanmin
 * @Update : 2019-3-11
 */
@Controller
@RequestMapping("/serviceInfo")
public class ServiceInfoController extends AbstractBaseController {
    @Autowired
    private ServiceInfoService serviceInfoService;
    @Autowired
    private SysDictionaryService sysDictionaryService;

    /**
     * @Description 跳转新增页面
     * @UpadteDate: 2019/2/22 14:37
     */
    @RequestMapping(value = {"/toadd"}, method = RequestMethod.GET)
    public String toAddSysService() {
        return "/system/serviceInfo/add";
    }

    @GetMapping(value = "/list")
    public String toListSysServices() {
        return "/system/serviceInfo/list";
    }

    /**
     * @Description 分页查询服务信息
     * @UpadteDate: 2019/3/11 14:42
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listSysServices(HttpServletRequest request) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);

        final Page<ServiceInfo> results = (Page<ServiceInfo>) serviceInfoService.listServiceInfosByParams(null);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }

    /**
     * @Description 保存服务信息（新增或修改）
     * @UpadteDate: 2019/3/11/14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateSysService(ServiceInfo serviceInfo) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            //新增
            if (serviceInfo.getId() == null) {
                serviceInfo.setDataStatus(GlobalConstant.DATA_VALID);
                serviceInfo.setCreateTime(new Date());
                serviceInfoService.addServiceInfo(serviceInfo);
                resultMap.put("flag", "true");
                resultMap.put("msg", "新增成功");
                return resultMap;
            } else {//编辑
                serviceInfo.setDataStatus(GlobalConstant.DATA_VALID);
                serviceInfo.setUpdateTime(new Date());
                serviceInfoService.updateServiceInfo(serviceInfo);
                resultMap.put("flag", "true");
                resultMap.put("msg", "编辑成功");
                return resultMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("state", "fail");
            resultMap.put("msg", "操作失败，系统异常");
            return resultMap;
        }
    }

    /**
     * @Description 删除、启用、禁用
     * @auther: fym
     * @UpadteDate: 2019/3/11 15:57
     */
    @RequestMapping(value = "/{operateType}")
    @ResponseBody
    public Map<String, Object> operateDictionary(@PathVariable String operateType, Integer id) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        ServiceInfo serviceInfo = serviceInfoService.getServiceInfoById(id);
        if (serviceInfo == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该类型不存在");
            return resultMap;
        }
        ServiceInfo temp = new ServiceInfo();
        temp.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            temp.setDataStatus("1");
            serviceInfoService.updateServiceInfo(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //启用
        if ("enable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_VALID);
            serviceInfoService.updateServiceInfo(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "启用成功");
            return resultMap;
        }
        //禁用
        if ("disable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_INVALID);
            serviceInfoService.updateServiceInfo(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "禁用成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }


    /**
     * @param
     * @Description 分页查询 所有字典列表
     * @auther: fym
     * @UpadteDate: 2019/3/13 14:42
     */
    @PostMapping(value = "/tableSelectAll/list")
    @ResponseBody
    public Map<String, Object> tableSelectAllList(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String aa = request.getParameter("selectRange");
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        params.put("status", "0");
        final Page<SysDictionary> results = (Page<SysDictionary>) sysDictionaryService.listSysDictionaryByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }
}
