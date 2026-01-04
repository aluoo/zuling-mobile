package com.zxtx.hummer.hk.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HkRelationCompanyVO implements Serializable {
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

    @ApiModelProperty("号卡套餐ID")
    private Long productId;
}