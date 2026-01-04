package com.zxtx.hummer.product.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类响应对象")
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "logo")
    private String icon;
    @ApiModelProperty(value = "分类层级")
    private Integer level;
    @ApiModelProperty(value = "上级分类ID")
    private Long parentId;
    @ApiModelProperty(value = "分类层级路径")
    private String ancestors;
    @ApiModelProperty(value = "上级名称")
    private String parentName;
}