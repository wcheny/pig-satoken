package com.wangchenyang.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.annotations.VisibleForTesting;
import com.wangchenyang.admin.api.dto.SmsSendMessage;
import com.wangchenyang.admin.api.entity.SysSmsChannel;
import com.wangchenyang.admin.api.entity.SysSmsTemplate;
import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.admin.service.*;
import com.wangchenyang.common.core.constant.enums.CommonStatusEnum;
import com.wangchenyang.common.core.constant.enums.UserTypeEnum;
import com.wangchenyang.common.core.dto.KeyValue;
import com.wangchenyang.common.core.util.SpringContextHolder;
import com.wangchenyang.common.sms.core.client.SmsClient;
import com.wangchenyang.common.sms.core.client.SmsClientFactory;
import com.wangchenyang.common.sms.core.client.SmsCommonResult;
import com.wangchenyang.common.sms.core.client.dto.SmsReceiveRespDTO;
import com.wangchenyang.common.sms.core.client.dto.SmsSendRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短信发送 Service 发送的实现
 *
 */
@Service
@RequiredArgsConstructor
public class SmsSendServiceImpl implements SmsSendService {

	private final SysSmsTemplateService smsTemplateService;

	private final SysSmsChannelService smsChannelService;

	private final SysSmsLogService smsLogService;

	private final SmsClientFactory smsClientFactory;

	private final SysUserService sysUserService;

	@Override
	public Long sendSingleSmsToAdmin(String mobile, Long userId, String templateCode,
			Map<String, Object> templateParams) {
		// 如果 mobile 为空，则加载用户编号对应的手机号
		if (StrUtil.isEmpty(mobile)) {
			SysUser sysUser = sysUserService.getById(userId);
			if (sysUser != null) {
				mobile = sysUser.getPhone();
			}
		}
		// 执行发送
		return this.sendSingleSms(mobile, userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
	}

	@Override
	public Long sendSingleSmsToMember(String mobile, Long userId, String templateCode,
			Map<String, Object> templateParams) {
		// 执行发送
		return this.sendSingleSms(mobile, userId, UserTypeEnum.MEMBER.getValue(), templateCode, templateParams);
	}

	@Override
	public Long sendSingleSms(String mobile, Long userId, Integer userType, String templateCode,
			Map<String, Object> templateParams) {
		// 校验短信模板是否合法
		SysSmsTemplate template = this.checkSmsTemplateValid(templateCode);
		// 校验短信渠道是否合法
		SysSmsChannel smsChannel = this.checkSmsChannelValid(template.getChannelId());

		// 校验手机号码是否存在
		mobile = this.checkMobile(mobile);
		// 构建有序的模板参数。为什么放在这个位置，是提前保证模板参数的正确性，而不是到了插入发送日志
		List<KeyValue<String, Object>> newTemplateParams = this.buildTemplateParams(template, templateParams);

		// 创建发送日志。如果模板被禁用，则不发送短信，只记录日志
		Boolean isSend = CommonStatusEnum.ENABLE.getStatus().equals(template.getStatus())
				&& CommonStatusEnum.ENABLE.getStatus().equals(smsChannel.getStatus());
		;
		String content = smsTemplateService.formatSmsTemplateContent(template.getContent(), templateParams);
		Long sendLogId = smsLogService.createSmsLog(mobile, userId, userType, isSend, template, content,
				templateParams);

		// 异步执行发送短信
		if (isSend) {
			SmsSendMessage message = new SmsSendMessage().setLogId(sendLogId).setMobile(mobile);
			message.setChannelId(template.getChannelId()).setApiTemplateId(template.getApiTemplateId())
					.setTemplateParams(newTemplateParams);
			SpringContextHolder.getApplicationContext().publishEvent(message);
		}
		return sendLogId;
	}

	public SysSmsChannel checkSmsChannelValid(Long channelId) {
		SysSmsChannel channel = smsChannelService.getById(channelId);
		// 短信模板不存在
		if (channel == null) {
			throw new RuntimeException("短信渠道不存在");
		}
		return channel;
	}

	public SysSmsTemplate checkSmsTemplateValid(String templateCode) {
		// 获得短信模板
		SysSmsTemplate template = smsTemplateService.selectByCode(templateCode);
		// 短信模板不存在
		if (template == null) {
			throw new RuntimeException("短信模板不存在");
		}
		return template;
	}

	/**
	 * 将参数模板，处理成有序的 KeyValue 数组
	 * <p>
	 * 原因是，部分短信平台并不是使用 key 作为参数，而是数组下标，例如说腾讯云
	 * https://cloud.tencent.com/document/product/382/39023
	 * @param template 短信模板
	 * @param templateParams 原始参数
	 * @return 处理后的参数
	 */
	@VisibleForTesting
	public List<KeyValue<String, Object>> buildTemplateParams(SysSmsTemplate template,
			Map<String, Object> templateParams) {
		List<String> params = JSON.parseArray(template.getParams(), String.class);
		return params.stream().map(key -> {
			Object value = templateParams.get(key);
			if (value == null) {
				throw new RuntimeException("模板参数不存在");
			}
			return new KeyValue<>(key, value);
		}).collect(Collectors.toList());
	}

	public String checkMobile(String mobile) {
		if (StrUtil.isEmpty(mobile)) {
			throw new RuntimeException("手机号错误");
		}
		return mobile;
	}

	@Override
	public void doSendSms(SmsSendMessage message) {
		// 获得渠道对应的 SmsClient 客户端
		SmsClient smsClient = smsClientFactory.getSmsClient(message.getChannelId());
		Assert.notNull(smsClient, "短信客户端({}) 不存在", message.getChannelId());
		// 发送短信
		SmsCommonResult<SmsSendRespDTO> sendResult = smsClient.sendSms(message.getLogId(), message.getMobile(),
				message.getApiTemplateId(), message.getTemplateParams());
		smsLogService.updateSmsSendResult(message.getLogId(), sendResult.getCode(), sendResult.getMsg(),
				sendResult.getApiCode(), sendResult.getApiMsg(), sendResult.getApiRequestId(),
				sendResult.getData() != null ? sendResult.getData().getSerialNo() : null);
	}

	@Override
	public void receiveSmsStatus(String channelCode, String text) throws Throwable {
		// 获得渠道对应的 SmsClient 客户端
		SmsClient smsClient = smsClientFactory.getSmsClient(channelCode);
		Assert.notNull(smsClient, "短信客户端({}) 不存在", channelCode);
		// 解析内容
		List<SmsReceiveRespDTO> receiveResults = smsClient.parseSmsReceiveStatus(text);
		if (CollUtil.isEmpty(receiveResults)) {
			return;
		}
		// 更新短信日志的接收结果. 因为量一般不大，所以先使用 for 循环更新
		receiveResults.forEach(result -> smsLogService.updateSmsReceiveResult(result.getLogId(), result.getSuccess(),
				result.getReceiveTime(), result.getErrorCode(), result.getErrorCode()));
	}

}
