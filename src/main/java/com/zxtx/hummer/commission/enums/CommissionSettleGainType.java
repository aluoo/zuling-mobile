package com.zxtx.hummer.commission.enums;

import com.zxtx.hummer.em.enums.OperateType;
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
public enum CommissionSettleGainType {

    /**
     * 1-直接做单获得
     */
    BY_MYSELF(1, "直接做单获得"),

    /**
     * 2-下级贡献
     */
    CHILD_CONTRIBUTE(2, "下级贡献"),
    ;

    private Integer type;

    private String typeName;

    public static String getDescByCode(Integer code) {
        for (CommissionSettleGainType typeEnum : CommissionSettleGainType.values()) {
            if (typeEnum.getType().equals(code)) {
                return typeEnum.getTypeName();
            }
        }
        return "";
    }

}
