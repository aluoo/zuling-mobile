package com.zxtx.hummer.company.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyChainInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long companyId;
    private Long employeeId;
    private Long agentId;
    private String agentName;
    private Long areaId;
    private String areaName;
    private Long bdId;
    private String bdName;
}