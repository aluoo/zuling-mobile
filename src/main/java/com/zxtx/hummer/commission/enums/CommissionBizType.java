package com.zxtx.hummer.commission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 佣金结算来源类型枚举类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-08
 */
@Getter
@AllArgsConstructor
public enum CommissionBizType {

    /**
     * 1-平台服务费
     */
    PLAT_SERVICE(1, "平台服务费"),
    /**
     * 2-APP拉新
     */
    APP_NEW(2, "APP拉新"),
    /**
     * 2-手机回收
     */
    PHONE_DOWN(3, "手机回收"),
    /**
     * 4-享转数保
     */
    INSURANCE_SERVICE(4, "享转数保"),
    /**
     * 5-号卡拉新
     */
    HK_SERVICE(5, "号卡拉新"),
    ;

    private Integer type;

    private String typeName;

}
