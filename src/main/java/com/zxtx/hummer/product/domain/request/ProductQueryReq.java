package com.zxtx.hummer.product.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ApiModel(value = "商品查询对象")
public class ProductQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "是否上架")
    private Boolean activated;
    @ApiModelProperty(value = "是否售卖数保")
    private Boolean digitalInsuranceAble;
    @ApiModelProperty(value = "是否参与租机")
    private Boolean mobileRentalAble;
}