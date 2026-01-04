package com.zxtx.hummer.product.domain.dto;

import com.zxtx.hummer.common.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/29
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Deprecated
public class ProductImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(name = "品牌")
    private String brand;
    @ExcelField(name = "系列")
    private String category;
    @ExcelField(name = "型号")
    private String product;

    private Long firstCategoryId;
    private Long secondCategoryId;
    private Long brandId;
}