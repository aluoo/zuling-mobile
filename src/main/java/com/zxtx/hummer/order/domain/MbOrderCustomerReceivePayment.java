package com.zxtx.hummer.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 报价订单-用户收款信息表（平台打钱给用户）
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("mb_order_customer_receive_payment")
@ApiModel(value = "MbOrderCustomerReceivePayment对象", description = "报价订单-用户收款信息表（平台打钱给用户）")
public class MbOrderCustomerReceivePayment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("更新时间")
    private Long createBy;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("是否删除0否1是")
    private Boolean deleted;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("支付宝商户订单号")
    private String outBizNo;

    @ApiModelProperty("收款人姓名")
    private String name;

    @ApiModelProperty("收款人手机号")
    private String mobile;

    @ApiModelProperty("收款人身份证号")
    private String idCard;

    @ApiModelProperty("收款金额")
    private Long amount;

    @ApiModelProperty("收款状态")
    private Boolean status;

    @ApiModelProperty("收款方式")
    private Boolean type;

    @ApiModelProperty("收款时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("支付宝open_id")
    private String openId;

    @ApiModelProperty("支付宝转账订单号")
    private String alipayOrderId;

    @ApiModelProperty("支付宝支付资金流水号")
    private String payFundOrderId;

    @ApiModelProperty("跳转连接二维码图片地址")
    private String qrCodeUrl;

    @ApiModelProperty("接口返回日志")
    private String remoteResp;
}
