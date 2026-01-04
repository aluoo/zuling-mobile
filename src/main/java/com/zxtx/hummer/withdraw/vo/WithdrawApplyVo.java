package com.zxtx.hummer.withdraw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 套餐信息Vo对象
 **/
@Data
@ApiModel(value = "提现对象", description = "提现对象")
public class WithdrawApplyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @IgnoreExcelField
    private Long id;

    @ApiModelProperty("提现单号")
    @ExcelField(name = "提现单号")
    private String applyNo;

    @ApiModelProperty("渠道公司")
    @ExcelField(name = "渠道公司")
    private String channelName;

    @ApiModelProperty("员工名称")
    @ExcelField(name = "员工名称")
    private String employeeName;

    @ApiModelProperty("员工id")
    @IgnoreExcelField
    private Long employeeId;

    @ApiModelProperty("提现金额(分)")
    @IgnoreExcelField
    private Long amount;


    @ApiModelProperty("提现金额(元)")
    @ExcelField(name = "提现金额")
    private String amountStr;

    @ApiModelProperty("代扣税额")
    @IgnoreExcelField
    private Long taxAmount;

    @ApiModelProperty("代扣税额")
    @ExcelField(name = "代扣税额")
    private String taxAmountStr;

    @ApiModelProperty("到手金额")
    @IgnoreExcelField
    private Long inAmount;

    @ApiModelProperty("到手金额")
    @ExcelField(name = "到手金额")
    private String inAmountStr;

    @ApiModelProperty("类型(1-银行卡、2-支付宝)")
    @ExcelField(name = "类型", replace = {"1_银行卡", "2_支付宝"})
    private Integer type;

    @ApiModelProperty("银行名称")
    @ExcelField(name = "银行名称")
    private String accountName;

    @ApiModelProperty("账号")
    @ExcelField(name = "账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    @ExcelField(name = "姓名")
    private String ownerName;

    @ApiModelProperty("身份证号")
    @ExcelField(name = "身份证号")
    private String idCard;


    @ApiModelProperty("申请状态")
    @ExcelField(name = "审核状态", replace = {"-1_失败", "0_待审核", "1_待打款", "2_打款成功", "3_打款中"})
    private Integer status;

    @ApiModelProperty("平台审核类型(0-后台、1-渠道子后台)")
    @ExcelField(name = "平台审核类型", replace = {"false_后台", "true_渠道子后台"})
    private Boolean toPlatform;

    /**
     * 备注
     */
    // @JsonIgnore
    @IgnoreExcelField
    private String remark;

    @ApiModelProperty("创建时间")
    @ExcelField(name = "申请时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @IgnoreExcelField
    private Date updateTime;


    @ApiModelProperty("收款人手机号码")
    @ExcelField(name = "收款人手机号码")
    private String mobileNumber;

    @ApiModelProperty("提现收入元")
    @IgnoreExcelField
    private String inAmountYuan;


}