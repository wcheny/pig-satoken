package com.wangchenyang.admin.controller;

import com.wangchenyang.admin.api.entity.SysUser;
import com.wangchenyang.common.anti.replay.annotation.NarutoAntiReplay;
import com.wangchenyang.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import online.inote.naruto.utils.DateTimeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController("test")
@Slf4j
public class TestController {

	@NarutoAntiReplay
	@RequestMapping("/demo/test")
	public R<Void> test(@RequestBody SysUser user) throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		log.info("test.....");
		return R.ok(null,"ojbk");
	}

	public static void main(String[] args) {
		long millis = System.currentTimeMillis();
		System.out.println(millis);
		long seconds = DateTimeUtils.betweenNowSeconds(1658818895000L);
		System.out.println(seconds);
	}

}
