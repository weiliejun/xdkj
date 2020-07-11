package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.CustomerAppointmentService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.model.customerAppointment.bean.CustomerAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : ServiceApplyController
 * @Description 服务申请管理
 * module(小写): customization -定制研发咨询，iaas -云计算IAAS申请试用, paas - 云计算PAAS申请试用,saas - 免费服务API申请
 * @auther: FENG.yanmin
 * @Update : 2019-3-06
 */
@Controller
@RequestMapping("/serviceApply")
public class ServiceApplyController extends AbstractBaseController {
    @Autowired
    private CustomerAppointmentService customerAppointmentService;

    @GetMapping(value = "/{module}/list")
    public String toListServiceApply(@PathVariable String module) {
        return "/system/serviceApply/" + module + "list";
    }

    /**
     * @Description 分页查询sassApply信息
     * 查询参数说明：
     * serviceModule: 'SAAS','PAAS','IAAS','CUSTOMIZATION'
     * @UpadteDate: 2019/3/06 14:42
     */
    @PostMapping(value = "/{module}/list")
    @ResponseBody
    public Map<String, Object> listSaaSApply(HttpServletRequest request, @PathVariable String module) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        params.put("serviceModule", module.toUpperCase());
        final Page<Map<String, Object>> results = (Page<Map<String, Object>>) customerAppointmentService.listCustomerAppointmentByParams(params);

        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }

    /**
     * @Description 设置联系
     * @auther: fym
     * @UpadteDate: 2019/3/06 15:57
     */
    @RequestMapping(value = "/{module}/resetStatus")
    @ResponseBody
    public Map<String, Object> operate(@PathVariable String module, String operateType, Integer id) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        CustomerAppointment customerAppointment = customerAppointmentService.getCustomerAppointmentById(id);
        if (customerAppointment == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该记录不存在");
            return resultMap;
        }
        CustomerAppointment caTemp = new CustomerAppointment();
        caTemp.setId(id);
        //undo未联系，设置为已联系（0 -已联系，1 -未联系）
        if ("undo".equals(operateType)) {
            caTemp.setStatus("0");
            customerAppointmentService.updateCustomerAppointment(caTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "操作成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }

}
