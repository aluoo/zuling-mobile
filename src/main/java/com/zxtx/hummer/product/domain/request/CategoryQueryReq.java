package com.zxtx.hummer.product.domain.request;

import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import io.swagger.annotations.ApiModel;
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
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类查询请求对象")
public class CategoryQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上级ID")
    @NotNull(message = "上级ID不能为空", groups = ProductValidatorGroup.ByParent.class)
    private Long parentId;
    @ApiModelProperty("层级")
    @NotNull(message = "上级ID不能为空", groups = ProductValidatorGroup.ByLevel.class)
    private Integer level;
}