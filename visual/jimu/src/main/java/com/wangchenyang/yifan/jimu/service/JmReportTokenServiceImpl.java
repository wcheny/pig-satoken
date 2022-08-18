package com.wangchenyang.yifan.jimu.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.http.HttpStatus;

import java.util.Map;


@RequiredArgsConstructor
public class JmReportTokenServiceImpl implements JmReportTokenServiceI {

    /**
     * 校验 Token 是否有效，即验证通过
     *
     * @param token JmReport 前端传递的 token
     * @return 是否认证通过
     */
    @Override
    public Boolean verifyToken(String token) {
		if (StrUtil.isBlank(token)) {
			return false;
		}
		Object loginId = StpUtil.getLoginIdByToken(token);
		// 令牌不存在
		if (loginId == null) {
			return false;
		}
		return true;
    }

	@Override
	public String getUsername(String token) {
		return "admin";
	}
}
