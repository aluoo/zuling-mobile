package com.zxtx.hummer.hk.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkRelationCompanyEditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("号卡套餐ID")
    @NotNull(message = "号卡套餐ID不能为空")
    private Long productId;

    @ApiModelProperty("门店列表")
    private List<RelationCompanyDTO> companyList;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RelationCompanyDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("门店员工ID")
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
}