package com.zxtx.hummer.company.enums;

public enum ExchangeType {
    EXCHANGE(3, "换机"),
    ONEKEY(4, "一键拉新"),
    ;
    int code;
    String name;

    ExchangeType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (ExchangeType value : values()) {
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
