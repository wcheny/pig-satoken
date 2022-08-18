/*
 * Copyright (c) 2020 yifan4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wangchenyang.admin.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.common.core.constant.CacheConstants;
import com.wangchenyang.common.core.constant.CommonConstants;
import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.core.dto.SysUserOnline;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2018/9/4 getTokenPage 管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

	/**
	 * 分页token 信息
	 * @param params 参数集
	 * @return token集合
	 */
	@GetMapping("/page")
	public R token(@RequestParam Map<String, Object> params) {// 获取所有未过期的 token
		List<String> keys = StpUtil.searchTokenValue("", -1, 0);
		int current = MapUtil.getInt(params, CommonConstants.CURRENT);
		int size = MapUtil.getInt(params, CommonConstants.SIZE);
		List<String> pages = keys.stream().skip((long) (current - 1) * size).limit(size).collect(Collectors.toList());
		Page result = new Page(current, size);
		List<SysUserOnline> collect = pages.stream().map(obj -> {
			String token = obj.replace(CacheConstants.LOGIN_TOKEN_KEY, "");
			// 如果已经过期则踢下线
			if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < 0) {
				return null;
			}
			return (SysUserOnline)RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token);
		}).filter(Objects::nonNull).collect(Collectors.toList());
		result.setRecords(collect);
		result.setTotal(keys.size());
		return R.ok(result);
	}

	/**
	 * 删除
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_token_del')")
	public R<Boolean> delete(@PathVariable String id) {
		try {
			StpUtil.kickoutByTokenValue(id);
		} catch (NotLoginException e) {
		}
		return R.ok(true);
	}

}
