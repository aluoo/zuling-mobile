package com.zxtx.hummer.packageInfo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 套餐信息
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
@Getter
@Setter
@TableName("package_info_default")
@ApiModel(value = "PackageInfoDefault对象", description = "套餐信息")
public class PackageInfoDefault extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("佣金方案类型ID")
    private Long bizTypeId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("套餐编码")
    private String code;

    @ApiModelProperty("分佣比例")
    private BigDecimal platCommissionScale;

    @ApiModelProperty("最大分佣金额(单位：分)")
    private Long platCommissionFee;

    @ApiModelProperty("平台补贴金额")
    private Long platformSubsidyPrice;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Long priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Long priceHigh;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

    @ApiModelProperty("排序号")
    private Integer orderNo;
}
