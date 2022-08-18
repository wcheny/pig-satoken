package com.wangchenyang.auth.listener;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.wangchenyang.admin.api.entity.SysLog;
import com.wangchenyang.common.core.constant.CacheConstants;
import com.wangchenyang.common.core.constant.enums.UserType;
import com.wangchenyang.common.core.dto.LoginUser;
import com.wangchenyang.common.core.dto.SysUserOnline;
import com.wangchenyang.common.core.util.SpringContextHolder;
import com.wangchenyang.common.core.util.WebUtils;
import com.wangchenyang.common.log.event.SysLogEvent;
import com.wangchenyang.common.log.util.SysLogUtils;
import com.wangchenyang.common.redis.utils.RedisUtils;
import com.wangchenyang.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;

/**
 * 用户行为 侦听器的实现
 *
 * @author Lion Li
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UserActionListener implements SaTokenListener {

	private final SaTokenConfig tokenConfig;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
		String username = LoginHelper.getUsername();
		SysLog logVo = SysLogUtils.getSysLog();
		logVo.setTitle(username+"登录成功");
		// 发送异步日志事件
		logVo.setTime(0L);
		logVo.setCreateBy(username);
		logVo.setUpdateBy(username);
		SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		SysUserOnline userOnline = buildUser();
		userOnline.setTokenId(tokenValue);
		userOnline.setTokenTimeout(loginModel.getTimeout());
		RedisUtils.setCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue, userOnline, Duration.ofSeconds(tokenConfig.getTimeout()));
    }

	public SysUserOnline buildUser(){
		SysUserOnline userOnline=new SysUserOnline();
		if( WebUtils.getRequest().isPresent()){
			HttpServletRequest request=WebUtils.getRequest().get();
			UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
			userOnline.setBrowser(userAgent.getBrowser().getName());
			userOnline.setOs(userAgent.getOs().getName());
			userOnline.setIpaddr(ServletUtil.getClientIP(request));
		}
		LoginUser loginUser = LoginHelper.getLoginUser();
		userOnline.setUserName(loginUser.getUsername());
		userOnline.setUserType(loginUser.getUserType());
		userOnline.setLoginTime(new Date());
		return userOnline;
	}

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
		RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
		RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
		RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }


}
