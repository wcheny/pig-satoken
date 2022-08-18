package com.wangchenyang.admin.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lengleng
 * @date 2020/10/9
 * <p>
 * 日志查询传输对象
 */
@Data
public class SysLogDTO {

	/**
	 * 查询日志类型
	 */
	private String type;

	/**
	 * 创建时间区间 [开始时间，结束时间]
	 */
	private LocalDateTime[] createTime;

}
