package com.wangchenyang.admin;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestCode {

	private static final AES aes = new AES("CBC", "PKCS7Padding", "zIMThzhHWaS3OS2x".getBytes(),
			"FkVhNodJIP3FN1OX".getBytes());

	public static void main(String[] args) {
		// String msg="用户:{userName},验证码:{code}";
		// final Pattern pattern = Pattern.compile("\\{(.*?)}");
		// List<String> list = ReUtil.findAllGroup1(pattern, msg);
		// Map<String,Object> map=new HashMap<>();
		// list.stream().forEach(item->{
		// map.put(item, UUID.randomUUID().toString());
		// });
		// System.out.println(StrUtil.format(msg,map));
		System.out.println(aes.encryptBase64("王晨阳"));
		System.out.println(splitValueEncrypt("王晨阳", 2));
		// System.out.println(extractIndex("~CKoqAl2hWzh54uBFv9Suug==~1~"));
	}

	/**
	 * 分词加密
	 * @param value 分词值
	 * @param splitLength 分词长度
	 * @return
	 */
	public static String splitValueEncrypt(String value, int splitLength) {

		// 检查参数是否合法
		if (StringUtils.isBlank(value) && splitLength <= 0) {
			return null;
		}
		StringBuilder encryptValue = new StringBuilder();

		// 获取整个字符串可以被切割成字符子串的个数
		int n = (value.length() - splitLength + 1);

		// 分词（规则：分词长度根据【splitLength】且每次分割的开始跟结束下标加一）
		for (int i = 0; i < n; i++) {
			String splitValue = value.substring(i, splitLength++);
			System.out.println(splitValue);
			encryptValue.append(aes.encryptBase64(splitValue));
		}
		System.out.println("支持检索长度:" + encryptValue.toString().length());
		return encryptValue.toString();
	}

}
