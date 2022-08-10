package com.wangchenyang.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.v3.oas.annotations.media.Schema;
import com.wangchenyang.common.mybatis.anon.EncryptDecryptData;
import com.wangchenyang.common.mybatis.anon.EncryptDecryptField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *
 * @author Mr.wang
 * @date 2022-08-05 15:06:33
 */
@Data
@TableName("test_member")
@EqualsAndHashCode(callSuper = false)
@EncryptDecryptData
//@Schema(description = "")
public class Member {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    //@Schema(description ="id")
    private Long id;

    /**
     * 加密姓名
     */
    //@Schema(description ="加密姓名")
	@EncryptDecryptField
    private String realName;

    /**
     * 检索姓名 两字以上
     */
    //@Schema(description ="检索姓名 两字以上")
	@TableField(select = false)
    private String encryptRealName;

    /**
     * 加密手机号
     */
    //@Schema(description ="加密手机号")
	@EncryptDecryptField
    private String phone;

    /**
     * 检索手机号 后四位
     */
    //@Schema(description ="检索手机号 后四位")
	@TableField(select = false)
    private String encryptPhone;


}
