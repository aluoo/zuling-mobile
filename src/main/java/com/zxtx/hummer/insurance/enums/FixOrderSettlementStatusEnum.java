package com.zxtx.hummer.insurance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum FixOrderSettlementStatusEnum {
    FAILED(-1, "打款失败", -1, "打款失败", "打款失败"),
    PENDING_PAYMENT(0, "待打款", 0, "确认最终理赔金额", "确认最终理赔金额"),
    PAYING(1, "打款中", 1, "打款中", "打款中"),
    PAYED(2, "打款成功", 2, "打款成功", "打款成功"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
    @Getter
    private final Integer operation;
    @Getter
    private final String operationDesc;
    @Getter
    private final String operationRemark;
}