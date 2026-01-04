package com.zxtx.hummer.insurance.vo;

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
public class DiOptionVO implements Serializable {
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
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty("1单选 2多选 3图片 4视频")
    private Integer type;
    @ApiModelProperty(value = "分类层级")
    private Integer level;
    @ApiModelProperty(value = "上级分类ID")
    private Long parentId;
    @ApiModelProperty(value = "层级路径")
    private String ancestors;

    @ApiModelProperty("是否必填0否1是")
    private Boolean required;

    @ApiModelProperty(value = "上级名称")
    private String parentName;

    @ApiModelProperty(value = "示例图")
    private String sampleImage;

    @ApiModelProperty("实际图")
    private String value;

    @ApiModelProperty("选项code")
    private String code;
}