package com.zxtx.hummer.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单选项快照信息表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("mb_order_option_snapshot")
@ApiModel(value = "MbOrderOptionSnapshot对象", description = "订单选项快照信息表")
public class MbOrderOptionSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long orderId;

    private String detail;
}
