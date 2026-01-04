package com.zxtx.hummer.product.domain.response;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.product.domain.dto.OptionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ApiModel(value = "商品响应对象")
public class ProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分类全称")
    private String categoryFullName;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "是否上架")
    private Boolean activated;

    @ApiModelProperty("选中的选项ID列表")
    private List<Long> optionIds;

    @ApiModelProperty("选项信息")
    private List<Tree<Long>> options;

    @ApiModelProperty(value = "只作为展示选项数据结构信息，实际选项数据信息在options中")
    private List<OptionDTO> optionSample;


    @ApiModelProperty(value = "分类列表")
    private List<CategoryVO> categories;

    @ApiModelProperty(value = "是否售卖数保 0否1是")
    private Boolean digitalInsuranceAble;

    @ApiModelProperty(value = "是否参与租机 0否1是")
    private Boolean mobileRentalAble;

}