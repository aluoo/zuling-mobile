package com.zxtx.hummer.hk.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum HkOperationEnum {
    CREATE(1, "添加", "添加"),
    EDIT(2, "编辑", "编辑"),
    ONLINE(3, "上线", "上线"),
    OFFLINE(4, "下线", "下线"),
    DELETE(5, "删除", "删除"),
    COMMISSION_STATUS(6, "分佣设置", "分佣设置"),
    ;

    private final Integer code;
    private final String desc;
    private final String remark;
}