package com.zxtx.hummer.product.service;

import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.product.domain.Option;
import com.zxtx.hummer.product.domain.Product;
import com.zxtx.hummer.product.domain.ProductSku;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/4
 * @Copyright
 * @Version 1.0
 */
public class ProductSkuServiceTest extends HummerApplicationTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductSkuService skuService;

    @Test
    public void test() {
        List<Product> products = productService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
        List<Option> rams = optionService.lambdaQuery()
                .eq(Option::getCode, "RAM")
                .ne(Option::getName, "其他")
                .orderByAsc(Option::getSort)
                .list();
        List<Option> roms = optionService.lambdaQuery()
                .eq(Option::getCode, "ROM")
                .ne(Option::getName, "其他")
                .orderByAsc(Option::getSort)
                .list();
        List<ProductSku> skus = new ArrayList<>();
        products.forEach(p -> {
            String name = "{}+{}";
            rams.forEach(r -> {
                roms.forEach(ro -> {
                    ProductSku sku = ProductSku.builder()
                            .id(SnowflakeIdWorker.nextID())
                            .spec(StrUtil.format(name, r.getName(), ro.getName()))
                            .productId(p.getId())
                            .categoryId(p.getCategoryId())
                            .brandId(p.getBrandId())
                            .build();
                    skus.add(sku);
                });
            });
        });
        skuService.saveBatch(skus);
    }
}