package com.zxtx.hummer.exchange.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeCompanyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店员工ID")
    private Long employeeId;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("门店名称")
    private String companyName;

    @ApiModelProperty("负责人")
    private String contact;

    @ApiModelProperty("负责人手机号码")
    private String contactMobile;

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

    @ApiModelProperty("包TRUE存在")
    private Boolean verifyFlag;
    @ApiModelProperty("包名称")
    private String verifyInstallName;
    @ApiModelProperty("包名称")
    private Long verifyInstallId;

}