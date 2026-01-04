package com.zxtx.hummer.withdraw.req;

import lombok.Data;

import java.util.List;

@Data
public class WithdrawCheckReq {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 审核备注
     */
    private String vioResult;

    /**
     * 异常原因代码
     */
    private List<String> typeCodes;
}
