package com.zxtx.hummer.withdraw.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import com.zxtx.hummer.withdraw.req.WithdrawApplyCheckReq;
import com.zxtx.hummer.withdraw.service.IWithdrawCheckLogService;
import com.zxtx.hummer.withdraw.vo.WithdrawApplyCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 提现审核记录接口控制器
 * </p>
 *
 * @author chenjian
 * @since 2023-06-02
 */
@Slf4j
@Api(tags = "提现审核记录接口")
@Controller
@RequestMapping("/api/withdraw/check")
public class WithdrawApplyCheckController {

    @Autowired
    private IWithdrawCheckLogService withdrawCheckLogService;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @ApiOperation("提现申请分页列表")
    @ResponseBody
    @GetMapping("/list")
//    @RequiresPermissions("withdraw:check:list")
    public Response<PageUtils<WithdrawApplyCheckVo>> list(WithdrawApplyCheckReq req) {
        List<WithdrawApplyCheckVo> resultList = withdrawCheckLogService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }


}
