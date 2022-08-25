package com.wangchenyang.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangchenyang.admin.api.entity.SysSmsChannel;

/**
 * 短信渠道
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
public interface SysSmsChannelService extends IService<SysSmsChannel> {

    void createSmsChannel(SysSmsChannel sysSmsChannel);

	/**
	 * 初始化短信客户端
	 */
	void initSmsClients();

	void updateSmsChannel(SysSmsChannel sysSmsChannel);

	void delById(Long id);
}
