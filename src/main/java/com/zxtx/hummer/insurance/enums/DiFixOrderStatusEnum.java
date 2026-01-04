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
public enum DiFixOrderStatusEnum {
    // 提交待审核
    WAIT(0, "报险审核中"),
    DATA_WAIT(1, "资料审核中"),
    ONE_PASS(2, "审核通过,待上传维修资料"),
    ONE_FAIL(3, "审核失败,请修改报修资料"),
    DATA_PASS(4, "资料审核通过,待理赔"),
    DATA_EDIT(5, "资料审核失败,修改维修资料"),
    PAY_PASS(6, "理赔完成"),

    CANCEL(7, "报险单取消"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}