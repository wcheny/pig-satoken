package com.wangchenyang.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangchenyang.admin.api.entity.SysSmsLog;
import com.wangchenyang.admin.api.entity.SysSmsTemplate;
import com.wangchenyang.admin.api.enums.SmsReceiveStatusEnum;
import com.wangchenyang.admin.api.enums.SmsSendStatusEnum;
import com.wangchenyang.admin.mapper.SysSmsLogMapper;
import com.wangchenyang.admin.service.SysSmsLogService;
import com.wangchenyang.common.core.util.R;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 短信日志
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@Service
public class SysSmsLogServiceImpl extends ServiceImpl<SysSmsLogMapper, SysSmsLog> implements SysSmsLogService {

	@Override
	public Long createSmsLog(String mobile, Long userId, Integer userType, Boolean isSend, SysSmsTemplate template,
			String templateContent, Map<String, Object> templateParams) {
		SysSmsLog.SysSmsLogBuilder logBuilder = SysSmsLog.builder();
		// 根据是否要发送，设置状态
		logBuilder.sendStatus(Objects.equals(isSend, true) ? SmsSendStatusEnum.INIT.getStatus()
				: SmsSendStatusEnum.IGNORE.getStatus());
		// 设置手机相关字段
		logBuilder.mobile(mobile).userId(userId).userType(userType);
		// 设置模板相关字段
		logBuilder.templateId(template.getId()).templateCode(template.getCode()).templateType(template.getType());
		logBuilder.templateContent(templateContent).templateParams(JSON.toJSONString(templateParams))
				.apiTemplateId(template.getApiTemplateId());
		// 设置渠道相关字段
		logBuilder.channelId(template.getChannelId()).channelCode(template.getChannelCode());
		// 设置接收相关字段
		logBuilder.receiveStatus(SmsReceiveStatusEnum.INIT.getStatus());
		// 插入数据库
		SysSmsLog log = logBuilder.build();
		baseMapper.insert(log);
		return log.getId();
	}

	@Override
	public void updateSmsSendResult(Long id, Integer sendCode, String sendMsg, String apiSendCode, String apiSendMsg,
			String apiRequestId, String apiSerialNo) {
		SmsSendStatusEnum sendStatus = R.ok().getCode() == sendCode ? SmsSendStatusEnum.SUCCESS
				: SmsSendStatusEnum.FAILURE;
		baseMapper.updateById(SysSmsLog.builder().id(id).sendStatus(sendStatus.getStatus()).sendTime(new Date())
				.sendCode(sendCode).sendMsg(sendMsg).apiSendCode(apiSendCode).apiSendMsg(apiSendMsg)
				.apiRequestId(apiRequestId).apiSerialNo(apiSerialNo).build());
	}

	@Override
	public void updateSmsReceiveResult(Long id, Boolean success, Date receiveTime, String apiReceiveCode,
			String apiReceiveMsg) {
		SmsReceiveStatusEnum receiveStatus = Objects.equals(success, true) ? SmsReceiveStatusEnum.SUCCESS
				: SmsReceiveStatusEnum.FAILURE;
		baseMapper.updateById(SysSmsLog.builder().id(id).receiveStatus(receiveStatus.getStatus())
				.receiveTime(receiveTime).apiReceiveCode(apiReceiveCode).apiReceiveMsg(apiReceiveMsg).build());
	}

}
