package com.wangchenyang.yifan.jimu.service;

import cn.hutool.core.util.StrUtil;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.security.service.YifanUser;
import com.wangchenyang.common.security.util.OAuth2EndpointUtils;
import com.wangchenyang.common.security.util.OAuth2ErrorCodesExpand;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.util.Map;


@RequiredArgsConstructor
public class JmReportTokenServiceImpl implements JmReportTokenServiceI {

	private final OAuth2AuthorizationService authorizationService;

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
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		// 令牌不存在
		if (authorization == null || authorization.getAccessToken() == null) {
			return false;
		}
		return true;
    }

	@Override
	public String getUsername(String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		Map<String, Object> claims = authorization.getAccessToken().getClaims();
		YifanUser user = (YifanUser) claims.get(SecurityConstants.DETAILS_USER);
		return user.getUsername();
	}
}
