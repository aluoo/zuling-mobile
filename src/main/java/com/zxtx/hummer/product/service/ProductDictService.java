package com.zxtx.hummer.product.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.common.dao.mapper.DictMapper;
import com.zxtx.hummer.common.vo.Dicts;
import com.zxtx.hummer.product.domain.constant.DictPropertiesConstants;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/18
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductDictService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private DictMapper dictMapper;

    public List<String> getInsuranceFixOrderDataRejectReason() {
        String value = getValueByName("di_insurance_fix_order_data_reject_reason");
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultInsuranceFixOrderCloseReason();
    }

    public List<String> getInsuranceFixOrderRejectReason() {
        String value = getValueByName("di_insurance_fix_order_reject_reason");
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultInsuranceFixOrderCloseReason();
    }

    public List<String> getInsuranceFixOrderCloseReason() {
        String value = getValueByName("di_insurance_fix_order_close_reason");
        return StrUtil.isNotBlank(value)
                ? StrUtil.split(value, TreeConstants.SPLIT_CHAR)
                : getDefaultInsuranceFixOrderCloseReason();
    }

    private List<String> getDefaultInsuranceFixOrderCloseReason() {
        List<String> list = new LinkedList<>();
        list.add("本公司近期暂不回收此类机型");
        list.add("照片遮挡，无法给出准确报价");
        list.add("功能选项描述与图片信息不符，无法给出准确报价");
        list.add("山寨机、租赁机等，不予回收");
        list.add("无面容、原彩、指纹等核心功能照片，无法给出准确报价");
        list.add("无验机报告（沙漏/爱思截图），无法给出准确报价");
        list.add("图片过于模糊，无法给出准确报价");
        list.add("外观照片缺失，无法给出准确报价");
        list.add("无白色【关于本机】界面，无法判断屏幕问题，无法给出准确报价");
        list.add("无设备主界面相关图片，无法判断旧机来源是否合法");
        return list;
    }

    public int getShippingOrderExpiredMinutes() {
        int defaultValue = 60 * 5;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.SHIPPING_ORDER_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getShippingOrderExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }


    /**
     * 获取订单过期时间设置，超时关闭订单
     */
    public int getProductOrderExpiredMinutes() {
        int defaultValue = 60 * 2;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.PRODUCT_ORDER_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getProductOrderExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 获取订单报价过期时间设置，超时关闭报价功能
     */
    public int getQuoteExpiredMinutes() {
        int defaultValue = 5;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.PRODUCT_ORDER_QUOTE_EXPIRED_MINUTES)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getQuoteExpiredMinutes.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    public int getQuoteWarningThresholdPrice() {
        // 默认10000元
        int defaultValue = 1000000;
        String value = Optional.ofNullable(getValueByName(DictPropertiesConstants.QUOTE_WARNING_THRESHOLD_PRICE)).orElse(String.valueOf(defaultValue));
        try {
            defaultValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.info("CommonSysDictService.getQuoteWarningThresholdPrice.error: {}", e.getMessage());
        }
        return defaultValue;
    }

    private String getValueByName(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }
        String value = getFromCache(name);
        if (StrUtil.isBlank(value)) {
            value = getValueByNameFromStorage(name);
            setToCache(name, value);
        }
        return value;
    }

    private String getValueByNameFromStorage(String name) {
        return StrUtil.isBlank(name)
                ? null
                : Optional.ofNullable(dictMapper.selectOne(new LambdaQueryWrapper<Dicts>().eq(Dicts::getName, name))).map(Dicts::getValue).orElse(null);
    }

    private String getFromCache(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        }
        String key = buildKey(name);
        return redisTemplate.opsForValue().get(key);
    }

    private void setToCache(String name, String value) {
        if (StrUtil.isBlank(name) || StrUtil.isBlank(value)) {
            return;
        }
        String key = buildKey(name);
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.DAYS);
    }

    private String buildKey(String name) {
        return DictPropertiesConstants.DICT_NAME_CACHE_PREFIX + name;
    }
}