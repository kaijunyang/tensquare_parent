package com.tensquare.base.web.common;

import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler
    public ResultDTO exceptionHandler(Throwable e) {
        e.printStackTrace();
        return new ResultDTO(false, StatusCode.ERROR, e.getMessage());
    }
}
