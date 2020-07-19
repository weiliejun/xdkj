package com.xdkj.admin.business.wdmb.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xdkj.admin.business.productOtherAttachFile.service.ProductOtherAttachFileService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.model.productOtherAttachFile.bean.AttachFileHref;
import com.xdkj.common.model.productOtherAttachFile.bean.ProductOtherAttachFile;
import com.xdkj.common.model.sysManager.bean.SysManager;
import com.xdkj.common.util.DateHelper;
import com.xdkj.common.util.FileHelper;
import com.xdkj.common.util.RandomUtil;
import com.xdkj.common.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
 * 文档模板
 *
 * @author 魏列军
 * @date 2020/5/15 15:30:22
 */
@Controller
@RequestMapping("/wdmb")
@PropertySource(value = {"classpath:config/resource.properties"}, encoding = "utf-8")
public class WdmbController extends AbstractBaseController {

    private static Logger logger = LoggerFactory.getLogger(WdmbController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ProductOtherAttachFileService productOtherAttachFileService;

    /**
     * 图片服务器访问地址
     */
    @Value("${resourceServer.AccessURL}")
    private String resourceServerURL;

    public static Map<String, Object> object2Map(Object object) {
        Map<String, Object> result = new HashMap<>();
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

    @RequestMapping(value = {"/delete/{id}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteById(@PathVariable("id") String id) {
        ProductOtherAttachFile record = new ProductOtherAttachFile();
        record.setId(id);
        record.setDataStatus(GlobalConstant.DATA_INVALID);
        try {
            productOtherAttachFileService.updateDataStatusById(record);
        } catch (Exception e) {
            logger.error("/delete! id:" + id, e);
        }
        return "forward:/wdmb/list";
    }

    /**
     * 新增文档模板信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/toAdd"}, method = RequestMethod.GET)
    public String toAdd(Model model, @RequestParam(value = "id", required = false) String id, @RequestParam(value = "xmId", required = false) String xmId, @RequestParam(value = "xmjd", required = false) String xmjd, HttpServletRequest request) {
        if (StringHelper.isNotBlank(id)) {

            //任务附件信息
            ProductOtherAttachFile productOtherAttachFile = productOtherAttachFileService.selectProductOtherAttachFileById(id);
            if (productOtherAttachFile == null) {
                model.addAttribute("message", "产品附件不存在");
                model.addAttribute("productOtherAttachFile", new ProductOtherAttachFile());
                model.addAttribute("AttachFileList", new ArrayList<AttachFileHref>());
            } else {
                String attachFile = productOtherAttachFile.getAttachFile();
                if (StringUtils.isNotBlank(attachFile)) {
                    //对去除空格转义符处理
                    String a = HtmlUtils.htmlUnescape(attachFile.replaceAll(" ", ""));
                    Map<String, String> parse = (Map<String, String>) JSON.parse(a);
                    List<AttachFileHref> fileHrefList = new ArrayList<AttachFileHref>();
                    for (Map.Entry<String, String> entry : parse.entrySet()) {
                        AttachFileHref attachFileHref = new AttachFileHref();
                        attachFileHref.setFileName(entry.getKey());
                        attachFileHref.setFileUrl(entry.getValue());
                        fileHrefList.add(attachFileHref);
                    }
                    model.addAttribute("productOtherAttachFile", productOtherAttachFile);
                    model.addAttribute("AttachFileList", fileHrefList);
                } else {
                    model.addAttribute("productOtherAttachFile", new ProductOtherAttachFile());
                    model.addAttribute("AttachFileList", new ArrayList<AttachFileHref>());
                }
            }
        } else {
            ProductOtherAttachFile productOtherAttachFile = new ProductOtherAttachFile();


            model.addAttribute("productOtherAttachFile", productOtherAttachFile);
            model.addAttribute("productOtherAttachFile", new ProductOtherAttachFile());
            model.addAttribute("AttachFileList", new ArrayList<AttachFileHref>());
        }
        return "/business/wdmb/add";
    }

    /**
     * 修改文档模板信息页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 17:31
     * @Param
     **/
    @RequestMapping(value = {"/get/{type}/{id}"}, method = RequestMethod.GET)
    public String findWdmbById(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        ProductOtherAttachFile productOtherAttachFile = productOtherAttachFileService.selectProductOtherAttachFileById(id);
        if (productOtherAttachFile == null) {
            model.addAttribute("message", "产品附件不存在");
        } else {
            String attachFile = productOtherAttachFile.getAttachFile();
            if (StringUtils.isNotBlank(attachFile)) {
                //对去除空格转义符处理
                String a = HtmlUtils.htmlUnescape(attachFile.replaceAll(" ", ""));
                Map<String, String> parse = (Map<String, String>) JSON.parse(a);
                List<AttachFileHref> fileHrefList = new ArrayList<>();
                for (Map.Entry<String, String> entry : parse.entrySet()) {
                    AttachFileHref attachFileHref = new AttachFileHref();
                    attachFileHref.setFileName(entry.getKey());
                    attachFileHref.setFileUrl(entry.getValue());
                    fileHrefList.add(attachFileHref);
                }
                model.addAttribute("productOtherAttachFile", productOtherAttachFile);
                model.addAttribute("AttachFileList", fileHrefList);
            }

        }
        model.addAttribute("productOtherAttachFile", productOtherAttachFile);
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));

        if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("view")) {
            return "/business/wdmb/view";
        } else if (StringHelper.isNotBlank(type) && type.equalsIgnoreCase("update")) {
            return "/business/wdmb/add";
        }
        return "/business/wdmb/add";
    }

    /**
     * 文档模板信息加载页面
     *
     * @return
     * @Author 魏列军
     * @Description //TODO 使用shiro中用户session缓存在redis中每次请求中页面都会携带jsessionid所用通过thymeleaf渲染模板时需传递请求信息
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getWdmb(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "/business/wdmb/list";
    }

    /**
     * 文档模板信息列表
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 9:45
     * @Param
     **/
    @PostMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWdmb(HttpServletRequest request, Model model) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Map<String, Object> requestParams = formQueryRemember(request);

        PageHelper.startPage(Integer.parseInt(requestParams.get("currentPage").toString()),
                Integer.parseInt(requestParams.get("pageSize").toString()));
        Map<String, Object> params = getQureyParams(requestParams);

        String createTimeScope = null, startTime = null, endTime = null;
        if (params.get("createTimeScope") != null) {
            createTimeScope = params.get("createTimeScope").toString();
        }

        // 时间范围处理
        if (StringHelper.isNotEmpty(createTimeScope)) {
            String[] split = createTimeScope.split(" - ");
            startTime = split[0];
            endTime = split[1];
        }
        // 移除特殊参数
        params.remove("createTimeScope");
        // 传递新参数
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        final Page<ProductOtherAttachFile> results = (Page<ProductOtherAttachFile>) productOtherAttachFileService.listWdmbByParams(params);
        List<ProductOtherAttachFile> rtn = results.getResult();
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
        resultMap.put("flag", "true");
        resultMap.put("msg", "查询成功");
        resultMap.put("count", String.valueOf(results.getTotal()));
        resultMap.put("data", data);

        // 记录操作日志
        saveBusinessLog("文档模板信息管理", "文档模板信息列表", params);

        return resultMap;
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

    /**
     * 保存附件
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 15:07
     * @Param
     **/
    @PostMapping(value = "/rwfj")
    @ResponseBody
    public Map<String, Object> rwfj(@RequestBody ProductOtherAttachFile productOtherAttachFile) {
        SysManager currentManager = getSessionSysManager();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (StringHelper.isBlank(productOtherAttachFile.getId())) {
            productOtherAttachFile.setDataStatus(GlobalConstant.DATA_VALID);
            productOtherAttachFile.setId(RandomUtil.getSerialNumber());
            productOtherAttachFile.setCreatorId(currentManager.getId().toString());
            productOtherAttachFile.setCreatorName(currentManager.getName());
            productOtherAttachFile.setCreateTime(DateHelper.getYMDHMSFormatDate(new Date()));
            //附件类型  复核申请上传文件
            productOtherAttachFile.setType("wdmbAttachFile");
            productOtherAttachFile.setAttachFile(productOtherAttachFile.getAttachFile());
            productOtherAttachFileService.addProductOtherAttachFile(productOtherAttachFile);
        } else {
            productOtherAttachFile.setEditorId(currentManager.getId().toString());
            productOtherAttachFile.setEditorName(currentManager.getName());
            productOtherAttachFile.setEditTime(DateHelper.getYMDHMSFormatDate(new Date()));
            productOtherAttachFile.setAttachFile(productOtherAttachFile.getAttachFile());
            productOtherAttachFileService.updateProductOtherAttachFileById(productOtherAttachFile);
        }

        resultMap.put("flag", "true");
        resultMap.put("msg", "文档模板信息修改成功");

        // 记录操作日志
        saveBusinessLog("文档模板信息管理", "修改文档模板信息", productOtherAttachFile);


        return resultMap;
    }

    /**
     * 新增或修改文档模板信息
     *
     * @return
     * @Author 魏列军
     * @Description //TODO
     * @Date 2020/5/15 15:07
     * @Param
     **/
    @PostMapping(value = "/addOrUpdate")
    @ResponseBody
    public Map<String, Object> addOrUpdateWdmb(@RequestBody ProductOtherAttachFile productOtherAttachFile) {
        SysManager currentManager = getSessionSysManager();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        //新增
        if (StringHelper.isBlank(productOtherAttachFile.getId())) {
            //设置初始化值
            // 数据有效

            productOtherAttachFile.setDataStatus(GlobalConstant.DATA_VALID);
            productOtherAttachFile.setCreatorId(currentManager.getId().toString());
            productOtherAttachFile.setCreatorName(currentManager.getName());
            productOtherAttachFile.setCreateTime(DateHelper.getYMDHMSFormatDate(new Date()));
            productOtherAttachFile = productOtherAttachFileService.addProductOtherAttachFile(productOtherAttachFile);
            resultMap.put("flag", "true");
            resultMap.put("msg", "文档模板信息新增成功");

            // 记录操作日志
            saveBusinessLog("文档模板信息管理", "新增文档模板信息", productOtherAttachFile);

            return resultMap;
        } else {//编辑

            productOtherAttachFileService.updateProductOtherAttachFileById(productOtherAttachFile);


            resultMap.put("flag", "true");
            resultMap.put("msg", "文档模板信息修改成功");

            // 记录操作日志
            saveBusinessLog("文档模板信息管理", "修改文档模板信息", productOtherAttachFile);

        }
        return resultMap;
    }

}
