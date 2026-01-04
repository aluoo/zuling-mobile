package com.zxtx.hummer.company.enums;

public enum RecycleRechargeStatus {
    TO_AUDIT(0, "待审核"),
    FAIL(1, "审核失败"),
    PASS(2, "审核通过");

    Integer code;
    String name;

    RecycleRechargeStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        for (RecycleRechargeStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }
}
