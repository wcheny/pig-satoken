package com.wangchenyang.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangchenyang.admin.api.entity.SysSmsTemplate;

import java.util.Map;

/**
 * 短信模板
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
public interface SysSmsTemplateService extends IService<SysSmsTemplate> {

    void createSmsTemplate(SysSmsTemplate sysSmsTemplate);

	void updateSmsTemplate(SysSmsTemplate sysSmsTemplate);
	/**
	 * 格式化短信内容
	 *
	 * @param content 短信模板的内容
	 * @param params 内容的参数
	 * @return 格式化后的内容
	 */
    String formatSmsTemplateContent(String content, Map<String, Object> params);
	/**
	 * 查询模板
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 11:00
	 * @desc
	**/
	SysSmsTemplate selectByCode(String code);
}
