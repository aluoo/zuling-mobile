package com.zxtx.hummer.withdraw.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡异常类型
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Getter
@AllArgsConstructor
public enum WithdrawCardIllegalTypeEnum {

    /**
     * INFO_ERROR 账户信息不一致，请重新绑定
     */
    INFO_ERROR("INFO_ERROR", "账户信息不一致，请重新绑定"),

    /**
     * STATUS_ERROR 账户状态异常，请重新绑定
     */
    STATUS_ERROR("STATUS_ERROR", "账户状态异常，请重新绑定"),
    ;

    private String code;
    private String message;

    public static String getDescByCode(String code) {
        for (WithdrawCardIllegalTypeEnum typeEnum : WithdrawCardIllegalTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getMessage();
            }
        }
        return "";
    }
}
