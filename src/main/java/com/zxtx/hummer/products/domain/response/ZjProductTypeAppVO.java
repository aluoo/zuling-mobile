package com.zxtx.hummer.products.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
public class ZjProductTypeAppVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    private Integer id;

    @ApiModelProperty("种类")
    private String productCategory;

    @ApiModelProperty("1正常0隐藏")
    private Boolean hidden;

    @ApiModelProperty("产品id集合,用逗号分隔")
    private String productIds;

    @ApiModelProperty("产品数量")
    private Integer productNum;

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


    @ApiModelProperty("更新日期")
    private LocalDateTime updatedTime;

    @ApiModelProperty("产品集合")
    private List<String> productList;

    public Integer getProductNum() {
        return Optional.ofNullable(productIds)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.split(","))
                .map(ids -> Arrays.stream(ids)
                        .filter(id -> !id.trim().isEmpty())
                        .count())
                .map(Long::intValue)
                .orElse(0);
    }
}
