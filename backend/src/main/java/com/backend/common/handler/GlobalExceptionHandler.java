package com.backend.common.handler;

import com.backend.common.api.ResponseMessage;
import com.backend.common.exceptions.AuthFailureException;
import com.backend.common.exceptions.ResourceNotFoundException;
import com.backend.common.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器，用于统一处理 Controller 层抛出的异常。
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 全局处理,兜底处理所有未被捕获的异常（系统内部错误）
     */
    @ExceptionHandler(Exception.class)
    public ResponseMessage<?> handleGlobalException(Exception e) {
        // 记录详细异常堆栈，用于排查
        logger.error("发生未处理的系统异常: {}", e.getMessage(), e);

        // 返回 HTTP 500 对应的 Code
        Integer serverErrorCode = HttpStatus.INTERNAL_SERVER_ERROR.value(); // 500
        String errorMessage = "系统内部错误，请稍后再试。";

        return ResponseMessage.failure(serverErrorCode, errorMessage, null);
    }

    /**
     * 处理 AuthFailureException（认证失败异常）
     */
    @ExceptionHandler(AuthFailureException.class)
    public ResponseMessage<?> handleAuthFailureException(AuthFailureException e) {
        logger.warn("认证失败异常: {}", e.getMessage());
        // 返回 401
        return ResponseMessage.failure(e.getCode(), e.getMessage(), null);
    }

    /**
     * 处理 ResourceNotFoundException（404 找不到资源）
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseMessage<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.warn("资源未找到异常: {}", e.getMessage());
        // 返回 404
        return ResponseMessage.failure(e.getCode(), e.getMessage(), null);
    }

    /**
     * 处理 ValidationException（400 数据校验错误，如重复注册）
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseMessage<?> handleValidationException(ValidationException e) {
        logger.warn("数据校验异常: {}", e.getMessage());
        // 返回 400
        return ResponseMessage.failure(e.getCode(), e.getMessage(), null);
    }


}