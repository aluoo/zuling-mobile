package com.zxtx.hummer.exchange.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.vo.DictMapVO;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.MbVerifyInstall;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeEmployeeInfoReq;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.req.ExchangeVerifyCompanyReq;
import com.zxtx.hummer.exchange.req.ExchangeVerifyDTO;
import com.zxtx.hummer.exchange.service.MbExchangeInstallService;
import com.zxtx.hummer.exchange.service.MbInstallService;
import com.zxtx.hummer.exchange.service.MbVerifyInstallProcessService;
import com.zxtx.hummer.exchange.service.MbVerifyInstallService;
import com.zxtx.hummer.exchange.vo.ExchangeCompanyVO;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.exchange.vo.ExchangeVerifyInstallVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 验新包 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */

@Slf4j
@Api(tags = "验新包管理")
@RestController
@RequestMapping("/api/exchange/verify/install")
public class MbVerifyInstallController {
    @Autowired
    private MbVerifyInstallProcessService installProcessService;
    @Autowired
    private MbVerifyInstallService mbInstallService;
    @Autowired
    private DictService dictService;

    @ApiOperation("验新包列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<ExchangeVerifyInstallVO>> list(@RequestBody ExchangeInstallReq req) {
        return Response.ok(installProcessService.selectPage(req));
    }

    @ApiOperation("验新包类型")
    @ResponseBody
    @GetMapping("/type")
    public Response<DictMapVO> getType() {
        return Response.ok(dictService.getNameMap("exchange_verify_type"));
    }

    @ApiOperation("门店列表")
    @ResponseBody
    @PostMapping("/company")
    public Response<PageUtils<ExchangeCompanyVO>> companyList(@RequestBody ExchangeVerifyCompanyReq req) {
        return Response.ok(installProcessService.companyList(req));
    }

    @ApiOperation("验新包门店列表")
    @ResponseBody
    @PostMapping("/package/company")
    public Response<List<ExchangeCompanyVO>> packageCompanyList(@RequestBody ExchangeVerifyCompanyReq req) {
        return Response.ok(installProcessService.packageCompanyList(req));
    }

    @ApiOperation("安装包新增和编辑")
    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public Response saveOrUpdate(@RequestBody MbVerifyInstall dto) {
        mbInstallService.editMbInstall(dto);
        return Response.ok();
    }

    @ApiOperation("安装包删除")
    @ResponseBody
    @GetMapping("/remove")
    public Response saveOrUpdate(Long id) {
        installProcessService.removeVerify(id);
        return Response.ok();
    }

    @ApiOperation("安装包查看")
    @ResponseBody
    @GetMapping("/view")
    public Response<MbVerifyInstall> view(Long id) {
        return Response.ok(mbInstallService.getById(id));
    }

    @ApiOperation("安装包启动和禁用")
    @ResponseBody
    @PostMapping("/editStatus")
    public Response editStatus(@RequestBody InstallEditStatusDTO dto) {
        mbInstallService.editStatus(dto);
        return Response.ok();
    }

    @ApiOperation("拉新配置提交")
    @ResponseBody
    @PostMapping("/company/save")
    public Response companySave(@RequestBody ExchangeVerifyDTO dto) {
        installProcessService.companySave(dto);
        return Response.ok();
    }





}
