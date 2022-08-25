package com.wangchenyang.common.sms.core.client.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短信发送 Response DTO
 *
 */
@Data
@Accessors(chain = true)
public class SmsSendRespDTO {

	/**
	 * 短信 API 发送返回的序号
	 */
	private String serialNo;

}
