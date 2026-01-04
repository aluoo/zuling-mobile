package com.zxtx.hummer.product.controller;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.product.domain.request.CategoryOperatorReq;
import com.zxtx.hummer.product.domain.request.CategoryQueryReq;
import com.zxtx.hummer.product.domain.response.CategoryVO;
import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import com.zxtx.hummer.product.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "商品分类管理")
@RequestMapping("/api/product/category")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService service;

    @ApiOperation("根据level获取分类列表")
    @RequestMapping(value = "/list/by_level", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listCategoryByLevel(@RequestBody CategoryQueryReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.ByLevel.class);
        return Response.ok(service.listType(req.getLevel(), null));
    }

    @ApiOperation("根据上级ID获取子分类列表")
    @RequestMapping(value = "/list/by_parent", method = RequestMethod.POST)
    public Response<List<CategoryVO>> listCategoryByParent(@RequestBody CategoryQueryReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.ByParent.class);
        return Response.ok(service.listType(null, req.getParentId()));
    }

    @ApiOperation("根据ID获取分类详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<CategoryVO> detailById(@PathVariable("id") Long categoryId) {
        return Response.ok(service.detailById(categoryId));
    }

    @ApiOperation("获取分类树")
    @RequestMapping(value = "/list/tree", method = RequestMethod.GET)
    public Response<List<Tree<Long>>> tree() {
        return Response.ok(service.buildCategoryTree());
    }

    @Log(value = "添加分类", argsPos = {0})
    @ApiOperation("添加分类")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody CategoryOperatorReq req) {
        ValidatorUtil.validateBean(req);
        service.add(req);
        return Response.ok();
    }

    @Log(value = "编辑分类", argsPos = {0})
    @ApiOperation("编辑分类")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody CategoryOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        service.edit(req);
        return Response.ok();
    }

    @Log(value = "删除分类", argsPos = {0})
    @ApiOperation("删除分类")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody CategoryOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        service.delete(req.getId());
        return Response.ok();
    }
    // todo import
}