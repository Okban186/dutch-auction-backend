package com.auction.dutch.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auction.dutch.model.dto.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ErrorResponse> handleAppException(
      AppException ex,
      HttpServletRequest request) {

    ErrorCode errorCode = ex.getErrorCode();

    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        errorCode.getStatus(),
        ex.getMessage());

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

      errors.put(
          fieldError.getField(),
          fieldError.getDefaultMessage());
    }

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(
      Exception ex,
      HttpServletRequest request) {

    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        500,
        ErrorCode.INTERNAL_SERVER_ERROR.getMessage());

    log.error(
        "Unexpected error at {}",
        request.getRequestURI(),
        ex);

    return ResponseEntity.internalServerError()
        .body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(
      AccessDeniedException ex,
      HttpServletRequest request) {

    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        403,
        "ACCESS_DENIED");

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(response);
  }
}
