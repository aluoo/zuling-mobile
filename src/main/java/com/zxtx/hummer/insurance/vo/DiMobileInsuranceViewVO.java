package com.zxtx.hummer.insurance.vo;

import cn.hutool.core.lang.tree.Tree;
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
public class DiMobileInsuranceViewVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联产品列表")
    private List<DiInsuranceViewVO> productList;


}