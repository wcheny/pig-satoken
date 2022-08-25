package com.wangchenyang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.SysSmsChannel;
import com.wangchenyang.admin.api.entity.SysSmsLog;
import com.wangchenyang.admin.service.SysSmsLogService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 短信日志
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/smslog")
public class SysSmsLogController {

	private final SysSmsLogService sysSmsLogService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysSmsLog 短信日志
	 * @return
	 */
	@GetMapping("/page")
	@SaCheckPermission("smslog_get")
	public R getSysSmsLogPage(Page page, SysSmsLog sysSmsLog) {
		LambdaQueryWrapper<SysSmsLog> wrapper = Wrappers.lambdaQuery();
		wrapper.likeLeft(StringUtils.isNotEmpty(sysSmsLog.getMobile()), SysSmsLog::getMobile, sysSmsLog.getMobile())
				.eq(sysSmsLog.getChannelId() != null, SysSmsLog::getChannelId, sysSmsLog.getChannelId())
				.eq(sysSmsLog.getTemplateId() != null, SysSmsLog::getTemplateId, sysSmsLog.getTemplateId());
		return R.ok(sysSmsLogService.page(page, wrapper));
	}

	/**
	 * 通过id查询短信日志
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	@SaCheckPermission("smslog_get")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(sysSmsLogService.getById(id));
	}

}
