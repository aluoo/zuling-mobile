package com.zxtx.hummer.insurance.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.insurance.domain.DiType;
import com.zxtx.hummer.insurance.req.DiTypeDTO;
import com.zxtx.hummer.insurance.req.DiTypeReq;
import com.zxtx.hummer.insurance.service.DiTypeService;
import com.zxtx.hummer.insurance.vo.DiTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 险种类型表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@Api(tags = "碎屏险-产品类型")
@RestController
@RequestMapping("/api/di/type")
public class DiTypeController {

    @Autowired
    DiTypeService diTypeService;

    @ApiOperation("产品类型列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<DiTypeVO>> list(@RequestBody DiTypeReq req) {
        return Response.ok(PageUtils.create(diTypeService.selectPage(req)));
    }

    @ApiOperation("产品类型添加修改")
    @ResponseBody
    @PostMapping("/save")
    public Response save(@RequestBody DiType diType) {
        diTypeService.saveType(diType);
        return Response.ok();
    }

    @ApiOperation("产品类型查看")
    @ResponseBody
    @GetMapping("/view")
    public Response<DiType> view(Long id) {
        return Response.ok(diTypeService.getById(id));
    }


    @ApiOperation("产品类型状态变更")
    @ResponseBody
    @PostMapping("/editStatus")
    public Response editStatus(@RequestBody DiTypeDTO dto) {
        diTypeService.editStatus(dto);
        return Response.ok();
    }


}
