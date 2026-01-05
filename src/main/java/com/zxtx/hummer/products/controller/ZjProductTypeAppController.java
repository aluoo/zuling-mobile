package com.zxtx.hummer.products.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppOperatorReq;
import com.zxtx.hummer.products.domain.request.ZjProductTypeAppQueryReq;
import com.zxtx.hummer.products.domain.response.ZjProductTypeAppVO;
import com.zxtx.hummer.products.service.IZjProductTypeAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小程序产品类型 前端控制器
 * </p>
 *
 * @author L
 * @since 2026-01-05
 */
@Slf4j
@RestController
@Api(tags = "小程序产品类型管理")
@RequestMapping("/api/product-type-app")
public class ZjProductTypeAppController extends BaseController {

    @Autowired
    private IZjProductTypeAppService zjProductTypeAppService;

    @ApiOperation("获取小程序产品类型列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<ZjProductTypeAppVO>> list(@RequestBody ZjProductTypeAppQueryReq req) {
        return Response.ok(zjProductTypeAppService.listWithPage(req));
    }

    @ApiOperation("添加小程序产品类型")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody ZjProductTypeAppOperatorReq req) {
        zjProductTypeAppService.add(req);
        return Response.ok();
    }

    @ApiOperation("编辑小程序产品类型")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody ZjProductTypeAppOperatorReq req) {
        ValidatorUtil.validateBean(req, ZjProductTypeAppOperatorReq.Edit.class);
        zjProductTypeAppService.edit(req);
        return Response.ok();
    }

    @ApiOperation("删除小程序产品类型")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody ZjProductTypeAppOperatorReq req) {
        ValidatorUtil.validateBean(req, ZjProductTypeAppOperatorReq.Delete.class);
        zjProductTypeAppService.delete(req.getId());
        return Response.ok();
    }
}