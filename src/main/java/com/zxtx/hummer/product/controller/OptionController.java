package com.zxtx.hummer.product.controller;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.product.domain.enums.OptionCodeEnum;
import com.zxtx.hummer.product.domain.request.CategoryOperatorReq;
import com.zxtx.hummer.product.domain.request.OptionOperatorReq;
import com.zxtx.hummer.product.domain.response.OptionVO;
import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import com.zxtx.hummer.product.service.OptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "商品选项配置")
@RequestMapping("/api/product/option")
public class OptionController extends BaseController {
    @Autowired
    private OptionService service;

    @ApiOperation("获取选项code")
    @RequestMapping(value = "/codes", method = RequestMethod.GET)
    public Response<Map<String, String>> getOptionCode() {
        Map<String, String> collect =
                Arrays.stream(OptionCodeEnum.values()).collect(Collectors.toMap(OptionCodeEnum::getDesc, OptionCodeEnum::getCode, (k1,k2)->k1, LinkedHashMap::new));
        return Response.ok(collect);
    }

    @ApiOperation("获取选项树")
    @RequestMapping(value = "/list/tree", method = RequestMethod.GET)
    public Response<List<Tree<Long>>> tree() {
        return Response.ok(service.buildOptionTree());
    }

    @ApiOperation("根据ID获取选项详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<OptionVO> detailById(@PathVariable("id") Long optionId) {
        return Response.ok(service.detailById(optionId));
    }

    @Log(value = "添加选项", argsPos = {0})
    @ApiOperation("添加选项")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody OptionOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Add.class);
        service.add(req);
        return Response.ok();
    }

    @Log(value = "编辑选项", argsPos = {0})
    @ApiOperation("编辑选项")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody OptionOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        service.edit(req);
        return Response.ok();
    }

    @Log(value = "删除选项", argsPos = {0})
    @ApiOperation("删除选项")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody CategoryOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        service.delete(req.getId());
        return Response.ok();
    }

    // todo import
}