package com.zxtx.hummer.insurance.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
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
public class DiInsuranceOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    @ExcelField(name = "订单编号")
    private Long id;

    @ApiModelProperty("门店")
    @ExcelField(name = "门店")
    private String storeName;

    @ApiModelProperty("店员姓名")
    @ExcelField(name = "店员姓名")
    private String storeEmployeeName;

    @ApiModelProperty("店员手机号")
    @ExcelField(name = "店员手机号")
    private String storePhone;

    @ApiModelProperty("产品名称")
    @ExcelField(name = "产品名称")
    private String insuranceName;

    @ApiModelProperty("金额")
    @IgnoreExcelField
    private Long price;

    @ExcelField(name = "金额")
    private String priceStr;

    @ApiModelProperty("品牌型号")
    @ExcelField(name = "品牌型号")
    private String productName;

    @ApiModelProperty("顾客姓名")
    @ExcelField(name = "顾客姓名")
    private String customName;

    @ApiModelProperty("顾客手机号")
    @ExcelField(name = "顾客手机号")
    private String customPhone;

    @ApiModelProperty("身份证号")
    @ExcelField(name = "身份证号")
    private String idCard;

    @ApiModelProperty("IMEI码")
    @ExcelField(name = "IMEI码")
    private String imeiNo;

    @ApiModelProperty("订单状态")
    @ExcelField(name = "订单状态", replace = {"-2_退款中", "-1_已关闭", "0_待支付", "1_待审核", "2_已完成"})
    private Integer status;

    @ApiModelProperty("订单子状态")
    @ExcelField(name = "订单子状态", replace = {"0_待支付", "1_资料待审核", "2_退款待审核", "3_待上传", "4_待出保", "5_已出保", "6_资料审核失败", "7_退款审核失败", "8_手动取消", "9_自动取消", "10_已退款"})
    private Integer subStatus;

    @ApiModelProperty("创建日期")
    @ExcelField(name = "创建日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("支付日期")
    @ExcelField(name = "支付日期", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    public String getPrice() {
        return StringUtils.fenToYuan(price);
    }

    public void setPriceStr() {
        if (this.price!= null) {
            this.priceStr = StringUtils.fenToYuan(price);
        }
    }
}