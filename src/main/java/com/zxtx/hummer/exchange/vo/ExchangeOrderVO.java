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
public class ExchangeOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelField(name = "订单号")
    private Long id;

    @ApiModelProperty("连锁")
    @ExcelField(name = "连锁")
    private String chainStoreName;

    @ApiModelProperty("门店")
    @ExcelField(name = "门店")
    private String storeName;

    @ApiModelProperty("店员姓名")
    @ExcelField(name = "店员姓名")
    private String storeEmployee;

    @ApiModelProperty("手机号码")
    @ExcelField(name = "手机号码")
    private String storeMobile;

    @ApiModelProperty("晒单类型4一键安装3换机5快手绿洲")
    @ExcelField(name = "晒单类型",replace = {"4_一键安装","3_换机","5_快手绿洲","6_苹果抖音"})
    private Integer type;

    @ApiModelProperty("来源1APP门店端")
    @ExcelField(name = "来源",replace = {"0_外部吉迅","1_正常晒单","2_晒单跳过"})
    private Integer source;

    @ApiModelProperty("所属代理")
    @IgnoreExcelField
    private String agentName;

    @ApiModelProperty("所属区域经理")
    @ExcelField(name = "所属区域经理")
    private String areaName;

    @ApiModelProperty("合伙人")
    @ExcelField(name = "合伙人")
    private String bdName;

    @ApiModelProperty("手机系统")
    @IgnoreExcelField
    private String sysMobile;

    @ApiModelProperty("IMEI码")
    @ExcelField(name = "IMEI码")
    private String imeiNo;

    @ApiModelProperty("安卓广告标识")
    @IgnoreExcelField
    private String oaid;

    @ApiModelProperty("包编码")
    @ExcelField(name = "包编码")
    private String exchangePhoneNo;

    @ApiModelProperty("提交时间")
    @ExcelField(name = "提交时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("审核时间")
    @ExcelField(name = "审核时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trialTime;

    @IgnoreExcelField
    private Long updateBy;
    @ExcelField(name = "审核人姓名")
    private String updaterName;

    @ApiModelProperty("审核状态")
    @ExcelField(name = "审核状态", replace = {"-1_失败", "0_待审核", "1_系统通过", "2_人工通过","-2_系统不通过"})
    private Integer status;

    @ApiModelProperty("系统审核状态")
    @ExcelField(name = "系统审核状态", replace = {"-1_失败", "0_待审核", "1_系统通过", "2_人工通过","-2_系统不通过"})
    private Integer sysStatus;

    @ApiModelProperty("系统审核备注")
    @ExcelField(name = "系统审核备注")
    private String sysRemark;

    @ApiModelProperty("审单类型")
    @ExcelField(name = "审单类型", replace = {"1_平台审核", "0_自审"})
    private Integer platCheck;

    @ApiModelProperty("异常标识")
    @ExcelField(name = "异常标识", replace = {"1_异常", "0_正常"})
    private Integer illegal;

    @ApiModelProperty("结算状态")
    @ExcelField(name = "结算状态", replace = {"0_未结算", "1_已结算", "-1已追回"})
    private Integer settleStatus;

    @ApiModelProperty("备注")
    @ExcelField(name = "备注")
    private String remark;

    @ApiModelProperty("审核通过备注")
    @ExcelField(name = "审核通过备注")
    private String passRemark;

    @ApiModelProperty("顾客手机号")
    @IgnoreExcelField
    private String customPhone;


    @IgnoreExcelField
    private Long storeCompanyId;
    @IgnoreExcelField
    private Integer storeCompanyType;
    @IgnoreExcelField
    private Long storeCompanyPId;

    @IgnoreExcelField
    private Boolean commissionBackBtn = false;
}