package com.yupi.springbootinit.common.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    VERIFY_SEND_COOLDOWN(10000, "验证码发送冷却中，请等待一段时间后重试"),
    VERIFY_CODE_EXPIRE(10001, "验证码已过期，请重新获取"),
    VERIFY_FAILED(10002, "验证失败")

    ;

    private final Integer code;
    private final String message;
}
