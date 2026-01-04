package com.zxtx.hummer.commission.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author shenbh
 * @since 2023/3/29
 */
@Data
public class PlanListDTO implements Serializable {

    @ApiModelProperty("佣金方案类型ID")
    @NotNull(message = "佣金方案类型ID不能为空")
    private Long bizTypeId;

    @ApiModelProperty("员工ID")
    @NotNull(message = "员工ID不能为空")
    private Long employeeId;
}
