package com.zxtx.hummer.insurance.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.insurance.dao.mapper.DiOptionMapper;
import com.zxtx.hummer.insurance.domain.DiOption;
import com.zxtx.hummer.insurance.req.DiOptionDTO;
import com.zxtx.hummer.insurance.req.DiOptionOperatorReq;
import com.zxtx.hummer.insurance.vo.DiOptionVO;
import com.zxtx.hummer.product.domain.constant.TreeConstants;
import com.zxtx.hummer.product.domain.request.OptionOperatorReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数保产品选项表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiOptionService extends ServiceImpl<DiOptionMapper, DiOption>  {

    public List<DiOption> listByCode(String code) {
        return StrUtil.isBlank(code)
                ? new ArrayList<>()
                : this.lambdaQuery()
                .eq(DiOption::getCode, code)
                .orderByAsc(DiOption::getSort)
                .list();
    }

    public Map<Long, DiOption> getOptionsMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DiOption> list = this.lambdaQuery().in(DiOption::getId, ids).list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(DiOption::getId, Function.identity()));
    }

    public List<Tree<Long>> buildOptionTree() {
        return buildOptionTreeBase(null);
    }

    public List<Tree<Long>> buildOptionTree(List<Long> optionIds) {
        return buildOptionTreeBase(optionIds);
    }

    public DiOptionVO detailById(Long optionId) {
        DiOption bean = this.lambdaQuery()
                .eq(DiOption::getDeleted, false)
                .eq(DiOption::getId, optionId)
                .one();
        if (bean == null) {
            throw new BaseException(-1, "选项不存在");
        }
        DiOptionVO vo = BeanUtil.copyProperties(bean, DiOptionVO.class);
        if (!bean.getParentId().equals(TreeConstants.TOP_PARENT)) {
            DiOption parent = this.lambdaQuery()
                    .eq(DiOption::getDeleted, false)
                    .eq(DiOption::getId, vo.getParentId())
                    .one();
            vo.setParentName(Optional.ofNullable(parent).map(DiOption::getName).orElse(null));
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(DiOptionOperatorReq req) {
        DiOption option = DiOption.builder()
                .id(SnowflakeIdWorker.nextID())
                .build();
        BeanUtil.copyProperties(req, option, CopyOptions.create().ignoreNullValue());
        if (option.getParentId() == null) {
            option.setParentId(TreeConstants.TOP_PARENT);
            option.setLevel(TreeConstants.TOP_LEVEL);
            option.setAncestors(option.getId().toString());
        }
        if (!option.getParentId().equals(TreeConstants.TOP_PARENT)) {
            DiOption parent = this.getById(option.getParentId());
            if (parent == null) {
                throw new BaseException(-1, "上级选项不存在");
            }
            String ancestors = parent.getAncestors();
            ancestors = StrUtil.format("{},{}", ancestors, option.getId().toString());
            option.setAncestors(ancestors);
            option.setLevel(parent.getLevel() + 1);
        }
        this.save(option);
    }

    @Transactional(rollbackFor = Exception.class)
    public void edit(OptionOperatorReq req) {
        DiOption option = BeanUtil.copyProperties(req, DiOption.class);
        this.updateById(option);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long optionId) {
        DiOption option = this.getById(optionId);
        boolean success = this.lambdaUpdate()
                .set(DiOption::getDeleted, true)
                .eq(DiOption::getId, optionId)
                .eq(DiOption::getDeleted, false)
                .update(new DiOption());
        if (success) {
            this.lambdaUpdate()
                    .set(DiOption::getDeleted, true)
                    .likeRight(DiOption::getAncestors, option.getAncestors())
                    .eq(DiOption::getDeleted, false)
                    .update(new DiOption());
        }
    }

    private List<Tree<Long>> buildOptionTreeBase(List<Long> optionIds) {
        TreeNodeConfig treeNodeConfig = getTreeConfig();
        List<DiOptionDTO> options = listAllAvailableOption();
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

    private List<DiOptionDTO> listAllAvailableOption() {
        LambdaQueryWrapper<DiOption> qw = new LambdaQueryWrapper<DiOption>()
                .eq(DiOption::getDeleted, false)
                .orderByAsc(DiOption::getLevel)
                .orderByDesc(DiOption::getSort);
        return BeanUtil.copyToList(this.list(qw), DiOptionDTO.class);
    }

    private TreeNodeConfig getTreeConfig() {
        // build tree
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        return treeNodeConfig;
    }

}