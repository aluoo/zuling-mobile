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

/**
 * <p>
 * 拉新换机安装包关联表
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("mb_exchange_install")
@ApiModel(value = "MbExchangeInstall对象", description = "拉新换机安装包关联表")
public class MbExchangeInstall extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("安装包ID")
    private Long installId;

    @ApiModelProperty("换机包ID")
    private Long exchangePhoneId;

}
