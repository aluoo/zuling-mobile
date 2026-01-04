package com.zxtx.hummer.product.domain.request;

import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
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
@ApiModel(value = "商品SKU操作对象")
public class ProductSkuOperatorReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "SKU ID")
    @NotNull(message = "SKU ID不能为空", groups = {ProductValidatorGroup.Edit.class, ProductValidatorGroup.Switch.class, ProductValidatorGroup.Delete.class})
    private Long id;
    @ApiModelProperty(value = "价格")
    @NotBlank(message = "价格不能为空", groups = {ProductValidatorGroup.Edit.class, ProductValidatorGroup.Switch.class, ProductValidatorGroup.Delete.class})
    private String retailPrice;
}