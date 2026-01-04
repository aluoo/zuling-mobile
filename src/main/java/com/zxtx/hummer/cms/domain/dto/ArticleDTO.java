package com.zxtx.hummer.cms.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Date 2023/9/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    @ApiModelProperty("创建者")
    private Long createBy;
    @ApiModelProperty("更新者")
    private Long updateBy;
    @ApiModelProperty("是否显示 0隐藏1显示")
    private Boolean activated;
    private String activatedStr;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("分类ID")
    private Long categoryId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("副标题")
    private String subTitle;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("是否热门")
    private Boolean isPopular;
    @ApiModelProperty("是否置顶")
    private Boolean isTop;
    @ApiModelProperty("阅读数")
    private Long views;

    private int page = 1;

    private int pageSize = 10;
}