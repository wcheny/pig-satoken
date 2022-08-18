package com.wangchenyang.auth.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.nacos.api.common.Constants;
import com.wangchenyang.admin.api.entity.SysLog;
import com.wangchenyang.auth.form.LoginBody;
import com.wangchenyang.auth.service.SysLoginService;
import com.wangchenyang.common.core.constant.enums.DeviceType;
import com.wangchenyang.common.core.constant.enums.UserType;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.core.util.SpringContextHolder;
import com.wangchenyang.common.log.event.SysLogEvent;
import com.wangchenyang.common.log.util.SysLogUtils;
import com.wangchenyang.common.satoken.utils.LoginHelper;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * token 控制
 *
 */
@RestController
public class TokenController {

	@Autowired
	private SysLoginService loginService;

    /**
     * 后台登陆
     */
    @PostMapping("/login")
    public R login(@Validated LoginBody loginBody) {
        return loginService.login(loginBody);
    }

	/**
	 * 登出方法
	 */
	@DeleteMapping("logout")
	public R<Void> logout() {
		try{
			String username = LoginHelper.getUsername();
			StpUtil.logout();
			SysLog logVo = SysLogUtils.getSysLog();
			logVo.setTitle(username+"退出登陆");
			// 发送异步日志事件
			logVo.setTime(0L);
			logVo.setCreateBy(username);
			logVo.setUpdateBy(username);
			SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		}catch (Exception e){}
		return R.ok();
	}

}
