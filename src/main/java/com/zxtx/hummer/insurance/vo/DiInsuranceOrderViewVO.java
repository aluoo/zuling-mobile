package com.zxtx.hummer.insurance.vo;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrderPicture;
import com.zxtx.hummer.insurance.enums.DiOrderPayTypeEnum;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class DiInsuranceOrderViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private Long id;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("产品名称")
    private String insuranceName;

    @ApiModelProperty("金额")
    @ExcelField(name = "金额")
    private Long price;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @ApiModelProperty("支付方式")
    private Integer type;

    @ApiModelProperty("保险服务单号")
    private String insuranceNo;

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

    @ApiModelProperty("店员")
    private String storeEmployeeName;

    @ApiModelProperty("店员手机号")
    private String storeEmployeePhone;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("代理名称")
    private String agentName;

    @ApiModelProperty("代理手机号")
    private String agentPhone;

    @ApiModelProperty("合伙人名称")
    private String bdName;

    @ApiModelProperty("合伙人手机号")
    private String bdPhone;

    @ApiModelProperty("市场零售价格")
    private Integer productSkuRetailPrice;

    @ApiModelProperty("手机发票购机价")
    private Integer productSkuPurchaseInvoicePrice;

    @ApiModelProperty("手机发票购机时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productSkuPurchaseInvoiceTime;

    @ApiModelProperty("门店进价")
    private Integer insuranceNormalPrice;

    @ApiModelProperty("平台底价")
    private Integer insuranceDownPrice;

    @ApiModelProperty("投保备注")
    private String remark;

    @ApiModelProperty("图片视频")
    private List<DiInsuranceOrderPicture> picList;

    @ApiModelProperty("操作日志")
    List<OrderLogDTO> orderLogList;



    public String getPrice() {
        return StringUtils.fenToYuan(price);
    }

    public String getProductSkuRetailPrice() {
        return StringUtils.convertMoney(productSkuRetailPrice);
    }

    public String getProductSkuPurchaseInvoicePrice() {
        return StringUtils.convertMoneyDefaultNull(productSkuPurchaseInvoicePrice );
    }

    public String getInsuranceNormalPrice() {
        return StringUtils.convertMoney(insuranceNormalPrice);
    }

    public String getInsuranceDownPrice() {
        return StringUtils.convertMoney(insuranceDownPrice);
    }

    public String getType(){
        if(type!=null){
            return EnumUtil.getBy(DiOrderPayTypeEnum::getCode,type).getDesc();
        }
        return "";
    }


}