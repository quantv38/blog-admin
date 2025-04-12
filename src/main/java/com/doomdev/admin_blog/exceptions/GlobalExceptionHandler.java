package com.doomdev.admin_blog.exceptions;

import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.responses.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // Normal:
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<DefaultResponse<String>> handleAllExceptions(RuntimeException e) {
        log.error(e.getMessage());
        DefaultResponse<String> response = new DefaultResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<DefaultResponse<String>> handleAppExceptions(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        DefaultResponse<String> response = new DefaultResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<DefaultResponse<String>> handleAppExceptions(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        DefaultResponse<String> response = new DefaultResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<DefaultResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }
        DefaultResponse<String> response = DefaultResponse.error(String.join(", ", errorMessages), ErrorCode.INVALID_PARAMS_REQUEST.getCode());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    ResponseEntity<DefaultResponse<String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        DefaultResponse<String> response = new DefaultResponse<>();
        response.setSuccess(Boolean.FALSE);
        response.setCode(ErrorCode.INVALID_PARAMS_REQUEST.getCode());
        response.setMessage(e.getParameterName() + " is required");
        return ResponseEntity.badRequest().body(response);
    }
}
