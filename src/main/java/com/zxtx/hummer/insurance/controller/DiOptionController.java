package com.zxtx.hummer.insurance.controller;

import cn.hutool.core.lang.tree.Tree;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.insurance.enums.DiOptionCodeEnum;
import com.zxtx.hummer.insurance.req.DiOptionOperatorReq;
import com.zxtx.hummer.insurance.service.DiOptionService;
import com.zxtx.hummer.insurance.vo.DiOptionVO;
import com.zxtx.hummer.product.domain.enums.OptionCodeEnum;
import com.zxtx.hummer.product.domain.request.CategoryOperatorReq;
import com.zxtx.hummer.product.domain.request.OptionOperatorReq;
import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
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
 * <p>
 * 数保产品选项表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@RestController
@Api(tags = "碎屏险-选项配置")
@RequestMapping("/api/di/option")
public class DiOptionController {

    @Autowired
    DiOptionService diOptionService;

    @ApiOperation("获取选项code")
    @RequestMapping(value = "/codes", method = RequestMethod.GET)
    public Response<Map<String, String>> getOptionCode() {
        Map<String, String> collect =
                Arrays.stream(DiOptionCodeEnum.values()).collect(Collectors.toMap(DiOptionCodeEnum::getDesc, DiOptionCodeEnum::getCode, (k1, k2)->k1, LinkedHashMap::new));
        return Response.ok(collect);
    }

    @ApiOperation("获取选项树")
    @RequestMapping(value = "/list/tree", method = RequestMethod.GET)
    public Response<List<Tree<Long>>> tree() {
        return Response.ok(diOptionService.buildOptionTree());
    }

    @ApiOperation("根据ID获取选项详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<DiOptionVO> detailById(@PathVariable("id") Long optionId) {
        return Response.ok(diOptionService.detailById(optionId));
    }

    @Log(value = "添加选项", argsPos = {0})
    @ApiOperation("添加选项")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody DiOptionOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Add.class);
        diOptionService.add(req);
        return Response.ok();
    }

    @Log(value = "编辑选项", argsPos = {0})
    @ApiOperation("编辑选项")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody OptionOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        diOptionService.edit(req);
        return Response.ok();
    }

    @Log(value = "删除选项", argsPos = {0})
    @ApiOperation("删除选项")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody CategoryOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        diOptionService.delete(req.getId());
        return Response.ok();
    }

}
