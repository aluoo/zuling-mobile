package com.zxtx.hummer.insurance.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.insurance.domain.DiOption;
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
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiInsuranceFixOrderDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报险单ID")
    private Long id;

    @ApiModelProperty("投保单ID")
    private Long orderId;

    @ApiModelProperty("订单唯一码")
    private String orderNo;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("手机串号")
    private String imeiNo;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("报险次数")
    @ExcelField(name = "报险次数")
    private Integer fixCount;

    @ApiModelProperty("数保产品名称")
    private String insuranceName;

    @ApiModelProperty("数保产品类型")
    private String insuranceType;

    @ApiModelProperty("产品年限 0表示终身")
    private Integer insurancePeriod;

    @ApiModelProperty("0安卓1Ios")
    private Integer mobileType;

    @ApiModelProperty("服务类型")
    private DiOption serviceType;

    @ApiModelProperty("理赔项目")
    private List<DiOption> fixItemList;

    @ApiModelProperty("报险材料示例图集合")
    private List<InsuranceDemoImageVO> fixDataList;

    @ApiModelProperty("补充材料示例图集合")
    private List<InsuranceDemoImageVO> suppleDataList;

    @ApiModelProperty("理赔材料示例图集合")
    private List<InsuranceDemoImageVO> settleDataList;

    @ApiModelProperty("折抵金额")
    private Integer discountAmount;

    @ApiModelProperty("折抵金额描述")
    private String discountAmountDesc;

    @ApiModelProperty("旧机市场零售价")
    private Integer oldSkuRetailPrice;

    @ApiModelProperty("旧机发票购机价")
    private Integer oldSkuPurchaseInvoicePrice;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态中文")
    private String statusStr;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("失效时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    @ApiModelProperty("迭代手机商品名称")
    private String newProductName;
    @ApiModelProperty("迭代手机规格名称")
    private String newProductSpec;

    @ApiModelProperty("迭代手机SKUID")
    private Long productSkuId;

    @ApiModelProperty("迭代升级手机市场零售价")
    private Integer newProductSkuRetailPrice;

    @ApiModelProperty("新机档口价格")
    private Integer newProductSkuStallPrice;

    @ApiModelProperty("补交金额")
    private Integer suppleAmount;

    @ApiModelProperty("理赔金额")
    private Integer settleAmount;

    @ApiModelProperty(value = "迭代升级")
    private Integer upProduct;
    @ApiModelProperty(value = "迭代升级中文")
    private String upProductName;
    @ApiModelProperty(value = "可调出串号")
    private Boolean imeiRead;
    @ApiModelProperty(value = "可调出串号中文")
    private String imeiReadName;

    @ApiModelProperty(value = "服务类型中文(理赔服务)")
    private String serviceTypeName;

    @ApiModelProperty(value = "理赔类型中文(理赔项目)")
    private String claimItemName;

    @ApiModelProperty(value = "手机故障")
    private String breakDown;
    @ApiModelProperty(value = "维修城市")
    private String fixCity;
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "理赔人姓名")
    private String fixName;

    @ApiModelProperty(value = "理赔人支付宝号")
    private String fixAlipay;

    @ApiModelProperty(value = "操作日志")
    private List<OrderLogDTO> logs;

    private Long createBy;
    @ApiModelProperty("报险人姓名")
    private String creatorName;
    @ApiModelProperty("报险人手机号")
    private String creatorPhone;

    @ApiModelProperty("所属门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("所属门店")
    private String storeCompanyName;
    @ApiModelProperty("门店负责人")
    private String storeCompanyManageName;
    @ApiModelProperty("门店负责人手机号")
    private String storeCompanyManageMobile;

    @ApiModelProperty("联系人电话")
    private String contactMobile;

    @ApiModelProperty("新机IMEI")
    private String newImei;

    public String getDiscountAmount() {
        return StringUtils.convertMoneyDefaultNull(this.discountAmount);
    }

    public String getOldSkuRetailPrice() {
        return StringUtils.convertMoneyDefaultNull(this.oldSkuRetailPrice);
    }

    public String getNewProductSkuRetailPrice() {
        return StringUtils.convertMoneyDefaultNull(this.newProductSkuRetailPrice);
    }

    public String getSuppleAmount() {
        return StringUtils.convertMoneyDefaultNull(this.suppleAmount);
    }

    public String getSettleAmount() {
        return StringUtils.convertMoneyDefaultNull(this.settleAmount);
    }

    public String getOldSkuPurchaseInvoicePrice() {
        return StringUtils.convertMoneyDefaultNull(this.oldSkuPurchaseInvoicePrice);
    }

    public String getNewProductSkuStallPrice() {
        return StringUtils.convertMoneyDefaultNull(this.newProductSkuStallPrice);
    }
}