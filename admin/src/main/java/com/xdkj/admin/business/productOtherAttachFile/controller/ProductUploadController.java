package com.xdkj.admin.business.productOtherAttachFile.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xdkj.admin.business.productOtherAttachFile.service.ProductOtherAttachFileService;
import com.xdkj.admin.web.base.AbstractBaseController;
import com.xdkj.common.components.filesync.FileSynchronizer;
import com.xdkj.common.constant.GlobalConstant;
import com.xdkj.common.util.FileHelper;
import com.xdkj.common.util.ImageHelper;
import com.xdkj.common.util.RandomUtil;
import com.xdkj.common.util.StringHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 附件，app配置图片上传
 * @Author lvjian
 * @UpdateDate 2019/7/30 14:11
 */
@Controller
public class ProductUploadController extends AbstractBaseController {

    private final Logger logger = Logger.getLogger(ProductUploadController.class);

    @Autowired
    private FileSynchronizer fileSynchronizer;

    @Autowired
    private ProductOtherAttachFileService productOtherAttachFileService;

    private static boolean deleteDir(File dir) {
        if (dir.isFile()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();// 递归删除目录中的子目录下
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
     * @Description 并将文件上传到网络端
     * @Author lvjian
     * @UpdateDate 2019/6/19 16:19
     */
    @RequestMapping("/fujian")
    public String accountRealAuth(HttpServletRequest request, Model model) {
        model.addAllAttributes((Map<String, Object>) request.getSession().getAttribute(request.getRequestURI()));
        return "app/product/6附件";
    }


    /**
     * @param request
     * @param model
     * @return
     * @throws IOException
     * @description 多文件上传
     * @version v1.0
     * @author 吕剑
     * @update 2019年6月24日 上午14:56:50
     */
    @RequestMapping("/fuJian/upload")
    public @ResponseBody
    Map<String, Object> uploadFuJ(HttpServletRequest request, Model model)
            throws IOException {
        Map<String, Object> map = new HashMap<>();

        String realPath = request.getSession().getServletContext()
                .getRealPath(File.separator);

        //拿到文件集合 并对文件进行处理
        Map<String, MultipartFile> formMultipartFile = getFormMultipartFile(request);
        if (formMultipartFile == null) {
            map.put("code", "false");
            map.put("msg", "不能上传空的文件");
        }
        //将每次文件的相关数据都存入jsonArray
        JSONArray jsonAry = null;
        for (Map.Entry<String, MultipartFile> entry : formMultipartFile.entrySet()) {
            MultipartFile uploadFile = entry.getValue();
            String fileName = FileHelper.disposeFileName(uploadFile
                    .getOriginalFilename());
            String extension = StringHelper.unqualify(fileName).toLowerCase();
            int type = ImageHelper.IMAGE_UNKNOWN;
            if (uploadFile.getSize() > 20480000000L) {
                map.put("code", "big");
                map.put("tmpFileName", fileName);
            } else if (extension.equals("jpg") || extension.equals("jpeg")
                    || extension.equals("gif") || extension.equals("png")
                    || "doc".equals(extension) ||
                    "docx".equals(extension) ||
                    "pdf".equals(extension) ||
                    "xls".equals(extension) ||
                    "xlsx".equals(extension) ||
                    "rar".equals(extension) ||
                    "zip".equals(extension) ||
                    "ppt".equals(extension) ||
                    "pptx".equals(extension)) {

                if (extension.equals("jpg") || extension.equals("jpeg")) {
                    type = ImageHelper.IMAGE_JPEG;
                } else if (extension.equals("gif") || extension.equals("png")) {
                    type = ImageHelper.IMAGE_PNG;
                    if (extension.toLowerCase().equals("gif")) {
                        extension = "png";// We cannot handle gifs
                    }
                }
                try {
                    // 临时文件名称不能有汉字，统一重命名
                    String tmpFileName = RandomUtil.getSerialNumber() + "." + extension;
                    String tmpPath = GlobalConstant.APP_QIYEBAO_TEMP_USER_PHOTO_PATH;
                    if (uploadFile != null && !uploadFile.isEmpty()) {
                        logger.info("原临时图片不存在或已删除！");
                        logger.info("tmpPath=======================" + tmpPath);
                        File tmpFile = new File(realPath + tmpPath);
                        if (!tmpFile.exists()) {
                            tmpFile.mkdirs();
                        }
                        String tmpFilePath = realPath + tmpPath + File.separator + tmpFileName;
                        if (extension.equals("jpg") || extension.equals("jpeg")
                                || extension.equals("gif") || extension.equals("png")) {
                            //保存图片
                            BufferedImage image = ImageHelper.resizeImage(
                                    uploadFile.getInputStream(), type, 226, 150);
                            ImageHelper.saveImage(image, tmpFilePath, type);
                        } else {
                            uploadFile.transferTo(new File(tmpFilePath));
                        }
                        File file = new File(tmpFilePath);
                        //数据库-资源服务器地址
                        String savePath = GlobalConstant.APP_QIYEBAO_USER_PHOTO_PATH + File.separator;
                        boolean b = fileSynchronizer.syncFile(file, savePath, file.getName());// 上传到网站端-资源服务器
                        if (b == false) {
                            map.put("tmpFileName", tmpFileName);
                            map.put("code", "lose");
                        } else {
                            jsonAry = new JSONArray();
                            JSONObject uploadFileInfo = new JSONObject();
                            uploadFileInfo.put("name", file.getName());
                            uploadFileInfo.put("path", FileSynchronizer.getReceiverUrl() + savePath.replaceAll("\\\\", "/") + file.getName());
                            uploadFileInfo.put("type", file.getName().substring(file.getName().indexOf(".") + 1));
                            uploadFileInfo.put("thumbnail", "null");
                            jsonAry.add(uploadFileInfo);
                            logger.info("tempFilePath==" + File.separator + tmpFileName);
                            map.put("tmpFilePath", tmpPath + File.separator
                                    + tmpFileName);
                            map.put("code", "0");
                            map.put("oldFileName", fileName);
                            map.put("tmpFileName", tmpFileName);
                        }
                    } else {
                        map.put("tmpFileName", tmpFileName);
                        map.put("code", "1");
                    }
                } catch (Exception e) {
                    map.put("code", "2");
                    map.put("message", "服务忙碌请稍后重试");
                    e.printStackTrace();
                }
            } else {
                map.put("code", "false");
                map.put("message", "请选择正确格式的文件");
            }
        }
        if (jsonAry == null) {
            return map;
        }
        map.put("attachFile", jsonAry.toJSONString());
        return map;
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @description 上传图片
     * @version v1.0
     * @author 吕剑
     * @update 2019年9月02日 上午14:56:50
     */
    @RequestMapping("/upload/logoImg")
    public @ResponseBody
    Map<String, Object> uploadAppLunBoTu(HttpServletRequest request, Model model)
            throws IOException {
        Map<String, Object> map = new HashMap<>();

        String realPath = request.getSession().getServletContext()
                .getRealPath(File.separator);

        //拿到文件集合 并对文件进行处理
        Map<String, MultipartFile> formMultipartFile = getFormMultipartFile(request);
        if (formMultipartFile == null) {
            map.put("code", "false");
            map.put("msg", "不能上传空的文件");
        }
        for (Map.Entry<String, MultipartFile> entry : formMultipartFile.entrySet()) {
            MultipartFile uploadFile = entry.getValue();
            String fileName = FileHelper.disposeFileName(uploadFile
                    .getOriginalFilename());
            String extension = StringHelper.unqualify(fileName).toLowerCase();
            int type = ImageHelper.IMAGE_UNKNOWN;
            if (uploadFile.getSize() > 1048576) {
                map.put("code", "big");
                map.put("tmpFileName", fileName);
            } else if (extension.equals("jpg") || extension.equals("jpeg")
                    || extension.equals("gif") || extension.equals("png")) {

                if (extension.equals("jpg") || extension.equals("jpeg")) {
                    type = ImageHelper.IMAGE_JPEG;
                } else if (extension.equals("gif") || extension.equals("png")) {
                    type = ImageHelper.IMAGE_PNG;
                    if (extension.toLowerCase().equals("gif")) {
                        extension = "png";// We cannot handle gifs
                    }
                }
                try {
                    // 临时文件名称不能有汉字，统一重命名
                    String tmpFileName = RandomUtil.getSerialNumber() + "." + extension;
                    String tmpPath = GlobalConstant.APP_QIYEBAO_TEMP_USER_PHOTO_PATH + File.separator;
                    if (uploadFile != null && !uploadFile.isEmpty()) {
                        logger.info("原临时图片不存在或已删除！");
                        logger.info("tmpPath=======================" + tmpPath);
                        File tmpFile = new File(realPath + tmpPath);
                        if (!tmpFile.exists()) {
                            tmpFile.mkdirs();
                        }
                        String tmpFilePath = realPath + tmpPath + File.separator + tmpFileName;
                        if (extension.equals("jpg") || extension.equals("jpeg")
                                || extension.equals("gif") || extension.equals("png")) {
                            //保存图片
                            BufferedImage image = ImageHelper.resizeImage(
                                    uploadFile.getInputStream(), type, 226, 150);
                            ImageHelper.saveImage(image, tmpFilePath, type);
                        } else {
                            uploadFile.transferTo(new File(tmpFilePath));
                        }
                        File file = new File(tmpFilePath);
                        //数据库-资源服务器地址
                        String savePath = GlobalConstant.APP_QIYEBAO_USER_PHOTO_PATH + File.separator;
                        boolean b = fileSynchronizer.syncFile(file, savePath, file.getName());// 上传到网站端-资源服务器
                        if (b == false) {
                            map.put("tmpFileName", tmpFileName);
                            map.put("code", "lose");
                        } else {
                            map.put("name", file.getName());
                            map.put("path", FileSynchronizer.getReceiverUrl() + savePath + file.getName());
                            map.put("tmpFilePath", tmpPath + File.separator
                                    + tmpFileName);
                            map.put("code", "0");
                            map.put("oldFileName", fileName);
                            map.put("tmpFileName", tmpFileName);
                            logger.info("tempFilePath==" + File.separator + tmpFileName);
                        }
                    } else {
                        map.put("tmpFileName", tmpFileName);
                        map.put("code", "1");
                    }
                } catch (Exception e) {
                    map.put("code", "2");
                    map.put("message", "服务忙碌请稍后重试");
                    e.printStackTrace();
                }
            } else {
                map.put("code", "false");
                map.put("message", "请选择正确格式的文件");
            }
        }
        return map;
    }
}
