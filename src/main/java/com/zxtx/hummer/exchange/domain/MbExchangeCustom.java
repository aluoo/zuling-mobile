package com.zxtx.hummer.exchange.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 客户安装换机包
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_custom")
@ApiModel(value = "MbExchangeCustom对象", description = "客户安装换机包")
public class MbExchangeCustom extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("员工公司")
    private Long storeCompanyId;
    @ApiModelProperty("换机助手员工手机号")
    private String employeePhone;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("换机包Id")
    private Long exchangePhoneId;

    @ApiModelProperty("安装包id")
    private Long installId;

    @ApiModelProperty("安装包名称")
    private String installName;

    @ApiModelProperty("安装包渠道编码")
    private String installChannelNo;

    @ApiModelProperty("打开时长秒")
    private Integer openTime;

    @ApiModelProperty("单号")
    private String orderSn;

    @ApiModelProperty("单号")
    private String oaid;

    @ApiModelProperty("打开应用市场标识")
    private Boolean marketFlag;
}
