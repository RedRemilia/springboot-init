package com.yupi.springbootinit.common.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    VERIFY_SEND_COOLDOWN(10000, "验证码发送冷却中，请等待一段时间后重试"),
    VERIFY_CODE_EXPIRE(10001, "验证码已过期，请重新获取"),
    VERIFY_FAILED(10002, "验证码错误"),
    VERIFY_FAIL_EXCEEDED(10003, "验证失败次数过多"),
    VERIFY_PHONE_ERROR(10004, "手机号不正确"),
    VERIFY_PHONE_CHANGED(10005, "手机号发生变化，请重新获取验证码"),

    USER_NOT_EXIST(10100, "用户不存在"),
    WRONG_PASSWORD(10101, "密码错误")
    ;

    private final Integer code;
    private final String message;
}
