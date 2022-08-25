package com.wangchenyang.common.sms.core.client.impl.aliyun;

import cn.hutool.http.HttpStatus;
import com.squareup.okhttp.internal.framed.ErrorCode;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.sms.core.client.SmsCodeMapping;

/**
 * 阿里云的 SmsCodeMapping 实现类
 *
 * 参见 https://help.aliyun.com/document_detail/101346.htm 文档
 *
 */
public class AliyunSmsCodeMapping implements SmsCodeMapping {

	@Override
	public Integer apply(String apiCode) {
		switch (apiCode) {
			case "OK":
				return R.ok().getCode();
			default:
				return R.failed().getCode();
		}
	}

}
