package com.zxtx.hummer.common.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author bootdo
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    public static final String DECIMAL_FORMAT = "0.00";
    public static final int HUNDRED = 100;

    public static String buildArea(String province, String city, String district, String delimiter) {
        if (StrUtil.isBlank(delimiter)) {
            delimiter = "/";
        }
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(province)) {
            sb.append(province);
        }
        if (StrUtil.isNotBlank(city)) {
            if (sb.length() > 0) {
                sb.append(delimiter).append(city);
            } else {
                sb.append(city);
            }
        }
        if (StrUtil.isNotBlank(district)) {
            if (sb.length() > 0) {
                sb.append(delimiter).append(district);
            } else {
                sb.append(district);
            }
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    public static String decimalFormat(Double value) {
        return NumberUtil.decimalFormat(DECIMAL_FORMAT, value / HUNDRED);
    }

    public static String fenToYuan(Long amount) {
        if (amount == null) {
            return "-";
        }
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static Long fenToMoney(Long amount) {
        return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).longValue();
    }

    public static String convertMoney(Integer fee) {
        if (fee == null) {
            return "-";
        }
        return convertMoneyBase(fee);
    }

    public static Integer yuanToFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal(HUNDRED)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public static String convertMoneyDefaultNull(Integer fee) {
        return fee == null ? null : convertMoneyBase(fee);
    }

    public static Integer yuanToFen(String amount) {
        return new BigDecimal(amount).multiply(new BigDecimal(HUNDRED)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private static String convertMoneyBase(int fee) {
        return new BigDecimal(fee).divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY).toString();
    }
}