package com.zxtx.hummer.exchange.vo;

import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeEmployeeInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private String ancestors;

    @ApiModelProperty("代理ID")
    @IgnoreExcelField
    private Long employeeId;

    @ApiModelProperty("代理名称")
    @ExcelField(name = "代理名称")
    private String employeeName;

    @ApiModelProperty("代理手机号")
    @ExcelField(name = "代理手机号")
    private String employeePhone;


    @ApiModelProperty("合伙人ID")
    @IgnoreExcelField
    private Long bdId;

    @ApiModelProperty("合伙人名称")
    @ExcelField(name = "合伙人名称")
    private String bdName;

    @ApiModelProperty("合伙人手机号")
    @ExcelField(name = "合伙人手机号")
    private String bdPhone;

    @ApiModelProperty("负责地区")
    @ExcelField(name = "负责地区")
    private String address;

    @IgnoreExcelField
    @ApiModelProperty("可用余额")
    private Long ableBalance;

    @ApiModelProperty("可用余额")
    @ExcelField(name = "可用余额")
    private String ableBalanceStr;

    @ApiModelProperty("状态")
    @IgnoreExcelField
    private Integer status;

    @ApiModelProperty("状态")
    @ExcelField(name = "状态")
    private String statusStr;

    @ApiModelProperty("业务开展")
    @ExcelField(name = "业务开展")
    private String business;

    @ApiModelProperty("级别")
    @IgnoreExcelField
    private Integer level;

    @ApiModelProperty("级别")
    @ExcelField(name = "级别")
    private String levelStr;


}