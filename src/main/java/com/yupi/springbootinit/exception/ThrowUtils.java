package com.yupi.springbootinit.exception;

import com.yupi.springbootinit.common.OldErrorCode;

/**
 * 抛异常工具类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param oldErrorCode
     */
    public static void throwIf(boolean condition, OldErrorCode oldErrorCode) {
        throwIf(condition, new BaseException(oldErrorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param oldErrorCode
     * @param message
     */
    public static void throwIf(boolean condition, OldErrorCode oldErrorCode, String message) {
        throwIf(condition, new BaseException(oldErrorCode, message));
    }
}
