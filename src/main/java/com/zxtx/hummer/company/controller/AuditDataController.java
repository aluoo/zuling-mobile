package com.zxtx.hummer.company.controller;

import cn.hutool.core.collection.CollUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.enums.CompanyStatus;
import com.zxtx.hummer.company.req.CompanyBaseInfoEditReq;
import com.zxtx.hummer.company.req.CompanyExchangeDTO;
import com.zxtx.hummer.company.service.CompanyBatchCreateService;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.company.vo.ComListRes;
import com.zxtx.hummer.company.vo.CompanyExchangeVo;
import com.zxtx.hummer.company.vo.CompanyListReq;
import com.zxtx.hummer.company.vo.CompanyReq;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.exchange.service.MbVerifyInstallProcessService;
import com.zxtx.hummer.hk.service.HkProductService;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import com.zxtx.hummer.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "门店操作")
@RequestMapping("api/company/audit")
public class AuditDataController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmService emService;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;
    @Autowired
    MbVerifyInstallProcessService verifyInstallProcessService;
    @Autowired
    private CompanyBatchCreateService companyBatchCreateService;
    @Autowired
    private HkProductService hkProductService;

    @Log(value = "导入批量创建门店", argsPos = {0})
    @ApiOperation(value = "导入批量创建门店")
    @RequestMapping(value = "/import/batch/create", method = RequestMethod.POST)
    @RequiresPermissions("company:audit:batch:create:import")
    public Response<?> batchCreateCompanyFromImport(@RequestParam("file") MultipartFile file) {
        companyBatchCreateService.batchCreateCompanyFromImport(file);
        return Response.ok();
    }

    @Log(value = "导入批量创建员工", argsPos = {0})
    @ApiOperation(value = "导入批量创建员工")
    @RequestMapping(value = "/import/batch/create/employee", method = RequestMethod.POST)
    @RequiresPermissions("company:audit:batch:create:employee:import")
    public Response<?> batchCreateEmployeeFromImport(@RequestParam("file") MultipartFile file) {
        companyBatchCreateService.batchCreateEmployeeFromImport(file);
        return Response.ok();
    }

    @Log(value = "编辑门店基础信息", argsPos = {0})
    @ApiOperation(value = "编辑门店基础信息")
    @RequestMapping(value = "/edit/base/info", method = RequestMethod.POST)
    @RequiresPermissions("company:audit:base:info")
    public Response<?> editCompanyBaseInfo(@RequestBody CompanyBaseInfoEditReq req) {
        ValidatorUtil.validateBean(req);
        companyService.editCompanyBaseInfo(req);
        return Response.ok();
    }

    @Log(value = "查看代理商审核", argsPos = {0})
    @ApiOperation(value = "查看代理商审核")
    @PostMapping("list")
    /*@RequiresPermissions("company:audit:list")*/
    public Response<PageUtils<ComListRes>> list(@RequestBody CompanyListReq req) {
        return Response.ok(companyService.listByCondition(req));
    }


    @Log(value = "通过代理商审核", argsPos = {0})
    @ApiOperation(value = "通过代理商审核")
    @PostMapping("agree")
    /*@RequiresPermissions("company:audit:edit")*/
    public Response agree(@RequestParam Long id) {
        companyService.agree(id);
        return Response.ok();
    }


    @Log(value = "拒绝代理商审核", argsPos = {0})
    @ApiOperation(value = "拒绝代理商审核")
    @PostMapping("refuse")
    /*@RequiresPermissions("company:audit:edit")*/
    public Response refuse(@RequestParam Long id) {
        companyService.refuse(id);
        return Response.ok();
    }

    @Log(value = "更改开票设置", argsPos = {0})
    @ApiOperation(value = "更改开票设置")
    @PostMapping("changeInvoice")
    public Response changeInvoice(@RequestParam Long id) {
        companyService.changeInvoice(id);
        return Response.ok();
    }

    @Log(value = "增加门店后台登录角色", argsPos = {0})
    @ApiOperation("增加门店后台登录角色")
    @PostMapping("addUser")
    @ResponseBody
    Response addUser(Long id) {
        companyService.addUser(id);
        return Response.ok();
    }

    @Log(value = "更改押金设置", argsPos = {0})
    @ApiOperation(value = "更改押金设置")
    @PostMapping("changeDeposit")
//    @RequiresPermissions("company:audit:changeInvoice")
    public Response changeDeposit(@RequestParam Long id) {
        companyService.changeDeposit(id);
        return Response.ok();
    }

    @Log(value = "更改佣金设置", argsPos = {0})
    @ApiOperation(value = "更改佣金设置")
    @PostMapping("changeCommission")
//    @RequiresPermissions("company:audit:changeInvoice")
    public Response changeCommission(@RequestParam Long id) {
        companyService.changeCommission(id);
        return Response.ok();
    }

    @Log(value = "更新门店信息", argsPos = {0})
    @PostMapping("channel/update")
    @ResponseBody
    @ApiOperation("更新门店信息")
        /*    @RequiresPermissions("company:channel:edit")*/
    Response update(@Valid CompanyReq companyReq) {
        companyService.updateChannel(companyReq);
        return Response.ok();
    }

    @Log(value = "下线渠道", argsPos = {0})
    @ResponseBody
    @PostMapping("channel/offline")
    @ApiOperation("下线渠道")
        /* @RequiresPermissions("company:channel:offline")*/
    Response offlineChannel(Long id) {
        companyService.offlineChannel(id);
        return Response.ok();
    }


    @Log(value = "上线渠道", argsPos = {0})
    @ResponseBody
    @PostMapping("channel/online")
    @ApiOperation("上线渠道")
        /*    @RequiresPermissions("company:channel:online")*/
    Response onlineChannel(Long id) {
        companyService.onlineChannel(id);
        return Response.ok();
    }

    @Log(value = "门店注销", argsPos = {0})
    @ResponseBody
    @PostMapping("channel/cancel")
    @ApiOperation("门店注销")
        /* @RequiresPermissions("company:channel:offline")*/
    Response cancel(Long id) {
        companyService.cancelCompany(id);
        return Response.ok();
    }

    @ApiOperation("是否有待审核数据")
    @ResponseBody
    @GetMapping("/checkVoice")
    public Response checkVoice() {
        List<Company> resultList = companyService.lambdaQuery()
                .eq(Company::getStatus, CompanyStatus.TO_AUDIT.getCode())
                .in(Company::getType,Arrays.asList(CompanyType.STORE.getCode(),CompanyType.CHAIN.getCode())).list();

        if(CollUtil.isEmpty(resultList)){
            return Response.ok(false);
        }
        return Response.ok(true);
    }

    @ApiOperation("门店换机包和验新包")
    @ResponseBody
    @GetMapping("/exchange/package")
    public Response<CompanyExchangeVo> exchangePackage (Long id) {
        return Response.ok(verifyInstallProcessService.companyExchangePackage(id));
    }

    @ApiOperation("门店换机包和验新包")
    @ResponseBody
    @PostMapping("/exchange/packageSave")
    public Response<CompanyExchangeVo> companyExchangePackageSave (@RequestBody CompanyExchangeDTO dto) {
        verifyInstallProcessService.companyExchangePackageSave(dto);
        return Response.ok();
    }

    @ApiOperation(value = "门店关联号卡配置信息")
    @RequestMapping(value = "/hk/product/relation/info", method = RequestMethod.POST)
    public Response<CompanyExchangeVo> companyHkProductRelationInfo(@RequestBody CompanyExchangeDTO req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(hkProductService.companyHkProductRelationInfo(req.getCompanyId()));
    }

    @Log(value = "门店关联号卡配置", argsPos = {0})
    @ApiOperation(value = "门店关联号卡配置")
    @RequestMapping(value = "/hk/product/relation/edit", method = RequestMethod.POST)
    @RequiresPermissions("company:audit:hk:product:relation:edit")
    public Response<?> hkCompanyProductRelationEdit(@RequestBody CompanyExchangeDTO req) {
        ValidatorUtil.validateBean(req);
        hkProductService.hkCompanyProductRelationEdit(req);
        return Response.ok();
    }
}