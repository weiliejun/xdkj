package com.xdkj.common.components.filesync.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Controller
public class DeleteFile {
    //删除的路径
    private static Map<String, Boolean> deletePathMap = new HashMap<String, Boolean>();

    static {
        deletePathMap.put("/upload/authentication/temp", true);
    }

    /**
     * 删除上传的缓存图片
     */
    @RequestMapping(value = {"/portal/file/delete"})
    public @ResponseBody
    boolean receive(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "fileName", required = true) String fileName, @RequestParam(value = "filePath", required = true) String filePath) {
        boolean delete_flag = false;
        //判断是否是可删除的路径，限制删除路径，避免删除其他资源
        if (isDelete(filePath)) {
            if (filePath.indexOf("/") == 0) {
                filePath = filePath.substring(1);
            }
            String rootPath = request.getSession().getServletContext().getRealPath(File.separator) + filePath;
            String realPath = rootPath + File.separator + fileName;
            File file = new File(realPath);
            if (file.exists() && file.isFile() && file.delete()) {
                delete_flag = true;
            } else {
                delete_flag = false;
            }
        }
        return delete_flag;
    }

    private boolean isDelete(String filePath) {
        boolean flag = false;
        if (deletePathMap.get(filePath) != null) {
            flag = deletePathMap.get(filePath);
        }
        return flag;

    }
}
