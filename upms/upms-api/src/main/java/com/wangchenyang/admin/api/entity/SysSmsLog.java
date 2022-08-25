package com.wangchenyang.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wangchenyang.common.mybatis.base.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * 短信日志
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@Data
@TableName("sys_sms_log")
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysSmsLog extends BaseEntity {

	/**
	 * 编号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 短信渠道编号
	 */
	private Long channelId;

	/**
	 * 短信渠道编码
	 */
	private String channelCode;

	/**
	 * 模板编号
	 */
	private Long templateId;

	/**
	 * 模板编码
	 */
	private String templateCode;

	/**
	 * 短信类型
	 */
	private Integer templateType;

	/**
	 * 短信内容
	 */
	private String templateContent;

	/**
	 * 短信参数
	 */
	private String templateParams;

	/**
	 * 短信 API 的模板编号
	 */
	private String apiTemplateId;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 用户类型
	 */
	private Integer userType;

	/**
	 * 发送状态
	 */
	private Integer sendStatus;

	/**
	 * 发送时间
	 */
	private Date sendTime;

	/**
	 * 发送结果的编码
	 */
	private Integer sendCode;

	/**
	 * 发送结果的提示
	 */
	private String sendMsg;

	/**
	 * 短信 API 发送结果的编码
	 */
	private String apiSendCode;

	/**
	 * 短信 API 发送失败的提示
	 */
	private String apiSendMsg;

	/**
	 * 短信 API 发送返回的唯一请求 ID
	 */
	private String apiRequestId;

	/**
	 * 短信 API 发送返回的序号
	 */
	private String apiSerialNo;

	/**
	 * 接收状态
	 */
	private Integer receiveStatus;

	/**
	 * 接收时间
	 */
	private Date receiveTime;

	/**
	 * API 接收结果的编码
	 */
	private String apiReceiveCode;

	/**
	 * API 接收结果的说明
	 */
	private String apiReceiveMsg;

	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;

}
