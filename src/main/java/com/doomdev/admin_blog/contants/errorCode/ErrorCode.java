package com.doomdev.admin_blog.contants.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(1000, "Có lỗi xảy ra vui lòng liên hệ Admin", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1001, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "Bạn không có quyền: {name}", HttpStatus.FORBIDDEN),
    EXCEPTION_MONITOR(1003, "EXCEPTION_MONITOR", HttpStatus.BAD_REQUEST),
    INVALID_HEADER(1004, "Trường header '%s' không được để trống", HttpStatus.BAD_REQUEST),
    OBJECT_NOT_FOUND(1005, "Object not found", HttpStatus.BAD_REQUEST),
    ACTION_NOT_SUPPORT(1006, "Hành động không được hỗ trợ", HttpStatus.BAD_REQUEST),
    INVALID_PARAMS_REQUEST(1007, "Tham số không hợp lệ", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1008, "Không tìm thấy bài viết.", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAILED(1009, "Có lỗi khi upload ảnh.", HttpStatus.BAD_REQUEST),
    TYPE_IMAGE_INVALID(1010, "Loại file không hợp lệ.", HttpStatus.BAD_REQUEST),
    RATE_LIMIT(1011, "Bạn đã yêu cầu vượt quá giới hạn, vui lòng thử lại sau.", HttpStatus.TOO_MANY_REQUESTS),
    EXCEPTION_SAVE_POST(1012, "Có lỗi khi lưu bài viết.", HttpStatus.BAD_REQUEST),
    EXCEPTION_SAVE_CATEGORY(1013, "Có lỗi khi lưu category.", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1014, "Không tìm thấy category.", HttpStatus.BAD_REQUEST),
    ACTION_AUTHENTICATION_NOT_SUPPORT(1015, "Phương thức xác thực không hỗ trợ.", HttpStatus.BAD_REQUEST),
    ;

    private Integer code;
    private String message;
    private HttpStatusCode statusCode;
}
