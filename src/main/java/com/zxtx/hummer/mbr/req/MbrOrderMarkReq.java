package com.zxtx.hummer.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/5/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrOrderMarkReq implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("订单号")
    @NotNull(message = "订单号不能为空")
    private Long id;

    @ApiModelProperty("备注")
    private String remark;
}