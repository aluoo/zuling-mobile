package com.zxtx.hummer.insurance.enums;

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
public enum DiOptionCodeEnum {
    FIXSERVICE("FIXSERVICE", "服务类型"),
    FIXITEM("FIXITEM", "理赔项目"),
    FIXDATA("FIXDATA", "接件单"),
    SUPPLEDATA("SUPPLEDATA", "验机资料"),
    SETTLEDATA("SETTLEDATA", "理赔资料"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}