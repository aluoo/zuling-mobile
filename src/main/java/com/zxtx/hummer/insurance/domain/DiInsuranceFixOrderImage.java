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

/**
 * <p>
 * 数保报修订单选项表
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("di_insurance_fix_order_image")
@ApiModel(value = "DiInsuranceFixOrderOption对象", description = "数保报修图片信息表")
public class DiInsuranceFixOrderImage extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("数保报修订单ID")
    private Long fixOrderId;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("1.报险资料2换机补充材料3.理赔上传材料")
    private Integer type;
}