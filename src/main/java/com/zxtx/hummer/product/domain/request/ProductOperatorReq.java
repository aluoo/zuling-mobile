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
import java.util.List;

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
@ApiModel(value = "商品操作对象")
public class ProductOperatorReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空", groups = {ProductValidatorGroup.Edit.class, ProductValidatorGroup.Switch.class, ProductValidatorGroup.Delete.class})
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;
    @ApiModelProperty(value = "分类ID")
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "是否上架")
    @NotNull(message = "状态不能为空", groups = {ProductValidatorGroup.Switch.class})
    private Boolean activated;
    @ApiModelProperty("选中的选项ID列表")
    private List<Long> optionIds;
}