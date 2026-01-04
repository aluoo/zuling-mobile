package com.zxtx.hummer.withdraw.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提现方式类型
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Getter
@AllArgsConstructor
public enum WithdrawCardTypeEnum {

    /**
     * 银行卡 1
     */
    BANK_CARD(1),

    /**
     * 支付宝账号 2
     */
    ZFB_ACCOUNT(2),

    /**
     * 对公账户 3
     */
    PUBLIC_ACCOUNT(3),
    ;

    private int type;
}
