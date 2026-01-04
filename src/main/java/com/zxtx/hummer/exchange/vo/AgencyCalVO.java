package com.zxtx.hummer.exchange.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgencyCalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成员数量")
    private Integer staffNum;

    @ApiModelProperty("门店数量")
    private Integer companyNum;

}
