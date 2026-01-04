package com.zxtx.hummer.em.enums;

import com.zxtx.hummer.company.enums.CompanyStatus;

public enum EmStatus {
    NORMAL((byte) 1, "正常"),
    CANCEL((byte) 2, "注销"),
    OFFLINE((byte) 3, "下线");

    byte code;
    String name;

    EmStatus(Byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static String getNameByCode(Byte code) {
        for (EmStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
