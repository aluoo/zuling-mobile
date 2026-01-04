package com.zxtx.hummer.mbr.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/6/11
 * @Copyright
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HwzOrderDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long saleOrderId;
    private String status;
    private String outShopId;
    private String serialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String skuName;
    private Device device;
    private List<SaleOrderPhase> phases;

    @ApiModelProperty("操作日志")
    List<OrderLogDTO> orderLogList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Device implements Serializable {
        private static final long serialVersionUID = 1L;

        private String serialNumber;
        private String control;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date lastConnectTime;
        private String lostStatus;
        private String productName;
        private String imei;
        private String meid;
        private String battery;
        private Boolean activationLockEnabled;
        private String phoneNumber;
        private String osVersion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaleOrderPhase implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer number;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date startDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date endDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date expectPayDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date completedAt;
        private BigDecimal totalRent;
        private BigDecimal paidRent;
        private Boolean completed;
        private Integer overdueDay;
        private BigDecimal overdueInterest;
        private BigDecimal paidOverdueInterest;
    }
}