package com.wangchenyang.common.sms.core.client;

import com.wangchenyang.common.core.util.R;

import java.util.function.Function;

/**
 * 将 API 的错误码，转换为通用的错误码
 *
 * @see SmsCommonResult
 *
 */
public interface SmsCodeMapping extends Function<String, Integer> {
}
