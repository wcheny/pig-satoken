/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yifan4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.wangchenyang.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wangchenyang.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公共参数配置
 *
 * @author Lucky
 * @date 2019-04-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPublicParam extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long publicId;

	/**
	 * 公共参数名称
	 */
	private String publicName;

	/**
	 * 公共参数地址值,英文大写+下划线
	 */
	private String publicKey;

	/**
	 * 值
	 */
	private String publicValue;

	/**
	 * 状态（1有效；2无效；）
	 */
	private String status;

	/**
	 * 公共参数编码
	 */
	private String validateCode;

	/**
	 * 是否是系统内置
	 */
	private String systemFlag;

	/**
	 * 配置类型：0-默认；1-检索；2-原文；3-报表；4-安全；5-文档；6-消息；9-其他
	 */
	private String publicType;

}
