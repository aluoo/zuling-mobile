package com.zxtx.hummer.product.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.product.dao.ProductMapper;
import com.zxtx.hummer.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    public boolean checkNameExist(String name) {
        if (StrUtil.isBlank(name)) {
            return false;
        }
        return this.baseMapper.exists(new LambdaQueryWrapper<Product>().eq(Product::getName, name).eq(AbstractBaseEntity::getDeleted, false));
    }

    public boolean checkNameExist(String name, Long productId) {
        if (StrUtil.isBlank(name) || productId == null) {
            return false;
        }
        return this.baseMapper.exists(new LambdaQueryWrapper<Product>().eq(Product::getName, name).eq(AbstractBaseEntity::getDeleted, false).ne(Product::getId, productId));
    }
}