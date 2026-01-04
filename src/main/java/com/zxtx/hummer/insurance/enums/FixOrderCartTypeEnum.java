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
public enum FixOrderCartTypeEnum {
    BANK_ACCOUNT(1, "银行卡"),
    ALIPAY_ACCOUNT(2, "支付宝"),
    PUBLIC_ACCOUNT(3, "对公账户"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}