package com.wangchenyang.admin.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class SmsTemplateSendReqVO {

    @NotNull(message = "手机号不能为空")
    private String mobile;

    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    private Map<String, Object> templateParams;

}
