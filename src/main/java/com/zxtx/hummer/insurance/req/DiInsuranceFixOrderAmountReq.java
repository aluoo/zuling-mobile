package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceFixOrderAmountReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty("金额")
    @NotNull(message = "金额不能为空")
    @Min(value = 0, message = "金额不能小于0")
    private BigDecimal amount;
}