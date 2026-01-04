package com.zxtx.hummer.company.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 门店换机和验证包
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyExchangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long companyId;

    @ApiModelProperty("换机包ID")
    private List<Long> exchangePhoneId;

    @ApiModelProperty("验新包ID")
    private List<Long> exchangeVerifyId;


    @ApiModelProperty("号卡套餐ID")
    private List<Long> hkProductId;
}