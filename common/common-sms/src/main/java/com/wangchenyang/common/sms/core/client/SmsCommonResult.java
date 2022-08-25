package com.wangchenyang.common.sms.core.client;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import com.squareup.okhttp.internal.framed.ErrorCode;
import com.wangchenyang.common.core.util.R;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 短信的 CommonResult 拓展类
 *
 * 考虑到不同的平台，返回的 code 和 msg 是不同的，所以统一额外返回 {@link #apiCode} 和 {@link #apiMsg} 字段
 *
 * 另外，一些短信平台（例如说阿里云、腾讯云）会返回一个请求编号，用于排查请求失败的问题，我们设置到 {@link #apiRequestId} 字段
 *
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class SmsCommonResult<T> extends R<T> {

    /**
     * API 返回错误码
     *
     * 由于第三方的错误码可能是字符串，所以使用 String 类型
     */
    private String apiCode;
    /**
     * API 返回提示
     */
    private String apiMsg;

    /**
     * API 请求编号
     */
    private String apiRequestId;

    private SmsCommonResult() {
    }

    public static <T> SmsCommonResult<T> build(String apiCode, String apiMsg, String apiRequestId,
                                               T data,SmsCodeMapping codeMapping) {
		Assert.notNull(codeMapping, "参数 codeMapping 不能为空");
		SmsCommonResult<T> result = new SmsCommonResult<T>().setApiCode(apiCode).setApiMsg(apiMsg).setApiRequestId(apiRequestId);
		result.setCode(codeMapping.apply(apiCode));
		result.setMsg("短信发送失败");
		result.setData(data);
        return result;
    }

    public static <T> SmsCommonResult<T> error(Throwable ex) {
        SmsCommonResult<T> result = new SmsCommonResult<>();
        result.setCode(R.failed().getCode());
        result.setMsg(ExceptionUtil.getRootCauseMessage(ex));
        return result;
    }

}
