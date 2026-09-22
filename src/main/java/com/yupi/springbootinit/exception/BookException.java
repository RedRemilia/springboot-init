package com.yupi.springbootinit.exception;

import com.yupi.springbootinit.common.OldErrorCode;

public class BookException extends BaseException {

    public BookException(int code, String message) {
        super(code, message);
    }

    public BookException(OldErrorCode oldErrorCode) {
        super(oldErrorCode);
    }

    public BookException(OldErrorCode oldErrorCode, String message) {
        super(oldErrorCode, message);
    }
}
