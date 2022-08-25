package com.wangchenyang.admin.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.annotations.VisibleForTesting;
import com.wangchenyang.admin.api.entity.SysSmsChannel;
import com.wangchenyang.admin.api.entity.SysSmsTemplate;
import com.wangchenyang.admin.api.util.DictResolver;
import com.wangchenyang.admin.api.util.ParamResolver;
import com.wangchenyang.admin.mapper.SysSmsTemplateMapper;
import com.wangchenyang.admin.service.SysSmsChannelService;
import com.wangchenyang.admin.service.SysSmsTemplateService;
import com.wangchenyang.common.core.constant.CacheConstants;
import com.wangchenyang.common.core.constant.CommonConstants;
import com.wangchenyang.common.core.constant.enums.CommonStatusEnum;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.redis.utils.RedisUtils;
import com.wangchenyang.common.sms.core.client.SmsClient;
import com.wangchenyang.common.sms.core.client.SmsClientFactory;
import com.wangchenyang.common.sms.core.client.SmsCommonResult;
import com.wangchenyang.common.sms.core.client.dto.SmsTemplateRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 短信模板
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@Service
@RequiredArgsConstructor
public class SysSmsTemplateServiceImpl extends ServiceImpl<SysSmsTemplateMapper, SysSmsTemplate> implements SysSmsTemplateService {

	private final SysSmsChannelService smsChannelService;

	private final SmsClientFactory smsClientFactory;

	/**
	 * 正则表达式，匹配 {} 中的变量
	 */
	private static final Pattern PATTERN_PARAMS = Pattern.compile("\\{(.*?)}");

    @Override
    public void createSmsTemplate(SysSmsTemplate sysSmsTemplate) {
		SysSmsChannel channel = checkSmsChannel(sysSmsTemplate.getChannelId());
		checkSmsTemplateCodeDuplicate(null, sysSmsTemplate.getCode());
		checkApiTemplate(sysSmsTemplate.getChannelId(), sysSmsTemplate.getApiTemplateId());
		sysSmsTemplate.setParams(JSONUtil.toJsonStr(ReUtil.findAllGroup1(PATTERN_PARAMS, sysSmsTemplate.getContent())));
		sysSmsTemplate.setChannelCode(channel.getCode());
		sysSmsTemplate.setDelFlag(CommonConstants.STATUS_NORMAL);
		baseMapper.insert(sysSmsTemplate);
	}

	@Override
	public void updateSmsTemplate(SysSmsTemplate sysSmsTemplate) {
		// 校验短信渠道
		SysSmsChannel channel = checkSmsChannel(sysSmsTemplate.getChannelId());
		// 校验短信编码是否重复
		checkSmsTemplateCodeDuplicate(sysSmsTemplate.getId(), sysSmsTemplate.getCode());
		// 校验短信模板
		checkApiTemplate(sysSmsTemplate.getChannelId(), sysSmsTemplate.getApiTemplateId());
		sysSmsTemplate.setParams(JSONUtil.toJsonStr(ReUtil.findAllGroup1(PATTERN_PARAMS, sysSmsTemplate.getContent())));
		sysSmsTemplate.setChannelCode(channel.getCode());
		baseMapper.updateById(sysSmsTemplate);
	}

    @Override
    public String formatSmsTemplateContent(String content, Map<String, Object> params) {
		return StrUtil.format(content, params);
    }

	@Override
	public SysSmsTemplate selectByCode(String code) {
		return lambdaQuery().eq(SysSmsTemplate::getCode,code).one();
	}

	/**
	 * 校验 API 短信平台的模板是否有效
	 *
	 * @param channelId 渠道编号
	 * @param apiTemplateId API 模板编号
	 */
	public void checkApiTemplate(Long channelId, String apiTemplateId) {
		// 获得短信模板
		SmsClient smsClient = smsClientFactory.getSmsClient(channelId);
		Assert.notNull(smsClient, String.format("短信客户端(%d) 不存在", channelId));
		SmsCommonResult<SmsTemplateRespDTO> templateResult = smsClient.getSmsTemplate(apiTemplateId);
		if(R.ok().getCode()!=templateResult.getCode()){
			throw new RuntimeException(templateResult.getMsg());
		}
	}

	public void checkSmsTemplateCodeDuplicate(Long id, String code) {
		SysSmsTemplate template = baseMapper.selectOne(Wrappers.lambdaQuery(SysSmsTemplate.class).eq(SysSmsTemplate::getCode,code));
		if (template == null) {
			return;
		}
		// 如果 id 为空，说明不用比较是否为相同 id 的字典类型
		if (id == null) {
			throw new RuntimeException("已经存在编码为【"+code+"】的短信模板");
		}
		if (!template.getId().equals(id)) {
			throw new RuntimeException("已经存在编码为【"+code+"】的短信模板");
		}
	}


	public SysSmsChannel checkSmsChannel(Long channelId) {
		SysSmsChannel channel = smsChannelService.getById(channelId);
		if (channel == null) {
			throw new RuntimeException("短信渠道未找到");
		}
		if (!Objects.equals(channel.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
			throw new RuntimeException("短信渠道已关闭");
		}
		return channel;
	}
}
