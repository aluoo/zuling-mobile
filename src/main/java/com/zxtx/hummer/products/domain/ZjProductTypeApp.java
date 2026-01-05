package com.zxtx.hummer.products.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 小程序产品种类
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
@Getter
@Setter
@TableName("zj_product_type_app")
@ApiModel(value = "ZjProductTypeApp对象", description = "小程序产品种类")
public class ZjProductTypeApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("种类")
    private String productCategory;

    @ApiModelProperty("1正常0隐藏")
    private Boolean hidden;

    @ApiModelProperty("产品id集合,用逗号分隔")
    private String productIds;

    @ApiModelProperty("图片url地址")
    private String img;

    @ApiModelProperty("附属图片url地址,用逗号分隔")
    private String imgs;

    @ApiModelProperty("跳转页")
    private String jumpPage;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("配置信息")
    private String configInfo;

    @ApiModelProperty("创建日期")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新日期")
    private LocalDateTime updatedTime;

    private String createdBy;

    private String updatedBy;
}
