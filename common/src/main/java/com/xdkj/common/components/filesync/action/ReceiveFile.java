package com.xdkj.common.components.filesync.action;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author zhangyf
 * @date 2014-04-13 网站端接收文件
 */

@Controller
public class ReceiveFile {

    @RequestMapping(value = {"/portal/file/receive"})
    public void receive(HttpServletRequest request, HttpServletResponse response, String fileName, String filePath, @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        if (ServletFileUpload.isMultipartContent(request)) {
            String rootPath = request.getSession().getServletContext().getRealPath(File.separator) + filePath;
            File rootFile = new File(rootPath);
            if (!rootFile.exists()) {
                FileUtils.forceMkdir(rootFile);
            }
            String picPath = rootPath + File.separator + fileName;
            File picFile = new File(picPath);
            file.transferTo(picFile);
        }
    }
}
