package com.zxtx.hummer.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.product.dao.CategoryMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CategoryServiceTest extends HummerApplicationTest {
    @Autowired
    CategoryService service;
    @Autowired
    CategoryMapper dao;

    @Test
    public void test() {
        service.list();
        dao.selectList(new QueryWrapper<>());
    }

    @Test
    public void getCategoryFullPathNameByChildId() {
        String path = service.getCategoryFullNameByChildId(2L);
        System.out.println(path);
    }
}