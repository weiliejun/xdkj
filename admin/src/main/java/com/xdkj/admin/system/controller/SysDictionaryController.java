package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.SysDictionaryService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysdictionary.bean.SysDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : SysDictionaryController
 * @Description 系统字典管理
 * @auther: FENG.yanmin
 * @Update : 2019-2-21
 */
@Controller
@RequestMapping("/sysdictionary")
public class SysDictionaryController extends AbstractBaseController {
    @Autowired
    private SysDictionaryService sysDictionaryService;

    /**
     * @Description get请求 返回到list页面
     * @UpadteDate: 2019/2/22 14:42
     */
    @GetMapping(value = "/list")
    public String toListSysDictionarys() {
        return "/system/sysdictionary/list";
    }

    /**
     * @Description post请求 分页查询字典信息
     * @UpadteDate: 2019/2/22 14:42
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listSysDictionarys(HttpServletRequest request) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<SysDictionary> results = (Page<SysDictionary>) sysDictionaryService.listSysDictionaryByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }

    /**
     * @Description 跳转新增页面
     * @UpadteDate: 2019/2/22 14:37
     */
    @RequestMapping(value = {"/toadd"}, method = RequestMethod.GET)
    public String toAddSysDictionary() {
        return "/system/sysdictionary/add";
    }


    /**
     * @Description 保存字典信息（新增或修改）
     * @UpadteDate: 2019/2/22 14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateSysDictionary(SysDictionary sysDictionary) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            //新增
            if (sysDictionary.getId() == null) {
                SysDictionary exitDicByName = sysDictionaryService.getSysDictionaryByName(sysDictionary.getParentCode(), sysDictionary.getName(), sysDictionary.getHasChild());
                if (exitDicByName != null) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "该类型名称已经存在！");
                    return resultMap;
                }
                SysDictionary exitDicByCode = sysDictionaryService.getSysDictionaryByCode(sysDictionary.getCode());
                if (exitDicByCode != null) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "该类型编号已经存在！");
                    return resultMap;
                }
                sysDictionary.setDataStatus(GlobalConstant.DATA_VALID);
                sysDictionary.setCreateTime(new Date());
                sysDictionaryService.addSysDictionary(sysDictionary);
                resultMap.put("flag", "true");
                resultMap.put("msg", "新增成功");
                return resultMap;
            } else {//编辑
                SysDictionary exitDicByName = sysDictionaryService.getSysDictionaryByName(sysDictionary.getParentCode(), sysDictionary.getName(), sysDictionary.getHasChild());
                if (exitDicByName != null && !exitDicByName.getName().equals(sysDictionary.getName())) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "该类型名称已经存在！");
                    return resultMap;
                }
                sysDictionary.setUpdateTime(new Date());
                sysDictionaryService.updateSysDictionary(sysDictionary);
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
        SysDictionary dictionary = sysDictionaryService.getSysDictionaryById(id);
        if (dictionary == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该类型不存在");
            return resultMap;
        }
        SysDictionary temp = new SysDictionary();
        temp.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            temp.setDataStatus(GlobalConstant.STATUS_INVALID);
            sysDictionaryService.updateSysDictionary(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //设置为启用 -0
        if ("enable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_VALID);
            temp.setUpdateTime(new Date());
            sysDictionaryService.updateSysDictionary(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "启用成功");
            return resultMap;
        }
        //设置为禁用 -1
        if ("disable".equals(operateType)) {
            temp.setStatus(GlobalConstant.STATUS_INVALID);
            sysDictionaryService.updateSysDictionary(temp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "禁用成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }

    /**
     * @Description 分页查询 父级字典列表
     * @auther: fym
     * @UpadteDate: 2019/3/13 14:42
     */
    @PostMapping(value = "/tableSelect/list")
    @ResponseBody
    public Map<String, Object> tableSelectList(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String aa = request.getParameter("selectRange");
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        final Map<String, Object> params = getQureyParams(requestParams);
        params.put("grade", 1);
        params.put("status", GlobalConstant.STATUS_VALID);
        final Page<SysDictionary> results = (Page<SysDictionary>) sysDictionaryService.listSysDictionaryByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


}
