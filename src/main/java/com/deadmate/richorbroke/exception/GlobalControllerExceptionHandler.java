package com.deadmate.richorbroke.exception;

import com.deadmate.richorbroke.model.ApiError;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleBadRequestException(FeignException e) {
        log.error("Caught FeignException: {}", e.getMessage());
        return ResponseEntity
                .status(e.status())
                .body(ApiError.builder()
                        .status(e.status())
                        .title(HttpStatus.valueOf(e.status()).getReasonPhrase())
                        .details(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleBadRequestException(ApiException e) {
        log.error("Caught ApiException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiError.builder()
                        .status(e.getStatus().value())
                        .title(e.getStatus().getReasonPhrase())
                        .details(e.getMessage())
                        .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(ApiError.builder()
                        .status(status.value())
                        .title(status.getReasonPhrase())
                        .details(e.getMessage())
                        .build());
    }
}
