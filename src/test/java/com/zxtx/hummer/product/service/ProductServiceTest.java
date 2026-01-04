package com.zxtx.hummer.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.product.dao.ProductMapper;
import com.zxtx.hummer.product.domain.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends HummerApplicationTest {
    @Autowired
    ProductService service;
    @Autowired
    ProductMapper dao;

    @Test
    public void test() {
        service.list();
        dao.selectList(new QueryWrapper<>());
    }

    @Test
    public void add() {
        Product bean = Product.builder()
                .id(SnowflakeIdWorker.nextID())
                .name("iPhone 15")
                .build();
        service.save(bean);
    }
}