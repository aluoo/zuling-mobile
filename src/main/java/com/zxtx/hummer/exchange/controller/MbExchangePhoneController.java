package com.zxtx.hummer.exchange.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.exchange.domain.MbInstall;
import com.zxtx.hummer.exchange.domain.dto.ExchangeMemberAddDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangePhoneAddDTO;
import com.zxtx.hummer.exchange.domain.dto.InstallEditStatusDTO;
import com.zxtx.hummer.exchange.req.ExchangeInstallReq;
import com.zxtx.hummer.exchange.req.ExchangeVerifyDTO;
import com.zxtx.hummer.exchange.service.*;
import com.zxtx.hummer.exchange.vo.ExchangeInstallVO;
import com.zxtx.hummer.exchange.vo.ExchangePhoneVO;
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
@Api(tags = "换机包管理")
@RestController
@RequestMapping("/api/exchange/phone")
public class MbExchangePhoneController {
    @Autowired
    private MbExchangePhoneService mbExchangePhoneService;
    @Autowired
    private MbInstallService mbInstallService;
    @Autowired
    private EmService emService;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;
    @Autowired
    private MbExchangeInstallService mbExchangeInstallService;
    @Autowired
    private MbVerifyInstallProcessService verifyInstallProcessService;

    @ApiOperation("换机包列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<ExchangePhoneVO>> list(@RequestBody ExchangeInstallReq req) {
        List<ExchangePhoneVO> resultList = mbExchangePhoneService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ApiOperation("换机包的安装包列表")
    @ResponseBody
    @GetMapping("/install")
    public Response<List<MbInstall>> installList(Long id) {
        List<MbInstall> resultList = mbExchangeInstallService.packageInstall(id);
        return Response.ok(resultList);
    }

    @ApiOperation("安装包列表")
    @ResponseBody
    @GetMapping("/installList")
    public Response<List<MbInstall>> installList() {
        List<MbInstall> resultList = mbInstallService.ableInstallList();
        return Response.ok(resultList);
    }

    @ApiOperation("换机包新增")
    @ResponseBody
    @PostMapping("/add")
    public Response add(@RequestBody ExchangePhoneAddDTO dto) {
        mbExchangePhoneService.add(dto);
        return Response.ok();
    }

    @ApiOperation("换机包编辑")
    @ResponseBody
    @PostMapping("/update")
    public Response update(@RequestBody ExchangePhoneAddDTO dto) {
        mbExchangePhoneService.update(dto);
        return Response.ok();
    }

    @ApiOperation("换机包查看")
    @ResponseBody
    @GetMapping("/view")
    public Response<ExchangePhoneAddDTO> view(Long id) {
        return Response.ok(mbExchangePhoneService.view(id));
    }

    @ApiOperation("换机包删除")
    @ResponseBody
    @GetMapping("/remove")
    public Response saveOrUpdate(Long id) {
        mbExchangePhoneService.removePackage(id);
        return Response.ok();
    }

    @ApiOperation("安装包启动和禁用")
    @ResponseBody
    @PostMapping("/editStatus")
    public Response editStatus(@RequestBody InstallEditStatusDTO dto) {
        mbExchangePhoneService.editStatus(dto);
        return Response.ok();
    }

    @ApiOperation("合伙人列表")
    @ResponseBody
    @GetMapping("/bdList")
    public Response<List<Employee>> bdList() {
        return Response.ok(emService.BdList());
    }

    @ApiOperation("区域经理列表")
    @ResponseBody
    @GetMapping("/areaList")
    public Response<List<Employee>> areaList(String bdEmployeeId) {
        return Response.ok(emService.areaList(bdEmployeeId));
    }

    @ApiOperation("代理列表")
    @ResponseBody
    @GetMapping("/agentList")
    public Response<List<Employee>> agentList(String bdEmployeeId) {
        return Response.ok(emService.agentList(bdEmployeeId));
    }

    @ApiOperation("换机包门店配置")
    @ResponseBody
    @PostMapping("/employee")
    public Response editStatus(@RequestBody ExchangeVerifyDTO dto) {
        verifyInstallProcessService.exchangeCompanySave(dto);
        return Response.ok();
    }


}
