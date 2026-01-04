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
import java.util.Date;

/**
 * <p>
 * 换机晒单表
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_order")
@ApiModel(value = "MbExchangeOrder对象", description = "换机晒单表")
public class MbExchangeOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("店员ID")
    private Long storeEmployeeId;

    @ApiModelProperty("订单类型")
    private Integer type;

    @ApiModelProperty("来源")
    private Integer source;

    @ApiModelProperty("合伙人ID")
    private Long bdId;

    @ApiModelProperty("区域经理ID")
    private Long areaId;

    @ApiModelProperty("代理ID")
    private Long agentId;

    @ApiModelProperty("IMEi")
    private String imeiNo;

    @ApiModelProperty("oaid")
    private String oaid;

    @ApiModelProperty("手机系统")
    private String sysMobile;

    @ApiModelProperty("换机包编码")
    private String exchangePhoneNo;
    @ApiModelProperty("客户手机号")
    private String customPhone;
    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("结算状态")
    private Integer settleStatus;

    @ApiModelProperty("审核时间")
    private Date trialTime;

    private String remark;

    @ApiModelProperty("机审状态")
    private Integer sysStatus;

    @ApiModelProperty("机审备注")
    private String sysRemark;

    @ApiModelProperty("平台审核标识")
    private Boolean platCheck;

    @ApiModelProperty("异常标识")
    private Boolean illegal;

    @ApiModelProperty("审核通过备注")
    private String passRemark;

}
