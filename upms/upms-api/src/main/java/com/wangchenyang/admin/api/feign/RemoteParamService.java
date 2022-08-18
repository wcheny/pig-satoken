package com.wangchenyang.admin.api.feign;

import com.wangchenyang.common.core.constant.SecurityConstants;
import com.wangchenyang.common.core.constant.ServiceNameConstants;
import com.wangchenyang.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author lengleng
 * @date 2020/5/12
 * <p>
 * 查询参数相关
 */
@FeignClient(contextId = "remoteParamService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteParamService {

	/**
	 * 通过key 查询参数配置
	 * @param key key
	 * @return
	 */
	@GetMapping("/param/publicValue/{key}")
	R<String> getByKey(@PathVariable("key") String key);

}
