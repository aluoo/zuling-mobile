package com.zxtx.hummer.commission.req;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PlanViewReq {

    @ApiModelProperty("佣金方案ID")
    private Long planId;

    @ApiModelProperty("后端自己传")
    private Long employeeId;


}
