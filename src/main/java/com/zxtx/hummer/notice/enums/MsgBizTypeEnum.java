package com.zxtx.hummer.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum MsgBizTypeEnum {
    COMM("comm", "普通消息"),
    WITHDRAW("withdraw", "提现"),
    SYSNOTICE("sysnotice", "系统公告"),
    ACTIVE_APPEAL("active_appeal", "激活申诉"),
    DEPOSIT_APPEAL("deposit_appeal", "押金申诉"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}