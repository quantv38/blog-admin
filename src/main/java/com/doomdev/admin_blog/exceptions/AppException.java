package com.doomdev.admin_blog.exceptions;

import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
