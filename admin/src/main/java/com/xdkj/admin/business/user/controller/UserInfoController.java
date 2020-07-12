package com.xdkj.admin.business.user.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.system.service.UserInfoService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.components.filesync.FileSynchronizer;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName UserInfoController
 * @Description 用一句话说明这个方法做什么
 * @Version 1.0
 * @Author 徐赛平
 * @UpdateDate 2019/3/5 10:22
 */
@Controller
@RequestMapping("/userInfo")
@PropertySource(value = {"classpath:config/resource.properties"}, encoding = "utf-8")
public class UserInfoController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FileSynchronizer FileSynchronizer;

    /**
     * 图片服务器访问地址
     */
    @Value("${resourceServer.AccessURL}")
    private String resourceServerURL;

    @RequestMapping(value = {"/delete/{id}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteById(@PathVariable("id") String id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(new Integer(id));
        userInfo.setDataStatus(GlobalConstant.DATA_INVALID);
        try {
            userInfoService.updateUserInfo(userInfo);
        } catch (Exception e) {
            logger.error("/delete! id:" + id, e);
        }
        return "forward:/userInfo/list";
    }

    /**
     * 新增用户信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/toAdd"}, method = RequestMethod.GET)
    public String toAdd(Model model, @RequestParam(value = "id", required = false) String id, HttpServletRequest request) {
        if (id != null) {
            UserInfo userInfo = userInfoService.getUserById(new Integer(id));
            model.addAttribute("userInfo", userInfo);
        } else {
            model.addAttribute("userInfo", new UserInfo());
        }
        return "/business/userInfo/add";
    }

    /**
     * 修改用户信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/get/{type}/{id}"}, method = RequestMethod.GET)
    public String findUserInfoById(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        UserInfo userInfo = userInfoService.getUserById(new Integer(id));
        model.addAttribute("userInfo", userInfo);
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("view")) {
            return "/business/userInfo/view";
        } else if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("rybgsq")) {
            return "/business/userInfo/rybgsq";
        }
        return "/business/userInfo/add";
    }

    /**
     * @return
     * @Author 魏列军
     * @Description //TODO 加载用户详情页面的参与项目
     * @Date 2020/5/21 17:22
     * @Param
     **/
    @PostMapping("/detail/{id}")
    @ResponseBody
    public Map<String, Object> getUserInfoDetailPage(HttpServletRequest request, Model model, @PathVariable("id") String id) {
        logger.debug(request.getRequestURI() + "加载用户详情页面");
        Map<String, Object> resultMap = new HashMap<String, Object>(16);
        String flag = "true", msg = "获取成功";
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", new Integer(id));
            List<UserInfo> xmInfo = userInfoService.listUserInfosByParams(params);
            resultMap.put("data", xmInfo);
        } catch (Exception e) {
            logger.error("异常");
            flag = "false";
            msg = "获取数据失败";
        }
        resultMap.put("flag", flag);
        resultMap.put("msg", msg);

        return resultMap;

    }

    /**
     * 用户信息加载页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getUserInfo(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/business/userInfo/list";
    }

    /**
     * 用户信息列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listUserInfo(HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);

        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        Map<String, Object> params = getQureyParams(requestParams);

        final Page<UserInfo> results = (Page<UserInfo>) userInfoService.listUserInfosByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());

        // 记录查看日志
        saveBusinessLog("用户信息管理", "用户信息列表", "第" + results.getPageNum() + "页list");

        return resultMap;
    }


    /**
     * 新增或修改用户信息
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 15:07
     * @Param
     **/
    @PostMapping(value = "/addOrUpdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateUserInfo(@RequestBody UserInfo userInfo) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysManager currentManager = getSessionSysManager();
        //新增
        if (userInfo.getId()==null) {
            //设置初始化值
            // 数据有效
            userInfo.setDataStatus(GlobalConstant.DATA_VALID);
            userInfo.setCreateTime(new Date());
            userInfoService.addUserInfo(userInfo);
            resultMap.put("flag", "true");
            resultMap.put("msg", "用户信息新增成功");

            // 记录查看日志
            saveBusinessLog("用户信息管理", "新增用户信息", userInfo);
            return resultMap;
        } else {//编辑
            userInfoService.updateUserInfo(userInfo);
            resultMap.put("flag", "true");
            resultMap.put("msg", "用户信息修改成功");

            // 记录查看日志
            saveBusinessLog("用户信息管理", "修改用户信息", userInfo);
            return resultMap;
        }

    }
}
