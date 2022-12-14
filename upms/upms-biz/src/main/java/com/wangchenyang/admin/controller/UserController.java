/*
 * Copyright (c) 2020 yifan4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wangchenyang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.dto.UserDTO;
import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.admin.api.vo.UserExcelVO;
import com.wangchenyang.admin.api.vo.UserInfoVO;
import com.wangchenyang.admin.api.vo.UserVO;
import com.wangchenyang.admin.service.SysUserService;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.exception.ErrorCodes;
import com.wangchenyang.common.core.util.MsgUtils;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import com.wangchenyang.common.satoken.utils.LoginHelper;

import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final SysUserService userService;

	/**
	 * ??????????????????????????????
	 * @return ????????????
	 */
	@GetMapping(value = { "/info" })
	public R<UserInfoVO> info() {
		String username = LoginHelper.getUsername();
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
		}
		LoginUser userInfo = userService.getUserInfo(user);
		UserInfoVO vo = new UserInfoVO();
		vo.setSysUser(user);
		vo.setRoles(userInfo.getRoleIds());
		vo.setPermissions(userInfo.getMenuPermissions());
		return R.ok(vo);
	}

	/**
	 * ??????????????????????????????
	 * @return ????????????
	 */
	@GetMapping("/info/{username}")
	public R<LoginUser> info(@PathVariable String username) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, username));
		}
		return R.ok(userService.getUserInfo(user));
	}

	/**
	 * ????????????id???????????????????????? id ??????
	 * @param deptIds ??????id ??????
	 * @return ?????? id ??????
	 */
	@GetMapping("/ids")
	public R<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds) {
		return R.ok(userService.listUserIdByDeptIds(deptIds));
	}

	/**
	 * ??????ID??????????????????
	 * @param id ID
	 * @return ????????????
	 */
	@GetMapping("/{id:\\d+}")
	public R<UserVO> user(@PathVariable Long id) {
		return R.ok(userService.getUserVoById(id));
	}

	/**
	 * ????????????????????????
	 * @param userDTO ????????????
	 * @return
	 */
	@GetMapping("/check/exsit")
	public R<Boolean> isExsit(UserDTO userDTO) {
		List<SysUser> sysUserList = userService.list(new QueryWrapper<>(userDTO));
		if (CollUtil.isNotEmpty(sysUserList)) {
			return R.ok(Boolean.TRUE, MsgUtils.getMessage(ErrorCodes.SYS_USER_EXISTING));
		}
		return R.ok(Boolean.FALSE);
	}

	/**
	 * ??????????????????
	 * @param id ID
	 * @return R
	 */
	@SysLog("??????????????????")
	@DeleteMapping("/{id:\\d+}")
	@SaCheckPermission("sys_user_del")
	public R<Boolean> userDel(@PathVariable Long id) {
		SysUser sysUser = userService.getById(id);
		return R.ok(userService.removeUserById(sysUser));
	}

	/**
	 * ????????????
	 * @param userDto ????????????
	 * @return success/false
	 */
	@SysLog("????????????")
	@PostMapping
	@SaCheckPermission("sys_user_add")
	public R<Boolean> user(@RequestBody UserDTO userDto) {
		return R.ok(userService.saveUser(userDto));
	}

	/**
	 * ??????????????????
	 * @param userDto ????????????
	 * @return R
	 */
	@SysLog("??????????????????")
	@PutMapping
	@SaCheckPermission("sys_user_edit")
	public R<Boolean> updateUser(@Valid @RequestBody UserDTO userDto) {
		return R.ok(userService.updateUser(userDto));
	}

	/**
	 * ??????????????????
	 * @param page ?????????
	 * @param userDTO ??????????????????
	 * @return ????????????
	 */
	@GetMapping("/page")
	public R<IPage<UserVO>> getUserPage(Page page, UserDTO userDTO) {
		return R.ok(userService.getUserWithRolePage(page, userDTO));
	}

	/**
	 * ??????????????????
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("??????????????????")
	@PutMapping("/edit")
	public R<Boolean> updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		userDto.setUsername(LoginHelper.getUsername());
		return R.ok(userService.updateUserInfo(userDto));
	}

	/**
	 * @param username ????????????
	 * @return ????????????????????????
	 */
	@GetMapping("/ancestor/{username}")
	public R<List<SysUser>> listAncestorUsers(@PathVariable String username) {
		return R.ok(userService.listAncestorUsersByUsername(username));
	}

	/**
	 * ??????excel ??????
	 * @param userDTO ????????????
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@SaCheckPermission("sys_user_import_export")
	public List<UserExcelVO> export(UserDTO userDTO) {
		return userService.listUser(userDTO);
	}

	/**
	 * ????????????
	 * @param excelVOList ????????????
	 * @param bindingResult ??????????????????
	 * @return R
	 */
	@PostMapping("/import")
	@SaCheckPermission("sys_user_import_export")
	public R importUser(@RequestExcel List<UserExcelVO> excelVOList, BindingResult bindingResult) {
		return userService.importUser(excelVOList, bindingResult);
	}

}
