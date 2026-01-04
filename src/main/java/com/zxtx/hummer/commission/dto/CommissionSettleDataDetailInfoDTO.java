package com.zxtx.hummer.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionSettleDataDetailInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long employeeId;

    private Integer bizType;

    private Integer gainType;

    private Integer settleBalance;

    private Integer correlationType;

    private Long correlationId;

    private Date settleTime;

    private String remark;

}