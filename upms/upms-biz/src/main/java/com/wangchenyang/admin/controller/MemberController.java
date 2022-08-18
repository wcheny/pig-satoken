package com.wangchenyang.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.Member;
import com.wangchenyang.admin.service.MemberService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 *
 *
 * @author Mr.wang
 * @date 2022-08-05 15:06:33
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/member" )
public class MemberController {

    private final MemberService memberService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param member
     * @return
     */
    @GetMapping("/page" )
    public R getMemberPage(Page page, Member member) {
        return R.ok(memberService.page(page, Wrappers.query(member)));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(memberService.getById(id));
    }

    /**
     * 新增
     * @param member
     * @return R
     */
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody Member member) {
        return R.ok(memberService.save(member));
    }

    /**
     * 修改
     * @param member
     * @return R
     */
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody Member member) {
        return R.ok(memberService.updateById(member));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Long id) {
        return R.ok(memberService.removeById(id));
    }

}
