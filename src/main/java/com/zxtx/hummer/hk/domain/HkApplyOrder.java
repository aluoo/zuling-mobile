package com.zxtx.hummer.hk.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hk_apply_order")
public class HkApplyOrder extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("商品编码")
    private String fetchCode;
    @ApiModelProperty("商品名称")
    private String fetchName;
    @ApiModelProperty("三方订单号")
    private String thirdOrderSn;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("省份")
    private String provinceName;
    @ApiModelProperty("市")
    private String cityName;
    @ApiModelProperty("区")
    private String townName;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("预约手机号")
    private String planMobileNumber;
    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("门店ID")
    private Long companyId;
    @ApiModelProperty("层级")
    private String ancestors;
    @ApiModelProperty("状态")
    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "激活时间", hidden = true)
    private Date activeTime;

    @ApiModelProperty("理由")
    private String reason;
    @ApiModelProperty("物流单号")
    private String expressBill;

    @ApiModelProperty("供应商ID")
    private Long supplierId;
    @ApiModelProperty("运营商ID")
    private Long operatorId;

    @ApiModelProperty("物流公司")
    private String express;
}