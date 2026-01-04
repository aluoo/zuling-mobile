package com.zxtx.hummer.product.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.utils.ExcelImportUtil;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.insurance.domain.DiSkuInsurance;
import com.zxtx.hummer.insurance.service.DiSkuInsuranceService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/21
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class SkuDownPriceImportTest extends HummerApplicationTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductSkuService skuService;
    @Autowired
    private DiSkuInsuranceService diSkuInsuranceService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BeanTest implements Serializable {
        private static final long serialVersionUID = 1L;

        @ExcelField(name = "productId")
        private String productId;
        @ExcelField(name = "brand")
        private String brand;
        @ExcelField(name = "productName")
        private String productName;
        @ExcelField(name = "insuranceId")
        private String insuranceId;
        @ExcelField(name = "insuranceName")
        private String insuranceName;
        @ExcelField(name = "insuranceType")
        private String insuranceType;
        @ExcelField(name = "insurancePeriod")
        private String insurancePeriod;
        @ExcelField(name = "insuranceDesc")
        private String insuranceDesc;
        @ExcelField(name = "skuId")
        private String skuId;
        @ExcelField(name = "skuSpec")
        private String skuSpec;
        @ExcelField(name = "skuRetailPrice")
        private String skuRetailPrice;
        @ExcelField(name = "originalDownPrice")
        private String originalDownPrice;
        @ExcelField(name = "downPrice")
        private String downPrice;
    }

    @Ignore
    @Test
    public void importSkuPrice() {
        InputStream is = FileUtil.getInputStream("D:/down-price-data.xlsx");
        try {
            List<BeanTest> list = ExcelImportUtil.importFromExcel(is, BeanTest.class);
            log.info("before size {}", list.size());
            List<DiSkuInsurance> data = new ArrayList<>();
            for (BeanTest obj : list) {
                Long skuId = Long.valueOf(obj.getSkuId());
                Long insuranceId = Long.valueOf(obj.getInsuranceId());
                if (StrUtil.isBlank(obj.getDownPrice())) {
                    log.info("skuId:{} has blank down price", skuId);
                    continue;
                }

                long downPrice = new BigDecimal(obj.getDownPrice()).multiply(new BigDecimal("100")).setScale(0).longValue();
                DiSkuInsurance diSkuInsurance = new DiSkuInsurance();
                diSkuInsurance.setId(SnowflakeIdWorker.nextID());
                diSkuInsurance.setSkuId(skuId);
                diSkuInsurance.setInsuranceId(insuranceId);
                diSkuInsurance.setDownPrice(downPrice);
                data.add(diSkuInsurance);
            }
            log.info("after size {}", data.size());
            diSkuInsuranceService.saveBatch(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}