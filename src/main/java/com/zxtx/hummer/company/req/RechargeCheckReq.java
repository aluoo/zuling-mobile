package com.zxtx.hummer.company.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RechargeCheckReq {
    /**
     * 订单id
     */
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "id")
    private Long id;
    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "1拒绝2通过")
    private Integer status;
    @ApiModelProperty(value = "拒绝时候备注")
    private String remark;
}
