package com.xdkj.common.util;


import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Zip压缩/解压缩工具类
 * 实现对目标路径及其子路径下的所有文件及空目录的压缩
 */

public class FileHelper {
    //换行符
    public static final String NEW_LINE = "\r\n";
    // 逗号
    public static final String COMMA = ",";
    //竖线
    public static final String VERTICAL_LINE = "|";
    //压缩文件后缀名
    public static final String ZIP_FILE_SUFFIX = ".zip";
    //压缩文件后缀名
    public static final String ENCFILE_SUFFIX = ".enc";

    //缓冲器大小
    private static final int BUFFER = 512;
    private static Logger logger = Logger.getLogger(FileHelper.class);

    /**
     * 获取给定源目录下的所有文件及空的子目录
     * 递归实现
     *
     * @param srcFile
     * @return
     */
    private static List<File> getAllFiles(File srcFile) {
        List<File> fileList = new ArrayList<File>();
        File[] tmp = srcFile.listFiles();

        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                fileList.add(tmp[i]);
                logger.debug("add file: " + tmp[i].getName());
            }
            if (tmp[i].isDirectory()) {
                if (tmp[i].listFiles().length != 0) {//若不是空目录，则递归添加其下的目录和文件
                    fileList.addAll(getAllFiles(tmp[i]));
                } else {//若是空目录，则添加这个目录到fileList
                    fileList.add(tmp[i]);
                    logger.debug("add empty dir: " + tmp[i].getName());
                }
            }
        }
        return fileList;
    }

    /**
     * 取相对路径
     * 依据文件名和压缩源路径得到文件在压缩源路径下的相对路径
     *
     * @param dirPath 压缩源路径
     * @param file
     * @return 相对路径
     */
    private static String getRelativePath(String dirPath, File file) {
        File dir = new File(dirPath);
        String relativePath = file.getName();

        while (true) {
            file = file.getParentFile();
            if (file == null) {
                break;
            }
            if (file.equals(dir)) {
                break;
            } else {
                relativePath = file.getName() + File.separator + relativePath;
            }
        }
        return relativePath;
    }

    /**
     * 创建文件
     * 根据压缩包内文件名和解压缩目的路径，创建解压缩目标文件，
     * 生成中间目录
     *
     * @param dstPath  解压缩目的路径
     * @param fileName 压缩包内文件名
     * @return 解压缩目标文件
     * @throws IOException
     */
    private static File createFile(String dstPath, String fileName) throws IOException {
        String[] dirs = fileName.split(File.separator);//将文件名的各级目录分解
        File file = new File(dstPath);

        if (dirs.length > 1) {//文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);//依次创建文件对象直到文件的上一级目录
            }
            if (!file.exists()) {
                file.mkdirs();//文件对应目录若不存在，则创建
                logger.debug("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[dirs.length - 1]);//创建文件
            return file;
        } else {
            if (!file.exists()) {
                file.mkdirs();//若目标路径的目录不存在，则创建
                logger.debug("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[0]);//创建文件
            return file;
        }
    }

    /**
     * 读取文本文件内容
     *
     * @param filePath 文件所在路径
     */
    public static String readTxtFile(String filePath) {
        File file = new File(filePath);
        //文件不存在或不是文件
        if (!file.exists() || !file.isFile()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        BufferedReader reader = null;
        String line = null; // 用来保存每行读取的内容
        try {
            is = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                buffer.append(line); // 将读到的内容添加到 buffer 中
                buffer.append(NEW_LINE); // 添加换行符
                line = reader.readLine(); // 读取下一行
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 解压缩方法
     *
     * @param zipFileName 压缩文件名
     * @param dstPath     解压目标路径
     * @return
     */
    public static boolean unZip(String zipFileName, String dstPath) {
        logger.debug("zip uncompressing...");
        FileInputStream fis = null;
        ZipInputStream zipInputStream = null;
        OutputStream outputStream = null;
        try {
            fis = new FileInputStream(zipFileName);
            zipInputStream = new ZipInputStream(fis);
            ZipEntry zipEntry = null;
            byte[] buffer = new byte[BUFFER];//缓冲器
            int readLength = 0;//每次读出来的长度

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {//若是zip条目目录，则需创建这个目录
                    File dir = new File(dstPath + File.separator + zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                        logger.debug("mkdirs: " + dir.getCanonicalPath());
                        continue;//跳出
                    }
                }
                File file = createFile(dstPath, zipEntry.getName());//若是文件，则需创建该文件
                logger.debug("file created: " + file.getCanonicalPath());
                outputStream = new FileOutputStream(file);
                while ((readLength = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
                    outputStream.write(buffer, 0, readLength);
                }
                //关闭文件流，不能删除
                outputStream.close();
                logger.debug("file uncompressed: " + file.getCanonicalPath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("unzip fail:" + e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("unzip fail:" + e.getMessage());
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.debug("unzip success!");
        return true;
    }

    /**
     * 压缩方法
     * （可以压缩空的子目录）
     *
     * @param srcPath     压缩源路径
     * @param zipFileName 目标压缩文件全路径
     * @return
     */
    public static boolean zip(String srcPath, String zipFileName) {
        logger.debug("zip compressing...");
        boolean zipResult = false;
        //如果压缩文件存在，就先删除
        File zipFile = new File(zipFileName);
        if (zipFile.exists()) {
            zipFile.delete();
        }
        File srcFile = new File(srcPath);
        List<File> fileList = getAllFiles(srcFile);//所有要压缩的文件
        byte[] buffer = new byte[BUFFER];//缓冲器
        ZipEntry zipEntry = null;
        int readLength = 0;//每次读出来的长度
        FileOutputStream fos = null;
        ZipOutputStream zipOutputStream = null;
        InputStream inputStream = null;
        try {
            fos = new FileOutputStream(zipFileName);
            zipOutputStream = new ZipOutputStream(fos);
            for (File file : fileList) {
                //如果是压缩文件本身，则跳过
                if (zipFileName.equals(file.getPath())) {
                    continue;
                }
                if (file.isFile()) {//若是文件，则压缩这个文件
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file));
                    zipEntry.setSize(file.length());
                    zipEntry.setTime(file.lastModified());
                    zipOutputStream.putNextEntry(zipEntry);
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                    while ((readLength = inputStream.read(buffer, 0, BUFFER)) != -1) {
                        zipOutputStream.write(buffer, 0, readLength);
                    }
                    //关闭文件流，不能删除
                    inputStream.close();
                    logger.debug("file compressed: " + file.getCanonicalPath());
                } else {//若是目录（即空目录）则将这个目录写入zip条目
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file) + File.separator);
                    zipOutputStream.putNextEntry(zipEntry);
                    logger.debug("dir compressed: " + file.getCanonicalPath() + "/");
                }

            }

            zipOutputStream.close();
            logger.debug("zip success!");
            zipResult = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("zip fail: " + e.getMessage());
            zipResult = false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("zip fail: " + e.getMessage());
            zipResult = false;
        } finally {
            if (inputStream == null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return zipResult;
    }


    public static boolean bufferedWriterFile(List<Map<String, String>> fileContent, String fileAllPath, String split) {
        return bufferedWriterFile(fileContent, fileAllPath, true, split, "UTF-8", BUFFER);
    }

    /**
     * 根据list集合，生成文件
     *
     * @param fileContent 文件内容
     * @param fileAllPath 生成文件的全路径
     * @param append      是否是在文件尾追加内容
     * @param split       记录字段分割符
     * @param charsetName 文件编码
     * @param bufferSize  缓存大小
     * @return
     */
    public static boolean bufferedWriterFile(List<Map<String, String>> fileContent, String fileAllPath, boolean append, String split, String charsetName, int bufferSize) {
        boolean createFlag = false;
        File resultFile = new File(fileAllPath);
        if (bufferSize <= 0) {
            bufferSize = BUFFER;
        }
        if (split == null) {
            split = COMMA;
        }
        BufferedWriter bw = null;
        OutputStreamWriter osw = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(resultFile);
            osw = new OutputStreamWriter(fos, charsetName);
            bw = new BufferedWriter(osw, bufferSize);
            if (fileContent != null && fileContent.size() > 0) {
                if (resultFile.length() > 0) {
                    bw.write(NEW_LINE);
                }
                Iterator<Map.Entry<String, String>> it = null;
                Map.Entry<String, String> entry = null;
                String value = "";
                int index = 0;
                for (int i = 0; i < fileContent.size(); i++) {
                    index = 0;
                    //记录与记录之间换行
                    if (i > 0) {
                        bw.write(NEW_LINE);
                    }
                    //单条记录写入文件
                    for (it = fileContent.get(i).entrySet().iterator(); it.hasNext(); ) {
                        entry = it.next();
                        value = entry.getValue();
                        value = StringHelper.isNotEmpty(value) ? value : "";
                        if (index == 0) {
                            bw.write(value);
                        } else {
                            bw.append(split);
                            bw.append(value);
                        }
                        index++;
                    }
                }
            }
            bw.flush();
            createFlag = true;
            return createFlag;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return createFlag;
    }

    public static String disposeFileName(String originalFilename) {
        String fileName = "";
        if (originalFilename != null) {
            fileName = originalFilename.replaceAll(" ", "_").replaceAll("　", "_");//过滤中英文空格
            fileName = fileName.toLowerCase();
        }
        return fileName;
    }

    public static String getTempFileName(String extension) {
        long random = new Random(100000000).nextLong();
        String tmpFileName = "upload_tmp_" + DateHelper.getCurrentDate().getTime() + "_" + random + "." + extension;
        return tmpFileName;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}