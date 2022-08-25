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
	/**
	 * 创建短信渠道
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 15:49
	 * @desc
	**/
	void createSmsChannel(SysSmsChannel sysSmsChannel);

	/**
	 * 初始化短信渠道
	 */
	void initSmsClients();
	/**
	 * 更新短信渠道
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 15:49
	 * @desc
	**/
	void updateSmsChannel(SysSmsChannel sysSmsChannel);
	/**
	 * 删除短信渠道
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/25 15:50
	 * @desc
	**/
	void delById(Long id);

}
