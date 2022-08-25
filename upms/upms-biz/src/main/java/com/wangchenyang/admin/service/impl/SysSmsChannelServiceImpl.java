package com.wangchenyang.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangchenyang.admin.api.entity.SysSmsChannel;
import com.wangchenyang.admin.mapper.SysSmsChannelMapper;
import com.wangchenyang.admin.service.SysSmsChannelService;
import com.wangchenyang.common.core.constant.CommonConstants;
import com.wangchenyang.common.core.util.SpringContextHolder;
import com.wangchenyang.common.sms.core.client.SmsClientFactory;
import com.wangchenyang.common.sms.core.property.SmsChannelProperties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 短信渠道
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@Service
public class SysSmsChannelServiceImpl extends ServiceImpl<SysSmsChannelMapper, SysSmsChannel> implements SysSmsChannelService {

	@Autowired
	private SmsClientFactory smsClientFactory;

    @Override
    public void createSmsChannel(SysSmsChannel sysSmsChannel) {
		sysSmsChannel.setDelFlag(CommonConstants.STATUS_NORMAL);
		save(sysSmsChannel);
		SpringContextHolder.getApplicationContext().publishEvent(buildProperties(sysSmsChannel));
    }

	@Override
	@PostConstruct
	public void initSmsClients() {
		List<SysSmsChannel> smsChannels = lambdaQuery().eq(SysSmsChannel::getStatus, 0).list();
		smsChannels.forEach(item->smsClientFactory.createOrUpdateSmsClient(buildProperties(item)));
	}

	@Override
	public void updateSmsChannel(SysSmsChannel sysSmsChannel) {
		updateById(sysSmsChannel);
		SpringContextHolder.getApplicationContext().publishEvent(buildProperties(sysSmsChannel));
	}

	@Override
	public void delById(Long id) {
		removeById(id);
		SpringContextHolder.getApplicationContext().publishEvent(new SmsChannelProperties().setId(id));
	}

	private SmsChannelProperties buildProperties(SysSmsChannel channel){
		return new SmsChannelProperties().setCode(channel.getCode())
				.setApiKey(channel.getApiKey()).setApiSecret(channel.getApiSecret())
				.setId(channel.getId()).setSignature(channel.getSignature())
				.setCallbackUrl(channel.getCallbackUrl());
	}
}
