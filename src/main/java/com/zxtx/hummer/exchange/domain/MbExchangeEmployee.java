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
 * 合伙人换机包
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_employee")
@ApiModel(value = "MbExchangeEmployee对象", description = "合伙人换机包")
public class MbExchangeEmployee extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("换机包ID")
    private Long exchangePhoneId;

    @ApiModelProperty("换机包名称")
    private String exchangePhoneName;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("合伙人ID")
    private Long bdId;

    @ApiModelProperty("区域经理ID")
    private Long areaId;

    @ApiModelProperty("代理ID")
    private Long agentId;

    @ApiModelProperty("合伙人名称")
    private String bdName;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("代理名称")
    private String agentName;


}
