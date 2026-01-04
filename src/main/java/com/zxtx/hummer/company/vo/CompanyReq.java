package com.zxtx.hummer.company.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompanyReq {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String contact;

    @NotNull
    private String contactMobile;

    private Integer payAmount;

    private Integer deposit;

    private String sn;

    private String bank;

    private String bankAccount;

    private List<Long> etcTypeIds;

    private List<Long> packageIds;
}
