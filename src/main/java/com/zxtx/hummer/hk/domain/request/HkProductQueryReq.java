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
public class HkProductQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "运营商ID")
    private Long operatorId;
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    @ApiModelProperty(value = "套餐名称")
    private String name;
    @ApiModelProperty(value = "套餐编码")
    private String code;
    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;
    /**
     * @see HkApplyOrderStatusEnum
     */
    @ApiModelProperty(value = "分佣条件（订单状态）")
    private Integer commissionStatus;

    @ApiModelProperty("创建日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createStartTime;

    @ApiModelProperty("创建日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createEndTime;

    @ApiModelProperty("更新日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateStartTime;

    @ApiModelProperty("更新日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateEndTime;
}