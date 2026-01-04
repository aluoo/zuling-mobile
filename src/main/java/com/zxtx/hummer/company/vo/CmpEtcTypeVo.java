package com.zxtx.hummer.company.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CmpEtcTypeVo {

    private Long companyId;

    private Long etcTypeId;

    private String etcTypeName;

    private Boolean checked;

}
