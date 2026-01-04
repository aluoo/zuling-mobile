package com.zxtx.hummer.common.wx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/14
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonRefundReq implements Serializable {
    private static final long serialVersionUID = 1L;

    //交易流水号
    private String tradeNo;

    //退款流水号
    private String refundNo;

    //交易金额
    private Integer fee;

    //退款金额
    private  Integer refundFee;

    //三方支付流水号
    private String transactionId;
}