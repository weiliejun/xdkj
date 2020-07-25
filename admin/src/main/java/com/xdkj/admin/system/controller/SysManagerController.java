package com.xdkj.admin.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.components.shiro.PasswordHelper;
import com.xdkj.admin.components.shiro.RetryLimitHashedCredentialsMatcher;
import com.xdkj.admin.system.service.SecurityService;
import com.xdkj.admin.system.service.SysManagerService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.sysManagerRole.bean.SysManagerRole;
import com.xdkj.common.model.sysRole.bean.SysRole;
import com.xdkj.common.util.StringHelper;
import com.xdkj.common.web.annotations.AvoidDuplicateSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName : SysManagerController
 * @Description 系统用户管理
 * @auther: zhangkele
 * @UpadteDate: 2019/2/21 14:35
 */
@Controller
@RequestMapping("/sysmanager")
public class SysManagerController extends AbstractBaseController {

    @Autowired
    private SysManagerService sysManagerService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher;

    /**
     * @Description 新增用户页面
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 14:37
     */
    @GetMapping(value = {"/toadd"})
    @AvoidDuplicateSubmission(needSaveToken = true)
    public String toAddSysManager() {
        return "/system/sysAdmin/add";
    }

    /**
     * @Description 修改密码页面
     * @auther: zhangkele
     * @UpadteDate: 2019/3/13 10:22
     */
    @GetMapping(value = {"/password/edit"})
    public String toPasswordEdit() {
        return "/system/sysAdmin/passwordEdit";
    }

    /**
     * @Description 修改密码
     * @auther: zhangkele
     * @UpadteDate: 2019/3/13 10:22
     */
    @PostMapping(value = {"/password/edit"})
    @ResponseBody
    public Map<String, Object> passwordEdit(@RequestParam String oldPassword, @RequestParam String password) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysManager currentManager = getSessionSysManager();
        if (StringHelper.isEmpty(oldPassword) || StringHelper.isEmpty(password)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数不能为空");
        }
        String encryptOldPassword = PasswordHelper.encryptPassword(currentManager.getCode(), oldPassword);
        if (!currentManager.getPassword().equals(encryptOldPassword)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "原密码不正确");
            return resultMap;
        }
        SysManager sysManageTemp = new SysManager();
        sysManageTemp.setId(currentManager.getId());
        sysManageTemp.setPassword(PasswordHelper.encryptPassword(currentManager.getCode(), password));
        sysManagerService.updateSysManager(sysManageTemp);
        resultMap.put("flag", "true");
        resultMap.put("msg", "修改成功");
        return resultMap;
    }

    /**
     * @Description 锁屏解锁
     * @auther: zhangkele
     * @UpadteDate: 2019/3/13 10:23
     */
    @PostMapping(value = {"/unlock/page"})
    @ResponseBody
    public Map<String, Object> unlockPage(@RequestParam String loginPassword) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysManager currentManager = getSessionSysManager();
        if (StringHelper.isEmpty(loginPassword)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数不能为空");
        }
        String encryptOldPassword = PasswordHelper.encryptPassword(currentManager.getCode(), loginPassword);
        if (!currentManager.getPassword().equals(encryptOldPassword)) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "密码错误，请重新输入！");
            return resultMap;
        }
        resultMap.put("flag", "true");
        resultMap.put("msg", "解锁成功");
        return resultMap;
    }

    /**
     * @Description 用户列表页
     * @auther: zhangkele
     * @UpadteDate: 2019/3/13 10:22
     */
    @GetMapping(value = {"/list"})
    public String toListSysManagers(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/system/sysAdmin/list";
    }

    /**
     * @Description 分页查询用户信息
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
        final Page<SysManager> results = (Page<SysManager>) sysManagerService.listSysManagersByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());
        return resultMap;
    }


    /**
     * @Description 保存用户信息（新增或修改）
     * @auther: zhangkele
     * @UpadteDate: 2019/2/21 14:44
     */
    @RequestMapping(value = "/addorupdate")
    @ResponseBody
    @AvoidDuplicateSubmission(needRemoveToken = true)
    public Map<String, Object> addOrUpdateSysManager(SysManager sysManager) {
        SysManager currentManager = getSessionSysManager();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        //参数校验
        if (StringHelper.isEmpty(sysManager.getCode())) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "用户名不能为空");
            resetSessionToken(resultMap);
            return resultMap;
        }
        SysManager existSysManager = null;
        //新增
        if (sysManager.getId() == null) {
            //首先判断用户名是否可用
            existSysManager = sysManagerService.getSysManagerByCode(sysManager.getCode());
            if (existSysManager != null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "用户名已经存在");
                resetSessionToken(resultMap);
                return resultMap;
            }
            //设置初始密码
            sysManager.setPassword(PasswordHelper.encryptPassword(sysManager.getCode(), GlobalConstant.CONSOLE_PASSWORD_INIT));
            sysManager.setStatus(GlobalConstant.STATUS_VALID);
            sysManager.setDataStatus(GlobalConstant.DATA_VALID);
            sysManager.setCreatorId(currentManager.getId());
            sysManager.setCreatorName(currentManager.getName());
            sysManager.setCreateTime(new Date());
            sysManagerService.addSysManager(sysManager);
            resultMap.put("flag", "true");
            resultMap.put("msg", "用户保存成功");
            return resultMap;
        } else {//编辑
            existSysManager = sysManagerService.getSysManagerById(sysManager.getId());
            if (existSysManager == null) {
                resultMap.put("flag", "false");
                resultMap.put("msg", "该用户不存在");
                resetSessionToken(resultMap);
                return resultMap;
            }
            //判断修改的用户名是否唯一
            if (!existSysManager.getCode().equals(sysManager.getCode())) {
                SysManager oldSysManager = sysManagerService.getSysManagerByCode(sysManager.getCode());
                if (oldSysManager != null) {
                    resultMap.put("flag", "false");
                    resultMap.put("msg", "用户名已存在");
                    resetSessionToken(resultMap);
                    return resultMap;
                }
            }
            sysManager.setCreatorId(currentManager.getId());
            sysManager.setCreatorName(currentManager.getName());
            sysManager.setEditTime(new Date());
            sysManagerService.updateSysManager(sysManager);
            resultMap.put("flag", "true");
            resultMap.put("msg", "用户修改成功");
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
        SysManager sysManager = sysManagerService.getSysManagerById(id);
        if (sysManager == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "该用户不存在");
            return resultMap;
        }
        SysManager sysManageTemp = new SysManager();
        sysManageTemp.setId(id);
        //删除
        if ("delete".equals(operateType)) {
            sysManagerService.deleteSysManagerById(id);
            resultMap.put("flag", "true");
            resultMap.put("msg", "删除成功");
            return resultMap;
        }
        //启用
        if ("enable".equals(operateType)) {
            sysManageTemp.setStatus(GlobalConstant.STATUS_VALID);
            sysManagerService.updateSysManager(sysManageTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "启用成功");
            return resultMap;
        }
        //禁用
        if ("disable".equals(operateType)) {
            sysManageTemp.setStatus(GlobalConstant.STATUS_INVALID);
            sysManagerService.updateSysManager(sysManageTemp);
            resultMap.put("flag", "true");
            resultMap.put("msg", "禁用成功");
            return resultMap;
        }

        //账户解锁
        if ("unlock".equals(operateType)) {
            retryLimitHashedCredentialsMatcher.unlockAccount(sysManager.getCode());
            resultMap.put("flag", "true");
            resultMap.put("msg", "解锁成功");
            return resultMap;
        }
        resultMap.put("flag", "false");
        resultMap.put("msg", "操作异常");
        return resultMap;
    }


    /**
     * @Description 设置用户角色
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 15:44
     */
    @RequestMapping(value = {"/role/setting"}, method = RequestMethod.GET)
    public String toRoleSetting(Model model, @RequestParam String managerId) {
        model.addAttribute("managerId", managerId);
        return "/system/sysAdmin/roleSetting";
    }

    /**
     * @Description 加载角色数据
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 15:44
     */
    @PostMapping(value = "/role/load")
    @ResponseBody
    public Map<String, Object> loadRoles(Integer managerId) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<SysRole> roles = securityService.listAllSysRoles();
        List<SysManagerRole> sysManagerRoles = securityService.listSysManagerRoleByManagerId(managerId);
        List<Integer> roleIds = new ArrayList<Integer>();
        for (SysManagerRole sysManagerRole : sysManagerRoles) {
            roleIds.add(sysManagerRole.getRoleId());
        }

        JSONArray selectRolesArray = new JSONArray();
        JSONArray unSelectRolesArray = new JSONArray();
        for (SysRole role : roles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", role.getId());
            jsonObject.put("name", role.getName());
            if (roleIds.contains(role.getId())) {
                selectRolesArray.add(jsonObject);
            } else {
                unSelectRolesArray.add(jsonObject);
            }
        }
        resultMap.put("selectRoles", selectRolesArray);
        resultMap.put("unSelectRoles", unSelectRolesArray);
        resultMap.put("flag", "true");
        resultMap.put("msg", "加载成功");
        return resultMap;
    }

    /**
     * @Description 保存用户角色
     * @auther: zhangkele
     * @UpadteDate: 2019/2/27 15:44
     */
    @PostMapping(value = "/role/save")
    @ResponseBody
    public Map<String, Object> saveRoleSet(@RequestParam(value = "roleIds[]", required = true) Integer[] roleIds, Integer managerId) {
        SysManager currentManager = getSessionSysManager();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (managerId == null) {
            resultMap.put("flag", "false");
            resultMap.put("msg", "参数为空");
            return resultMap;
        }
        SysManager sysManager = new SysManager();
        sysManager.setCreatorId(currentManager.getId());
        sysManager.setCreatorName(currentManager.getName());
        securityService.grantSysManagerRoles(roleIds, managerId, currentManager);
        resultMap.put("flag", "true");
        resultMap.put("msg", "设置成功");
        return resultMap;
    }
}
