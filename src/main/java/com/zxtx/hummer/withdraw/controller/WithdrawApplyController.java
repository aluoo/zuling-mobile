package com.zxtx.hummer.withdraw.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import com.zxtx.hummer.withdraw.constant.WithdrawApplyStatusEnum;
import com.zxtx.hummer.withdraw.constant.WithdrawCardIllegalTypeEnum;
import com.zxtx.hummer.withdraw.domain.WithdrawEmployeeApply;
import com.zxtx.hummer.withdraw.req.WithdrawApplyReq;
import com.zxtx.hummer.withdraw.req.WithdrawCheckReq;
import com.zxtx.hummer.withdraw.service.IWithdrawEmployeeApplyService;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyExportVo;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyVo;
import com.zxtx.hummer.withdraw.vo.WithdrawCardIlegalTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 提现模块相关接口控制器
 * </p>
 *
 * @author chenjian
 * @since 2023-06-02
 */
@Slf4j
@Api(tags = "提现模块相关接口")
@RestController

@RequestMapping("/api/withdraw/apply")
public class WithdrawApplyController {

    @Autowired
    private IWithdrawEmployeeApplyService withdrawEmployeeApplyService;
    @Autowired
    private EmService emService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    DeptService deptService;

    @ApiOperation("提现申请分页列表")
    @ResponseBody
    @GetMapping("/list")
//    @RequiresPermissions("withdraw:apply:list")
    public Response<PageUtils<WithdrawApplyVo>> list(WithdrawApplyReq req) {
        List<WithdrawApplyVo> resultList = withdrawEmployeeApplyService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ApiOperation("审核详情接口")
    @GetMapping("/toReview/{id}")
    public Response toReview(@PathVariable Long id, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        WithdrawEmployeeApply apply = withdrawEmployeeApplyService.getById(id);
        resultMap.put("apply", apply);
        if (StrUtil.isNotBlank(apply.getInvoiceImgs())) {
            List<String> split = Arrays.asList(apply.getInvoiceImgs().split(","));
            resultMap.put("imgList", split);
        }

        if (apply.getEmployeeId() != null) {
            Employee employee = emService.getById(apply.getEmployeeId());
            if (employee != null) {
                resultMap.put("mobileNumber", employee.getMobileNumber());
            }
        }

        return Response.ok(resultMap);
    }

    /**
     * 打款设置页面
     *
     * @param id
     * @param model
     * @return
     */
    @ApiOperation("设置已打款页面接口")
    @GetMapping("/payReview/{id}")
    public Response payReview(@PathVariable Long id, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        WithdrawEmployeeApply apply = withdrawEmployeeApplyService.getById(id);
        resultMap.put("apply", apply);

        List<WithdrawCardIlegalTypeVo> typeList = new ArrayList<>();
        for (WithdrawCardIllegalTypeEnum typeEnum : WithdrawCardIllegalTypeEnum.values()) {
            WithdrawCardIlegalTypeVo typeVo = new WithdrawCardIlegalTypeVo();
            typeVo.setLegalCode(typeEnum.getCode());
            typeVo.setLegalName(typeEnum.getMessage());
            typeList.add(typeVo);
        }
        resultMap.put("typeList", typeList);
        return Response.ok(resultMap);
    }

    @Log(value = "提现申请审核不通过", argsPos = {0})
    @ApiOperation("提现申请审核不通过")
    @ResponseBody
    @PostMapping("/checkUnPass")
//    @RequiresPermissions("withdraw:apply:check")
    public Response checkUnPass(WithdrawCheckReq req) {
        withdrawEmployeeApplyService.checkUnPass(req);
        return Response.ok();
    }

    @Log(value = "提现申请审核通过", argsPos = {0})
    @ApiOperation("提现申请审核通过")
    @ResponseBody
    @PostMapping("/checkPass")
    public Response checkPass(WithdrawCheckReq req) {
        withdrawEmployeeApplyService.checkPass(req);
        return Response.ok();
    }

    @Log(value = "打款失败", argsPos = {0})
    @ApiOperation("打款失败")
    @ResponseBody
    @PostMapping("/payUnPass")
//    @RequiresPermissions("withdraw:apply:pay")
    public Response payUnPass(WithdrawCheckReq req) {
        withdrawEmployeeApplyService.payUnPass(req);
        return Response.ok();
    }

    @Log(value = "打款成功", argsPos = {0})
    @ApiOperation("打款成功")
    @ResponseBody
    @PostMapping("/payPass")
//    @RequiresPermissions("withdraw:apply:pay")
    public Response payPass(WithdrawCheckReq req) {
        withdrawEmployeeApplyService.payPass(req);
        return Response.ok();
    }

    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation("导出提现申请信息")
    @Log(value = "导出提现申请信息", argsPos = {0, 1})
    @PostMapping(value = "/exportExcel")
//    @RequiresPermissions("withdraw:apply:exportExcel")
    public void exportExcel(@RequestBody WithdrawApplyReq req, HttpServletResponse response) throws Exception {
        req.setPage(1);
        req.setPageSize(5000);
        req.setStatus(WithdrawApplyStatusEnum.wait_pay.getType());
        List<WithdrawApplyVo> resultList = withdrawEmployeeApplyService.selectPage(req);
        List<WithdrawApplyExportVo> exportVoList = new ArrayList<>();
        for (WithdrawApplyVo applyVo : resultList) {
            WithdrawApplyExportVo vo = new WithdrawApplyExportVo();
            vo.setApplyNo(applyVo.getApplyNo());
            vo.setAccountNo(applyVo.getAccountNo());
            vo.setOwnerName(applyVo.getOwnerName());
            vo.setIdCard(applyVo.getIdCard());
            vo.setMobileNumber(applyVo.getMobileNumber());
            vo.setInAmountYuan(StringUtils.fenToYuan(applyVo.getInAmount()));
            exportVoList.add(vo);
        }
        Map<String, Object> data = new HashedMap();

        data.put("myTestList", exportVoList);
        String tplName = "excel/withdraw.xls";
        ExcelExportUtil.exportByTemplate(response, "提现申请单-" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
                tplName, data);
    }

    @ApiOperation("导入青蚨打款对账单")
    @Log(value = "导入青蚨打款对账单", noArgs = true)
    @PostMapping("/import")
    @ResponseBody
//    @RequiresPermissions("withdraw:apply:import")
    public Response importExcel(@RequestParam("file") MultipartFile file) {
        String resultMessage = withdrawEmployeeApplyService.importExcel(file);
        if (StrUtil.isBlank(resultMessage)) {
            return Response.ok();
        } else {
            return Response.failed(resultMessage);
        }
    }

    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation("导出提现申请信息")
    @Log(value = "导出提现申请信息", argsPos = {0, 1})
    @PostMapping(value = "/exportWithDraw")
//    @RequiresPermissions("withdraw:apply:exportWithDraw")
    public void exportWithDraw(@RequestBody WithdrawApplyReq req, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "提现申请" + format.format(new Date().getTime()) + ".xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();
        try {
            req.setPage(1);
            req.setPageSize(5000);
            List<WithdrawApplyVo> resultList = withdrawEmployeeApplyService.selectPage(req);
            if (CollUtil.isNotEmpty(resultList)) {
                for (WithdrawApplyVo applyVo : resultList) {
                    applyVo.setAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getAmount()) ? applyVo.getAmount() : 0));
                    applyVo.setTaxAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getTaxAmount()) ? applyVo.getTaxAmount() : 0));
                    applyVo.setInAmountStr(StringUtils.fenToYuan(ObjectUtil.isNotNull(applyVo.getInAmount()) ? applyVo.getInAmount() : 0));
                }
            }
            ExcelExportUtil.exportWorkbook(resultList).write(out);
        } finally {
            out.close();
        }
    }

}