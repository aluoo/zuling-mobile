package com.zxtx.hummer.em.enums;

import com.zxtx.hummer.company.enums.CompanyStatus;

public enum CompanyType {
    COMPANY(1, "公司"),
    STORE(2, "门店"),
    CHAIN(3, "连锁"),
    RECYCLE(4, "回收商");
    int code;
    String name;

    CompanyType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (CompanyType value : values()) {
            if (value.getCode()==code) {
                return value.getName();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
