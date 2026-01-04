package com.zxtx.hummer.commission.dto;

import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionSettleCheckSumVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数保交易")
    private Long insuranceTotal;

    @ApiModelProperty("app拉新")
    private Long appTotal;

    @ApiModelProperty("二手交易")
    private Long mobileTotal;


    public String getInsuranceTotal() {
        return  StringUtils.fenToYuan(this.insuranceTotal);
    }

    public String getAppTotal() {
        return  StringUtils.fenToYuan(this.appTotal);
    }

    public String getMobileTotal() {
        return  StringUtils.fenToYuan(this.mobileTotal);
    }

}