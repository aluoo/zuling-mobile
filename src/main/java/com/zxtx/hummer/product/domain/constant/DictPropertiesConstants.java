package com.zxtx.hummer.product.domain.constant;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
public class DictPropertiesConstants {
    public static final String DICT_NAME_CACHE_PREFIX = "dict:name:";
    /**
     * 报价订单过期时间
     */
    public static final String PRODUCT_ORDER_EXPIRED_MINUTES = "product_order_expired_minutes";
    /**
     * 报价订单报价功能过期时间
     */
    public static final String PRODUCT_ORDER_QUOTE_EXPIRED_MINUTES = "product_order_quote_expired_minutes";
    /**
     * 发货订单过期时间
     */
    public static final String SHIPPING_ORDER_EXPIRED_MINUTES = "shipping_order_expired_minutes";
    /**
     * 平台设置的报价预警金额，超过该金额提示预警
     */
    public static final String QUOTE_WARNING_THRESHOLD_PRICE = "quote_warning_threshold_price";
    /**
     * 当天发货单笔金额阈值，大于等于此值时截止至当天发货，否则截止至本周末发货，默认为200元，单位分
     */
    public static final String SHIPPING_OVER_DUE_TIME_THRESHOLD_PRICE = "shipping_over_due_time_threshold_price";
}