package com.wangchenyang.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangchenyang.admin.api.entity.Member;
import com.wangchenyang.admin.service.MemberService;
import com.wangchenyang.common.core.util.R;
import com.wangchenyang.common.log.annotation.SysLog;
import com.wangchenyang.common.security.annotation.Inner;
import org.springframework.security.access.prepost.PreAuthorize;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
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
@Inner(value=false)
//@Tag(name = "管理")
//@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MemberController {

    private final MemberService memberService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param member
     * @return
     */
    //@Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page" )
    public R getMemberPage(Page page, Member member) {
        return R.ok(memberService.page(page, Wrappers.query(member)));
    }


    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    //@Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(memberService.getById(id));
    }

    /**
     * 新增
     * @param member
     * @return R
     */
    //@Operation(summary = "新增", description = "新增")
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
    //@Operation(summary = "修改", description = "修改")
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
    //@Operation(summary = "通过id删除", description = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Long id) {
        return R.ok(memberService.removeById(id));
    }

}
