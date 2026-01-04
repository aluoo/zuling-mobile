package com.zxtx.hummer.insurance.domain;

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
 * 数保订单表
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("di_sku_insurance")
public class DiSkuInsurance extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Long skuId;
    private Long insuranceId;
    private Long downPrice;

}
