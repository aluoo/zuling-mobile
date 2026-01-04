package com.zxtx.hummer.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单选项信息表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("mb_order_option")
@ApiModel(value = "MbOrderOption对象", description = "订单选项信息表")
public class MbOrderOption extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("更新时间")
    private Long createBy;

    @ApiModelProperty("更新者")
    private Long updateBy;

    @ApiModelProperty("是否删除0否1是")
    private Boolean deleted;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("选项ID")
    private Long optionId;

    @ApiModelProperty("选项code")
    private String code;

    @ApiModelProperty("选项标题")
    private String title;

    @ApiModelProperty("选项值")
    private String value;
}
