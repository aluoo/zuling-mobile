package com.zxtx.hummer.common.utils;

public class SecureUtils {


    public static String getSecIdCardStr(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return null;
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
    }


    public static String getSecMobileStr(String mobileNumber) {

        if (StringUtils.isBlank(mobileNumber)) {
            return null;
        }
        return mobileNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


}
