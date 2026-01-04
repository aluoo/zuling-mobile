package com.zxtx.hummer.system.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.config.Constant;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.system.domain.RoleDO;
import com.zxtx.hummer.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api/sys/role")
@Api(tags = "角色管理")
@Controller
public class RoleController extends BaseController {

	@Autowired
	RoleService roleService;

	/*@RequiresPermissions("sys:role:role")*/
	@GetMapping("/list")
	@ApiOperation("列表")
	@ResponseBody()
	public Response list() {
		List<RoleDO> roles = roleService.list();
		return Response.ok(roles);
	}

	@Log(value = "编辑页面获取数据", argsPos = {0, 1})
	/*@RequiresPermissions("sys:role:edit")*/
	@ApiOperation("列表")
	@GetMapping("/edit/{id}")
	public Response edit(@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();
		RoleDO roleDO = roleService.get(id);
		map.put("role", roleDO);
		return Response.ok(map);
	}

	@Log(value = "保存角色", argsPos = {0})
	/*@RequiresPermissions("sys:role:add")*/
	@PostMapping("/save")
	@ApiOperation("保存角色")
	@ResponseBody()
	Response save(@RequestBody RoleDO role) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return Response.failed("演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.save(role) > 0) {
			return Response.ok();
		} else {
			return Response.failed("保存失败");
		}
	}

	@Log(value = "更新角色", argsPos = {0})
	/*@RequiresPermissions("sys:role:edit")*/
	@PostMapping("/update")
	@ApiOperation("更新角色")
	@ResponseBody()
	Response update(@RequestBody RoleDO role) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return Response.failed("演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.update(role) > 0) {
			return Response.ok();
		} else {
			return Response.failed("保存失败");
		}
	}

	@Log(value = "删除角色", argsPos = {0})
	/*@RequiresPermissions("sys:role:remove")*/
	@PostMapping("/remove")
	@ApiOperation("删除角色")
	@ResponseBody()
	Response save(Long id) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return Response.failed("演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.remove(id) > 0) {
			return Response.ok();
		} else {
			return Response.failed("删除失败");
		}
	}

	/*@RequiresPermissions("sys:role:batchRemove")*/
	@Log(value = "批量删除角色", argsPos = {0})
	@PostMapping("/batchRemove")
	@ApiOperation("删除角色")
	@ResponseBody
	Response batchRemove(@RequestParam("ids[]") Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return Response.failed("演示系统不允许修改,完整体验请部署程序");
		}
		int r = roleService.batchremove(ids);
		if (r > 0) {
			return Response.ok();
		}
		return Response.failed("删除失败");
	}
}
