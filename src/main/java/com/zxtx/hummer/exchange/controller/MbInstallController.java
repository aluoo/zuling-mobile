package com.zxtx.hummer.exchange.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.service.MbExchangeInstallService;
import com.zxtx.hummer.exchange.service.MbInstallService;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 合伙人换机包 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */

@Slf4j
@Api(tags = "安装包管理")
@RestController
@RequestMapping("/api/exchange/install")
public class MbInstallController {
    @Autowired
    private MbExchangeInstallService mbExchangeInstallService;
    @Autowired
    private MbInstallService mbInstallService;

    @ApiOperation("安装包列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<ExchangeInstallVO>> list(@RequestBody ExchangeInstallReq req) {
        List<ExchangeInstallVO> resultList = mbExchangeInstallService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ApiOperation("安装包新增和编辑")
    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public Response saveOrUpdate(@RequestBody MbInstall dto) {
        mbInstallService.editMbInstall(dto);
        return Response.ok();
    }

    @ApiOperation("安装包删除")
    @ResponseBody
    @GetMapping("/remove")
    public Response saveOrUpdate(Long id) {
        mbInstallService.removeById(id);
        return Response.ok();
    }

    @ApiOperation("安装包查看")
    @ResponseBody
    @GetMapping("/view")
    public Response<MbInstall> view(Long id) {
        return Response.ok(mbInstallService.getById(id));
    }

    @ApiOperation("安装包启动和禁用")
    @ResponseBody
    @PostMapping("/editStatus")
    public Response editStatus(@RequestBody InstallEditStatusDTO dto) {
        mbInstallService.editStatus(dto);
        return Response.ok();
    }







}
