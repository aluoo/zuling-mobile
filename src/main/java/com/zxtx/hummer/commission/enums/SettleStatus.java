package com.zxtx.hummer.commission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 系统结算状态枚举类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-08
 */
@Getter
@AllArgsConstructor
public enum SettleStatus {

    /**
     * 0-不结算
     */
    NO_SETTLE(0, "不结算"),

    /**
     * 1-待结算
     */
    WAIT_TO_SETTLE(1, "待结算"),

    /**
     * 2-已结算
     */
    HAVING_SETTLE(2, "已结算"),
    ;

    private Integer status;

    private String name;


    public static String getDescByCode(Integer code) {
        for (SettleStatus typeEnum : SettleStatus.values()) {
            if (typeEnum.getStatus().equals(code)) {
                return typeEnum.getName();
            }
        }
        return "";
    }

}
