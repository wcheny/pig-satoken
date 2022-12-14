/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yifan4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.wangchenyang.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.admin.mapper.SysUserMapper;
import com.wangchenyang.admin.service.AppService;
import com.wangchenyang.admin.service.SmsSendService;
import com.wangchenyang.common.core.constant.CacheConstants;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.core.exception.ErrorCodes;
import com.wangchenyang.common.core.util.MsgUtils;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.redis.utils.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/11/14
 * <p>
 * 手机登录相关业务实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {

	private final SysUserMapper userMapper;

	private final SmsSendService smsSendService;

	// 验证码模板Code
	private final static String SEND_TEMPLATE_CODE = "";

	/**
	 * 发送手机验证码
	 * @param phone 手机号
	 * @return code
	 */
	@Override
	public R<Boolean> sendSmsCode(String phone) {
		List<SysUser> userList = userMapper.selectList(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, phone));

		if (CollUtil.isEmpty(userList)) {
			log.info("手机号未注册:{}", phone);
			return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_PHONE_UNREGISTERED, phone));
		}
		Object codeObj = RedisUtils.getCacheObject(CacheConstants.DEFAULT_CODE_KEY + phone);

		if (codeObj != null) {
			log.info("手机号验证码未过期:{}，{}", phone, codeObj);
			return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_OFTEN));
		}

		String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
		log.info("手机号生成验证码成功:{},{}", phone, code);
		RedisUtils.setCacheObject(CacheConstants.DEFAULT_CODE_KEY + phone, code,
				Duration.ofSeconds(SecurityConstants.CODE_TIME));
		// 调用短信通道发送
		Map<String, Object> paramsMap = new HashMap<>(1);
		paramsMap.put("code", code);
		smsSendService.sendSingleSmsToAdmin(phone, null, SEND_TEMPLATE_CODE, paramsMap);
		return R.ok(Boolean.TRUE);
	}

}
