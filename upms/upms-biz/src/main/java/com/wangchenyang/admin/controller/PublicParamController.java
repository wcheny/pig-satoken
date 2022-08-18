/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yifan4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.wangchenyang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.SysPublicParam;
import com.wangchenyang.admin.service.SysPublicParamService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 公共参数
 *
 * @author Lucky
 * @date 2019-04-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/param")
public class PublicParamController {

	private final SysPublicParamService sysPublicParamService;

	/**
	 * 通过key查询公共参数值
	 * @param publicKey
	 * @return
	 */
	@GetMapping("/publicValue/{publicKey}")
	public R publicKey(@PathVariable("publicKey") String publicKey) {
		return R.ok(sysPublicParamService.getSysPublicParamKeyToValue(publicKey));
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysPublicParam 公共参数
	 * @return
	 */
	@GetMapping("/page")
	public R getSysPublicParamPage(Page page, SysPublicParam sysPublicParam) {
		return R.ok(sysPublicParamService.page(page, Wrappers.query(sysPublicParam)));
	}

	/**
	 * 通过id查询公共参数
	 * @param publicId id
	 * @return R
	 */
	@GetMapping("/{publicId}")
	public R getById(@PathVariable("publicId") Long publicId) {
		return R.ok(sysPublicParamService.getById(publicId));
	}

	/**
	 * 新增公共参数
	 * @param sysPublicParam 公共参数
	 * @return R
	 */
	@SysLog("新增公共参数")
	@PostMapping
	@SaCheckPermission("sys_publicparam_add")
	public R save(@RequestBody SysPublicParam sysPublicParam) {
		return R.ok(sysPublicParamService.save(sysPublicParam));
	}

	/**
	 * 修改公共参数
	 * @param sysPublicParam 公共参数
	 * @return R
	 */
	@SysLog("修改公共参数")
	@PutMapping
	@SaCheckPermission("sys_publicparam_edit")
	public R updateById(@RequestBody SysPublicParam sysPublicParam) {
		return sysPublicParamService.updateParam(sysPublicParam);
	}

	/**
	 * 通过id删除公共参数
	 * @param publicId id
	 * @return R
	 */
	@SysLog("删除公共参数")
	@DeleteMapping("/{publicId}")
	@SaCheckPermission("sys_publicparam_del")
	public R removeById(@PathVariable Long publicId) {
		return sysPublicParamService.removeParam(publicId);
	}

	/**
	 * 同步参数
	 * @return R
	 */
	@SysLog("同步参数")
	@PutMapping("/sync")
	public R sync() {
		return sysPublicParamService.syncParamCache();
	}

}
