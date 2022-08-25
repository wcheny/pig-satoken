package com.wangchenyang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.SysSmsTemplate;
import com.wangchenyang.admin.api.vo.SmsTemplateSendReqVO;
import com.wangchenyang.admin.service.SmsSendService;
import com.wangchenyang.admin.service.SysSmsTemplateService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import com.wangchenyang.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 短信模板
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/smstemplate")
public class SysSmsTemplateController {

	private final SysSmsTemplateService sysSmsTemplateService;

	private final SmsSendService smsSendService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysSmsTemplate 短信模板
	 * @return
	 */
	@GetMapping("/page")
	@SaCheckPermission("smstemplate_get")
	public R getSysSmsTemplatePage(Page page, SysSmsTemplate sysSmsTemplate) {
		return R.ok(sysSmsTemplateService.page(page, Wrappers.query(sysSmsTemplate)));
	}

	/**
	 * 通过id查询短信模板
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	@SaCheckPermission("smstemplate_get")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(sysSmsTemplateService.getById(id));
	}

	/**
	 * 新增短信模板
	 * @param sysSmsTemplate 短信模板
	 * @return R
	 */
	@SysLog("新增短信模板")
	@PostMapping
	@SaCheckPermission("smstemplate_add")
	public R save(@RequestBody @Validated SysSmsTemplate sysSmsTemplate) {
		sysSmsTemplateService.createSmsTemplate(sysSmsTemplate);
		return R.ok();
	}

	/**
	 * 修改短信模板
	 * @param sysSmsTemplate 短信模板
	 * @return R
	 */
	@SysLog("修改短信模板")
	@PutMapping
	@SaCheckPermission("smstemplate_edit")
	public R updateById(@RequestBody @Validated SysSmsTemplate sysSmsTemplate) {
		sysSmsTemplateService.updateSmsTemplate(sysSmsTemplate);
		return R.ok();
	}

	/**
	 * 通过id删除短信模板
	 * @param id id
	 * @return R
	 */
	@SysLog("通过id删除短信模板")
	@DeleteMapping("/{id}")
	@SaCheckPermission("smstemplate_del")
	public R removeById(@PathVariable Long id) {
		return R.ok(sysSmsTemplateService.removeById(id));
	}

	/**
	 * 测试发送短信
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/24 14:08
	 * @desc
	 **/
	@PostMapping("/send")
	@SaCheckPermission("smstemplate_edit")
	public R sendSms(@Valid @RequestBody SmsTemplateSendReqVO sendReqVO) {
		return R.ok(smsSendService.sendSingleSmsToAdmin(sendReqVO.getMobile(), LoginHelper.getUserId(),
				sendReqVO.getTemplateCode(), sendReqVO.getTemplateParams()));
	}

}
