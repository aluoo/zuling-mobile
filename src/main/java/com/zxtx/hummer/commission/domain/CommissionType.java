package com.zxtx.hummer.commission.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 佣金方案表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("commission_type")
@ApiModel(value = "CommissionBizType对象", description = "佣金方案")
public class CommissionType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("方案类型名称")
    private String name;


    private Boolean deleted;
}
