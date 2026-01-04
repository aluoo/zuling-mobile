package com.zxtx.hummer.system.controller;


import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.config.Constant;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.Query;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.system.domain.RoleDO;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.service.OpenChannelService;
import com.zxtx.hummer.system.service.RoleService;
import com.zxtx.hummer.system.service.UserService;
import com.zxtx.hummer.system.vo.UserFullInfVo;
import com.zxtx.hummer.system.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/sys/user")
@Api(tags = "用户管理")
@Controller
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    DictService dictService;
    @Autowired
    private OpenChannelService openChannelService;

    @ApiOperation("根据手机号查询员工信息")
    @ResponseBody
    @RequestMapping(value = "/queryEmployee/{mobile}", method = RequestMethod.POST)
    public Response<Employee> queryEmployeeByMobile(@PathVariable("mobile") String mobile) {
        return Response.ok(userService.getEmployeeByMobileNumber(mobile));
    }

    @GetMapping("/list")
    @ApiOperation("列表")
    @ResponseBody
    Response list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<UserFullInfVo> sysUserList = userService.lsUserFullInfo(query);
        int total = userService.count(query);
        PageUtils pageUtil = new PageUtils(sysUserList, total);
        return Response.ok(pageUtil);
    }


    /*@RequiresPermissions("sys:user:edit")*/
    @ResponseBody
    @ApiOperation("编辑数据获取")
    @GetMapping("/edit/{id}")
    public Response edit(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>(16);
        UserDO userDO = userService.get(id);
        map.put("user", userDO);
        List<RoleDO> roles = roleService.list(id);
        map.put("roles", roles);
        map.put("empInfo", userService.getEmployee(userDO));
        return Response.ok(map);
    }

    /*    @RequiresPermissions("sys:user:add")*/
    @ApiOperation("保存")
    @PostMapping("/save")
    @ResponseBody
    Response save(@RequestBody UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.save(user) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }

    /*   @RequiresPermissions("sys:user:edit")*/
    @ApiOperation("更新用户")
    @PostMapping("/update")
    @ResponseBody
    Response update(@RequestBody UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.update(user) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /*    @RequiresPermissions("sys:user:edit")*/
    @ApiOperation("更新用户")
    @PostMapping("/updatePeronal")
    @ResponseBody
    Response updatePeronal(@RequestBody UserDO user) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.updatePersonal(user) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /*    @RequiresPermissions("sys:user:remove")*/
    @Log("删除用户")
    @ApiOperation("删除用户")
    @PostMapping("/removeByEmployee")
    @ResponseBody
    Response removeByEmployee(Long employeeId) {

        if (userService.removeByEmployeeId(employeeId) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /*    @RequiresPermissions("sys:user:remove")*/
    @Log("删除用户")
    @ApiOperation("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    Response remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (userService.remove(id) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    @PostMapping("/resetPwd")
    @ApiOperation("个人重置密码")
    @ResponseBody
    Response resetPwd(@RequestBody UserVO userVO) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.resetPwd(userVO, getUser());
            return Response.ok();
        } catch (Exception e) {
            return Response.failed(e.getMessage());
        }

    }

    /*    @RequiresPermissions("sys:user:resetPwd")*/
    @ApiOperation("超管重置密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    Response adminResetPwd(@RequestBody UserVO userVO) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        try {
            userService.adminResetPwd(userVO);
            return Response.ok();
        } catch (Exception e) {
            return Response.failed(e.getMessage());
        }

    }


}