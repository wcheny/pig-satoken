package com.wangchenyang.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.admin.service.AppService;
import com.wangchenyang.admin.service.SysUserService;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.exception.ErrorCodes;
import com.wangchenyang.common.core.util.MsgUtils;
import com.wangchenyang.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lengleng
 * @date 2021/9/16 移动端登录
 */
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class AppController {

	private final AppService appService;

	private final SysUserService userService;

	@GetMapping("/{mobile}")
	public R<Boolean> sendSmsCode(@PathVariable String mobile) {
		return appService.sendSmsCode(mobile);
	}

	/**
	 * 获取指定用户全部信息
	 * @param phone 手机号
	 * @return 用户信息
	 */
	@GetMapping("/info/{phone}")
	public R<LoginUser> infoByMobile(@PathVariable String phone) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, phone));
		if (user == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, phone));
		}
		return R.ok(userService.getUserInfo(user));
	}

}
