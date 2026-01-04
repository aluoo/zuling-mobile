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
import com.zxtx.hummer.product.dao.OptionMapper;
import com.zxtx.hummer.product.domain.Option;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import com.zxtx.hummer.product.domain.dto.OptionDTO;
import com.zxtx.hummer.product.domain.request.OptionOperatorReq;
import com.zxtx.hummer.product.domain.response.OptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OptionService extends ServiceImpl<OptionMapper, Option> {

    public List<Tree<Long>> buildOptionTree() {
        return buildOptionTreeBase(null);
    }

    public List<Tree<Long>> buildOptionTree(List<Long> optionIds) {
        return buildOptionTreeBase(optionIds);
    }

    public OptionVO detailById(Long optionId) {
        Option bean = this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Option::getId, optionId)
                .one();
        if (bean == null) {
            throw new BaseException(-1, "选项不存在");
        }
        OptionVO vo = BeanUtil.copyProperties(bean, OptionVO.class);
        if (!bean.getParentId().equals(TreeConstants.TOP_PARENT)) {
            Option parent = this.lambdaQuery()
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .eq(Option::getId, vo.getParentId())
                    .one();
            vo.setParentName(Optional.ofNullable(parent).map(Option::getName).orElse(null));
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(OptionOperatorReq req) {
        Option option = Option.builder()
                .id(SnowflakeIdWorker.nextID())
                .build();
        BeanUtil.copyProperties(req, option, CopyOptions.create().ignoreNullValue());
        if (option.getParentId() == null) {
            option.setParentId(TreeConstants.TOP_PARENT);
            option.setLevel(TreeConstants.TOP_LEVEL);
            option.setAncestors(option.getId().toString());
            option.setCode(req.getCode());
        }
        if (!option.getParentId().equals(TreeConstants.TOP_PARENT)) {
            Option parent = this.getById(option.getParentId());
            if (parent == null) {
                throw new BaseException(-1, "上级选项不存在");
            }
            String ancestors = parent.getAncestors();
            ancestors = StrUtil.format("{},{}", ancestors, option.getId().toString());
            option.setAncestors(ancestors);
            option.setLevel(parent.getLevel() + 1);
            option.setCode(req.getCode());
        }
        this.save(option);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(OptionOperatorReq req) {
        Option option = BeanUtil.copyProperties(req, Option.class);
        this.updateById(option);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long optionId) {
        boolean success = this.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(Option::getId, optionId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new Option());
        if (success) {
            String ancestors = StrUtil.format("{},", optionId);
            this.lambdaUpdate()
                    .set(AbstractBaseEntity::getDeleted, true)
                    .likeRight(Option::getAncestors, ancestors)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .update(new Option());
        }
    }

    private List<Tree<Long>> buildOptionTreeBase(List<Long> optionIds) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<OptionDTO> options = listAllAvailableOption();
        boolean hasOption = CollUtil.isNotEmpty(optionIds);
        return TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("description", treeNode.getDescription());
            //tree.putExtra("ancestors", treeNode.getAncestors());
            if (hasOption) {
                tree.putExtra("checked", optionIds.contains(treeNode.getId()));
            } else {
                tree.putExtra("checked", false);
            }
        });
    }

    private List<OptionDTO> listAllAvailableOption() {
        LambdaQueryWrapper<Option> qw = new LambdaQueryWrapper<Option>()
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByAsc(Option::getLevel)
                .orderByDesc(Option::getSort);
        return BeanUtil.copyToList(this.list(qw), OptionDTO.class);
    }

    private TreeNodeConfig getTreeConfig() {
        // build tree
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        return treeNodeConfig;
    }
}