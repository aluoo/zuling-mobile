package com.zxtx.hummer.withdraw.vo;

import com.zxtx.hummer.common.annotation.ExcelField;
import lombok.Data;

@Data
public class ImportWithdrawVO {

    @ExcelField(name = "款项属性")
    private String applyNo;

    @ExcelField(name = "付款状态")
    private String payStatus;


}
