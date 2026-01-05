package com.zxtx.hummer.products.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.products.domain.request.ZjProductTypeOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeVO;
import com.zxtx.hummer.products.service.IZjProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品类目 前端控制器
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
@Slf4j
@RestController
@Api(tags = "商品类目管理")
@RequestMapping("/api/product-type")
public class ZjProductTypeController {

    @Autowired
    private IZjProductTypeService zjProductTypeService;


    @ApiOperation("添加商品类型")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody ZjProductTypeOperatorReq req) {
        ValidatorUtil.validateBean(req);
        zjProductTypeService.add(req);
        return Response.ok();
    }

    @ApiOperation("编辑商品类型")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody ZjProductTypeOperatorReq req) {
        ValidatorUtil.validateBean(req, ZjProductTypeOperatorReq.Edit.class);
        zjProductTypeService.edit(req);
        return Response.ok();
    }

    @ApiOperation("获取商品类型列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<ZjProductTypeVO>> list(@RequestBody ZjProductTypeQueryReq req) {
        return Response.ok(zjProductTypeService.listWithPage(req));
    }

    @ApiOperation("删除商品类型")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody ZjProductTypeOperatorReq req) {
        ValidatorUtil.validateBean(req, ZjProductTypeOperatorReq.Delete.class);
        zjProductTypeService.delete(req.getId());
        return Response.ok();
    }

    @ApiOperation("获取父类选择树")
    @RequestMapping(value = "/parent/tree", method = RequestMethod.GET)
    public Response<List<ZjProductTypeVO>> parentTree(@RequestParam(required = false) Integer excludeId) {
        return Response.ok(zjProductTypeService.getSelectTree(excludeId));
    }

}
