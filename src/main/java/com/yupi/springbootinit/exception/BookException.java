package com.yupi.springbootinit.exception;

import com.yupi.springbootinit.common.ErrorCode;

public class BookException extends BusinessException {

    public BookException(int code, String message) {
        super(code, message);
    }

    public BookException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BookException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
