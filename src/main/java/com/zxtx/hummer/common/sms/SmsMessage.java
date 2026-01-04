package com.zxtx.hummer.common.sms;


import java.util.HashMap;
import java.util.Map;

public class SmsMessage {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 短信签名名称
     */
    private String signName = "安逸出行";
    /**
     * 模板编号
     */
    private String templateCode;
    /**
     * 短信参数
     */
    private Map<String, String> params = new HashMap<>();

    private SmsMessage() {
    }

    public static SmsMessage create() {
        return new SmsMessage();
    }

    public SmsMessage mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public SmsMessage signName(String signName) {
        this.signName = signName;
        return this;
    }

    public SmsMessage templateCode(String templateCode) {
        this.templateCode = templateCode;
        return this;
    }

    public SmsMessage addParam(String key, String param) {
        params.put(key, param);
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSignName() {
        return signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "mobile='" + mobile + '\'' +
                ", signName='" + signName + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", params=" + params +
                '}';
    }
}
