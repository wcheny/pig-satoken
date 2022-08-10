package com.wangchenyang.common.anti.replay.validator;

import com.wangchenyang.common.anti.replay.props.ReplayProperties;
import online.inote.naruto.cache.CacheManager;
import online.inote.naruto.cache.CacheSupport;
import online.inote.naruto.exception.replay.ReplayException;
import online.inote.naruto.utils.Assert;
import online.inote.naruto.utils.DateTimeUtils;
import online.inote.naruto.utils.StringUtils;

/**
 * @author XQF.Sui
 * @description 禁止重复请求校验器
 * @date 2021/07/30 00:40
 */
public class AntiReplayValidator {

    public static AntiReplayWorker builder() {
        return new AntiReplayWorker();
    }

    public static class AntiReplayWorker implements AutoCloseable {

        /**
         * 请求标识
         */
        private String nonce;
        /**
         * 方法名称
         */
        private String methodName;
        /**
         * 请求URL(Header)
         */
        private String url;
        /**
         * 请求URL(真实)
         */
        private String targetUrl;
        /**
         * 请求时间
         */
        private long timestamp;
        /**
         * redis数据版本
         */
        private Long version;

        public AntiReplayWorker nonce(String nonce) {
            Assert.notNull(nonce, "请求标识不能为空");
            this.nonce = nonce;
            return this;
        }

        public AntiReplayWorker methodName(String methodName) {
            Assert.notNull(methodName, "请求方法名称不能为空");
            this.methodName = methodName;
            return this;
        }

        public AntiReplayWorker url(String url) {
            Assert.notNull(url, "URL不能为空");
            this.url = StringUtils.startAt(url, "/");
            return this;
        }

        public AntiReplayWorker targetUrl(String targetUrl) {
            Assert.notNull(targetUrl, "targetUrl不能为空");
            this.targetUrl = StringUtils.startAt(targetUrl, "/");
            return this;
        }

        public AntiReplayWorker timestamp(Long timestamp) {
            Assert.notNull(timestamp, "请求时间不能为空");
            this.timestamp = timestamp;
            return this;
        }

        public void execute() {
            if (!StringUtils.equals(url, targetUrl)) {
                throw new ReplayException("请求URL与实际请求路径不符");
            }

            ReplayProperties props = ReplayProperties.props();

            if (DateTimeUtils.betweenNowSeconds(timestamp) > props.getRequest().getExpireTime()) {
                throw new ReplayException("请求已过期");
            }

            String key = genKey();

            this.version = CacheSupport.increment(key);

            if (props.getCache().getLockHoldTime() > 0) {
                CacheSupport.expire(key, props.getCache().getLockHoldTime());
            }

            if (version > CacheManager.VERSION_INCREMENT_STEP) {
                throw new ReplayException("请求正在执行,请勿重复提交");
            }
        }

        private String genKey() {
            return ReplayProperties.props().getCache().getCacheKeyPrefix()
                    + this.methodName
                    + ":"
                    + this.nonce;
        }

        @Override
        public void close() {
            if (this.version == CacheManager.VERSION_INCREMENT_STEP) {
                CacheSupport.delete(genKey());
            }
        }
    }
}
