package com.yupi.springbootinit.common.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HttpErrorCode implements ErrorCode {
    SUCCESS(200, "OK"),
    PARAMS_ERROR(1000, "参数校验失败");

    private final Integer code;
    private final String message;

}
