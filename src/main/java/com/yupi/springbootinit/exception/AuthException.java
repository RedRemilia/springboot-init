package com.yupi.springbootinit.exception;

import com.yupi.springbootinit.common.OldErrorCode;
import com.yupi.springbootinit.common.errorcode.ErrorCode;

public class AuthException extends BaseException {

    public AuthException(int code, String message) {
        super(code, message);
    }

    public AuthException(OldErrorCode oldErrorCode) {
        super(oldErrorCode);
    }

    public AuthException(OldErrorCode oldErrorCode, String message) {
        super(oldErrorCode, message);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
