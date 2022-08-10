package com.wangchenyang.common.mybatis.anon;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptDecryptField {
}
