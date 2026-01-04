package com.zxtx.hummer.commission.controller;

import cn.hutool.json.JSONUtil;
import com.zxtx.hummer.commission.dto.*;
import com.zxtx.hummer.commission.req.PlanMemberDelReq;
import com.zxtx.hummer.commission.req.PlanReq;
import com.zxtx.hummer.commission.req.PlanViewReq;
import com.zxtx.hummer.commission.req.UpdatePlanReq;
import com.zxtx.hummer.commission.service.CommissionPlanService;
import com.zxtx.hummer.commission.service.CommissionTypeService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.enums.CompanyType;
import com.zxtx.hummer.em.service.EmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/6
 */
@RestController
@Api(tags = "分佣方案")
@RequestMapping("api/commission/plan/")
public class PlanInfoController extends BaseController {
    @Autowired
    private CommissionPlanService planService;
    @Autowired
    private CommissionTypeService commissioTypeService;
    @Autowired
    private EmService emService;

    @ResponseBody
    @GetMapping("/employee_plan/info/data/{id}")
    public Response<List<EmployeeCommissionPlanInfoDTO>> getEmployeeCommissionPlanInfo(@PathVariable Long id) {
        return Response.ok(planService.getEmployeeCommissionPlanInfo(id));
    }


    /**
     * 查询员工所有类型佣金方案数
     *
     * @return 实例对象
     */
    @ApiOperation("查询员工所有类型佣金方案数")
    @GetMapping("/overview")
    public Response<List<OverviewDTO>> overview(Long employeeId) {
        List<OverviewDTO> resultList = commissioTypeService.overview(employeeId);
        //代理没有压价方案
        if(emService.getById(employeeId).getCompanyType().equals(CompanyType.COMPANY.getCode())){
            resultList = resultList.stream().filter(e -> e.getBizTypeId().intValue()!=3).collect(Collectors.toList());
        }else{
            resultList = resultList.stream().filter(e -> e.getBizTypeId().intValue()!=1).collect(Collectors.toList());
        }

        return Response.ok(resultList);
    }

    /**
     * 员工推广佣金方案列表
     *
     * @return 实例对象
     */
    @ApiOperation("员工推广佣金方案列表")
    @PostMapping("/plan/list")
    public Response<List<PlanDTO>> planList(@RequestBody PlanListDTO dto) {
        ValidatorUtil.validateBean(dto);
        return Response.ok(planService.planList(dto.getEmployeeId(), dto.getBizTypeId()));
    }

    @ApiOperation("获取能给下级配置的套餐")
    @PostMapping("/plan/issue/getDefaut")
    public Response<List<CmPlanPackageDTO>> getChildIssueConf(@RequestBody PlanListDTO dto) {
        return Response.ok(planService.getChildIssueConf(dto.getEmployeeId(), dto.getBizTypeId()));
    }

    @ApiOperation("获取能给下级配置的套餐")
    @PostMapping("/plan/issue/admin/getDefaut")
    public Response<List<CmPlanPackageDTO>> getAdminChildIssueConf(@RequestBody PlanListDTO dto) {
        return Response.ok(planService.getAdminChildIssueConf(dto.getEmployeeId(), dto.getBizTypeId()));
    }

    /**
     * 创建推广佣金方案
     *
     * @return 实例对象
     */
    @ApiOperation("创建佣金方案")
    @PostMapping("/plan/create")
    public Response create(@RequestBody PlanReq planReq) {
        ValidatorUtil.validateBean(planReq);
        planService.create(planReq);
        return Response.ok();
    }

    /**
     * 查看推广佣金方案详情
     *
     * @return 实例对象
     */
    @ApiOperation("查看推广佣金方案详情")
    @PostMapping("/plan/issue/detail")
    public Response<Map<String, Object>> detail(@RequestBody PlanViewReq req) {
        Employee employee = emService.getById(req.getEmployeeId());
        Map<String, Object> result = new HashMap<>();
        result.put("planConf", planService.detail(employee.getLevel(), employee.getId(), req.getPlanId()));
        return Response.ok(result);
    }

    /**
     * 更新推广佣金方案接口
     *
     * @return 实例对象
     */
    @ApiOperation("更新推广佣金方案接口")
    @PostMapping("plan/issue/update")
    public Response update(@RequestBody @Validated UpdatePlanReq updatePlanReq) {
        planService.update(updatePlanReq);
        return Response.ok();
    }

    /**
     * 查看推广佣金方案详情
     *
     * @return 实例对象
     */
    @ApiOperation("删除推广佣金方案接口")
    @PostMapping("/plan/issue/delete")
    public Response delete(@RequestBody PlanViewReq req) {
        planService.delete(req.getEmployeeId(), req.getPlanId());
        return Response.ok();
    }

    /**
     * 查看佣金方案已添加成员列表接口
     *
     * @return 实例对象
     */
    @ApiOperation("查看佣金方案已添加成员列表接口")
    @PostMapping("/plan/members")
    public Response<MemberDTO> members(@RequestBody PlanViewReq req) {
        return Response.ok(planService.members(req.getEmployeeId(), req.getPlanId()));
    }

    /**
     * 删除佣金方案成员
     *
     * @return 实例对象
     */
    @ApiOperation("删除佣金方案成员")
    @PostMapping("/plan/members/remove")
    public Response<MemberDTO> removeMembers(@RequestBody PlanMemberDelReq req) {
        planService.removeMembers(req.getEmployeeId(), req.getPlanId(), req.getDelMembers());
        return Response.ok();
    }


    /**
     * 添加佣金方案成员
     *
     * @return 实例对象
     */
    @ApiOperation("添加佣金方案成员")
    @PostMapping("/plan/members/add")
    public Response<MemberDTO> addMembers(@RequestBody PlanMemberDelReq req) {
        planService.addMembers(req.getEmployeeId(), req.getPlanId(), req.getDelMembers());
        return Response.ok();
    }

    /**
     * 共用
     *
     * @return
     */
    @ApiOperation("当前用户直接下级员工列表")
    @PostMapping("/plan/members/available")
    public Response<MemberDTO> childMembers(@RequestBody PlanListDTO dto) {
        return Response.ok(planService.childMembers(dto.getEmployeeId(), dto.getBizTypeId()));
    }


}