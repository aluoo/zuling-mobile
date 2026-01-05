package com.zxtx.hummer.products.domain.request;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "ZjProductTypeQueryReq", description = "商品类型查询请求对象")
public class ZjProductTypeQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型（1级分类，2级品牌）")
    private Integer type;

    @ApiModelProperty("类型名称")
    private String tit;

    @ApiModelProperty("显示类型：list列表，tree树形")
    private String showType = "list";

}
