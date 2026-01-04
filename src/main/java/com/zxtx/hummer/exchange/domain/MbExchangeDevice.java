package com.zxtx.hummer.exchange.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 晒单客户机基础信息
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_device")
@ApiModel(value = "MbExchangeDevice对象", description = "晒单客户机基础信息")
public class MbExchangeDevice extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("出厂日期")
    private Date factoryDate;

    @ApiModelProperty("安装日期")
    private Date installDate;

    @ApiModelProperty("开机时间")
    private Integer openTime;

    @ApiModelProperty("机器IP")
    private String ip;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("系统版本号")
    private String sysVersion;

    @ApiModelProperty("oaid")
    private String oaid;

}
