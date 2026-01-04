package com.zxtx.hummer.product.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.product.dao.ProductOptionMapper;
import com.zxtx.hummer.product.domain.ProductOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductOptionService extends ServiceImpl<ProductOptionMapper, ProductOption> {

    @Transactional(rollbackFor = Exception.class)
    public <T> void addRelation(Collection<T> optionIds, Long productId) {
        if (productId == null) {
            return;
        }
        if (CollUtil.isEmpty(optionIds)) {
            removeExistRelationByProductId(productId);
            return;
        }

        List<ProductOption> list = buildRelation(optionIds, productId);

        if (CollUtil.isNotEmpty(list)) {
            removeExistRelationByProductId(productId);
            this.saveBatch(list);
        }
    }

    public List<Long> getOptionIdsByProductId(Long productId) {
        if (productId == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ProductOption> qw = new LambdaQueryWrapper<ProductOption>().eq(ProductOption::getProductId, productId);
        return this.list(qw).stream().map(ProductOption::getOptionId).collect(Collectors.toList());
    }

    private <T> List<ProductOption> buildRelation(Collection<T> optionIds, Long productId) {
        return optionIds.stream()
                .map(optId -> ProductOption.builder().id(SnowflakeIdWorker.nextID()).optionId((Long) optId).productId(productId).build())
                .collect(Collectors.toList());
    }

    private void removeExistRelationByProductId(Long productId) {
        if (productId == null) {
            return;
        }
        this.remove(new LambdaQueryWrapper<ProductOption>().eq(ProductOption::getProductId, productId));
    }
}