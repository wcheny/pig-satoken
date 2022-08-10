package com.wangchenyang.common.anti.replay.validator;

import com.alibaba.fastjson.JSON;
import com.wangchenyang.common.anti.replay.props.ReplayProperties;
import com.wangchenyang.common.anti.replay.util.MdFiveUtils;
import lombok.extern.log4j.Log4j2;
import online.inote.naruto.exception.replay.DataSignatureException;
import online.inote.naruto.utils.ConvertUtils;
import online.inote.naruto.utils.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author XQF.Sui
 * @description 验签校验器
 * @date 2021/7/30 01:23
 */
@Log4j2
public class SignatureValidator {

    public static SignatureWorker builder() {
        return new SignatureWorker();
    }

    public static class SignatureWorker {

        /**
         * 请求标识
         */
        private String nonce;
        /**
         * 请求时间
         */
        private Long timestamp;
        /**
         * 请求URL
         */
        private String url;
        /**
         * 请求URL
         */
        private String token;
        /**
         * 请求参数
         */
        private Object[] arguments;
        /**
         * 请求参数
         */
        private String params;
        /**
         * 请求URL
         */
        private String signature;

        /**
         * 盐值
         */
        private String salt;

        public SignatureWorker nonce(String nonce) {
            Assert.notNull(nonce, "请求标识不能为空");
            this.nonce = nonce;
            return this;
        }

        public SignatureWorker timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SignatureWorker url(String url) {
            Assert.notNull(url, "请求URL不能为空");
            this.url = url;
            return this;
        }

        public SignatureWorker token(String token) {
            this.token = token;
            return this;
        }

        public SignatureWorker arguments(Object[] arguments) {
            this.arguments = arguments;
            return this;
        }

        public SignatureWorker params(Map<String, String[]> parameterMap) {
            this.params = JSON.toJSONString(parameterMap);
            return this;
        }

        public SignatureWorker signature(String signature) {
            Assert.notNull(signature, "签名摘要不能为空");
            this.signature = signature;
            return this;
        }

        public SignatureWorker salt(String salt) {
            Assert.notNull(salt, "盐值不能为空");
            this.salt = salt;
            return this;
        }

        public SignatureWorker data(HttpServletRequest request) {
            ReplayProperties.HeaderKey headerKey = ReplayProperties.props().getHeaderKey();
            ReplayProperties.SignatureAlgorithm signatureAlgorithm = ReplayProperties.props().getSignatureAlgorithm();

            return this.nonce(request.getHeader(headerKey.getNonce()))
                    .timestamp(ConvertUtils.StringToLong(request.getHeader(headerKey.getTimestamp())))
                    .url(request.getHeader(headerKey.getUrl()))
                    .token(request.getHeader(headerKey.getToken()))
                    .params(request.getParameterMap())
                    .signature(request.getHeader(headerKey.getSignature()))
                    .salt(signatureAlgorithm.getSalt());
        }

        public void execute() {
            String digest =
                    MdFiveUtils.digest(
                            this.salt,this.nonce, this.url, this.timestamp, this.token, this.params, this.arguments);

            if (!StringUtils.equals(this.signature, digest)) {
                if (log.isDebugEnabled()) {
                    log.debug("数据签名验证未通过, 传入签名:[ {} ], 生成签名:[ {} ]", signature, digest);
                }
                throw new DataSignatureException("数据签名验证未通过");
            }
        }
    }
}
