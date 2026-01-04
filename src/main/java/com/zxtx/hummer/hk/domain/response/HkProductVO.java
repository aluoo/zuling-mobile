package com.zxtx.hummer.hk.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HkProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    private Long createBy;
    private Long updateBy;

    @ApiModelProperty(value = "创建者")
    private String creator;
    @ApiModelProperty(value = "更新者")
    private String updater;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "运营商ID")
    private Long operatorId;
    @ApiModelProperty(value = "运营商")
    private String operator;
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    @ApiModelProperty(value = "供应商")
    private String supplier;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;

    @JsonIgnore
    private Long price;
    @ApiModelProperty(value = "价格")
    private String priceStr;
    @ApiModelProperty(value = "是否选号 0否1是")
    private Boolean requireSelectMobile;
    /**
     * @see HkApplyOrderStatusEnum
     */
    @ApiModelProperty(value = "分佣条件（订单状态）")
    private Integer commissionStatus;

    @ApiModelProperty(value = "分佣套餐ID")
    private Long commissionTypePackageId;

    private String commissionStatusStr;

    @ApiModelProperty(value = "列表主图")
    private String masterImage;
    @ApiModelProperty(value = "详情图")
    private String detailImage;
    @ApiModelProperty(value = "卖点1")
    private String sellPointOne;
    @ApiModelProperty(value = "卖点2")
    private String sellPointTwo;
    @ApiModelProperty(value = "卖点3")
    private String sellPointThree;
    @ApiModelProperty(value = "标签1")
    private String tagOne;
    @ApiModelProperty(value = "标签2")
    private String tagTwo;
    @ApiModelProperty(value = "标签3")
    private String tagThree;

    @ApiModelProperty(value = "归属地-省")
    private String province;
    @ApiModelProperty(value = "归属地-市")
    private String city;

    @ApiModelProperty(value = "归属地")
    private String area;

    @ApiModelProperty(value = "关联门店数")
    private Long companyNum;

    @ApiModelProperty(value = "操作日志")
    private List<OrderLogDTO> logs;

    public static <T> Set<T> extractIds(Collection<? extends HkProductVO> vos, Function<? super HkProductVO, T> ext1, Function<? super HkProductVO, T> ext2) {
        return vos.stream()
                .flatMap(o -> Stream.concat(
                        Optional.ofNullable(ext1.apply(o)).map(Stream::of).orElseGet(Stream::empty),
                        Optional.ofNullable(ext2.apply(o)).map(Stream::of).orElseGet(Stream::empty)
                ))
                .collect(Collectors.toSet());
    }
}