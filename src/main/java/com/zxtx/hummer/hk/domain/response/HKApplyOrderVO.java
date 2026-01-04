package com.zxtx.hummer.hk.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/4
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HKApplyOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @ExcelField(name = "订单编号")
    private Long id;

    @ApiModelProperty("门店")
    @ExcelField(name = "门店")
    private String companyName;
    @ApiModelProperty("下单人")
    @ExcelField(name = "下单人")
    private String employeeName;
    @ApiModelProperty("员工手机号")
    @ExcelField(name = "员工手机号")
    private String employeeMobile;
    @ApiModelProperty("员工ID")
    @IgnoreExcelField
    private Long employeeId;
    @ApiModelProperty("门店ID")
    @IgnoreExcelField
    private Long companyId;

    @ApiModelProperty("连锁店")
    @IgnoreExcelField
    private String chainStoreName;

    @ApiModelProperty("供应商")
    @ExcelField(name = "供应商")
    private String supplier;
    @ApiModelProperty("运营商")
    @ExcelField(name = "运营商")
    private String operator;
    @IgnoreExcelField
    private Long supplierId;
    @IgnoreExcelField
    private Long operatorId;

    @ApiModelProperty("套餐")
    @ExcelField(name = "套餐")
    private String fetchName;
    @ApiModelProperty("套餐编码")
    @ExcelField(name = "套餐编码")
    private String fetchCode;

    @ApiModelProperty("三方订单号")
    @IgnoreExcelField
    private String thirdOrderSn;

    @ApiModelProperty("申请人")
    @ExcelField(name = "申请人")
    private String name;
    @ApiModelProperty("预约手机号（申请号码）")
    @ExcelField(name = "申请号码")
    private String planMobileNumber;
    @ApiModelProperty("身份证")
    @ExcelField(name = "身份证")
    private String idCard;
    @ApiModelProperty("手机号")
    @IgnoreExcelField
    private String mobile;

    @ApiModelProperty("地区")
    @ExcelField(name = "地区")
    private String area;
    @ApiModelProperty("省份")
    @IgnoreExcelField
    private String provinceName;
    @ApiModelProperty("市")
    @IgnoreExcelField
    private String cityName;
    @ApiModelProperty("区")
    @IgnoreExcelField
    private String townName;
    @ApiModelProperty("详细地址")
    @IgnoreExcelField
    private String address;


    @ApiModelProperty("层级")
    @IgnoreExcelField
    private String ancestors;
    @IgnoreExcelField
    private Integer status;
    @ApiModelProperty("状态")
    @ExcelField(name = "最新状态")
    private String statusStr;

    @ApiModelProperty("物流单号")
    @ExcelField(name = "物流单号")
    private String expressBill;

    @ApiModelProperty("物流公司")
    @ExcelField(name = "物流公司")
    private String express;

    @ApiModelProperty(value = "创建时间")
    @ExcelField(name = "下单时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "激活时间")
    @ExcelField(name = "激活时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeTime;

    @ApiModelProperty("理由")
    @IgnoreExcelField
    private String reason;

    @ApiModelProperty(value = "操作日志")
    @IgnoreExcelField
    private List<OrderLogDTO> logs;
}