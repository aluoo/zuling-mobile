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
 * 回收商报价记录表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("mb_order_quote_price_log")
@ApiModel(value = "MbOrderQuotePriceLog对象", description = "回收商报价记录表")
public class MbOrderQuotePriceLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("更新时间")
    private Long createBy;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("是否删除0否1是")
    private Boolean deleted;

    @ApiModelProperty("报价订单ID")
    private Long orderId;

    @ApiModelProperty("回收商员工ID")
    private Long employeeId;

    @ApiModelProperty("回收商ID")
    private Long companyId;

    @ApiModelProperty("原始报价")
    private Long originalQuotePrice;

    @ApiModelProperty("成交价")
    private Long finalPrice;

    @ApiModelProperty("实际付款价格=原始报价+平台抽成金额")
    private Long actualPaymentPrice;

    @ApiModelProperty("平台抽成规则")
    private Long platformCommissionRule;

    @ApiModelProperty("平台抽成金额")
    private Long platformCommission;

    @ApiModelProperty("门店抽成规则")
    private Long commissionRule;

    @ApiModelProperty("门店抽成金额")
    private Long commission;

    @ApiModelProperty("是否已报价，默认为否")
    private Boolean quoted;

    @ApiModelProperty("报价时间")
    private Date quoteTime;

    @ApiModelProperty("报价用时=报价订单创建时间-报价时间 单位毫秒")
    private Long quoteTimeSpent;
}
