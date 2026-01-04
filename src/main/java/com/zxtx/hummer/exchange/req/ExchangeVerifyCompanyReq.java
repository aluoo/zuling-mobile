package com.zxtx.hummer.exchange.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class ExchangeVerifyCompanyReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("验证包公司列表传true换机传FALSE")
    private Boolean verifyFlag;
    @ApiModelProperty("包ID获取包门店时候传")
    private Long packageId;
    @ApiModelProperty("门店列表时候传")
    private String typeCode;
}
