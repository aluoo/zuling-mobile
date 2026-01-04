package com.zxtx.hummer.common.enums;

public enum SMSEnum {
    OPEN_ETC("open-etc", "SMS_167195338"),
    EM_OBU("em-obu", "SMS_167195350"),
    BIND_MOBILE("bind-mobile", "SMS_167180510"),
    EM_LOGIN("em-login", "SMS_167180378"),
    EM_Register("em-register", "SMS_167180391"),
    quo_order_success("quo_order_success", "SMS_170836432"),
    quo_order_failed("quo_order_failed", "SMS_170836438"),
    quo_order_paid("quo_order_paid", "SMS_170841465"),
    quo_order_paid_failed("quo_order_paid_failed", "SMS_170836423"),
    user_order_posted("user_order_posted", "SMS_171352800"),
    sys_user_add("sys_user_add", "SMS_172742827"),
    sys_user_pwd_reset("sys_user_pwd_reset", "SMS_172742830");
    private String biz;

    private String templateCode;

    SMSEnum(String biz, String templateCode) {
        this.biz = biz;
        this.templateCode = templateCode;
    }

    public String getBiz() {
        return biz;
    }

    public String getTemplateCode() {
        return templateCode;
    }
}
