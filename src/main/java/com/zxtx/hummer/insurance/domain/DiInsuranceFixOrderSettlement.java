package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.insurance.enums.FixOrderSettlementStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/16
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("di_insurance_fix_order_settlement")
@ApiModel(value = "数保理赔打款结算表")
public class DiInsuranceFixOrderSettlement extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("结算单号")
    private String applyNo;

    @ApiModelProperty("报险单号")
    private Long fixOrderId;

    @ApiModelProperty("合伙人ID")
    private Long bdId;

    @ApiModelProperty("区域经理ID")
    private Long areaId;

    @ApiModelProperty("提现金额(分)")
    private Long amount;

    @ApiModelProperty("代扣税额")
    private Long taxAmount;

    @ApiModelProperty("到手金额")
    private Long inAmount;

    @ApiModelProperty("类型(1-银行卡、2-支付宝、3-对公账户)")
    private Integer type;

    @ApiModelProperty("收款人")
    private String ownerName;

    @ApiModelProperty("银行名称")
    private String accountName;

    @ApiModelProperty("收款账号")
    private String accountNo;

    @ApiModelProperty("收款人身份证")
    private String idCard;

    /**
     * @see FixOrderSettlementStatusEnum
     */
    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("打款时间")
    private Date payTime;

    private String ancestors;
}