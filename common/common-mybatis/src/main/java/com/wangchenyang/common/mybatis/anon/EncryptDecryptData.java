package com.wangchenyang.common.mybatis.anon;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptDecryptData {

}
