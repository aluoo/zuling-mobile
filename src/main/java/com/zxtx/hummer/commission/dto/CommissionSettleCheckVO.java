package com.zxtx.hummer.commission.dto;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionSettleCheckVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @IgnoreExcelField
    @ApiModelProperty("id")
    private Long id;

    @IgnoreExcelField
    private Date createTimeOne;
    @IgnoreExcelField
    private Date createTimeTwo;
    @IgnoreExcelField
    private Date createTimeThree;

    @ApiModelProperty("业务类型")
    @ExcelField(name = "业务类型",replace = {"1_平台服务费", "2_手机回收", "3_换机业务", "4_一键更新", "5_享转数保", "6_享转数保", "7_快手绿洲", "8_苹果抖音"})
    private Integer commissionType;

    @ApiModelProperty("结算时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelField(name = "结算时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settleTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelField(name = "创建时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("合伙人金额")
    @IgnoreExcelField
    private Long settleBalance;

    @ApiModelProperty("合伙人金额")
    @ExcelField(name = "合伙人金额")
    private String settleBalanceStr;

    @ApiModelProperty("支出金额")
    @IgnoreExcelField
    private Long payAmount;

    @ApiModelProperty("支出金额")
    @ExcelField(name = "支出金额")
    private String payAmountStr;

    @ApiModelProperty("总金额")
    @IgnoreExcelField
    private Long allAmount;

    @ApiModelProperty("总金额")
    @ExcelField(name = "总金额")
    private String allAmountStr;

    @ApiModelProperty("区域经理")
    @ExcelField(name = "区域经理")
    private String regionName;

    @ApiModelProperty("合伙人")
    @ExcelField(name = "合伙人")
    private String bdName;

    @ApiModelProperty("合伙人手机")
    @ExcelField(name = "合伙人手机")
    private String mobileNumber;

    @ApiModelProperty("订单号")
    @ExcelField(name = "订单号")
    private String correlationId;


    public String getCommissionType(){
        return EnumUtil.getBy(CommissionPackage::getType,Long.valueOf(commissionType)).getTypeName();
    }


}