package com.xdkj.admin.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.admin.system.service.SecurityService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysFunction.bean.SysFunction;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysRole.bean.SysRole;
import com.xdkj.common.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 角色管理
 * @auther: zhangkele
 * @UpadteDate: 2019/2/25 9:25
 */
@Controller
@RequestMapping("/sysfunction")
public class SysFunctionController extends AbstractBaseController {

    @Autowired
    private SecurityService securityService;

    /**
     * @Description 新增权限页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:43
     */
    @GetMapping(value = {"/toadd"})
    public String toAddSysFunction() {
        return "/system/sysFunction/add";
    }

    /**
     * @Description 菜单图标
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:43
     */
    @GetMapping(value = {"/systemSetting/icons"})
    public String toIconSetting() {
        return "/system/sysFunction/icons";
    }

    /**
     * @Description 菜单图标
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:43
     */
    @GetMapping(value = {"/list/icons"})
    public String toMyicons() {
        return "/system/sysFunction/myicons";
    }

    /**
     * @Description 角色列表页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:44
     */
    @GetMapping(value = {"/list"})
    public String toListSysRoles(HttpServletRequest request, Model model) {
        return "/system/sysFunction/function";
    }


    /**
     * @Description 保存权限信息（新增或修改）
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:48
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateSysFunction(SysFunction sysFunction) {
        SysManager sysManager = getSessionSysManager();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        //参数校验
        if (StringHelper.isEmpty(sysFunction.getCode())) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "权限编码不能为空");
            return resultMap;
        }
        SysFunction existSysFunction = null;
        //新增
        if (sysFunction.getId() == null) {
            //唯一性校验
            existSysFunction = securityService.getSysFunctionByCode(sysFunction.getCode());
            if (existSysFunction != null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "该权限已经存在");
                return resultMap;
            }
            sysFunction.setDataStatus(GlobalConstant.DATA_VALID);
            securityService.addSysFunction(sysFunction);
            //给系统超级管理员授权
            SysRole superManagerRole = securityService.getSysRoleByName(GlobalConstant.CONSOLE_SUPER_ADMIN_ROLE);
            if (superManagerRole != null) {
                securityService.grantRoleRights(superManagerRole.getId(), new String[]{sysFunction.getCode()}, sysManager);
            }
            resultMap.put("flag", "true");
            resultMap.put("msg", "权限保存成功");
            return resultMap;
        } else {//编辑
            existSysFunction = securityService.getSysFunctionById(sysFunction.getId());
            if (existSysFunction == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "该权限不存在");
                return resultMap;
            }
            //判断唯一性
            if (!existSysFunction.getCode().equals(sysFunction.getCode())) {
                SysFunction oldSysFunction = securityService.getSysFunctionByCode(sysFunction.getCode());
                if (oldSysFunction != null) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "该权限已存在");
                    return resultMap;
                }
            }
            securityService.updateSysFunction(sysFunction);
            resultMap.put("flag", "true");
            resultMap.put("msg", "权限修改成功");
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
    public Map<String, Object> operate(@PathVariable String operateType, String code) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (StringHelper.isEmpty(code)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数为空");
            return resultMap;
        }
        SysFunction sysFunction = securityService.getSysFunctionByCode(code);

        if (sysFunction == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该权限不存在");
            return resultMap;
        }
        //删除
        if ("delete".equals(operateType)) {
            securityService.deleteSysFunctionByCode(code);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }


    /**
     * @Description 获取权限
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 11:21
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    public Map<String, Object> getSysFunction(String code) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (StringHelper.isEmpty(code)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数为空");
            return resultMap;
        }
        SysFunction sysFunction = securityService.getSysFunctionByCode(code);
        if (sysFunction != null) {
            resultMap.put("flag", "true");
            resultMap.put("msg", "获取成功");
            resultMap.put("sysFunction", sysFunction);
            return resultMap;
        } else {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该权限信息不存在");
            return resultMap;
        }

    }


    //设置用户角色
    @RequestMapping(value = "/saveroles")
    @ResponseBody
    public Map<String, Object> saveRoleSet(Integer[] role, Integer id) {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            resultmap.put("state", "success");
            resultmap.put("mesg", "设置成功");
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            resultmap.put("state", "fail");
            resultmap.put("mesg", "设置失败，系统异常");
            return resultmap;
        }
    }


    /**
     * 加载权限菜单
     */
    @ResponseBody
    @RequestMapping("/load/menu")
    public String loadMenuInfo(@RequestParam String parentCode) {
        SysManager sysManager = getSessionSysManager();
        JSONArray jsonArray = getMenuTreeJson(sysManager.getId(), parentCode);
        return JSON.toJSONString(jsonArray);
    }

    /**
     * @Description 加载权限树
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 11:22
     */
    @RequestMapping("/load/sysfunction")
    @ResponseBody
    public String loadMenuInfo(@RequestParam String parentCode, Integer roleId) {
        List<String> roleFunctionCodes = new ArrayList<String>();
        if (roleId != null) {
            roleFunctionCodes = securityService.listFunctionCodeByRoleId(roleId);
        }
        JSONArray jsonArray = getFunctionTreeJson(parentCode, roleFunctionCodes);
        return JSON.toJSONString(jsonArray);
    }

    /**
     * @Description 获取系统左侧菜单json数据
     * @auther: zhangkele
     * @UpadteDate: 2019/3/7 9:06
     */
    private JSONArray getMenuTreeJson(Integer manager, String parentCode) {
        List<SysFunction> sysFunctions = securityService.listSysFunctionByManagerIdAndParentCode(manager, parentCode);
        JSONArray jsonArray = new JSONArray();
        for (SysFunction sysFunction : sysFunctions) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sysFunction.getCode()); // 节点id
            jsonObject.put("title", sysFunction.getName()); // 节点名称
            jsonObject.put("spread", false); // 不展开
            jsonObject.put("icon", sysFunction.getIcon());
            if (StringUtils.isNotEmpty(sysFunction.getUrl())) {
                jsonObject.put("href", sysFunction.getUrl()); // 菜单请求地址
            }
            Integer subSysFunctionCount = securityService.countSysFunctionsByParentCode(sysFunction.getCode());
            if (subSysFunctionCount > 0) {//有子节点
                jsonObject.put("children", getMenuTreeJson(manager, sysFunction.getCode()));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    /**
     * @Description 获取系统功能菜单树json数据
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 15:57
     */
    private JSONArray getFunctionTreeJson(String parentCode, List<String> roleFunctionCodes) {
        List<SysFunction> sysFunctions = securityService.listSysFunctionsByParentCode(parentCode);
        JSONArray jsonArray = new JSONArray();
        for (SysFunction sysFunction : sysFunctions) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", sysFunction.getCode());// 节点id
            jsonObject.put("name", sysFunction.getName());// 节点名称

            Integer subSysFunctionCount = securityService.countSysFunctionsByParentCode(sysFunction.getCode());
            if (subSysFunctionCount <= 0) {
                jsonObject.put("open", "false"); // 无子节点
            } else {
                jsonObject.put("open", "true");//有子节点
                jsonObject.put("children", getFunctionTreeJson(sysFunction.getCode(), roleFunctionCodes));
            }
            if (roleFunctionCodes.contains(sysFunction.getCode())) {
                jsonObject.put("checked", true);
            }
            jsonObject.put("functionType", String.valueOf(sysFunction.getFunctionType()));
            jsonObject.put("iconValue", sysFunction.getIcon());
            jsonObject.put("pId", sysFunction.getParentCode());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

}
