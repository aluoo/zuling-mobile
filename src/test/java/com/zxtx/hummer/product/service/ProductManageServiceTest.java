package com.zxtx.hummer.product.service;

import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.product.domain.request.ProductOperatorReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class ProductManageServiceTest extends HummerApplicationTest {
    @Autowired
    ProductManageService service;

    @Test
    public void add() {
        ProductOperatorReq req = ProductOperatorReq.builder()
                .name("junit-test-all-option")
                .categoryId(3L)
                .build();

        service.add(req);
    }

    @Test
    public void edit() {
        ProductOperatorReq req = ProductOperatorReq.builder()
                .id(1200073295880110081L)
                .name("junit-test-pd-edit")
                .categoryId(2L)
                .optionIds(Arrays.asList(4L, 5L))
                .build();

        service.edit(req);
    }

    @Test
    public void deleteSkuTest() {
        service.deleteSku(1L);
    }
}