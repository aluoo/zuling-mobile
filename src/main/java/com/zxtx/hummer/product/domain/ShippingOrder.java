package com.zxtx.hummer.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/20
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mb_shipping_order")
@ApiModel(value = "发货订单表")
public class ShippingOrder extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "门店发货员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty(value = "回收商确认收货员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;

    /**
     * @see com.zxtx.hummer.product.domain.enums.ShippingOrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "物流下单时间")
    private Date applyLogisticsTime;
    @ApiModelProperty(value = "确认收货时间")
    private Date confirmReceiptTime;

    @ApiModelProperty("寄件类型 1线上 2线下")
    private Integer shippingType;

    @ApiModelProperty("快递公司编码")
    private String trackCompanyCode;
    @ApiModelProperty("快递公司名称")
    private String trackCompanyName;

    @ApiModelProperty("快递物流单号")
    private String trackNo;

    @ApiModelProperty("期望揽收开始时间")
    private Date pickupStartTime;
    @ApiModelProperty("期望揽收结束时间")
    private Date pickupEndTime;

    @ApiModelProperty("运费")
    private Integer price;
}