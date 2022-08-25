package com.wangchenyang.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wangchenyang.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

/**
 * 短信渠道
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@Data
@TableName("sys_sms_channel")
@EqualsAndHashCode(callSuper = true)
public class SysSmsChannel extends BaseEntity {

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 短信签名
	 */
	@NotNull(message = "短信签名不能为空")
	private String signature;

	/**
	 * 渠道编码
	 */
	@NotNull(message = "渠道编码不能为空")
	private String code;

	/**
	 * 开启状态
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 短信 API 的账号
	 */
	@NotNull(message = "短信 API 的账号不能为空")
	private String apiKey;

	/**
	 * 短信 API 的秘钥
	 */
	@NotNull(message = "短信 API 的秘钥不能为空")
	private String apiSecret;

	/**
	 * 短信发送回调 URL
	 */
	@URL(message = "回调 URL 格式不正确")
	private String callbackUrl;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	private String delFlag;

}
