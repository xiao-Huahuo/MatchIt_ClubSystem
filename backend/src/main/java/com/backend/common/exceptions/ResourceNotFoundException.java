package com.backend.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 资源（如用户、记录）未找到时抛出的异常
 */
public class ResourceNotFoundException extends RuntimeException {

    private final static Integer DEFAULT_CODE = HttpStatus.NOT_FOUND.value(); // 404
    private final Integer code = DEFAULT_CODE;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }
}