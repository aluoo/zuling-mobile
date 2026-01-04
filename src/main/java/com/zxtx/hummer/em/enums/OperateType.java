package com.zxtx.hummer.em.enums;

public enum OperateType {
    ON_LINE(1, "上线"),
    CLOSE(2, "下线"),
    CANCEL(3, "注销"),
    ;
    Integer code;
    String name;

    OperateType(Integer code, String name) {
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
        for (OperateType typeEnum : OperateType.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getName();
            }
        }
        return "";
    }
}
