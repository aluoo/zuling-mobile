package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 数保支付订单表
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("di_insurance_order_payment")
@ApiModel(value = "DiInsuranceOrderPayment对象", description = "数保支付订单表")
public class DiInsuranceOrderPayment extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("数保订单ID")
    private Long insuranceOrderId;

    @ApiModelProperty("用户openId")
    private String openId;

    @ApiModelProperty("平台交易订单号")
    private String outTradeNo;

    @ApiModelProperty("微信支付订单号")
    private String transactionId;

    @ApiModelProperty("平台退款订单号")
    private String refundNo;

    @ApiModelProperty("微信退款订单号")
    private String refundId;

    @ApiModelProperty("金额")
    private Long amount;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("支付方式")
    private Integer type;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("跳转连接二维码图片地址")
    private String qrCodeUrl;

    @ApiModelProperty("退款时间")
    private Date refundTime;

    @ApiModelProperty("退款原因")
    private String refundReason;

    @ApiModelProperty("退款备注")
    private String refundRemark;
}