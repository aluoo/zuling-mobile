package com.zxtx.hummer.exchange.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeOrderFillDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private String id;

    @ApiModelProperty("单号")
    private String orderSn;


}