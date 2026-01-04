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
public enum CommissionPackage {

    /**
     * 1-平台服务费
     */
    PLAT_SERVICE(1L, "平台服务费",true),

    /**
     * 手机压价
     */
    PHONE_DOWN(2L, "手机压价",false),
    /**
     * 换机业务
     */
    HUAN_JI(3L, "换机业务",true),
    /**
     * 一键拉新
     */
    YJLX(4L, "一键更新",true),
    /**
     * 享转数保
     */
    INSURANCE_SERVICE(5L, "享转数保",true),
    /**
     * 享转数保门店
     */
    INSURANCE_COMPANY_SERVICE(6L, "享转数保",false),
    /**
     * 快手绿洲
     */
    KUAI_SHOU_LV_SHOU(7L, "快手绿洲",true),
    IPHONE_DOUYIN(8L, "苹果抖音",true),
    HK_SERVICE(10L, "号卡拉新",true),
    ;

    private Long type;

    private String typeName;

    private Boolean agentFlag;

}
