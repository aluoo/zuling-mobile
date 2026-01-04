package com.zxtx.hummer.product.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/26
 * @Copyright
 * @Version 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    private Long createBy;
    @ApiModelProperty(value = "创建者")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;
    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "操作状态")
    private Integer operationStatus;
    @ApiModelProperty(value = "操作描述")
    private String operation;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "订单状态中文")
    private String statusStr;
    @ApiModelProperty(value = "操作状态中文")
    private String operationStatusStr;
}