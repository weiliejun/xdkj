package com.xdkj.common.components.filesync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * 向多个平台传文件
 */
@Component
public class MoreSystemFileSynchronizer {

    private Map<String, String> receiverSystemUrl;

    private String fileReceiverUrl;

    @Autowired
    private FileSynchronizer fileSynchronizer;

    /**
     * @param file
     * @param filePath
     * @param fileName
     * @param receiverSystemUrlKey 向多个平台传文件
     * @return
     * @throws Exception
     */
    public boolean syncFile(File file, String filePath, String fileName, String[] receiverSystemUrlKey) throws Exception {
        boolean result = false;
        if (receiverSystemUrlKey.length > 0) {
            for (String key : receiverSystemUrlKey) {
                result = syncFile(file, filePath, fileName, key);
                if (!result)
                    break;
            }
        }

        return result;
    }

    /**
     * @param file
     * @param filePath
     * @param fileName
     * @param receiverSystemUrlKey 向指定平台传文件
     * @return
     * @throws Exception
     */
    public boolean syncFile(File file, String filePath, String fileName, String receiverSystemUrlKey) throws Exception {
        boolean result = false;
        String receiverUrl = receiverSystemUrl.get(receiverSystemUrlKey) + fileReceiverUrl;
        fileSynchronizer.setReceiverUrl(receiverUrl);
        result = fileSynchronizer.syncFile(file, filePath, fileName);

        return result;
    }

    /**
     * @param file
     * @param filePath
     * @param fileName 向所有平台传文件
     * @return
     * @throws Exception
     */
    public boolean syncFile(File file, String filePath, String fileName) throws Exception {
        boolean result = false;
        for (String key : receiverSystemUrl.keySet()) {
            result = syncFile(file, filePath, fileName, key);
            if (!result)
                break;
        }
        return result;
    }

    public Map<String, String> getReceiverSystemUrl() {
        return receiverSystemUrl;
    }

    public void setReceiverSystemUrl(Map<String, String> receiverSystemUrl) {
        this.receiverSystemUrl = receiverSystemUrl;
    }

    public String getFileReceiverUrl() {
        return fileReceiverUrl;
    }

    public void setFileReceiverUrl(String fileReceiverUrl) {
        this.fileReceiverUrl = fileReceiverUrl;
    }

}
