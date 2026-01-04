package com.zxtx.hummer.hk.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/21
 * @Copyright
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HkApplyOrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店名称")
    private String companyName;
    @ApiModelProperty("下单人名称")
    private String employeeName;
    @ApiModelProperty("下单人手机号")
    private String employeeMobile;

    @ApiModelProperty(value = "订单编号")
    private Long id;
    @ApiModelProperty("商品编码")
    private String fetchCode;
    @ApiModelProperty("申请人姓名")
    private String name;
    @ApiModelProperty("申请号码")
    private String planMobileNumber;
    @ApiModelProperty(value = "运营商ID")
    private Long operatorId;
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    @ApiModelProperty("省份")
    private String provinceName;
    @ApiModelProperty("市")
    private String cityName;
    @ApiModelProperty("区")
    private String townName;
    /**
     * @see HkApplyOrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty("下单日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createStartTime;

    @ApiModelProperty("下单日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createEndTime;

    @ApiModelProperty("激活日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeStartTime;

    @ApiModelProperty("激活日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeEndTime;
}