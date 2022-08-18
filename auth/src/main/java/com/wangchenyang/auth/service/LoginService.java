package com.wangchenyang.auth.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.wangchenyang.admin.api.feign.RemoteUserService;
import com.wangchenyang.auth.form.LoginBody;
import com.wangchenyang.common.core.constant.enums.DeviceType;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.core.util.RetOps;
import com.wangchenyang.common.satoken.utils.LoginHelper;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class LoginService {

	private final RemoteUserService remoteUserService;

	public R login(LoginBody loginBody) {
		R<LoginUser> result = remoteUserService.info(loginBody.getUsername());
		LoginUser userInfo = RetOps.of(result).getData().orElseThrow(() -> new RuntimeException(result.getMsg()));
		if(!BCrypt.checkpw(loginBody.getPassword(), userInfo.getPassword())){
			return R.failed("密码错误");
		}
		// 获取登录token
		LoginHelper.loginByDevice(userInfo, DeviceType.PC);
		return R.ok(StpUtil.getTokenValue());
	}

}
