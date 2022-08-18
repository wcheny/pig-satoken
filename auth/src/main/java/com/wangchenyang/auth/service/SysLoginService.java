package com.wangchenyang.auth.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.admin.api.feign.RemoteUserService;
import com.wangchenyang.auth.form.LoginBody;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.core.constant.enums.DeviceType;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.exception.ValidateCodeException;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.core.util.RetOps;
import com.wangchenyang.common.feign.sentinel.handle.GlobalBizExceptionHandler;
import com.wangchenyang.common.satoken.utils.LoginHelper;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.misc.MessageUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
@Service
public class SysLoginService {

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
