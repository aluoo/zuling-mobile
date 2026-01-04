package com.zxtx.hummer.hk.domain.request;

import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkProductCommissionStatusReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * @see HkApplyOrderStatusEnum
     */
    @ApiModelProperty(value = "分佣条件（订单状态）")
    @NotNull(message = "分佣条件不能为空")
    private Integer commissionStatus;


    @ApiModelProperty(value = "分佣套餐ID")
    @NotNull(message = "分佣套餐ID")
    private Long commissionTypePackageId;
}