package com.zxtx.hummer.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 手机SKU类型枚举类
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum ProductSkuTypeEnum {
    ANDROID(1, "安卓"),
    APPLE(2, "苹果"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}