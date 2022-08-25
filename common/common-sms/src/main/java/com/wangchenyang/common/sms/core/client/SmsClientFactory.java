package com.wangchenyang.common.sms.core.client;

import com.wangchenyang.common.sms.core.property.SmsChannelProperties;

/**
 * 短信客户端的工厂接口
 *
 */
public interface SmsClientFactory {

	/**
	 * 获得短信 Client
	 * @param channelId 渠道编号
	 * @return 短信 Client
	 */
	SmsClient getSmsClient(Long channelId);

	/**
	 * 获得短信 Client
	 * @param channelCode 渠道编号
	 * @return 短信 Client
	 */
	SmsClient getSmsClient(String channelCode);

	/**
	 * 移除短信 Client
	 *
	 * @author 王晨阳
	 * @param channelId 渠道编号
	 * @version 1.0
	 * @date 2022/8/23 16:48
	 */
	void removeSmsClient(Long channelId);

	/**
	 * 创建短信 Client
	 * @param properties 配置对象
	 */
	void createOrUpdateSmsClient(SmsChannelProperties properties);

}
