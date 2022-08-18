package com.wangchenyang.common.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 当前在线会话
 *
 * @author Lion Li
 */

@Data
@NoArgsConstructor
public class SysUserOnline implements Serializable {

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;

	/**
	 * 浏览器类型
	 */
	private String browser;
	/**
	 * 操作系统
	 */
	private String os;
	/**
	 * 登录IP地址
	 */
	private String ipaddr;

    /**
     * 登录时间
     */
    private Date loginTime;
	/**
	 * token 剩余有效时间 (单位: 秒)
	 *
	**/
    private long tokenTimeout;

}
