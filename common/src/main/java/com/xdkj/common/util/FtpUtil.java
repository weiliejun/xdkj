package com.xdkj.common.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class FtpUtil {
    private FTPClient ftpClient;

    public void connectServer(String server, int port, String user, String password) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setControlEncoding("utf-8");
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    public void closeServer() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public boolean changeDirectory(String path) throws IOException {
        boolean flag = true;
        if (ftpClient.changeWorkingDirectory(path)) {
            return flag;
        } else {
            return createDirectory(path);
        }
    }

    public boolean createDirectory(String path) throws IOException {
        boolean flag = true;
        String[] pathSplit = path.split("/");
        if (pathSplit.length > 0) {
            try {
                for (String dirName : pathSplit) {
                    if (ftpClient.changeWorkingDirectory(dirName)) {
                        continue;
                    } else {
                        if (ftpClient.makeDirectory(dirName)) {
                            ftpClient.changeWorkingDirectory(dirName);
                        }
                    }
                }
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    public boolean uploadFile(InputStream iStream, String newName) throws IOException {
        boolean flag = true;
        try {
            flag = ftpClient.storeFile(newName, iStream);
        } catch (IOException e) {
            flag = false;
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return flag;
    }

    public InputStream downloadFile(String filePath) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = ftpClient.retrieveFileStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
