package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/13
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceOrderAuditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @NotNull(message = "订单号不能为空")
    private Long id;

    @ApiModelProperty("手机发票购机价")
    private BigDecimal productSkuPurchaseInvoicePrice;

    @ApiModelProperty("手机发票购机时间")
    private String productSkuPurchaseInvoiceTime;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("审核状态 true通过，false不通过")
    private Boolean pass;
}