package com.wangchenyang.admin.consumer;

import com.wangchenyang.admin.api.dto.SmsSendMessage;
import com.wangchenyang.admin.service.SmsSendService;
import com.wangchenyang.common.core.dto.KeyValue;
import com.wangchenyang.common.sms.core.client.SmsClientFactory;
import com.wangchenyang.common.sms.core.property.SmsChannelProperties;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信监听
 *
 * @author 王晨阳
 * @version 1.0
 * @date 2022/8/25 10:27
 * @desc
 **/
@Component
@AllArgsConstructor
public class SmsListener {

	private final SmsClientFactory smsClientFactory;

	private final SmsSendService smsSendService;

	/**
	 * 短信渠道监听
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 10:28
	 * @desc
	 **/
	@EventListener
	@Async
	public void createOrUpdateSmsClientEvent(SmsChannelProperties smsChannelProperties) {
		if (StringUtils.isNotEmpty(smsChannelProperties.getApiKey())) {
			smsClientFactory.createOrUpdateSmsClient(smsChannelProperties);
		}
		else {
			smsClientFactory.removeSmsClient(smsChannelProperties.getId());
		}

	}

	/**
	 * 发送短信
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 10:28
	 * @desc
	 **/
	@EventListener
	@Async
	public void sendSmsSendMessage(SmsSendMessage message) {
		smsSendService.doSendSms(message);
	}

}
