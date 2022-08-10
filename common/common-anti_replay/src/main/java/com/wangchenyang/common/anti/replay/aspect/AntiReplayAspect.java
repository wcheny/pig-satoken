package com.wangchenyang.common.anti.replay.aspect;

import com.wangchenyang.common.anti.replay.annotation.NarutoAntiReplay;
import com.wangchenyang.common.anti.replay.props.ReplayProperties;
import com.wangchenyang.common.anti.replay.validator.AntiReplayValidator;
import com.wangchenyang.common.anti.replay.validator.SignatureValidator;
import online.inote.naruto.common.utils.SpringUtils;
import online.inote.naruto.utils.ConvertUtils;
import online.inote.naruto.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XQF.Sui
 * @description 禁止重复请求切面
 * @date 2021/07/30 01:15
 */
@Order(value = 1)
@Aspect
@Component
public class AntiReplayAspect {

    private final ReplayProperties props;
    private final String servletContextPath;

    AntiReplayAspect(ReplayProperties props, Environment env) {
        this.props = props;
        this.servletContextPath = env.getProperty("server.servlet.context-path");
    }

    @Around(value = "@annotation(narutoSecurity)")
    public Object around(ProceedingJoinPoint point, NarutoAntiReplay narutoSecurity)
            throws Throwable {

        HttpServletRequest request = getHttpServletRequest();
        // 签名验证
        if (narutoSecurity.checkSignature()) {
            SignatureValidator.builder().arguments(point.getArgs()).data(request).execute();
        }

        // 禁止重放验证
        if (narutoSecurity.antiReplay()) {
            // 请求路径
            String targetUrl = StringUtils.replace(request.getRequestURI(), servletContextPath, "");

            try (AntiReplayValidator.AntiReplayWorker worker =
                         AntiReplayValidator.builder()
                                 .methodName(SpringUtils.getMethodName(point))
                                 .nonce(request.getHeader(props.getHeaderKey().getNonce()))
                                 .url(request.getHeader(props.getHeaderKey().getUrl()))
                                 .targetUrl(targetUrl)
                                 .timestamp(
                                         ConvertUtils.StringToLong(
                                                 request.getHeader(props.getHeaderKey().getTimestamp())))) {
                worker.execute();
                return point.proceed();
            }
        }

        return point.proceed();
    }

    private HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
}
