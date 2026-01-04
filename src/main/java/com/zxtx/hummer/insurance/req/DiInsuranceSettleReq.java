package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class DiInsuranceSettleReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("报修单ID")
    private Long id;

    @ApiModelProperty("理赔金额")
    private Long amount;

    private String settleAmount;
}