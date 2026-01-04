package com.zxtx.hummer.mbr.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ProductTypeEnum {

    NEW(1, "新机"),
    OLD(2, "二手机"),
    ;

    private final Integer code;
    private final String desc;
}