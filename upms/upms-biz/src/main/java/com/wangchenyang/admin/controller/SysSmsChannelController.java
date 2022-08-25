package com.wangchenyang.admin.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.SysSmsChannel;
import com.wangchenyang.admin.service.SysSmsChannelService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 短信渠道
 *
 * @author Mr.wang
 * @date 2022-08-23 10:11:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/smschannel" )
public class SysSmsChannelController {

    private final SysSmsChannelService sysSmsChannelService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysSmsChannel 短信渠道
     * @return
     */
    @GetMapping("/page" )
    @SaCheckPermission("smschannel_get" )
    public R getSysSmsChannelPage(Page page, SysSmsChannel sysSmsChannel) {
		LambdaQueryWrapper<SysSmsChannel> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(StringUtils.isNotEmpty(sysSmsChannel.getSignature()),SysSmsChannel::getSignature,sysSmsChannel.getSignature());
		return R.ok(sysSmsChannelService.page(page, wrapper));
    }

	/**
	 * 短信渠道  用于选择
	 *
	 * @author 王晨阳
	 * @version 1.0
	 * @date 2022/8/24 11:16
	 * @desc
	**/
	@GetMapping("/all_simple" )
	@SaCheckPermission("smschannel_get" )
	public R getSysSmsChannelPage() {
		LambdaQueryWrapper<SysSmsChannel> wrapper = Wrappers.lambdaQuery();
		wrapper.select(SysSmsChannel::getId,SysSmsChannel::getSignature,SysSmsChannel::getCode);
		return R.ok(sysSmsChannelService.list(wrapper));
	}


    /**
     * 通过id查询短信渠道
     * @param id id
     * @return R
     */
    @GetMapping("/{id}" )
    @SaCheckPermission("smschannel_get" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysSmsChannelService.getById(id));
    }

    /**
     * 新增短信渠道
     * @param sysSmsChannel 短信渠道
     * @return R
     */
    @SysLog("新增短信渠道" )
    @PostMapping
    @SaCheckPermission("smschannel_add" )
    public R save(@Validated @RequestBody SysSmsChannel sysSmsChannel) {
		sysSmsChannelService.createSmsChannel(sysSmsChannel);
        return R.ok();
    }

    /**
     * 修改短信渠道
     * @param sysSmsChannel 短信渠道
     * @return R
     */
    @SysLog("修改短信渠道" )
    @PutMapping
    @SaCheckPermission("smschannel_edit" )
    public R updateById(@RequestBody SysSmsChannel sysSmsChannel) {
		sysSmsChannelService.updateSmsChannel(sysSmsChannel);
        return R.ok();
    }

    /**
     * 通过id删除短信渠道
     * @param id id
     * @return R
     */
    @SysLog("通过id删除短信渠道" )
    @DeleteMapping("/{id}" )
    @SaCheckPermission("smschannel_del" )
    public R removeById(@PathVariable Long id) {
		sysSmsChannelService.delById(id);
        return R.ok();
    }

}
