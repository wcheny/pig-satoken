package com.wangchenyang.common.sms.core.client.impl.tencent;

import com.squareup.okhttp.internal.framed.ErrorCode;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.sms.core.client.SmsCodeMapping;

/**
 * 腾讯云的 SmsCodeMapping 实现类
 *
 * 参见
 * https://cloud.tencent.com/document/api/382/52075#.E5.85.AC.E5.85.B1.E9.94.99.E8.AF.AF.E7.A0.81
 *
 */
public class TencentSmsCodeMapping implements SmsCodeMapping {

	@Override
	public Integer apply(String apiCode) {
		switch (apiCode) {
			case TencentSmsClient.API_SUCCESS_CODE:
				return R.ok().getCode();
			default:
				return R.failed().getCode();
		}
	}

}
