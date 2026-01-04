package com.zxtx.hummer.product.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.utils.ExcelImportUtil;
import com.zxtx.hummer.product.domain.dto.ProductImportDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Ignore
public class ExcelImportServiceTest extends HummerApplicationTest {
    @Autowired
    private ExcelImportService service;

    @Test
    public void test() {
        String filePath = "D:\\samp.xlsx";
        try {
            InputStream is = FileUtil.getInputStream(filePath);
            List<SampleImportDTO> list = ExcelImportUtil.importFromExcel(is, SampleImportDTO.class);
            System.out.println(JSONUtil.toJsonStr(list));
            Map<String, List<String>> collect = list.stream()
                    .collect(Collectors.groupingBy(SampleImportDTO::getBrand, Collectors.mapping(SampleImportDTO::getCategory, Collectors.toList())));
            service.importCategoryInfoFromExcel(collect);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void importBrands() {
        String filePath = "D:\\brands-data.xlsx";
        try {
            InputStream is = FileUtil.getInputStream(filePath);
            List<SampleImportDTO> list = ExcelImportUtil.importFromExcel(is, SampleImportDTO.class);
            Map<String, Set<String>> collect = list.stream()
                    .collect(Collectors.groupingBy(SampleImportDTO::getBrand, Collectors.mapping(SampleImportDTO::getCategory, Collectors.toSet())));
            service.importCategoryFromExcel(collect);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void importProducts() {
        String filePath = "D:\\brands-data.xlsx";
        try {
            InputStream is = FileUtil.getInputStream(filePath);
            List<ProductImportDTO> list = ExcelImportUtil.importFromExcel(is, ProductImportDTO.class);

            service.importProduct(list);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SampleImportDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ExcelField(name = "品牌")
        private String brand;
        @ExcelField(name = "系列")
        private String category;
        @ExcelField(name = "型号")
        private String product;
    }
}