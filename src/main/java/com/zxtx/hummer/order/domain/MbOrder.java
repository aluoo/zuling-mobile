package com.zxtx.hummer.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 报价订单表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("mb_order")
@ApiModel(value = "MbOrder对象", description = "报价订单表")
public class MbOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("更新时间")
    private Long createBy;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("是否删除0否1是")
    private Boolean deleted;

    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("订单编码")
    private String orderNo;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("IMEI号")
    private String imeiNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("原始报价")
    private Long originalQuotePrice;

    @ApiModelProperty("成交价")
    private Long finalPrice;

    @ApiModelProperty("门店抽成金额")
    private Long commission;

    @ApiModelProperty("平台补贴价格")
    private Long platformSubsidyPrice;

    @ApiModelProperty("回收商员工ID")
    private Long recyclerEmployeeId;

    @ApiModelProperty("回收商ID")
    private Long recyclerCompanyId;

    @ApiModelProperty("确认报价详情ID")
    private Long quotePriceLogId;

    @ApiModelProperty("确认报价时间")
    private LocalDateTime finishQuoteTime;

    @ApiModelProperty("是否绑码")
    private Boolean bound;

    @ApiModelProperty("是否核验")
    private Boolean verified;

    @ApiModelProperty("是否可报价（超时将关闭报价功能）")
    private Boolean quotable;

    @ApiModelProperty("发货超时时间")
    private Date shippingOverdueTime;
}
