package com.zxtx.hummer.exchange.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 二手机代理拓展信息表
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_employee_info")
@ApiModel(value = "MbExchangeEmployeeInfo对象", description = "二手机代理拓展信息表")
public class MbExchangeEmployeeInfo extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("代理ID")
    private Long employeeId;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("负责地区")
    private String address;

    @ApiModelProperty("已开通业务")
    private String business;

    @ApiModelProperty("平台审核")
    private Boolean platCheck;
}
