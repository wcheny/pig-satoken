package com.wangchenyang.common.anti.replay.annotation;

import com.wangchenyang.common.anti.replay.spring.NarutoAntiReplaySpringConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author XQF.Sui
 * @description 启用禁止重复请求
 * @date 2021/07/30 01:43
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(NarutoAntiReplaySpringConfiguration.class)
public @interface EnableNarutoAntiReplay {

}
