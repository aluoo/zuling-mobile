package com.zxtx.hummer.commission.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCommissionPlanInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("方案ID")
    private String planId;
    @ApiModelProperty("方案名称")
    private String planName;
    @ApiModelProperty("方案类型")
    private String bizTypeStr;
    @ApiModelProperty("套餐名称")
    private String packageInfoName;
    @ApiModelProperty("上级分成数值")
    private String superDivide;
    @ApiModelProperty("下级分成数值")
    private String childDivide;
    @ApiModelProperty("自收分成数值")
    private String selfDivide;
    @ApiModelProperty("上级分成比例")
    private String superScale;
    @ApiModelProperty("下级分成比例")
    private String childScale;
    @ApiModelProperty("自收分成比例")
    private String selfScale;

    @JsonIgnore
    private Long superDivideLong;
    private Long childDivideLong;
    private Long selfDivideLong;
}