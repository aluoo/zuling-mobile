package com.zxtx.hummer.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单操作日志表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mb_order_log")
@ApiModel(value = "MbOrderLog对象", description = "订单操作日志表")
public class MbOrderLog  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建者")
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("操作状态")
    private Integer operationStatus;

    @ApiModelProperty("操作描述")
    private String operation;

    @ApiModelProperty("备注")
    private String remark;
}
