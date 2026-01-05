package com.zxtx.hummer.products.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.products.dao.mapper.ZjProductTypeMapper;
import com.zxtx.hummer.products.domain.ZjProductType;
import com.zxtx.hummer.products.domain.request.ZjProductTypeOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeVO;
import com.zxtx.hummer.products.service.IZjProductTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品类型服务实现类
 */
@Service
public class ZjProductTypeServiceImpl extends ServiceImpl<ZjProductTypeMapper, ZjProductType> implements IZjProductTypeService {

    /**
     * 添加商品类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(ZjProductTypeOperatorReq req) {
        // 验证上级类目是否存在
        if (req.getUpid() != null && req.getUpid() > 0) {
            ZjProductType parent = this.getById(req.getUpid());
            if (parent == null || parent.getHidden() == false) {
                throw new BaseException(-1, "上级类目不存在或已隐藏");
            }
        }

        // 检查名称是否重复（同一上级下）
        boolean exists = this.lambdaQuery()
                .eq(ZjProductType::getTit, req.getTit())
                .eq(req.getUpid() != null, ZjProductType::getUpid, req.getUpid())
                .eq(ZjProductType::getHidden, 1)
                .exists();
        if (exists) {
            throw new BaseException(-1, "类型名称已存在");
        }

        ZjProductType productType = new ZjProductType();
        BeanUtil.copyProperties(req, productType);

        // 设置默认值
        if (productType.getUpid() == null) {
            productType.setUpid(0);
        }
        if (productType.getType() == null) {
            productType.setType(1);
        }
        if (productType.getSort() == null) {
            productType.setSort(0);
        }
        if (productType.getHidden() == null) {
            productType.setHidden(true);
        }

        this.save(productType);
    }

    /**
     * 修改商品类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void edit(ZjProductTypeOperatorReq req) {
        ZjProductType existType = this.getById(req.getId());
        if (existType == null) {
            throw new BaseException(-1, "类目不存在");
        }

        // 验证上级类目
        if (req.getUpid() != null && req.getUpid() > 0) {
            if (req.getUpid().equals(req.getId())) {
                throw new BaseException(-1, "不能选择自己作为上级类目");
            }

            ZjProductType parent = this.getById(req.getUpid());
            if (parent == null || parent.getHidden() == false) {
                throw new BaseException(-1, "上级类目不存在或已隐藏");
            }

            // 检查是否会形成循环引用
            Set<Integer> childIds = new HashSet<>();
            findAllChildIds(req.getId(), childIds);
            if (childIds.contains(req.getUpid())) {
                throw new BaseException(-1, "不能选择自己的子类目作为上级类目");
            }
        }

        // 检查名称是否重复（排除自己）
        boolean exists = this.lambdaQuery()
                .eq(ZjProductType::getTit, req.getTit())
                .eq(req.getUpid() != null, ZjProductType::getUpid, req.getUpid())
                .ne(ZjProductType::getId, req.getId())
                .exists();
        if (exists) {
            throw new BaseException(-1, "类型名称已存在");
        }

        ZjProductType productType = new ZjProductType();
        BeanUtil.copyProperties(req, productType);
        this.updateById(productType);
    }

    /**
     * 删除商品类型（级联删除所有子节点）
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        //查找要删除的节点及其所有子节点
        Set<Integer> allNodeIds = new HashSet<>();
        findAllChildIds(id, allNodeIds);

        //检查引用关系 留空做后期扩展

        //批量删除（物理删除）
        if(CollUtil.isNotEmpty(allNodeIds)) {
            this.removeByIds(allNodeIds);
        }
    }

    @Override
    public PageUtils<ZjProductTypeVO> listWithPage(ZjProductTypeQueryReq req) {
        if ("tree".equals(req.getShowType())) {
            return listAsTree(req);
        } else {
            return listAsPage(req);
        }
    }

    /**
     * 获取父类选择树（只包含id和tit）
     */
    @Override
    public List<ZjProductTypeVO> getSelectTree(Integer excludeId) {
        // 查询所有显示的类目
        List<ZjProductType> allNodes = this.lambdaQuery()
                .eq(ZjProductType::getHidden, true)
                .orderByAsc(ZjProductType::getSort)
                .orderByAsc(ZjProductType::getId)
                .list();

        // 过滤掉排除的节点及其子节点
        if (excludeId != null) {
            Set<Integer> excludeIds = new HashSet<>();
            findAllChildIds(excludeId, excludeIds);
            allNodes = allNodes.stream()
                    .filter(node -> !excludeIds.contains(node.getId()))
                    .collect(Collectors.toList());
        }

        // 转换为简化VO（只包含id和tit）
        List<ZjProductTypeVO> voList = allNodes.stream()
                .map(node -> {
                    ZjProductTypeVO vo = new ZjProductTypeVO();
                    vo.setId(node.getId());
                    vo.setUpid(node.getUpid());
                    vo.setTit(node.getTit());
                    vo.setChildren(new ArrayList<>());
                    return vo;
                })
                .collect(Collectors.toList());

        // 构建树形结构
        return buildSimpleTree(voList);
    }

    /**
     * 列表形式查询
     */
    private PageUtils<ZjProductTypeVO> listAsPage(ZjProductTypeQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());

        List<ZjProductType> list = this.lambdaQuery()
                .eq(req.getType() != null, ZjProductType::getType, req.getType())
                .like(StrUtil.isNotBlank(req.getTit()), ZjProductType::getTit, req.getTit())
                .orderByAsc(ZjProductType::getSort)
                .orderByAsc(ZjProductType::getId)
                .list();

        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }

        List<ZjProductTypeVO> voList = BeanUtil.copyToList(list, ZjProductTypeVO.class);
        return new PageUtils<>(voList, page.getTotal());
    }

    /**
     * 树形结构查询
     */
    private PageUtils<ZjProductTypeVO> listAsTree(ZjProductTypeQueryReq req) {
        //根据查询条件找到匹配的节点
        List<ZjProductType> matchedNodes = this.lambdaQuery()
                .eq(req.getType() != null, ZjProductType::getType, req.getType())
                .like(StrUtil.isNotBlank(req.getTit()), ZjProductType::getTit, req.getTit())
                .list();

        if (CollUtil.isEmpty(matchedNodes)) {
            return PageUtils.emptyPage();
        }

        //找到匹配节点及其所有子节点
        Set<Integer> allNodeIds = new HashSet<>();
        for (ZjProductType node : matchedNodes) {
            findAllChildIds(node.getId(), allNodeIds);
        }

        //查询所有相关节点
        List<ZjProductType> allNodes = this.lambdaQuery()
                .in(ZjProductType::getId, allNodeIds)
                .orderByAsc(ZjProductType::getSort)
                .orderByAsc(ZjProductType::getId)
                .list();

        //转换为VO并构建树形结构
        List<ZjProductTypeVO> voList = BeanUtil.copyToList(allNodes, ZjProductTypeVO.class);
        List<ZjProductTypeVO> treeList = buildVOTree(voList);

        return new PageUtils<>(treeList, (long) treeList.size());
    }

    /**
     * 递归查找所有子节点ID
     */
    private void findAllChildIds(Integer parentId, Set<Integer> result) {
        result.add(parentId);
        List<ZjProductType> children = this.lambdaQuery()
                .eq(ZjProductType::getUpid, parentId)
                .list();

        for (ZjProductType child : children) {
            findAllChildIds(child.getId(), result);
        }
    }

    /**
     * 构建VO树形结构（使用两阶段算法）
     */

    private List<ZjProductTypeVO> buildVOTree(List<ZjProductTypeVO> list) {
        Map<Integer, ZjProductTypeVO> nodeMap = new HashMap<>();
        List<ZjProductTypeVO> rootNodes = new ArrayList<>();

        // 构建映射，并初始化子节点列表
        for (ZjProductTypeVO node : list) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }

        // 建立父子关系
        for (ZjProductTypeVO node : list) {
            Integer upid = node.getUpid();
            if (upid == null || upid == 0) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 查找父节点
                ZjProductTypeVO parent = nodeMap.get(upid);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 如果父节点不存在，将该节点视为根节点
                    rootNodes.add(node);
                }
            }
        }

        // 排序
        rootNodes.sort(Comparator.comparing(ZjProductTypeVO::getSort)
                .thenComparing(ZjProductTypeVO::getId));
        sortChildren(rootNodes);

        return rootNodes;
    }

    /**
     * 构建简化的树形结构（使用两阶段算法）
     */
    private List<ZjProductTypeVO> buildSimpleTree(List<ZjProductTypeVO> list) {
        Map<Integer, ZjProductTypeVO> nodeMap = new HashMap<>();
        List<ZjProductTypeVO> rootNodes = new ArrayList<>();

        // 第一遍：构建节点映射，并初始化子节点列表
        for (ZjProductTypeVO node : list) {
            node.setChildren(new ArrayList<>());
            nodeMap.put(node.getId(), node);
        }

        // 第二遍：建立父子关系
        for (ZjProductTypeVO node : list) {
            Integer upid = node.getUpid();
            if (upid == null || upid == 0) {
                rootNodes.add(node);
            } else {
                ZjProductTypeVO parent = nodeMap.get(upid);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    rootNodes.add(node);
                }
            }
        }

        // 排序（保留原有的排序逻辑）
        rootNodes.sort(Comparator.comparing(ZjProductTypeVO::getSort)
                .thenComparing(ZjProductTypeVO::getId));
        sortChildren(rootNodes);

        return rootNodes;
    }

    /**
     * 递归排序子节点
     */
    private void sortChildren(List<ZjProductTypeVO> nodes) {
        for (ZjProductTypeVO node : nodes) {
            if (CollUtil.isNotEmpty(node.getChildren())) {
                node.getChildren().sort(Comparator.comparing(ZjProductTypeVO::getSort)
                        .thenComparing(ZjProductTypeVO::getId));
                sortChildren(node.getChildren());
            }
        }
    }
}