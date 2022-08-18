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
import com.wangchenyang.admin.api.entity.SysPost;
import com.wangchenyang.admin.api.vo.PostExcelVO;
import com.wangchenyang.admin.service.SysPostService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fxz
 * @date 2022-03-15 17:18:40
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	private final SysPostService sysPostService;

	/**
	 * 获取岗位列表
	 * @return 岗位列表
	 */
	@GetMapping("/list")
	public R<List<SysPost>> listPosts() {
		return R.ok(sysPostService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @return
	 */
	@GetMapping("/page")
	@SaCheckPermission("sys_post_get")
	public R getSysPostPage(Page page) {
		return R.ok(sysPostService.page(page, Wrappers.<SysPost>lambdaQuery().orderByAsc(SysPost::getPostSort)));
	}

	/**
	 * 通过id查询岗位信息表
	 * @param postId id
	 * @return R
	 */
	@GetMapping("/{postId}")
	@SaCheckPermission("sys_post_get")
	public R getById(@PathVariable("postId") Long postId) {
		return R.ok(sysPostService.getById(postId));
	}

	/**
	 * 新增岗位信息表
	 * @param sysPost 岗位信息表
	 * @return R
	 */
	@SysLog("新增岗位信息表")
	@PostMapping
	@SaCheckPermission("sys_post_add")
	public R save(@RequestBody SysPost sysPost) {
		return R.ok(sysPostService.save(sysPost));
	}

	/**
	 * 修改岗位信息表
	 * @param sysPost 岗位信息表
	 * @return R
	 */
	@SysLog("修改岗位信息表")
	@PutMapping
	@SaCheckPermission("sys_post_edit")
	public R updateById(@RequestBody SysPost sysPost) {
		return R.ok(sysPostService.updateById(sysPost));
	}

	/**
	 * 通过id删除岗位信息表
	 * @param postId id
	 * @return R
	 */
	@SysLog("通过id删除岗位信息表")
	@DeleteMapping("/{postId}")
	@SaCheckPermission("sys_post_del")
	public R removeById(@PathVariable Long postId) {
		return R.ok(sysPostService.removeById(postId));
	}

	/**
	 * 导出excel 表格
	 * @return
	 */
	@ResponseExcel
	@GetMapping("/export")
	@SaCheckPermission("sys_post_import_export")
	public List<PostExcelVO> export() {
		return sysPostService.listPost();
	}

	/**
	 * 导入岗位
	 * @param excelVOList 岗位列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@PostMapping("/import")
	@SaCheckPermission("sys_post_import_export")
	public R importRole(@RequestExcel List<PostExcelVO> excelVOList, BindingResult bindingResult) {
		return sysPostService.importPost(excelVOList, bindingResult);
	}

}
