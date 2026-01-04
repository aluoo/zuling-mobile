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
 * @Date 2024/1/26
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类操作请求对象")
public class CategoryOperatorReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @NotNull(message = "分类ID不能为空", groups = {ProductValidatorGroup.Edit.class, ProductValidatorGroup.Switch.class, ProductValidatorGroup.Delete.class})
    private Long id;
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "上级分类ID")
    private Long parentId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
}