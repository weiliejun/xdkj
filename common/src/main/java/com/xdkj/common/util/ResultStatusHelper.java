package com.xdkj.common.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * App进度条工具类
 *
 * @author 李洪斌
 * @date 2019-8-28 15:48:21
 */
public class ResultStatusHelper {

    /**
     * 获取进度list
     *
     * @return
     */
    public static List<Status> getStatusLis() {
        List<Status> statusList = new ArrayList<Status>();
        return statusList;
    }

    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO
     * @Date 2019/8/28 15:59
     * @Param progressTitle 当前进度标题
     * @Param progressTime 当前进度时间
     * @Param statusList 进度列表
     **/
    public static List<Status> addStatus(String progressTitle, String progressTime, List<Status> statusList) {
        Status status = new Status(progressTitle, progressTime);
        statusList.add(status);
        return statusList;

    }

    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO
     * @Date 2019/8/28 15:59
     * @Param progressValue 当前进度值
     * @Param progressTitle 当前进度标题
     * @Param progressTime 当前进度时间
     * @Param statusList 进度列表
     **/
    public static List<Status> addStatus(String progressValue, String progressTitle, String progressTime, String progressMsg, List<Status> statusList) {
        Status status = new Status(progressValue, progressTitle, progressTime, progressMsg);
        statusList.add(status);
        return statusList;

    }

    /**
     * @return
     * @Author 李洪斌
     * @Description //TODO
     * @Date 2019/8/28 15:59
     * @Param progressValue 当前进度值
     * @Param progressTitle 当前进度标题
     * @Param progressTime 当前进度时间
     * @Param progressMsg 当前进度信息
     * @Param statusList 进度列表
     **/
    public static List<Status> addStatus(String progressValue, String progressTitle, String progressTime, List<Status> statusList) {
        Status status = new Status(progressValue, progressTitle, progressTime);
        statusList.add(status);
        return statusList;

    }

    public static void main(String[] args) {

        List<Status> statusLis = ResultStatusHelper.getStatusLis();
        ResultStatusHelper.addStatus("转入成功", "2019-10-10 22:22；00", statusLis);
        ResultStatusHelper.addStatus("转入失败", "2019-10-10 22:22；00", statusLis);
        ResultStatusHelper.addStatus("转入22", "2019-10-10 22:22；00", statusLis);

        System.out.println(JSONObject.toJSON(statusLis));
    }

    public static class Status {

        /**
         * 当前进度值
         */
        private String progressValue;

        /**
         * 当前进度标题
         */
        private String progressTitle;

        /**
         * 当前进度时间
         */
        private String progressTime;

        /**
         * 当前进度信息
         */
        private String progressMsg;

        public Status(String progressTitle, String progressTime) {
            this.progressTitle = progressTitle;
            this.progressTime = progressTime;
        }

        public Status(String progressValue, String progressTitle, String progressTime) {
            this.progressValue = progressValue;
            this.progressTitle = progressTitle;
            this.progressTime = progressTime;
        }

        public Status(String progressValue, String progressTitle, String progressTime, String progressMsg) {
            this.progressValue = progressValue;
            this.progressTitle = progressTitle;
            this.progressTime = progressTime;
            this.progressMsg = progressMsg;
        }


        public String getProgressValue() {
            return progressValue;
        }

        public void setProgressValue(String progressValue) {
            this.progressValue = progressValue;
        }

        public String getProgressTitle() {
            return progressTitle;
        }

        public void setProgressTitle(String progressTitle) {
            this.progressTitle = progressTitle;
        }

        public String getProgressTime() {
            return progressTime;
        }

        public void setProgressTime(String progressTime) {
            this.progressTime = progressTime;
        }

        public String getProgressMsg() {
            return progressMsg;
        }

        public void setProgressMsg(String progressMsg) {
            this.progressMsg = progressMsg;
        }
    }
}
