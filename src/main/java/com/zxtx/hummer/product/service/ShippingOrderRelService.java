package com.zxtx.hummer.product.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.product.dao.ShippingOrderRelMapper;
import com.zxtx.hummer.product.domain.ShippingOrderRel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/20
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ShippingOrderRelService extends ServiceImpl<ShippingOrderRelMapper, ShippingOrderRel> {

    public List<ShippingOrderRel> listByShippingOrderId(Long shippingOrderId) {
        if (shippingOrderId == null) {
            return null;
        }
        return this.lambdaQuery().eq(ShippingOrderRel::getShippingOrderId, shippingOrderId).list();
    }

    public List<Long> listOrderIdsByShippingOrderId(Long shippingOrderId) {
        List<ShippingOrderRel> relations = this.listByShippingOrderId(shippingOrderId);
        return CollUtil.isNotEmpty(relations)
                ? relations.stream().map(ShippingOrderRel::getOrderId).filter(Objects::nonNull).collect(Collectors.toList())
                : null;
    }

    public List<Long> listOrderIdsByShippingOrderIds(List<Long> shippingOrderIds) {
        List<ShippingOrderRel> relations = this.lambdaQuery().in(ShippingOrderRel::getShippingOrderId, shippingOrderIds).list();
        return CollUtil.isNotEmpty(relations)
                ? relations.stream().map(ShippingOrderRel::getOrderId).filter(Objects::nonNull).collect(Collectors.toList())
                : null;
    }
}