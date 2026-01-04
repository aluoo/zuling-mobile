package com.zxtx.hummer.insurance.vo;

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
public class DiInsuranceFixOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报险单ID")
    @ExcelField(name = "报险单ID")
    private Long id;

    @ApiModelProperty("保险单ID")
    @ExcelField(name = "保险单ID")
    private Long orderId;

    @ApiModelProperty("保险服务单号")
    @ExcelField(name = "保险服务单号")
    private String insuranceNo;

    @ApiModelProperty("报险次数")
    @ExcelField(name = "报险次数")
    private Integer fixCount;

    @ApiModelProperty("数保产品类型")
    @ExcelField(name = "数保产品类型")
    private String insuranceType;

    @ApiModelProperty("数保产品名称")
    @ExcelField(name = "数保产品名称")
    private String insuranceName;

    @ApiModelProperty("数保产品年限")
    @ExcelField(name = "数保产品年限")
    private Integer insurancePeriod;

    // 理赔项目 optionId
    @IgnoreExcelField
    private Long serviceType;
    @ApiModelProperty("理赔项目")
    @ExcelField(name = "理赔项目")
    private String serviceTypeStr;

    // 理赔项目 optionId
    @IgnoreExcelField
    private Long claimItem;
    @ApiModelProperty("理赔项目")
    @ExcelField(name = "理赔项目")
    private String claimItemStr;

    @ApiModelProperty("手机商品名称")
    @ExcelField(name = "手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    @ExcelField(name = "手机规格名称")
    private String productSpec;

    @ApiModelProperty("imeiNo")
    @ExcelField(name = "IMEI")
    private String imeiNo;

    @IgnoreExcelField
    private Integer oldSkuRetailPrice;
    @ApiModelProperty("市场价")
    @ExcelField(name = "市场价")
    private String oldSkuRetailPriceStr;

    @IgnoreExcelField
    private Integer settleAmount;
    @ApiModelProperty("理赔金额")
    @ExcelField(name = "理赔金额")
    private String settleAmountStr;

    @ApiModelProperty("客户姓名")
    @ExcelField(name = "客户姓名")
    private String customName;
    @ApiModelProperty("客户手机号")
    @ExcelField(name = "客户手机号")
    private String customPhone;

    @IgnoreExcelField
    private Long createBy;
    @ApiModelProperty("报险人姓名")
    @ExcelField(name = "报险人姓名")
    private String creatorName;
    @ApiModelProperty("报险人手机号")
    @ExcelField(name = "报险人手机号")
    private String creatorPhone;

    @ApiModelProperty("维修城市")
    @ExcelField(name = "维修城市")
    private String fixCity;

    @ApiModelProperty("状态")
    @IgnoreExcelField
    private Integer status;
    @ApiModelProperty("状态")
    @ExcelField(name = "状态")
    private String statusStr;

    @ApiModelProperty("创建日期")
    @ExcelField(name = "创建日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新日期")
    @ExcelField(name = "更新日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}