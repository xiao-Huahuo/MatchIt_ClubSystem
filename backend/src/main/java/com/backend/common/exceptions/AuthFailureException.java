package com.backend.common.exceptions;

/**
 * 认证失败异常，用于用户名密码错误、Token无效等场景
 */
public class AuthFailureException extends RuntimeException {

    // 默认使用 401 Unauthorized 状态码或自定义业务码
    private final static Integer DEFAULT_CODE = 401;

    private final Integer code;

    public AuthFailureException(String message) {
        super(message);
        this.code = DEFAULT_CODE;
    }

    public AuthFailureException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}