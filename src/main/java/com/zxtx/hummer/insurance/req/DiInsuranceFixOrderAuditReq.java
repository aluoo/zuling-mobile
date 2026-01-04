package com.zxtx.hummer.insurance.req;

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
 * @Date 2024/10/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceFixOrderAuditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty(value = "1拒绝2通过")
    private Integer status;

    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("备注")
    private String remark;
}