package com.zxtx.hummer.insurance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 数保产品售价价格区间设置
 * </p>
 *
 * @author L
 * @since 2024-06-04
 */
@Data
public class ProductInsurancePriceVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("门店初始售价(单位：分)")
    private Integer salePrice;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Integer priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Integer priceHigh;

}
