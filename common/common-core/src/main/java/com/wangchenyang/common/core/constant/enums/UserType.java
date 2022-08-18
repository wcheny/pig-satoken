package com.wangchenyang.common.core.constant.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 用户类型
 *
 */
@Getter
@AllArgsConstructor
public enum UserType {

	/**
	 * pc端
	 */
	SYS_USER("sys_user");

	private final String userType;

	public static UserType getUserType(String str) {
		for (UserType value : values()) {
			if (StrUtil.contains(str, value.getUserType())) {
				return value;
			}
		}
		throw new RuntimeException("'用户类型错误：" + str);
	}

}
