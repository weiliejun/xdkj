package com.xdkj.pc.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xdkj.common.components.filesync.FileSynchronizer;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.deviceInfo.bean.DeviceInfo;
import com.xdkj.common.model.dlsInfo.bean.DlsInfo;
import com.xdkj.common.model.productOtherAttachFile.bean.AttachFileHref;
import com.xdkj.common.model.productOtherAttachFile.bean.ProductOtherAttachFile;
import com.xdkj.common.model.user.bean.UserInfo;
import com.xdkj.common.model.websiteBulletin.bean.WebsiteBulletin;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.FileHelper;
import com.xdkj.common.util.StringHelper;
import com.xdkj.pc.service.deviceInfo.DeviceInfoService;
import com.xdkj.pc.service.dlsInfo.DlsInfoService;
import com.xdkj.pc.service.infoPublishManage.WebsiteBulletinService;
import com.xdkj.pc.service.productOtherAttachFile.ProductOtherAttachFileService;
import com.xdkj.pc.web.base.AbstractBaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @ClassName DeviceInfoController
 * @Description 用一句话说明这个方法做什么
 * @Version 1.0
 * @Author 徐赛平
 * @UpdateDate 2019/3/5 10:22
 */
@Controller
@RequestMapping("/deviceInfo")
@PropertySource(value = {"classpath:config/resource.properties"}, encoding = "utf-8")
public class DeviceInfoController extends AbstractBaseController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceInfoController.class);

    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private DlsInfoService dlsInfoService;
    @Autowired
    private ProductOtherAttachFileService productOtherAttachFileService;
    @Autowired
    private WebsiteBulletinService websiteBulletinService;
    @Autowired
    private FileSynchronizer FileSynchronizer;

    /**
     * 图片服务器访问地址
     */
    @Value("${resourceServer.AccessURL}")
    private String resourceServerURL;

    @RequestMapping(value = {"/delete/{id}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteById(@PathVariable("id") String id) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setId(id);
        deviceInfo.setDataStatus(GlobalConstant.DATA_INVALID);
        try {
            deviceInfoService.updateDeviceInfo(deviceInfo);
        } catch (Exception e) {
            logger.error("/delete! id:" + id, e);
        }
        return "forward:/deviceInfo/list";
    }

    /**
     * 新增设备信息页面
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
            DeviceInfo deviceInfo = deviceInfoService.getDeviceInfoById(id);
            model.addAttribute("deviceInfo", deviceInfo);
        } else {
            model.addAttribute("deviceInfo", new DeviceInfo());
        }

        return "/business/deviceInfo/add";
    }

    /**
     * 修改设备信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/get/{type}/{id}"}, method = RequestMethod.GET)
    public String findDeviceInfoById(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        DeviceInfo deviceInfo = deviceInfoService.getDeviceInfoById(id);
        model.addAttribute("deviceInfo", deviceInfo);
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("view")) {
            return "/business/deviceInfo/view";
        } else if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("rybgsq")) {
            return "/business/deviceInfo/rybgsq";
        }
        return "/business/deviceInfo/add";
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
            params.put("dlsId", id);
            List<DeviceInfo> xmInfo = deviceInfoService.listDeviceInfoByParams(params);
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
     * 设备信息加载页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getDeviceInfo(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/business/deviceInfo/list";
    }

    /**
     * 设备信息列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listDeviceInfo(HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);

        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        Map<String, Object> params = getQureyParams(requestParams);

        final Page<DeviceInfo> results = (Page<DeviceInfo>) deviceInfoService.listDeviceInfoByParams(params);
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", results.getResult());

        // 记录查看日志
//        saveBusinessLog("设备信息管理", "设备信息列表", "第" + results.getPageNum() + "页list");

        return resultMap;
    }


    /**
     * 新增或修改设备信息
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 15:07
     * @Param
     **/
    @PostMapping(value = "/addOrUpdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateDeviceInfo(@RequestBody DeviceInfo deviceInfo) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        SysManager currentManager = getSessionSysManager();
        //新增
        if (StringHelper.isBlank(deviceInfo.getId())) {
            //设置初始化值
            // 数据有效
            deviceInfo.setDataStatus(GlobalConstant.DATA_VALID);
            deviceInfo.setCreateTime(DateHelper.getYMDHMSFormatDate(new Date()));
            deviceInfo = deviceInfoService.addDeviceInfo(deviceInfo);
            resultMap.put("dlsId", deviceInfo.getDlsId());
            resultMap.put("flag", "true");
            resultMap.put("msg", "设备信息新增成功");

            // 记录查看日志
//            saveBusinessLog("设备信息管理", "新增设备信息", deviceInfo);
            return resultMap;
        } else {//编辑
            deviceInfoService.updateDeviceInfo(deviceInfo);
            resultMap.put("dlsId", deviceInfo.getDlsId());
            resultMap.put("flag", "true");
            resultMap.put("msg", "设备信息修改成功");

            // 记录查看日志
//            saveBusinessLog("设备信息管理", "修改设备信息", deviceInfo);
            return resultMap;
        }

    }

    @RequestMapping("/sbwb")
    public String sbwb(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();
        /*param.put("userId", userInfo.getId());*/
        List<DeviceInfo> userExpenseList = deviceInfoService.listDeviceInfoByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<DeviceInfo>(userExpenseList);
//        final Page<DeviceInfo> pageInfo = (Page<DeviceInfo>) deviceInfoService.listDeviceInfoByParams(param);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/sbwb::recServiceList";
        } else {
            return "deviceInfo/sbwb";
        }
    }

    @RequestMapping("/sbzn")
    public String sbzn(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();
        /*param.put("userId", userInfo.getId());*/
        List<DeviceInfo> userExpenseList = deviceInfoService.listDeviceInfoByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<DeviceInfo>(userExpenseList);
//        final Page<DeviceInfo> pageInfo = (Page<DeviceInfo>) deviceInfoService.listDeviceInfoByParams(param);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/sbzn::recServiceList";
        } else {
            return "deviceInfo/sbzn";
        }
    }

    @RequestMapping("/dlsxx")
    public String dlsxx(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();
        String ppxh=request.getParameter("ppxh");
        param.put("ppxh", ppxh);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        List<DlsInfo> userExpenseList = dlsInfoService.listDlsInfoByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<DlsInfo>(userExpenseList);
//        final PageInfo<DlsInfo> pageInfo = (PageInfo<DlsInfo>) dlsInfoService.listDlsInfoByParams(param);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("ppxh", ppxh);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/dlsxx::recServiceList";
        } else {
            return "deviceInfo/dlsxx";
        }
    }

    @RequestMapping("/rjxz")
    public String rjxz(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();

        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        List<ProductOtherAttachFile> userExpenseList = productOtherAttachFileService.listWdmbByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<ProductOtherAttachFile>(userExpenseList);
//        final PageInfo<DlsInfo> pageInfo = (PageInfo<DlsInfo>) dlsInfoService.listDlsInfoByParams(param);

        List<ProductOtherAttachFile> rtn = pageInfo.getList();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (ProductOtherAttachFile productOtherAttachFile : rtn) {
            Map<String, Object> map = object2Map(productOtherAttachFile);

            String attachFile = productOtherAttachFile.getAttachFile();
            if (StringUtils.isNotBlank(attachFile)) {
                //对去除空格转义符处理
                String a = HtmlUtils.htmlUnescape(attachFile.replaceAll(" ", ""));
                Map<String, String> parse = (Map<String, String>) JSON.parse(a);
                List<AttachFileHref> fileHrefList = new ArrayList<AttachFileHref>();
                for (Map.Entry<String, String> entry : parse.entrySet()) {
                    map.put("attachFileName", entry.getKey());
                    map.put("attachFilePath", entry.getValue());
                }
            }
            data.add(map);
        }
        pageInfo.setList(data);

        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/rjxz::recServiceList";
        } else {
            return "deviceInfo/rjxz";
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response, Model model) {
        OutputStream outputStream = null;

        try {
            String attachFileName = request.getParameter("attachFileName");
            String attachFilePath = request.getParameter("attachFilePath");
            URL url = new URL(attachFilePath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] fileBtye = FileHelper.readInputStream(inputStream);

            String fileName = null;
            String userAgent = request.getHeader("User-Agent");
            // 基于IE内核
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                fileName = java.net.URLEncoder.encode(attachFileName, "UTF-8");
            } else {
                // 非IE内核
                fileName = new String(attachFileName.getBytes("UTF-8"), "ISO-8859-1");
            }

            outputStream = response.getOutputStream();
            response.reset();
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            outputStream.write(fileBtye);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static Map<String, Object> object2Map(Object object) {
        Map<String, Object> result = new HashMap<String, Object>();
        //获得类的的属性名 数组
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = new String(field.getName());
                result.put(name, field.get(object));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/hyzx")
    public String hyzx(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", "cpgg");
        param.put("publishStatus", "1");
        param.put("status", "1");
        List<WebsiteBulletin> userExpenseList = websiteBulletinService.listWebsiteBulletinByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<WebsiteBulletin>(userExpenseList);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/hyzx::recServiceList";
        } else {
            return "deviceInfo/hyzx";
        }
    }

    @RequestMapping("/xtgg")
    public String xtgg(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, String loadingType) {
        UserInfo userInfo = getUserInfoBySid(request);

        //引入分页查询，使用PageHelper分页功能在查询之前传入当前页，然后多少记录
        PageHelper.startPage(1, pageSize * pageNum);
        //startPage后紧跟的这个查询就是分页查询
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", "xtgg");
        param.put("publishStatus", "1");
        param.put("status", "1");
        List<WebsiteBulletin> userExpenseList = websiteBulletinService.listWebsiteBulletinByParams(param);
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo<WebsiteBulletin>(userExpenseList);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);

        if ("partLoad".equals(loadingType)) {
            return "deviceInfo/xtgg::recServiceList";
        } else {
            return "deviceInfo/xtgg";
        }
    }
}
