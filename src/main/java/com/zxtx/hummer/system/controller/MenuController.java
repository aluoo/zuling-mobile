package com.zxtx.hummer.system.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.config.Constant;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.domain.Tree;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.system.domain.MenuDO;
import com.zxtx.hummer.system.service.MenuService;
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

/**
 * @author bootdo 1992lcg@163.com
 */
@RequestMapping("api/sys/menu")
@Api(tags = "系统菜单")
@Controller
public class MenuController extends BaseController {
    @Autowired
    MenuService menuService;


    /*@RequiresPermissions("sys:menu:menu")*/
    @PostMapping("/list")
    @ApiOperation("列表")
    @ResponseBody
    Response list(@RequestParam Map<String, Object> params) {
        List<MenuDO> menus = menuService.list(params);
        return Response.ok(menus);
    }

	/*@Log(value = "添加菜单")
	@RequiresPermissions("sys:menu:add")
	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId") Long pId) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(pId).getName());
		}
		return prefix + "/add";
	}*/

    @Log("编辑菜单")
    @ApiOperation("编辑菜单")
    /*@RequiresPermissions("sys:menu:edit")*/
    @GetMapping("/edit/{id}")
    @ResponseBody
    Response edit(Model model, @PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>(16);
        MenuDO mdo = menuService.get(id);
        Long pId = mdo.getParentId();
        map.put("pId", pId);
        if (pId == 0) {
            model.addAttribute("pName", "根目录");
        } else {
            model.addAttribute("pName", menuService.get(pId).getName());
        }
        map.put("menu", mdo);
        return Response.ok(map);
    }

    @Log("保存菜单")
    @ApiOperation("保存菜单")
    /*@RequiresPermissions("sys:menu:add")*/
    @PostMapping("/save")
    @ResponseBody
    Response save(@RequestBody MenuDO menu) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.save(menu) > 0) {
            return Response.ok();
        } else {
            return Response.failed("保存失败");
        }
    }

    @Log("更新菜单")
    @ApiOperation("更新菜单")
    /*    @RequiresPermissions("sys:menu:edit")*/
    @PostMapping("/update")
    @ResponseBody
    Response update(@RequestBody MenuDO menu) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.update(menu) > 0) {
            return Response.ok();
        } else {
            return Response.failed("更新失败");
        }
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    /* @RequiresPermissions("sys:menu:remove")*/
    @PostMapping("/remove")
    @ResponseBody
    Response remove(Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (menuService.remove(id) > 0) {
            return Response.ok();
        } else {
            return Response.failed("删除失败");
        }
    }

    @GetMapping("/tree")
    @ResponseBody
    Tree<MenuDO> tree() {
        Tree<MenuDO> tree = menuService.getTree();
        return tree;
    }

    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Response roleMenu(@PathVariable("roleId") Long roleId) {
        return Response.ok(menuService.getMenuByRoleId(roleId));
    }
}
