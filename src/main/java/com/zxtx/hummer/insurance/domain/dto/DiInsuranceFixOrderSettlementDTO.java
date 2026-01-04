package com.zxtx.hummer.insurance.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModel;
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
 * @Date 2024/10/17
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保理赔打款结算")
public class DiInsuranceFixOrderSettlementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private Long id;

    @ApiModelProperty("结算单号")
    @ExcelField(name = "结算单号")
    private String applyNo;
    @ApiModelProperty("报险单号")
    @ExcelField(name = "报险单号")
    private Long fixOrderId;

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

    @ApiModelProperty("提现金额")
    @IgnoreExcelField
    private Long amount;
    @ExcelField(name = "提现金额")
    private String amountStr;

    @ApiModelProperty("代扣税额")
    @IgnoreExcelField
    private Long taxAmount;
    @ExcelField(name = "代扣税额")
    private String taxAmountStr;

    @ApiModelProperty("到手金额")
    @IgnoreExcelField
    private Long inAmount;
    @ExcelField(name = "到手金额")
    private String inAmountStr;

    @IgnoreExcelField
    private Integer type;
    @ApiModelProperty("账户类型(1-银行卡、2-支付宝、3-对公账户)")
    @ExcelField(name = "账户类型")
    private String typeStr;

    @ApiModelProperty(value = "收款人")
    @ExcelField(name = "收款人")
    private String ownerName;

    @ApiModelProperty(value = "收款账号")
    @ExcelField(name = "收款账号")
    private String accountNo;

    @ApiModelProperty(value = "收款人身份证")
    @ExcelField(name = "收款人身份证")
    private String idCard;

    @IgnoreExcelField
    private Long storeCompanyId;
    @ApiModelProperty(value = "门店")
    @ExcelField(name = "门店")
    private String storeCompanyName;
    @IgnoreExcelField
    private Long bdId;
    @ApiModelProperty(value = "合伙人")
    @ExcelField(name = "合伙人")
    private String bdName;
    @IgnoreExcelField
    private Long areaId;
    @ApiModelProperty(value = "区域经理")
    @ExcelField(name = "区域经理")
    private String areaName;

    @IgnoreExcelField
    private Long fixOrderCreateBy;
    @ApiModelProperty("报险人姓名(提交人)")
    @ExcelField(name = "报险人姓名")
    private String fixOrderCreatorName;
    @ApiModelProperty("报险人手机号(提交人)")
    @ExcelField(name = "报险人手机号")
    private String fixOrderCreatorPhone;

    @ApiModelProperty("提交时间(理赔金额确认时间)")
    @ExcelField(name = "提交时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("打款时间")
    @ExcelField(name = "打款时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @IgnoreExcelField
    private Integer status;
    @ApiModelProperty("状态 -1打款失败 0待打款 2打款成功")
    @ExcelField(name = "状态")
    private String statusStr;

    @ApiModelProperty("备注")
    @ExcelField(name = "备注")
    private String remark;

    @IgnoreExcelField
    @ApiModelProperty(value = "操作日志")
    private List<OrderLogDTO> logs;

    public String getInAmount() {
        return StringUtils.fenToYuan(inAmount);
    }

    public String getAmount() {
        return StringUtils.fenToYuan(amount);
    }

    public String getTaxAmount() {
        return StringUtils.fenToYuan(taxAmount);
    }

    public void setAmountStr() {
        this.amountStr = StringUtils.fenToYuan(this.amount);
        this.inAmountStr = StringUtils.fenToYuan(this.inAmount);
        this.taxAmountStr = StringUtils.fenToYuan(this.taxAmount);
    }
}