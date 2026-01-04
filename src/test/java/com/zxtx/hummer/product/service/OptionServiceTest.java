package com.zxtx.hummer.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxtx.hummer.HummerApplicationTest;
import com.zxtx.hummer.product.dao.OptionMapper;
import com.zxtx.hummer.product.domain.Option;
import com.zxtx.hummer.product.domain.ProductOption;
import com.zxtx.hummer.product.domain.dto.OptionDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionServiceTest extends HummerApplicationTest {
    @Autowired
    OptionService service;
    @Autowired
    OptionMapper dao;
    @Autowired
    ProductOptionService productOptionService;

    @Test
    public void test() {
        service.list();
        dao.selectList(new QueryWrapper<>());
    }

    @Test
    public void tree() {
        // build tree
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setWeightKey("sort");

        List<OptionDTO> options = BeanUtil.copyToList(service.list(), OptionDTO.class);

        for (OptionDTO option : options) {
            TreeNode<Long> node = new TreeNode<>();
            node.setId(option.getId());
            node.setParentId(option.getParentId());
            node.setName(option.getName());
            node.setWeight(option.getSort());
            Map<String, Object> extra = new HashMap<>(16);
            extra.put("type", option.getType());
            extra.put("required", option.getRequired());
            extra.put("level", option.getLevel());
            extra.put("ancestors", option.getAncestors());
            node.setExtra(extra);
            nodeList.add(node);
        }
        System.out.println(JSONUtil.toJsonStr(nodeList));
        List<Tree<Long>> build = TreeUtil.build(nodeList, -1L);
        System.out.println(JSONUtil.toJsonStr(build));
    }

    @Test
    public void tree1() {
        // build tree
        // choose this
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        //treeNodeConfig.setIdKey("id");
        treeNodeConfig.setWeightKey("sort");

        List<OptionDTO> options = BeanUtil.copyToList(service.list(new LambdaQueryWrapper<Option>().orderByAsc(Option::getLevel).orderByDesc(Option::getSort)), OptionDTO.class);
        System.out.println(JSONUtil.toJsonStr(options));

        List<Tree<Long>> build = TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("ancestors", treeNode.getAncestors());
        });
        System.out.println(JSONUtil.toJsonStr(build));
        //printTree(build, 0);
        //build.forEach(o -> System.out.println(o.toString()));
    }

    @Test
    public void singleTree() {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        List<OptionDTO> options = BeanUtil.copyToList(service.list(new LambdaQueryWrapper<Option>().orderByDesc(Option::getSort)), OptionDTO.class);

        Tree<Long> build = TreeUtil.buildSingle(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("ancestors", treeNode.getAncestors());
        });

        System.out.println(JSONUtil.toJsonStr(build));
    }
    private void printTree(List<Tree<Long>> trees, int deep) {
        if (CollUtil.isEmpty(trees)) {
            return;
        }
        for (Tree<Long> tree : trees) {
            String name = StrUtil.repeat("--", deep) + tree.getName();
            System.out.println(name);
            if (tree.hasChild()) {
                printTree(tree.getChildren(), deep + 1);
            }
        }
    }

    @Test
    public void productSelectTree() {
        // 根据商品ID获取关联的选项ID
        // 构建选项Tree时，判断选项ID是否包含在关联表内，是则checked=true选中
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("sort");
        List<OptionDTO> options = BeanUtil.copyToList(service.list(), OptionDTO.class);
        List<Long> optionIds = productOptionService.list(new LambdaQueryWrapper<ProductOption>().select(ProductOption::getOptionId).eq(ProductOption::getProductId, 2L))
                .stream()
                .map(ProductOption::getOptionId)
                .collect(Collectors.toList());
        List<Tree<Long>> build = TreeUtil.build(options, -1L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setName(treeNode.getName());
            tree.setParentId(treeNode.getParentId());
            tree.setWeight(treeNode.getSort());// 倒序需要取反排序值
            tree.putExtra("type", treeNode.getType());
            tree.putExtra("required", treeNode.getRequired());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("ancestors", treeNode.getAncestors());
            boolean checked = optionIds.contains(treeNode.getId());
            tree.putExtra("checked", checked);
        });

        System.out.println(JSONUtil.toJsonStr(build));
    }
    // todo order by sort?
    // todo operator, search, filter tree
    // parse tree
}