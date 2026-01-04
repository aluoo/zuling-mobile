package com.zxtx.hummer.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.product.dao.BrandMapper;
import com.zxtx.hummer.product.dao.CategoryMapper;
import com.zxtx.hummer.product.domain.Brand;
import com.zxtx.hummer.product.domain.Category;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import com.zxtx.hummer.product.domain.request.CategoryOperatorReq;
import com.zxtx.hummer.product.domain.response.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
    @Autowired
    private BrandMapper brandMapper;

    public List<CategoryVO> listType(Integer level, Long parentId) {
        List<Category> list = this.lambdaQuery()
                .eq(level != null, Category::getLevel, level)
                .eq(parentId != null, Category::getParentId, parentId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Category::getSort)
                .orderByAsc(Category::getId)
                .list();
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(list, CategoryVO.class);
    }

    public CategoryVO detailById(Long categoryId) {
        Category bean = this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Category::getId, categoryId)
                .one();
        if (bean == null) {
            throw new BaseException(-1, "分类不存在");
        }
        CategoryVO vo = BeanUtil.copyProperties(bean, CategoryVO.class);
        if (!bean.getParentId().equals(TreeConstants.TOP_PARENT)) {
            Category parent = this.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Category::getId, bean.getParentId())
                    .one();
            vo.setParentName(Optional.ofNullable(parent).map(Category::getName).orElse(null));
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(CategoryOperatorReq req) {
        // parentId，默认不填为顶层
        // 通过parentId找出ancestors
        Category category = Category.builder()
                .id(SnowflakeIdWorker.nextID())
                .build();
        BeanUtil.copyProperties(req, category,  CopyOptions.create().ignoreNullValue());
        if (category.getParentId() == null) {
            category.setParentId(TreeConstants.TOP_PARENT);
            category.setLevel(TreeConstants.TOP_LEVEL);
            category.setAncestors(category.getId().toString());
        }
        if (!category.getParentId().equals(TreeConstants.TOP_PARENT)) {
            Category parent = this.getById(category.getParentId());
            if (parent == null) {
                throw new BaseException(-1, "上级分类不存在");
            }
            String ancestors = parent.getAncestors();
            ancestors = StrUtil.format("{},{}", ancestors, category.getId().toString());
            category.setAncestors(ancestors);
            category.setLevel(parent.getLevel() + 1);
        }
        this.save(category);

        initBrand(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(CategoryOperatorReq req) {
        Category category = BeanUtil.copyProperties(req, Category.class);
        this.updateById(category);
        initBrand(category);
    }

    private void initBrand(Category category) {
        if (!category.getParentId().equals(TreeConstants.TOP_PARENT)) {
            return;
        }
        boolean exist = brandMapper.exists(new LambdaQueryWrapper<Brand>().eq(Brand::getName, category.getName()).eq(AbstractBaseEntity::getDeleted, false));
        if (exist) {
            return;
        }
        Brand brand = Brand.builder()
                .id(SnowflakeIdWorker.nextID())
                .name(category.getName())
                .build();
        brandMapper.insert(brand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long categoryId) {
        boolean success = this.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(Category::getId, categoryId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Category());
        if (success) {
            String ancestors = StrUtil.format("{},", categoryId);
            this.lambdaUpdate()
                    .set(AbstractBaseEntity::getDeleted, true)
                    .likeRight(Category::getAncestors, ancestors)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new Category());
        }
    }

    public String getCategoryFullNameByChildId(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = getCategory(categoryId);
        if (category == null) {
            return null;
        }
        List<String> ancestors = StrUtil.split(category.getAncestors(), ",");
        if (CollUtil.isEmpty(ancestors)) {
            return category.getName();
        }
        List<Category> list = this.list(new LambdaQueryWrapper<Category>().eq(AbstractBaseEntity::getDeleted, false).in(Category::getId, ancestors).orderByAsc(Category::getLevel));
        return list.stream().map(Category::getName).collect(Collectors.joining("/"));
    }

    public Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return getExistById(categoryId);
    }

    public List<Tree<Long>> buildCategoryTree() {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<Category> list = this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Category::getLevel)
                .orderByDesc(Category::getSort)
                .list();
        return TreeUtil.build(list, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("parentId", treeNode.getParentId());
            tree.putExtra("level", treeNode.getLevel());
        });
    }

    private TreeNodeConfig getTreeConfig() {
        // build tree
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        return treeNodeConfig;
    }

    private Category getExistById(Long categoryId) {
        return this.getOne(new LambdaQueryWrapper<Category>().eq(Category::getId, categoryId).eq(AbstractBaseEntity::getDeleted, false));
    }
}