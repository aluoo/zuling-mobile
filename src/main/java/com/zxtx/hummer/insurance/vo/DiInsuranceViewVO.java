package com.zxtx.hummer.insurance.vo;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.insurance.domain.DiProductInsurancePrice;
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
public class DiInsuranceViewVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("产品描述")
    private String description;
    @ApiModelProperty(value = "类型ID")
    private Long typeId;
    @ApiModelProperty(value = "套餐ID")
    private Long packageId;
    @ApiModelProperty(value = "年限")
    private Integer period;

    @ApiModelProperty("底价")
    private Long downPrice;

    @ApiModelProperty("保额")
    private Long coverPrice;

    @ApiModelProperty("选中的选项ID列表")
    private List<Long> optionIds;

    @ApiModelProperty("选项信息")
    private List<Tree<Long>> options;

    @ApiModelProperty("价格区间")
    List<DiProductInsurancePrice> priceList;

}