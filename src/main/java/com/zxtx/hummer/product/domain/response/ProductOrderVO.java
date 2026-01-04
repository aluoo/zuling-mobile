package com.zxtx.hummer.product.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxtx.hummer.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "手机订单响应对象")
public class ProductOrderVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long createBy;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Long updateBy;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty(value = "门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("下单人姓名(门店员工姓名)")
    private String storeEmployeeName;
    @ApiModelProperty("下单人电话")
    private String storeEmployeeMobile;
    @ApiModelProperty("下单门店名称")
    private String storeCompanyName;
    @ApiModelProperty(value = "商品ID")
    private Long productId;
    @ApiModelProperty(value = "商品名称")
    private String productName;
    @ApiModelProperty(value = "订单编码")
    private String orderNo;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    private String statusStr;
    @ApiModelProperty(value = "IMEI号")
    private String imeiNo;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "原始报价")
    private Integer originalQuotePrice;
    @ApiModelProperty(value = "成交价", hidden = true)
    @JsonIgnore
    private Integer finalPrice;
    @ApiModelProperty(value = "成交价格 (最终确认的报价，用户实际收款的价格，用户退款的价格，回收商报价的价格)")
    private String finalPriceStr;
    @ApiModelProperty(value = "门店抽成金额")
    private Integer commission;
    @ApiModelProperty(value = "平台补贴价格")
    private Integer platformSubsidyPrice;
    @ApiModelProperty(value = "回收商员工ID")
    private Long recyclerEmployeeId;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;
    @ApiModelProperty(value = "回收商名称")
    private String recyclerCompanyName;
    @ApiModelProperty("回收商报价师姓名")
    private String recyclerEmployeeName;
    @ApiModelProperty("回收商报价师电话")
    private String recyclerEmployeeMobile;
    @ApiModelProperty(value = "确认报价详情ID")
    private Long quotePriceLogId;
    @ApiModelProperty(value = "确认报价时间")
    private Date finishQuoteTime;

    @ApiModelProperty(value = "是否绑码")
    private Boolean bound;
    @ApiModelProperty(value = "是否核验")
    private Boolean verified;
    @ApiModelProperty(value = "是否可报价（超时将关闭报价功能）")
    private Boolean quotable;

    public void setFinalPriceStr() {
        if (this.getFinalPrice() != null && this.getFinalPrice() > 0) {
            this.finalPriceStr = StringUtils.convertMoney(this.getFinalPrice());
        }
    }

    public static <T> Set<T> extractIds(Collection<? extends ProductOrderVO> vos, Function<? super ProductOrderVO, T> ext1, Function<? super ProductOrderVO, T> ext2) {
        return vos.stream()
                .flatMap(o -> Stream.concat(
                        Optional.ofNullable(ext1.apply(o)).map(Stream::of).orElseGet(Stream::empty),
                        Optional.ofNullable(ext2.apply(o)).map(Stream::of).orElseGet(Stream::empty)
                ))
                .collect(Collectors.toSet());
    }
}