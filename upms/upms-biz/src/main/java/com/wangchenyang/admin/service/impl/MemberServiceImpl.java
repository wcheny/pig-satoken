package com.wangchenyang.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangchenyang.admin.api.entity.Member;
import com.wangchenyang.admin.mapper.MemberMapper;
import com.wangchenyang.admin.service.MemberService;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author Mr.wang
 * @date 2022-08-05 15:06:33
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}
