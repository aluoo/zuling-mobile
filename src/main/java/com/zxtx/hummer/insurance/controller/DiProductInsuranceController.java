package com.zxtx.hummer.insurance.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.insurance.req.DiProductInsuranceDTO;
import com.zxtx.hummer.insurance.req.DiProductQueryReq;
import com.zxtx.hummer.insurance.service.DiProductManageService;
import com.zxtx.hummer.insurance.vo.DiMobileInsuranceViewVO;
import com.zxtx.hummer.insurance.vo.DiProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 手机-数保产品关联表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@Api(tags = "碎屏险-数保手机管理")
@RestController
@RequestMapping("/api/di/product/insurance")
public class DiProductInsuranceController {

    @Autowired
    private DiProductManageService diProductManageService;

    @ApiOperation("列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<DiProductVO>> listProduct(@RequestBody DiProductQueryReq req) {
        return Response.ok(diProductManageService.listProduct(req));
    }

    @ApiOperation("关联产品")
    @RequestMapping(value = "/saveProductInsurance", method = RequestMethod.POST)
    public Response<?> saveProductInsurance(@RequestBody DiProductInsuranceDTO dto) {
        ValidatorUtil.validateBean(dto);
        diProductManageService.saveProductInsurance(dto);
        return Response.ok();
    }

    @ApiOperation("详情")
    @ResponseBody
    @PostMapping("/view/{productId}")
    public Response<DiMobileInsuranceViewVO> view (@PathVariable Long productId ) {
        return Response.ok(diProductManageService.view(productId));
    }




}