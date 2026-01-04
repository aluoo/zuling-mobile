package com.zxtx.hummer.product.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.utils.ExcelImportUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
public class ExcelImportTest extends HummerApplicationTest {

    public static void main(String[] args) {
        String filePath = "D:\\samp.xlsx";
        try {
            InputStream is = FileUtil.getInputStream(filePath);
            List<SampleImportDTO> list = ExcelImportUtil.importFromExcel(is, SampleImportDTO.class);
            System.out.println(JSONUtil.toJsonStr(list));
            Map<String, List<String>> collect = list.stream()
                    .collect(Collectors.groupingBy(SampleImportDTO::getBrand, Collectors.mapping(SampleImportDTO::getCategory, Collectors.toList())));
            System.out.println(JSONUtil.toJsonStr(collect));
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
    }
}