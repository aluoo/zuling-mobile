package com.zxtx.hummer.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 是否为Pro机型枚举类
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum ProductSkuProStatusEnum {
    NOT_PRO(0, "非Pro"),
    PRO(1, "Pro"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}