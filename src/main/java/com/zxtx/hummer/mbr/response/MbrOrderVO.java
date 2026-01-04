package com.zxtx.hummer.mbr.response;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 进件单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private Long id;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("机器类型1新机2二手机")
    private String productType;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("期数")
    private Integer period;

    @ApiModelProperty("门店员工ID")
    @JsonIgnore
    private Long storeEmployeeId;
    @ApiModelProperty("门店ID")
    @JsonIgnore
    private Long storeCompanyId;
    @ApiModelProperty("第三方订单号")
    private Long thirdOrderId;

    @ApiModelProperty("员工门店")
    private String storeCompanyName;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工手机号")
    private String mobileNumber;

    @ApiModelProperty("租机单状态")
    private Integer status;

    @ApiModelProperty("进件时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonIgnore
    private Long settleAmount;
    @ApiModelProperty("商品成本")
    private String settleAmountStr;

    @JsonIgnore
    private Long planAmount;
    @ApiModelProperty("方案价格")
    private String planAmountStr;

    @JsonIgnore
    private Long depositAmount;
    @ApiModelProperty("押金价格")
    private String depositAmountStr;

    public String getOrderStatus(){
        return EnumUtil.getBy(MbrOrderStatusEnum::getCode,getStatus()).getDesc();
    }


    public void setSettleAmountStr() {
        if (this.settleAmount != null) {
            this.settleAmountStr = StringUtils.fenToYuan(this.settleAmount);
        }
    }

    public void setPlanAmountStr() {
        if (this.planAmount != null) {
            this.planAmountStr = StringUtils.fenToYuan(this.planAmount);
        }
    }

    public void setDepositAmountStr() {
        if (this.depositAmount != null) {
            this.depositAmountStr = StringUtils.fenToYuan(this.depositAmount);
        }
    }

    public void setPrice() {
        this.setSettleAmountStr();
        this.setPlanAmountStr();
        this.setDepositAmountStr();
    }
}