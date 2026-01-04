package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
public class DiInsuranceOrderEditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @NotNull(message = "订单号不能为空")
    private Long id;

    @ApiModelProperty("客户姓名")
    @NotBlank(message = "姓名不能为空")
    private String customName;

    @ApiModelProperty("客户手机号")
    @NotBlank(message = "手机号不能为空")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    @NotBlank(message = "身份证不能为空")
    private String idCard;
}