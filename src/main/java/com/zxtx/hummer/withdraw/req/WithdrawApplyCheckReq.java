package com.zxtx.hummer.withdraw.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class WithdrawApplyCheckReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("申请订单编号")
    private String applyNo;

    @ApiModelProperty("归属审核人id(平台审核类型为渠道子后台时有值)")
    private Long toEmployeeId;

}
