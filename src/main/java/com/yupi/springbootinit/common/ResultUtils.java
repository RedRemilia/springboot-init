package com.yupi.springbootinit.common;

import com.yupi.springbootinit.common.errorcode.ErrorCode;

/**
 * 返回工具类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     */
    public static BaseResponse error(OldErrorCode oldErrorCode) {
        return new BaseResponse<>(oldErrorCode);
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     */
    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     */
    public static BaseResponse error(OldErrorCode oldErrorCode, String message) {
        return new BaseResponse(oldErrorCode.getCode(), null, message);
    }
}
