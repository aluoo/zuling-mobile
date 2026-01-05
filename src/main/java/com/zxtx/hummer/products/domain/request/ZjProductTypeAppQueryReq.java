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
@ApiModel(value = "ZjProductTypeAppQueryReq", description = "小程序商品类型查询请求对象")
public class ZjProductTypeAppQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("类型名称")
    private String productCategory;

}
