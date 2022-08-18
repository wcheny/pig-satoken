package com.wangchenyang.common.mybatis.plugins;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wangchenyang.common.mybatis.anon.EncryptDecryptData;
import com.wangchenyang.common.mybatis.utils.EncryptDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 字段加密
 *
 * @author 王晨阳
 * @version 1.0
 * @date 2022/8/18 19:45
 * @desc
**/
@Intercepts({
		@Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
@Slf4j
public class EncryptFieldInterceptor implements Interceptor {
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
		Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
		parameterField.setAccessible(true);
		//取出实例
		Object parameterObject = parameterField.get(parameterHandler);
		if (parameterObject != null) {
			Class<?> parameterObjectClass = parameterObject.getClass();
			//校验该实例的类是否被@EncryptDecryptData所注解
			EncryptDecryptData encryptDecryptData = AnnotationUtils.findAnnotation(parameterObjectClass, EncryptDecryptData.class);
			if (Objects.nonNull(encryptDecryptData)) {
				//取出当前当前类所有字段，传入加密方法
				Field[] declaredFields = parameterObjectClass.getDeclaredFields();
				EncryptDecrypt.encrypt(declaredFields, parameterObject);
			}

		}
		return invocation.proceed();
	}


	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
