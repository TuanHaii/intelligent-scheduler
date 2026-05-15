package com.example.intelligent_scheduler.presentation.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.intelligent_scheduler.application.common.dto.ErrorResponse;
import java.time.OffsetDateTime;

// Bắt lỗi tập trung
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Bắt lỗi logic nghiệp vụ được ném ra ( throw new IllegalArgumentException("Lỗi nghiệp vụ") ) và trả về phản hồi lỗi chuẩn cho client.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage(),
                OffsetDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    // bắt tất cả lỗi hệ thống không được xử lý khác và trả về phản hồi lỗi chuẩn cho client.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.",
                OffsetDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

