package com.zxtx.hummer.company.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CmpPayAmountReq {

    @NotNull
    @ApiModelProperty("渠道公司id")
    private Long id;

    @ApiModelProperty("押金")
    private Integer payAmount;

}
