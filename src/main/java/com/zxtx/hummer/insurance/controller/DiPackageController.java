package com.zxtx.hummer.insurance.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.insurance.domain.DiPackage;
import com.zxtx.hummer.insurance.req.DiPackageDTO;
import com.zxtx.hummer.insurance.req.DiPackageReq;
import com.zxtx.hummer.insurance.service.DiPackageService;
import com.zxtx.hummer.insurance.vo.DiPackageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 保险套餐表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@Api(tags = "碎屏险-产品套餐")
@RestController
@RequestMapping("/api/di/package")
public class DiPackageController {

    @Autowired
    DiPackageService diPackageService;

    @ApiOperation("套餐列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<DiPackageVO>> list(@RequestBody DiPackageReq req) {
        return Response.ok(PageUtils.create(diPackageService.selectPage(req)));
    }

    @ApiOperation("产品类型对应的套餐列表")
    @ResponseBody
    @GetMapping("/typeList/{typeId}")
    public Response<List<DiPackage>> typeList(@PathVariable Long typeId ) {
        return Response.ok(diPackageService.selectByTypeId(typeId));
    }

    @ApiOperation("套餐添加修改")
    @ResponseBody
    @PostMapping("/save")
    public Response save(@RequestBody DiPackage diPackage) {
        diPackageService.saveType(diPackage);
        return Response.ok();
    }

    @ApiOperation("套餐查看")
    @ResponseBody
    @GetMapping("/view")
    public Response<DiPackage> view(Long id) {
        return Response.ok(diPackageService.getById(id));
    }


    @ApiOperation("套餐状态变更")
    @ResponseBody
    @PostMapping("/editStatus")
    public Response editStatus(@RequestBody DiPackageDTO dto) {
        diPackageService.editStatus(dto);
        return Response.ok();
    }

}
