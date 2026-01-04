package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

/**
 * <p>
 * 数保产品选项表
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("di_option")
@ApiModel(value = "DiOption对象", description = "数保产品选项表")
public class DiOption extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("1单选 2多选 3图片")
    private Integer type;

    @ApiModelProperty("是否必填0否1是")
    private Boolean required;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("上级ID")
    private Long parentId;

    @ApiModelProperty("层级路径")
    private String ancestors;

    @ApiModelProperty("示例图")
    private String sampleImage;

    @ApiModelProperty("选项code")
    private String code;
}
