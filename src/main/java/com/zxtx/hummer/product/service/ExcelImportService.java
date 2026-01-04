package com.zxtx.hummer.product.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.product.domain.Brand;
import com.zxtx.hummer.product.domain.Category;
import com.zxtx.hummer.product.domain.Product;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import com.zxtx.hummer.product.domain.dto.ProductImportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
@Deprecated
public class ExcelImportService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    //todo
    @Transactional(rollbackFor = Exception.class)
    public <E> void importCategoryInfoFromExcel(E file) {
        // import file
        // key=品牌 value=系列集合
        // Map<String, List<String>> collect;
        // List<String> brands品牌名称列表
        // List<Brand>
        // 根据名称模糊搜索出存在的品牌列表，存在的不添加，不存在的添加
        Map<String, List<String>> collect = (Map<String, List<String>>) file;
        List<String> brandNames = collect.keySet().stream().collect(Collectors.toList());
        List<Brand> existBrands = brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false));
        Map<String, Brand> existBrandsMap = existBrands.stream().collect(Collectors.toMap(Brand::getName, Function.identity()));
        List<Brand> newBrands = new ArrayList<>();
        brandNames.forEach(bn -> {
            Brand brand = existBrandsMap.get(bn);
            if (brand == null) {
                brand = Brand.builder()
                        .name(bn)
                        .build();
                newBrands.add(brand);
            }
        });

        if (CollUtil.isNotEmpty(newBrands)) {
            brandService.saveBatch(newBrands);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public <E> void importBrandsFromExcel(E file) {
        List<String> fileBrands = (List<String>) file;
        List<Brand> existBrands = brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false));
        Map<String, Brand> existBrandsMap = existBrands.stream().collect(Collectors.toMap(Brand::getName, Function.identity()));
        List<Brand> newBrands = new ArrayList<>();
        fileBrands.forEach(bn -> {
            Brand brand = existBrandsMap.get(bn);
            if (brand == null) {
                brand = Brand.builder()
                        .name(bn)
                        .build();
                newBrands.add(brand);
            }
        });

        if (CollUtil.isNotEmpty(newBrands)) {
            brandService.saveBatch(newBrands);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <E> void importCategoryFromExcel(E file) {
        Map<String, Set<String>> collect = (Map<String, Set<String>>) file;
        List<Category> categories = listAllCategory();
        Map<String, Category> existCategoryMap = buildCategoryMap(categories);
        // first | second list
        List<Category> firstC = new ArrayList<>();
        List<Category> secondC = new ArrayList<>();

        collect.keySet().forEach(fn -> {
            Set<String> seconds = collect.get(fn);
            Category category = existCategoryMap.get(fn);
            Long id;
            if (category == null) {
                id = SnowflakeIdWorker.nextID();
                category = Category.builder()
                        .id(id)
                        .name(fn)
                        .parentId(TreeConstants.TOP_PARENT)
                        .level(TreeConstants.TOP_LEVEL)
                        .ancestors(id.toString())
                        .build();
                firstC.add(category);
            } else {
                id = category.getId();
            }
            for (String second : seconds) {
                Category secondCategory = existCategoryMap.get(second);
                if (secondCategory != null && secondCategory.getParentId().equals(id)) {
                    continue;
                }
                if (secondCategory == null) {
                    Long secondId = SnowflakeIdWorker.nextID();
                    secondCategory = Category.builder()
                            .id(secondId)
                            .name(StrUtil.isNotBlank(second) ? second : "其它系列")
                            .parentId(id)
                            .level(TreeConstants.TOP_LEVEL + 1)
                            .ancestors(id.toString() + "," + secondId)
                            .build();
                    secondC.add(secondCategory);
                }
            }
        });

        List<Category> newCategories = new ArrayList<>();
        newCategories.addAll(firstC);
        newCategories.addAll(secondC);
        if (CollUtil.isNotEmpty(newCategories)) {
            categoryService.saveBatch(newCategories);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void importProduct(List<ProductImportDTO> list) {
        List<Category> allCategory = listAllCategory();
        Map<String, Category> stringCategoryMap = buildCategoryMap(allCategory);
        Map<String, Category> fullCategoryNameMap = new HashMap<>(16);
        for (Category category : allCategory) {
            String fullName = categoryService.getCategoryFullNameByChildId(category.getId());
            fullCategoryNameMap.putIfAbsent(fullName, category);
        }
        List<Brand> allBrand = listAllBrand();
        Map<String, Brand> stringBrandMap = buildBrandMap(allBrand);
        List<Product> products = listAllProduct();
        Map<String, Product> existProductMap = buildProductMap(products);
        for (ProductImportDTO dto : list) {
            if (StrUtil.isBlank(dto.getCategory())) {
                dto.setCategory("其它系列");
            }
            if (StrUtil.isBlank(dto.getProduct())) {
                dto.setProduct(dto.getCategory());
            }
            Brand brand = stringBrandMap.get(dto.getBrand());
            String firstCategoryName = dto.getBrand();
            String categoryName = dto.getCategory();
            String fullName = firstCategoryName + "/" + categoryName;
            Category firstCategory = stringCategoryMap.get(firstCategoryName);
            //Category category = stringCategoryMap.get(categoryName);
            Category category = fullCategoryNameMap.get(fullName);
            dto.setFirstCategoryId(Optional.ofNullable(firstCategory).map(Category::getId).orElse(null));
            dto.setSecondCategoryId(Optional.ofNullable(category).map(Category::getId).orElse(null));
            dto.setBrandId(Optional.ofNullable(brand).map(Brand::getId).orElse(null));
        }
        int notExistCategory = 0;
        int existProduct = 0;
        List<Product> newProducts = new ArrayList<>();
        for (ProductImportDTO dto : list) {
            if (dto.getSecondCategoryId() == null) {
                notExistCategory++;
                continue;
            }
            Product product = existProductMap.get(dto.getProduct());
            if (product != null) {
                existProduct++;
                continue;
            }
            Product p = Product.builder()
                    .id(SnowflakeIdWorker.nextID())
                    .name(dto.getProduct())
                    .categoryId(dto.getSecondCategoryId())
                    .categoryFullName(categoryService.getCategoryFullNameByChildId(dto.getSecondCategoryId()))
                    .brandId(dto.getBrandId())
                    .build();
            newProducts.add(p);
        }
        if (CollUtil.isNotEmpty(newProducts)) {
            productService.saveBatch(newProducts);
        }
        log.info("notExistCategory:{}", notExistCategory);
        log.info("existProduct:{}", existProduct);

    }

    private List<Brand> listAllBrand() {
        return brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false));
    }

    private List<Category> listAllCategory() {
        return categoryService.list(new LambdaQueryWrapper<Category>().eq(AbstractBaseEntity::getDeleted, false));
    }

    private List<Category> listAllSecondCategory() {
        return categoryService.list(new LambdaQueryWrapper<Category>().eq(Category::getLevel, TreeConstants.TOP_LEVEL+1).eq(AbstractBaseEntity::getDeleted, false));
    }

    private List<Product> listAllProduct() {
        return productService.list(new LambdaQueryWrapper<Product>().eq(AbstractBaseEntity::getDeleted, false));
    }

    private Map<String, Brand> buildBrandMap(List<Brand> list) {
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>(1);
        }
        return list.stream().collect(Collectors.toMap(Brand::getName, Function.identity(), (oldBrand, newBrand) -> oldBrand));
    }

    private Map<String, Category> buildCategoryMap(List<Category> list) {
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>(1);
        }
        return list.stream().collect(Collectors.toMap(Category::getName, Function.identity(), (oldCategory, newCategory) -> oldCategory));
    }

    private Map<String, Product> buildProductMap(List<Product> list) {
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>(1);
        }
        return list.stream().collect(Collectors.toMap(Product::getName, Function.identity(), (oldProduct, newProduct) -> oldProduct));
    }
}