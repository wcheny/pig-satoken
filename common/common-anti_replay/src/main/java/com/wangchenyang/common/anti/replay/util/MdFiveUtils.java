package com.wangchenyang.common.anti.replay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import online.inote.naruto.utils.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author XQF.Sui
 * @description MD5工具类(密码加密)
 * @date 2021/08/14 15:59
 */
public class MdFiveUtils {

    private static final String EMPTY_JSON = "{}";

    public static String digest(DigestWorker worker) {
        return digest(null, worker.nonce, worker.url, worker.timestamp, worker.token, worker.params, worker.arguments);
    }

    public static String digest(
            final String salt,
            final String nonce,
            final String url,
            final Long timestamp,
            final String token,
            final Object... arguments) {
        return digest(salt, nonce, url, timestamp, token, null, arguments);
    }

    public static String digest(
            final String salt,
            final String nonce,
            final String url,
            final Long timestamp,
            final String token,
            final String params,
            final Object... arguments) {

        Assert.notBlank(nonce, "nonce不能为空");
        Assert.notBlank(url, "url不能为空");

        StringBuilder sb = new StringBuilder(salt + nonce + url);

        if (!Objects.isNull(timestamp)) {
            sb.append(timestamp);
        }

        if (!Objects.isNull(token)) {
            sb.append(token);
        }

        if (StringUtils.isNotBlank(params) && !StringUtils.equals(params, EMPTY_JSON)) {
            sb.append(params);
        }

        sb.append(argumentsSort(arguments));
		System.out.println("加密串："+sb.toString());
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String argumentsSort(final Object... arguments) {
        if (arguments != null && arguments.length > 0) {
            return  Arrays.stream(arguments)
							.map(obj->{
								String s=JSON.toJSONString(obj);
								try{
									JSON.parseObject(s);
									return s;
								}catch (Exception e){
									return "";
								}
							}).collect(Collectors.joining());
        }
        return "";
    }

    public static DigestWorker builder() {
        return new DigestWorker();
    }

    @Data
    public static class DigestWorker {
        private String nonce;
        private String url;
        private Long timestamp;
        private String token;
        private String params;
        private Object[] arguments;

        public DigestWorker nonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public DigestWorker url(String url) {
            this.url = url;
            return this;
        }

        public DigestWorker timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DigestWorker token(String token) {
            this.token = token;
            return this;
        }

        public DigestWorker params(String params) {
            this.params = params;
            return this;
        }

        public DigestWorker arguments(Object... arguments) {
            this.arguments = arguments;
            return this;
        }

        public String execute() {
            return MdFiveUtils.digest(this);
        }
    }
}
