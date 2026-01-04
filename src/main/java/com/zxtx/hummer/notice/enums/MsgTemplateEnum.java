package com.zxtx.hummer.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/17
 */
@AllArgsConstructor
@Getter
public enum MsgTemplateEnum {
    WELCOME_ACTIVE("ETC激活通知", "恭喜您成功激活ETC，感谢您选择安逸出行。安逸出行平台致力于为广大车主朋友提供方便快捷、安全保密、高效稳定的服务，为每位车主的高速通行保驾护航。", "ETC激活通知"),
    PAY_SUCCESS_A("ETC套餐办理通知", "您已成功支付办理费{amount}元，正式开通{package_name}。套餐无合约期、随时免费注销、设备质保{device_quality_months}年、先通行后付费、微信代扣、服务费{fee}%。", "ETC套餐办理通知A套餐"),
    PAY_SUCCESS_B("ETC套餐办理通知", "您已成功支付办理费{amount}元，正式开通{package_name}。套餐合约期{contract_months}个月达标奖励{amount}元、设备质保{device_quality_months}年、免费注销、先通行后付费、微信代扣、服务费{fee}%、余额不足平台先行垫付通行费。", "ETC套餐办理通知B套餐"),
    PAY_SUCCESS_C1C2("ETC套餐办理通知", "您已成功支付办理费{amount}元，正式开通{package_name}。套餐合约期{contract_months}个月、设备质保{device_quality_months}年、先通行后付费、微信代扣、服务费{fee}%、办理费可在初次通行3个月后且余额不足时用于抵扣通行费。", "ETC套餐办理通知C1/C2套餐"),
    PAY_SUCCESS_C3C4("ETC套餐办理通知", "您已成功支付办理费{amount}元，正式开通{package_name}。套餐合约期{contract_months}个月、设备质保{device_quality_months}年、先通行后付费、微信代扣、服务费{fee}%、初次通行3个月后可调整扣款顺序、办理费可用于抵扣通行费。", "ETC套餐办理通知C3/C4套餐"),
    ;

    private final String title;
    private final String template;
    private final String desc;
}