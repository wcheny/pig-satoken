package com.wangchenyang.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.wangchenyang.admin.api.entity.SysLog;
import com.wangchenyang.auth.form.LoginBody;
import com.wangchenyang.auth.service.LoginService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.core.util.SpringContextHolder;
import com.wangchenyang.common.log.event.SysLogEvent;
import com.wangchenyang.common.log.util.SysLogUtils;
import com.wangchenyang.common.satoken.utils.LoginHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * token 控制
 *
 */
@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

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
