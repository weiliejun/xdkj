package com.xdkj.common.util;

/**
 * 服务apiUrl辅助类
 */
public class APIUrlHelper {

    private static final String MBIGER_URL = "https://zd.cf360.com";

    /**
     * 根据接口名称获取请求url
     */
    public static String getRequestUrl(APIName apiName) {
        return apiName.getHostUrl() + apiName.getApiCallUrl();
    }

    public static enum APIName {
        //众大技术服务【全国天气预报查询】
        mbigerWeatherQuery("weatherQuery", MBIGER_URL, "/mbiger-services/WeatherQuery"),
        //众大技术服务【手机号码归属地查询
        mbigerMobileNumberPlaceQuery("mobileNumberPlaceQuery", MBIGER_URL, "/mbiger-services/MobileNumberPlaceQuery"),
        //众大技术服务【全球 IP 地址查询】
        mbigerIpPlaceQuery("ipPlaceQuery", MBIGER_URL, "/mbiger-services/IpPlaceQuery"),
        //众大技术服务【机动车驾考公开题库查询】
        mbigerDrivingQuestionsQuery("drivingQuestionsQuery", MBIGER_URL, "/mbiger-services/DrivingQuestionsQuery"),
        //众大技术服务【全球快递物流查询】
        mbigerExpressDeliveryQuery("expressDeliveryQuery", MBIGER_URL, "/mbiger-services/ExpressDeliveryQuery"),
        //众大技术服务【全国身份证实名认证】
        mbigerIdentityCardIdentify("identityCardIdentify", MBIGER_URL, "/mbiger-services/IdentityCardIdentify"),
        //众大技术服务【全国银行卡二、三、四要素实名认证】
        mbigerBankCardIdentify("bankCardIdentify", MBIGER_URL, "/mbiger-services/BankCardIdentify"),
        //众大技术服务【全国手机号（三网）实名认证】
        mbigerMobileNumberIdentify("mobileNumberIdentify", MBIGER_URL, "/mbiger-services/MobileNumberIdentify"),
        //众大技术服务【云短信-发送手机验证码短消息】
        mbigerSmsCodeSend("smsCodeSend", MBIGER_URL, "/mbiger-services/SmsCodeSend"),
        //众大技术服务【人工智能-智能问答助手】
        mbigerIntelligentAssistantQA("intelligentAssistantQA", MBIGER_URL, "/mbiger-services/IntelligentAssistantQA");
        /**
         * 接口名称
         */
        private String service;
        /**
         * 主路径
         */
        private String hostUrl;
        /**
         * 接口请求地址
         */
        private String apiCallUrl;

        private APIName(String service, String hostUrl, String apiCallUrl) {

            this.service = service;
            this.hostUrl = hostUrl;
            this.apiCallUrl = apiCallUrl;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getHostUrl() {
            return hostUrl;
        }

        public void setHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
        }

        public String getApiCallUrl() {
            return apiCallUrl;
        }

        public void setApiCallUrl(String apiCallUrl) {
            this.apiCallUrl = apiCallUrl;
        }

        /**
         * 根据接口名称获取接口枚举
         */
        public static APIName getAPIName(String service) {
            for (APIName api : APIName.values()) {
                if (api.getService().equals(service)) {
                    return api;
                }
            }
            return null;
        }
    }

}
