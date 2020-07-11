package com.xdkj.admin.system.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.SecurityService;
import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysRole.bean.SysRole;
import com.xdkj.common.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description 角色管理
 * @auther: zhangkele
 * @UpadteDate: 2019/2/25 9:25
 */
@Controller
@RequestMapping("/sysrole")
public class SysRoleController extends AbstractBaseController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SysManagerService sysManagerService;

    /**
     * @Description 新增角色页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:43
     */
    @GetMapping(value = {"/toadd"})
    public String toSysRole() {
        return "/system/sysRole/add";
    }

    /**
     * @Description 角色列表页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:44
     */
    @GetMapping(value = {"/list"})
    public String toListSysRoles(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/system/sysRole/list";
    }


    /**
     * @Description 分页查询角色信息
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:46
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listSysRoles(HttpServletRequest request) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);
        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        PageHelper.orderBy("CREATE_TIME desc");
        final Map<String, Object> params = getQureyParams(requestParams);
        final Page<SysRole> results = (Page<SysRole>) securityService.listSysRolesByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 保存用户信息（新增或修改）
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:48
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateSysRole(SysRole sysRole) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        //参数校验
        if (StringHelper.isEmpty(sysRole.getName())) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "角色名称不能为空");
            return resultMap;
        }
        SysRole existSysRole = null;
        //新增
        if (sysRole.getId() == null) {
            //首先判断用户名是否可用
            existSysRole = securityService.getSysRoleByName(sysRole.getName());
            if (existSysRole != null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "该角色已经存在");
                return resultMap;
            }

            sysRole.setDataStatus(GlobalConstant.DATA_VALID);
            sysRole.setCreatorId(1);
            sysRole.setCreatorName("系统管理员");
            sysRole.setCreateTime(new Date());
            securityService.addSysRole(sysRole);
            resultMap.put("flag", "true");
            resultMap.put("msg", "角色保存成功");
            return resultMap;
        } else {//编辑
            existSysRole = securityService.getSysRoleById(sysRole.getId());
            if (existSysRole == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "该角色已经存在");
                return resultMap;
            }
            //判断修改的角色是否唯一
            if (!existSysRole.getName().equals(sysRole.getName())) {
                SysRole oldSysRole = securityService.getSysRoleByName(sysRole.getName());
                if (oldSysRole != null) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "角色名称已存在");
                    return resultMap;
                }
            }
            sysRole.setEditorId(1);
            sysRole.setEditorName("系统管理员");
            sysRole.setEditTime(new Date());
            securityService.updateSysRole(sysRole);
            resultMap.put("flag", "true");
            resultMap.put("msg", "角色修改成功");
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
        SysRole sysRole = securityService.getSysRoleById(id);
        if (sysRole == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该角色不存在");
            return resultMap;
        }
        SysRole sysRoleTemp = new SysRole();
        sysRoleTemp.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            securityService.deleteSysRoleById(id);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //启用
        if ("enable".equals(operateType)) {
            sysRoleTemp.setStatus("0");
            securityService.updateSysRole(sysRoleTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "启用成功");
            return resultMap;
        }
        //禁用
        if ("disable".equals(operateType)) {
            sysRoleTemp.setStatus("1");
            securityService.updateSysRole(sysRoleTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "禁用成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "/get")
    public Map<String, Object> getSysManager(Integer id) {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            resultmap.put("state", "success");
            resultmap.put("mesg", "获取成功");
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            resultmap.put("state", "fail");
            resultmap.put("mesg", "获取失败，系统异常");
            return resultmap;
        }
    }


    /**
     * @Description 新增角色页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/25 9:43
     */
    @GetMapping(value = {"/grant/rights"})
    public String toGrantRoleRights() {
        return "/system/sysRole/grantRoleRights";
    }


    /**
     * @Description 保存角色权限信息
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 11:47
     */
    @RequestMapping(value = "/grant/rights/setting")
    @ResponseBody
    public Map<String, Object> grantRoleRights(Integer roleId,
                                               @RequestParam(value = "functionCodes[]", required = false) String[] functionCodes) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (roleId == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数为空");
            return resultMap;
        }
        SysManager sysManager = new SysManager();
        sysManager.setId(1);
        sysManager.setName("系统管理员");
        securityService.grantRoleRights(roleId, functionCodes, sysManager);
        resultMap.put("flag", "true");
        resultMap.put("msg", "设置成功");
        return resultMap;
    }
}
