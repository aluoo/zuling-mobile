package com.zxtx.hummer.mbr.response;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderStatusEnum;
import com.zxtx.hummer.mbr.domain.enums.MbrPreOrderStatusEnum;
import com.zxtx.hummer.mbr.domain.enums.ProductTypeEnum;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 进件单
 */
@Data
public class MbrPreOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("机器类型1新机2二手机")
    private Integer productType;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("期数")
    private Integer period;

    @ApiModelProperty("员工门店")
    private String storeCompanyName;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工手机号")
    private String mobileNumber;

    @ApiModelProperty("租机商户")
    private String recyclerCompanyName;

    @ApiModelProperty("进件单状态")
    private Integer preOrderStatus;

    @ApiModelProperty("租机单状态")
    private Integer orderStatus;

    @ApiModelProperty("进件时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("租机平台审核列表")
    private List<QuoteLogVO> quoteLogVOList;

    @ApiModelProperty("操作日志列表")
    List<OrderLogDTO> orderLogList;

    public String getProductType(){
        return EnumUtil.getBy(ProductTypeEnum::getCode,productType).getDesc();
    }

    public String getPreOrderStatus(){
        return EnumUtil.getBy(MbrPreOrderStatusEnum::getCode,preOrderStatus).getDesc();
    }

    public String getOrderStatus(){
        return EnumUtil.getBy(MbrOrderStatusEnum::getCode,orderStatus).getDesc();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuoteLogVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("平台名称")
        private String recyclerCompanyName;

        @ApiModelProperty("审核结果")
        private String statusName;

        @ApiModelProperty("审核时间")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date creteTime;

    }

}