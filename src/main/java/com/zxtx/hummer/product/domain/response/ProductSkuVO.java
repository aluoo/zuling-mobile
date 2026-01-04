package com.zxtx.hummer.product.domain.response;

import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value = "商品响应对象")
public class ProductSkuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "SKU ID")
    private Long id;
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "规格名称")
    private String spec;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分类全称")
    private String categoryFullName;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "零售价")
    private Integer retailPrice;
    @ApiModelProperty(value = "零售价")
    private String retailPriceStr;

    public void setRetailPriceStr() {
        if (this.retailPrice != null) {
            this.retailPriceStr = StringUtils.convertMoney(this.retailPrice);
        }
    }
}