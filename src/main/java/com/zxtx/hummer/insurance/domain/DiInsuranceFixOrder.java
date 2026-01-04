package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 数保报修订单
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("di_insurance_fix_order")
@ApiModel(value = "DiInsuranceFixOrder对象", description = "数保报修订单")
public class DiInsuranceFixOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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

    @ApiModelProperty("员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("门店ID")
    private Long storeCompanyId;

    @ApiModelProperty("联系人电话")
    private String contactMobile;

    @ApiModelProperty("新机IMEI")
    private String newImei;
}