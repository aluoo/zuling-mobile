package com.zxtx.hummer.insurance.domain.dto;

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
 * @Date 2024/9/19
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiFixOrderInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private Boolean deleted;

    @ApiModelProperty("保险单ID")
    private Long orderId;

    @ApiModelProperty("报险用户手机")
    private String mobile;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("关联DiOptionId")
    private Long serviceType;

    @ApiModelProperty("关联DiOptionId")
    private Long claimItem;

    @ApiModelProperty("是否读取串号")
    private Boolean imeiRead;

    @ApiModelProperty("手机故障")
    private String breakDown;

    @ApiModelProperty("迭代升级1同款同型2升级迭代")
    private Integer upProduct;

    @ApiModelProperty("理赔金额")
    private Integer settleAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("维修收款账号姓名")
    private String fixName;

    @ApiModelProperty("维修收款支付宝账号")
    private String fixAlipay;

    @ApiModelProperty("维修银行卡号")
    private String fixBankAccount;

    @ApiModelProperty("迭代升级商品SKUID")
    private Long productSkuId;

    @ApiModelProperty("迭代升级手机商品名称")
    private String productName;

    @ApiModelProperty("迭代升级手机规格名称")
    private String productSpec;

    @ApiModelProperty("迭代升级手机市场零售价")
    private Integer productSkuRetailPrice;

    @ApiModelProperty("新机档口价格")
    private Integer newProductSkuStallPrice;

    @ApiModelProperty("旧机手机市场零售价")
    private Integer oldSkuRetailPrice;

    @ApiModelProperty("补交金额")
    private Integer suppleAmount;

    @ApiModelProperty("折抵价格")
    private Integer discountAmount;

    @ApiModelProperty("订单编码")
    private String orderNo;

    @ApiModelProperty("维修城市")
    private String fixCity;

    @ApiModelProperty("门店员工ID")
    private Long orderStoreEmployeeId;
    @ApiModelProperty("门店ID")
    private Long orderStoreCompanyId;
    @ApiModelProperty("数保产品ID")
    private Long orderInsuranceId;
    @ApiModelProperty("商品SKU ID")
    private Long orderProductSkuId;
    @ApiModelProperty("手机商品名称")
    private String orderProductName;
    @ApiModelProperty("手机规格名称")
    private String orderProductSpec;
    @ApiModelProperty("手机发票购机价")
    private Integer orderProductSkuPurchaseInvoicePrice;
    @ApiModelProperty("手机发票购机时间")
    private Date orderProductSkuPurchaseInvoiceTime;
    @ApiModelProperty("数保产品年限")
    private Integer orderInsurancePeriod;
    @ApiModelProperty("数保产品名称")
    private String orderInsuranceName;
    @ApiModelProperty("数保产品类型")
    private String orderInsuranceType;
    @ApiModelProperty("手机串号")
    private String orderImeiNo;
    @ApiModelProperty("保险服务单号")
    private String orderInsuranceNo;
    @ApiModelProperty("顾客姓名")
    private String orderCustomName;
    @ApiModelProperty("手机号")
    private String orderCustomPhone;
}