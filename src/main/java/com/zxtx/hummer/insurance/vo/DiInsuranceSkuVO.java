package com.zxtx.hummer.insurance.vo;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.insurance.domain.DiProductInsurancePrice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
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
@ApiModel(value = "商品响应对象")
public class DiInsuranceSkuVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "skuId")
    private Long skuId;
    @ApiModelProperty("数保产品ID")
    private Long insuranceId;
    @ApiModelProperty("数保产品名称")
    private String insuranceName;
    @ApiModelProperty("数保产品年限")
    private Integer insurancePeriod;
    @ApiModelProperty("底价")
    private Long downPrice;
}