package com.zxtx.hummer.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 门店换机和验证包
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "门店换机和验证包", description = "门店换机和验证包")
public class CompanyExchangeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("换机包ID")
    private List<Long> exchangePhoneId;

    @ApiModelProperty("验新包ID")
    private List<Long> exchangeVerifyId;

    @ApiModelProperty("号卡套餐ID")
    private List<Long> hkProductId;
}