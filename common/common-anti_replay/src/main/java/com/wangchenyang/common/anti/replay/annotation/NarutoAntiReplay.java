package com.wangchenyang.common.anti.replay.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author XQF.Sui
 * @description 禁止重复切面
 * @date 2021/07/30 01:18
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface NarutoAntiReplay {

    /**
     * 禁止重放
     */
    boolean antiReplay() default true;

    /**
     * 验签签名
     */
    boolean checkSignature() default true;
}
