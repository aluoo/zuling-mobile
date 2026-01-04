package com.zxtx.hummer.em.enums;

public enum ReasonType {
    ON_LINE(1, "虚假宣传"),
    CLOSE(2, "其他"),
    ;
    Integer code;
    String name;

    ReasonType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getDescByCode(Integer code) {
        for (ReasonType typeEnum : ReasonType.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getName();
            }
        }
        return "";
    }
}
