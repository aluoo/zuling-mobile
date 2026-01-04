package com.zxtx.hummer.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum OptionCodeEnum {
    MCHSTAT_TITLE("MCHSTATTITLE", "机况选项标题"),
    MCHSTAT("MCHSTAT", "机况"),
    ROM_TITLE("ROMTITLE", "存储空间标题"),
    ROM("ROM", "存储空间"),
    RAM_TITLE("RAMTITLE", "运行内存标题"),
    RAM("RAM", "运行内存"),
    COLOR("COLOR", "颜色"),
    IMG("IMG", "图片"),
    OTHER_IMG("OTHERIMG", "其它图片"),
    FUNC_TITLE("FUNCTITLE", "功能确认项标题"),
    FUNC("FUNC", "功能确认"),
    FUNC_OK("FUNCOK", "功能确认-正常"),
    FUNC_ERR("FUNCERR", "功能确认-异常"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}