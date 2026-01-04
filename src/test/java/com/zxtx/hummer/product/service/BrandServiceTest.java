package com.zxtx.hummer.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.product.dao.BrandMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.*;

@ActiveProfiles("test")
public class BrandServiceTest extends HummerApplicationTest {
    @Autowired
    private BrandService service;
    @Autowired
    private BrandMapper dao;

    @Test
    public void test() {
        service.list();
        dao.selectList(new QueryWrapper<>());
    }
}