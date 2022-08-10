package com.wangchenyang.common.mybatis.plugins;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangchenyang.common.mybatis.anon.EncryptDecryptData;
import com.wangchenyang.common.mybatis.utils.EncryptDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

@Intercepts({
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Slf4j
public class ReadInterceptor implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//取出查询的结果
		Object resultObject = invocation.proceed();
		if (Objects.isNull(resultObject)) {
			return null;
		}
		//基于selectList
		if (resultObject instanceof ArrayList) {
			ArrayList resultList = (ArrayList) resultObject;
			if (!CollectionUtils.isEmpty(resultList) && needToDecrypt(resultList.get(0))) {
				for (Object result : resultList) {
					//逐一解密
					EncryptDecrypt.decrypt(result);
				}
			}
			//基于selectOne
		} else {
			if (needToDecrypt(resultObject)) {
				EncryptDecrypt.decrypt(resultObject);
			}
		}
		return resultObject;
	}

	private boolean needToDecrypt(Object object) {
		Class<?> objectClass = object.getClass();
		EncryptDecryptData sensitiveData = AnnotationUtils.findAnnotation(objectClass, EncryptDecryptData.class);
		return Objects.nonNull(sensitiveData);
	}


	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
