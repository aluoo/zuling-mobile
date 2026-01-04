package com.zxtx.hummer.product.domain.request;

import io.swagger.annotations.ApiModel;
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
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "商品SKU添加请求对象")
public class ProductSkuAddReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long id;
    @ApiModelProperty(value = "规格")
    @NotBlank(message = "规格不能为空")
    private String spec;
    @ApiModelProperty(value = "零售价")
    @NotBlank(message = "零售价不能为空")
    private String retailPrice;
}