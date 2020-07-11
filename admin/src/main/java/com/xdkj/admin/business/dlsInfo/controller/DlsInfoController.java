package com.xdkj.admin.business.dlsInfo.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.business.dlsInfo.service.DlsInfoService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.components.filesync.FileSynchronizer;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.dlsInfo.bean.DlsInfo;
import com.xdkj.common.model.sysManager.bean.SysManager;
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
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName DlsInfoController
 * @Description 用一句话说明这个方法做什么
 * @Version 1.0
 * @Author 徐赛平
 * @UpdateDate 2019/3/5 10:22
 */
@Controller
@RequestMapping("/dlsInfo")
@PropertySource(value = {"classpath:config/resource.properties"}, encoding = "utf-8")
public class DlsInfoController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(DlsInfoController.class);

    @Autowired
    private DlsInfoService dlsInfoService;

    @Autowired
    private FileSynchronizer FileSynchronizer;

    /**
     * 图片服务器访问地址
     */
    @Value("${resourceServer.AccessURL}")
    private String resourceServerURL;

    private static boolean deleteDir(File dir) {
        if (dir.isFile()) {
            if (dir.isDirectory()) {
                // 递归删除目录中的子目录下
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 新增代理商信息页面
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
            DlsInfo dlsInfo = dlsInfoService.getDlsInfoById(id);
            model.addAttribute("dlsInfo", dlsInfo);
        } else {
            model.addAttribute("dlsInfo", new DlsInfo());
        }
        return "/business/dlsInfo/add";
    }

    /**
     * 修改代理商信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/get/{type}/{id}"}, method = RequestMethod.GET)
    public String findDlsInfoById(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        DlsInfo dlsInfo = dlsInfoService.getDlsInfoById(id);
        model.addAttribute("dlsInfo", dlsInfo);
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("view")) {
            return "/business/dlsInfo/view";
        } else if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("rybgsq")) {
            return "/business/dlsInfo/rybgsq";
        }
        return "/business/dlsInfo/add";
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
            params.put("ryid", id);
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
     * 代理商信息加载页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getDlsInfo(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/business/dlsInfo/list";
    }

    /**
     * 代理商信息列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listDlsInfo(HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);

        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        Map<String, Object> params = getQureyParams(requestParams);

        final Page<DlsInfo> results = (Page<DlsInfo>) dlsInfoService.listDlsInfoByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());

        // 记录查看日志
        saveBusinessLog("代理商信息管理", "代理商信息列表", "第" + results.getPageNum() + "页list");

        return resultMap;
    }

    /**
     * 代理商信息查询负责人页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public String selectListGet(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        Map<String, Object> params = formQueryRemember(request);
        String ryid = params.containsKey("ryid") ? params.get("ryid").toString() : "";
        if (params.get("cxmk").equals("selectXmfzr")) {
            model.addAttribute("type", "radio");
            model.addAttribute("cxmk", "selectXmfzr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectFwfzr")) {
            model.addAttribute("type", "radio");
            model.addAttribute("cxmk", "selectFwfzr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectCwfzr")) {
            model.addAttribute("type", "radio");
            model.addAttribute("cxmk", "selectCwfzr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectSpr")) {
            model.addAttribute("type", "checkbox");
            model.addAttribute("cxmk", "selectSpr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectXmjbr")) {
            model.addAttribute("type", "radio");
            model.addAttribute("cxmk", "selectXmjbr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectXmqtcy")) {
            model.addAttribute("type", "checkbox");
            model.addAttribute("cxmk", "selectXmqtcy");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectJsxmSpr")) {
            model.addAttribute("type", "checkbox");
            model.addAttribute("cxmk", "selectJsxmSpr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectFzr")) {
            model.addAttribute("type", "radio");
            model.addAttribute("cxmk", "selectFzr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectFhr")) {
            model.addAttribute("type", "checkbox");
            model.addAttribute("cxmk", "selectFhr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        } else if (params.get("cxmk").equals("selectZhr")) {
            model.addAttribute("type", "checkbox");
            model.addAttribute("cxmk", "selectZhr");
            model.addAttribute("ryid", ryid);
            return "app/DlsInfo/selectList";
        }
        return "/business/dlsInfo/selectList";
    }

    /**
     * 代理商信息查询负责人页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
  /*  @PostMapping(value = "/selectList")
    @ResponseBody
    public Map<String, Object> selectList(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);

        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        Map<String, Object> params = getQureyParams(requestParams);

        final Page<DlsInfoSelect> results = (Page<DlsInfoSelect>) dlsInfoService.selectList(params);
        //设置选中状态
        List<DlsInfoSelect> rtn = results.getResult();
        List<JSONObject> data = new ArrayList<JSONObject>();
        String ryid = params.containsKey("ryid") ? params.get("ryid").toString() : "";
        String[] ryids = ryid.split(",");
        for (String s : ryids) {
            for (DlsInfoSelect DlsInfoSelect : rtn) {
                JSONObject json = JSONObject.fromObject(DlsInfoSelect);
                if (s.equalsIgnoreCase(json.getString("id"))) {
                    json.put("LAY_CHECKED", true);
                    data.add(json);
                } else {
                    data.add(json);
                }
            }
        }
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", data);


        return resultMap;
    }*/

    /**
     * 新增或修改代理商信息
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 15:07
     * @Param
     **/
    @PostMapping(value = "/addOrUpdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateDlsInfo(@RequestBody DlsInfo dlsInfo) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SysManager currentManager = getSessionSysManager();
        //新增
        if (StringHelper.isBlank(dlsInfo.getId())) {
            //设置初始化值
            // 数据有效
            dlsInfo.setDataStatus(GlobalConstant.DATA_VALID);
            dlsInfo.setCreateTime(DateHelper.getYMDHMSFormatDate(new Date()));
            dlsInfo = dlsInfoService.addDlsInfo(dlsInfo);
            resultMap.put("flag", "true");
            resultMap.put("msg", "代理商信息新增成功");

            // 记录查看日志
            saveBusinessLog("代理商信息管理", "新增代理商信息", dlsInfo);
            return resultMap;
        } else {//编辑
            dlsInfoService.updateDlsInfo(dlsInfo);
            resultMap.put("flag", "true");
            resultMap.put("msg", "代理商信息修改成功");

            // 记录查看日志
            saveBusinessLog("代理商信息管理", "修改代理商信息", dlsInfo);
            return resultMap;
        }

    }
}
