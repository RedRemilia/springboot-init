package com.yupi.springbootinit.exception;

import com.yupi.springbootinit.common.errorcode.ErrorCode;
import com.yupi.springbootinit.common.OldErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(OldErrorCode oldErrorCode) {
        super(oldErrorCode.getMessage());
        this.code = oldErrorCode.getCode();
    }

    public BaseException(OldErrorCode oldErrorCode, String message) {
        super(message);
        this.code = oldErrorCode.getCode();
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

}
