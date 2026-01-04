package com.zxtx.hummer.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.mbr.domain.MbrRentalProductSku;
import com.zxtx.hummer.mbr.service.MbrRentalProductSkuService;
import com.zxtx.hummer.product.domain.*;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import com.zxtx.hummer.product.domain.request.*;
import com.zxtx.hummer.product.domain.response.CategoryVO;
import com.zxtx.hummer.product.domain.response.ProductSkuVO;
import com.zxtx.hummer.product.domain.response.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class ProductManageService {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductOptionService productOptionService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private MbrRentalProductSkuService rentalProductSkuService;

    public PageUtils<ProductSkuVO> listRentalSku(ProductQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<MbrRentalProductSku> list  = new ArrayList<>();
        if (StrUtil.isNotBlank(req.getName())) {
            PageHelper.clearPage();
            List<Product> products = productService.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .like(Product::getName, req.getName())
                    .orderByAsc(Product::getSort)
                    .list();
            if (CollUtil.isNotEmpty(products)) {
                page = PageHelper.startPage(req.getPage(), req.getPageSize());
                list = rentalProductSkuService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .in(MbrRentalProductSku::getProductId, products.stream().map(Product::getId).collect(Collectors.toSet()))
                        .orderByAsc(MbrRentalProductSku::getBrandId)
                        .orderByAsc(MbrRentalProductSku::getCategoryId)
                        .orderByAsc(MbrRentalProductSku::getSort)
                        .orderByDesc(AbstractBaseEntity::getCreateTime)
                        .orderByDesc(AbstractBaseEntity::getUpdateTime)
                        .list();
            }
        } else {
            list = rentalProductSkuService.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .orderByAsc(MbrRentalProductSku::getBrandId)
                    .orderByAsc(MbrRentalProductSku::getCategoryId)
                    .orderByAsc(MbrRentalProductSku::getSort)
                    .orderByDesc(AbstractBaseEntity::getCreateTime)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        }
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        Set<Long> productIds = list.stream().map(MbrRentalProductSku::getProductId).collect(Collectors.toSet());
        Set<Long> categoryIds = list.stream().map(MbrRentalProductSku::getCategoryId).collect(Collectors.toSet());
        Set<Long> brandIds = list.stream().map(MbrRentalProductSku::getBrandId).collect(Collectors.toSet());
        List<Product> products = productService.lambdaQuery().in(Product::getId, productIds).list();
        List<Category> categories = categoryService.list(new LambdaQueryWrapper<Category>().eq(AbstractBaseEntity::getDeleted, false).in(Category::getId, categoryIds));
        List<Brand> brands = brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false).in(Brand::getId, brandIds));
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));
        Map<Long, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        List<ProductSkuVO> resp = BeanUtil.copyToList(list, ProductSkuVO.class);

        resp.forEach(vo -> {
            Optional<Category> category = Optional.ofNullable(categoryMap.get(vo.getCategoryId()));
            Optional<Brand> brand = Optional.ofNullable(brandMap.get(vo.getBrandId()));
            Optional<Product> product = Optional.ofNullable(productMap.get(vo.getProductId()));
            vo.setCategoryName(category.map(Category::getName).orElse(null));
            vo.setBrandName(brand.map(Brand::getName).orElse(null));
            vo.setName(product.map(Product::getName).orElse(null));
            vo.setCategoryFullName(product.map(Product::getCategoryFullName).orElse(null));
            vo.setRetailPriceStr();
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    public PageUtils<ProductSkuVO> listSku(ProductQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<ProductSku> list  = new ArrayList<>();
        if (StrUtil.isNotBlank(req.getName())) {
            PageHelper.clearPage();
            List<Product> products = productService.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .like(Product::getName, req.getName())
                    .orderByAsc(Product::getSort)
                    .list();
            if (CollUtil.isNotEmpty(products)) {
                page = PageHelper.startPage(req.getPage(), req.getPageSize());
                list = productSkuService.lambdaQuery()
                        .eq(AbstractBaseEntity::getDeleted, false)
                        .in(ProductSku::getProductId, products.stream().map(Product::getId).collect(Collectors.toSet()))
                        .orderByAsc(ProductSku::getBrandId)
                        .orderByAsc(ProductSku::getCategoryId)
                        .orderByAsc(ProductSku::getSort)
                        .orderByDesc(AbstractBaseEntity::getCreateTime)
                        .orderByDesc(AbstractBaseEntity::getUpdateTime)
                        .list();
            }
        } else {
            list = productSkuService.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .orderByAsc(ProductSku::getBrandId)
                    .orderByAsc(ProductSku::getCategoryId)
                    .orderByAsc(ProductSku::getSort)
                    .orderByDesc(AbstractBaseEntity::getCreateTime)
                    .orderByDesc(AbstractBaseEntity::getUpdateTime)
                    .list();
        }
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        Set<Long> productIds = list.stream().map(ProductSku::getProductId).collect(Collectors.toSet());
        Set<Long> categoryIds = list.stream().map(ProductSku::getCategoryId).collect(Collectors.toSet());
        Set<Long> brandIds = list.stream().map(ProductSku::getBrandId).collect(Collectors.toSet());
        List<Product> products = productService.lambdaQuery().in(Product::getId, productIds).list();
        List<Category> categories = categoryService.list(new LambdaQueryWrapper<Category>().eq(AbstractBaseEntity::getDeleted, false).in(Category::getId, categoryIds));
        List<Brand> brands = brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false).in(Brand::getId, brandIds));
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));
        Map<Long, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        List<ProductSkuVO> resp = BeanUtil.copyToList(list, ProductSkuVO.class);

        resp.forEach(vo -> {
            Optional<Category> category = Optional.ofNullable(categoryMap.get(vo.getCategoryId()));
            Optional<Brand> brand = Optional.ofNullable(brandMap.get(vo.getBrandId()));
            Optional<Product> product = Optional.ofNullable(productMap.get(vo.getProductId()));
            vo.setCategoryName(category.map(Category::getName).orElse(null));
            vo.setBrandName(brand.map(Brand::getName).orElse(null));
            vo.setName(product.map(Product::getName).orElse(null));
            vo.setCategoryFullName(product.map(Product::getCategoryFullName).orElse(null));
            vo.setRetailPriceStr();
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    public PageUtils<ProductVO> listProduct(ProductQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        LambdaQueryWrapper<Product> qw = new LambdaQueryWrapper<Product>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(req.getId() != null, Product::getId, req.getId())
                .eq(req.getCategoryId() != null, Product::getCategoryId, req.getCategoryId())
                .eq(req.getActivated() != null, Product::getActivated, req.getActivated())
                .eq(req.getDigitalInsuranceAble() != null, Product::getDigitalInsuranceAble, req.getDigitalInsuranceAble())
                .eq(req.getMobileRentalAble() != null, Product::getMobileRentalAble, req.getMobileRentalAble())
                .like(StrUtil.isNotBlank(req.getName()), Product::getName, req.getName())
                .orderByDesc(Product::getActivated)
                .orderByAsc(Product::getSort)
                .orderByDesc(AbstractBaseEntity::getCreateTime);
                // .orderByDesc(AbstractBaseEntity::getUpdateTime);
        List<Product> list = productService.list(qw);
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<Long> categoryIds = list.stream().map(Product::getCategoryId).collect(Collectors.toList());
        List<Long> brandIds = list.stream().map(Product::getBrandId).collect(Collectors.toList());
        List<Category> categories = categoryService.list(new LambdaQueryWrapper<Category>().eq(AbstractBaseEntity::getDeleted, false).in(Category::getId, categoryIds));
        List<Brand> brands = brandService.list(new LambdaQueryWrapper<Brand>().eq(AbstractBaseEntity::getDeleted, false).in(Brand::getId, brandIds));
        Map<Long, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Function.identity()));
        Map<Long, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        List<ProductVO> resp = BeanUtil.copyToList(list, ProductVO.class);
        resp.forEach(vo -> {
            Optional<Category> category = Optional.ofNullable(categoryMap.get(vo.getCategoryId()));
            Optional<Brand> brand = Optional.ofNullable(brandMap.get(vo.getBrandId()));
            vo.setCategoryName(category.map(Category::getName).orElse(null));
            vo.setBrandName(brand.map(Brand::getName).orElse(null));
        });
        return new PageUtils<>(resp, page.getTotal());
        //return PageUtils.createPage(resp, (int) page.getTotal());
    }

    public ProductVO detailById(Long productId) {
        Product bean = productService.getOne(new LambdaQueryWrapper<Product>().eq(AbstractBaseEntity::getDeleted, false).eq(Product::getId, productId));
        if (bean == null) {
            throw new BaseException(-1, "商品不存在");
        }
        ProductVO vo = BeanUtil.copyProperties(bean, ProductVO.class);
        Category category = categoryService.getCategory(vo.getCategoryId());
        vo.setCategoryName(Optional.ofNullable(category).map(Category::getName).orElse(null));
        //todo optimize
        if (category != null) {
            List<String> categoryIds = StrUtil.split(category.getAncestors(), TreeConstants.SPLIT_CHAR);
            List<Category> categoryList = categoryService.lambdaQuery().in(Category::getId, categoryIds).eq(AbstractBaseEntity::getDeleted, false).orderByAsc(Category::getLevel).list();
            List<CategoryVO> categoryVOList = BeanUtil.copyToList(categoryList, CategoryVO.class);
            vo.setCategories(categoryVOList);
        }

        Brand brand = brandService.getBrand(vo.getBrandId());
        vo.setBrandName(Optional.ofNullable(brand).map(Brand::getName).orElse(null));

        List<Long> optionIds = productOptionService.getOptionIdsByProductId(productId);
        vo.setOptionIds(optionIds);

        // 构建选择树，然后赋值商品选中信息
        List<Tree<Long>> trees = optionService.buildOptionTree(optionIds);
        vo.setOptions(trees);

        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(ProductOperatorReq req) {
        // check name
        checkNameExist(productService.checkNameExist(req.getName()));
        // category info
        String categoryFullName = categoryService.getCategoryFullNameByChildId(req.getCategoryId());
        // product info
        Product product = Product.builder()
                .id(SnowflakeIdWorker.nextID())
                .categoryFullName(categoryFullName)
                .build();
        BeanUtil.copyProperties(req, product, CopyOptions.create().ignoreNullValue());
        Brand brand = getBrandByCategory(req.getCategoryId());
        product.setBrandId(brand.getId());
        productService.save(product);
        // option relation info
        List<Long> optionIds = req.getOptionIds();
        if (CollUtil.isEmpty(optionIds)) {
            // 添加时如果没有传入任何选项，则默认生成所有选项关联
            optionIds = optionService.list(new LambdaQueryWrapper<Option>()
                            .select(Option::getId)
                            .eq(AbstractBaseEntity::getDeleted, false))
                    .stream()
                    .map(Option::getId)
                    .collect(Collectors.toList());
        }
        productOptionService.addRelation(optionIds, product.getId());
    }

    private Brand getBrandByCategory(Long categoryId) {
        // 根据分类Id找出顶级分类ID，然后根据顶级分类名称找出brand，设置brandId
        Category category = categoryService.getCategory(categoryId);
        List<String> ancestors = Arrays.stream(category.getAncestors().split(",")).collect(Collectors.toList());
        Long topCategoryId = Long.valueOf(ancestors.get(0));
        Category topCategory = categoryService.getCategory(topCategoryId);
        return brandService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Brand::getName, topCategory.getName())
                .one();
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(ProductOperatorReq req) {
        // check name
        checkNameExist(productService.checkNameExist(req.getName(), req.getId()));
        // category info
        String categoryFullName = categoryService.getCategoryFullNameByChildId(req.getCategoryId());
        Product product = BeanUtil.copyProperties(req, Product.class);
        product.setCategoryFullName(categoryFullName);
        Brand brand = getBrandByCategory(req.getCategoryId());
        product.setBrandId(brand.getId());
        productService.updateById(product);
        productOptionService.addRelation(req.getOptionIds(), product.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void addSku(ProductSkuAddReq req) {
        Product product = Optional.ofNullable(productService.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "商品不存在"));
        boolean exists = productSkuService.lambdaQuery()
                .eq(ProductSku::getSpec, req.getSpec())
                .eq(ProductSku::getProductId, product.getId())
                .eq(AbstractBaseEntity::getDeleted, false).exists();
        if (exists) {
            throw new BaseException(-1, "规格已存在");
        }
        ProductSku sku = ProductSku.builder()
                .id(SnowflakeIdWorker.nextID())
                .spec(req.getSpec())
                .productId(product.getId())
                .categoryId(product.getCategoryId())
                .brandId(product.getBrandId())
                .retailPrice(StringUtils.yuanToFen(req.getRetailPrice()))
                .build();
        productSkuService.save(sku);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRentalSku(ProductSkuAddReq req) {
        Product product = Optional.ofNullable(productService.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "商品不存在"));
        boolean exists = rentalProductSkuService.lambdaQuery()
                .eq(MbrRentalProductSku::getSpec, req.getSpec())
                .eq(MbrRentalProductSku::getProductId, product.getId())
                .eq(AbstractBaseEntity::getDeleted, false).exists();
        if (exists) {
            throw new BaseException(-1, "规格已存在");
        }
        MbrRentalProductSku sku = MbrRentalProductSku.builder()
                .id(SnowflakeIdWorker.nextID())
                .spec(req.getSpec())
                .productId(product.getId())
                .categoryId(product.getCategoryId())
                .brandId(product.getBrandId())
                .retailPrice(StringUtils.yuanToFen(req.getRetailPrice())) // todo
                .build();
        rentalProductSkuService.save(sku);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addSkuBatch(ProductSkuAddBatchReq req) {
        Product product = Optional.ofNullable(productService.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "商品不存在"));
        List<ProductSkuAddBatchReq.Sku> skus = req.getSkus();
        StringBuilder msg = StrUtil.builder();
        boolean flag = false;
        for (ProductSkuAddBatchReq.Sku sku : skus) {
            boolean exists = productSkuService.lambdaQuery()
                    .eq(ProductSku::getSpec, sku.getSpec())
                    .eq(ProductSku::getProductId, product.getId())
                    .eq(AbstractBaseEntity::getDeleted, false).exists();
            msg.append(sku.getSpec()).append(" ");
            if (exists) {
                flag = true;
            }
        }
        if (flag) {
            throw new BaseException(-1, StrUtil.format("{}规格已存在", msg));
        }
        List<ProductSku> skuList = new ArrayList<>();
        for (ProductSkuAddBatchReq.Sku o : skus) {
            ProductSku sku = ProductSku.builder()
                    .id(SnowflakeIdWorker.nextID())
                    .spec(o.getSpec())
                    .productId(product.getId())
                    .categoryId(product.getCategoryId())
                    .brandId(product.getBrandId())
                    .retailPrice(StringUtils.yuanToFen(o.getRetailPrice()))
                    .build();
            skuList.add(sku);
        }
        productSkuService.saveBatch(skuList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addRentalSkuBatch(ProductSkuAddBatchReq req) {
        Product product = Optional.ofNullable(productService.getById(req.getId())).orElseThrow(() -> new BaseException(-1, "商品不存在"));
        List<ProductSkuAddBatchReq.Sku> skus = req.getSkus();
        StringBuilder msg = StrUtil.builder();
        boolean flag = false;
        for (ProductSkuAddBatchReq.Sku sku : skus) {
            boolean exists = rentalProductSkuService.lambdaQuery()
                    .eq(MbrRentalProductSku::getSpec, sku.getSpec())
                    .eq(MbrRentalProductSku::getProductId, product.getId())
                    .eq(AbstractBaseEntity::getDeleted, false).exists();
            msg.append(sku.getSpec()).append(" ");
            if (exists) {
                flag = true;
            }
        }
        if (flag) {
            throw new BaseException(-1, StrUtil.format("{}规格已存在", msg));
        }
        List<MbrRentalProductSku> skuList = new ArrayList<>();
        for (ProductSkuAddBatchReq.Sku o : skus) {
            MbrRentalProductSku sku = MbrRentalProductSku.builder()
                    .id(SnowflakeIdWorker.nextID())
                    .spec(o.getSpec())
                    .productId(product.getId())
                    .categoryId(product.getCategoryId())
                    .brandId(product.getBrandId())
                    .retailPrice(StringUtils.yuanToFen(o.getRetailPrice())) // todo
                    .build();
            skuList.add(sku);
        }
        rentalProductSkuService.saveBatch(skuList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editSku(ProductSkuOperatorReq req) {
        productSkuService.lambdaUpdate()
                .set(ProductSku::getRetailPrice, StringUtils.yuanToFen(req.getRetailPrice()))
                .eq(ProductSku::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ProductSku());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editRentalSku(ProductSkuOperatorReq req) {
        rentalProductSkuService.lambdaUpdate()
                .set(MbrRentalProductSku::getRetailPrice, StringUtils.yuanToFen(req.getRetailPrice()))
                .eq(MbrRentalProductSku::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new MbrRentalProductSku());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long productId) {
        productService.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(Product::getId, productId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Product());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSku(Long skuId) {
        productSkuService.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(ProductSku::getId, skuId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ProductSku());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRentalSku(Long skuId) {
        rentalProductSkuService.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(MbrRentalProductSku::getId, skuId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new MbrRentalProductSku());
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchProductActivatedStatus(ProductOperatorReq req) {
        productService.lambdaUpdate()
                .set(Product::getActivated, req.getActivated())
                .eq(Product::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Product());
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchProductInsuranceStatus(ProductOperatorReq req) {
        productService.lambdaUpdate()
                .set(Product::getDigitalInsuranceAble, req.getActivated())
                .eq(Product::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Product());
    }

    @Transactional(rollbackFor = Exception.class)
    public void switchProductRentalStatus(ProductOperatorReq req) {
        productService.lambdaUpdate()
                .set(Product::getMobileRentalAble, req.getActivated())
                .eq(Product::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Product());
    }

    private void checkNameExist(boolean exist) {
        if (exist) {
            throw new BaseException(-1, "商品名称已存在");
        }
    }
}