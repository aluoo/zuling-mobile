package com.zxtx.hummer.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.product.dao.BrandMapper;
import com.zxtx.hummer.product.domain.Brand;
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
public class BrandService extends ServiceImpl<BrandMapper, Brand> {

    public Brand getBrand(Long brandId) {
        if (brandId == null) {
            return null;
        }
        return getExistById(brandId);
    }

    private Brand getExistById(Long brandId) {
        return this.getOne(new LambdaQueryWrapper<Brand>().eq(Brand::getId, brandId).eq(AbstractBaseEntity::getDeleted, false));
    }
}