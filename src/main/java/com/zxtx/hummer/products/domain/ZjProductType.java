package com.zxtx.hummer.products.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品类目
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
@Getter
@Setter
@TableName("zj_product_type")
@ApiModel(value = "ZjProductType对象", description = "商品类目")
public class ZjProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("上级ID")
    private Integer upid;

    @ApiModelProperty("类别（1级分类，2级品牌）")
    private Integer type;

    @ApiModelProperty("类型名称")
    private String tit;

    @ApiModelProperty("介绍")
    private String cont;

    @ApiModelProperty("图标")
    private String image;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("0隐藏1显示")
    private Boolean hidden;
}
