package com.zxtx.hummer.commission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.commission.enums.CommissionSettleGainType;
import com.zxtx.hummer.commission.enums.SettleStatus;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 系统结算单
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionSettleDTO {

    @ApiModelProperty("拓展关联ID")
    @ExcelField(name = "订单编号")
    private Long correlationId;

    @IgnoreExcelField
    private Long commissionType;
    @ApiModelProperty("业务类型")
    @ExcelField(name = "业务类型")
    private String commissionTypeStr;

    @ApiModelProperty("产品名称")
    @ExcelField(name = "产品名称")
    private String productName;

    @ApiModelProperty("门店")
    @ExcelField(name = "门店")
    private String correlationOrderCompanyName;
    @ApiModelProperty("连锁")
    @ExcelField(name = "连锁")
    private String correlationOrderParentCompanyName;

    @ApiModelProperty("所属区域经理")
    @ExcelField(name = "所属区域经理")
    private String areaName;

    @ApiModelProperty("成员姓名")
    @ExcelField(name = "成员姓名")
    private String employeeName;
    @ApiModelProperty("手机号")
    @ExcelField(name = "手机号")
    private String mobileNumber;
    @ApiModelProperty("1-直接做单获得,2-下级贡献")
    @IgnoreExcelField
    private Integer gainType;
    @ExcelField(name = "获得途径", replace = {"1_直接做单获得", "2_下级贡献"})
    private String gainTypeStr;
    @ApiModelProperty("角色类型")
    @ExcelField(name = "角色类型")
    private String employeeRole;
    @ApiModelProperty("结算金额")
    @ExcelField(name = "结算金额(元)")
    private String settleBalanceStr;
    @ExcelField(name = "结算状态", replace = {"0_不结算", "1_待结算", "2_已结算"})
    private String settleStatusStr;
    @ApiModelProperty("结算时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelField(name = "结算时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gainTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelField(name = "创建时间", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @IgnoreExcelField
    private Long id;
    @IgnoreExcelField
    private Long employeeId;
    @IgnoreExcelField
    private Integer companyType;
    @IgnoreExcelField
    private Integer deptType;
    @IgnoreExcelField
    private Integer employeeType;
    @IgnoreExcelField
    private Integer employeeLevel;
    @IgnoreExcelField
    private Long settleBalance;
    @IgnoreExcelField
    private Integer settleStatus;

    @ApiModelProperty("备注")
    @IgnoreExcelField
    private String remark;

    @IgnoreExcelField
    private Long correlationOrderEmployeeId;
    @IgnoreExcelField
    private Long correlationOrderCompanyId;


    public String getSettleBalanceStr() {
        return StringUtils.fenToYuan(settleBalance);
    }

    public String getSettleStatusStr() {
        return SettleStatus.getDescByCode(settleStatus);
    }

    public String getGainTypeStr() {
        return CommissionSettleGainType.getDescByCode(settleStatus);
    }

}