package com.example.birthday.chatservice.exception;
import jakarta.servlet.http.HttpServletRequest; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant;
@RestControllerAdvice public class GlobalExceptionHandler{
 @ExceptionHandler(BusinessException.class)ResponseEntity<ApiError> business(BusinessException e,HttpServletRequest r){return out(e.getStatus(),e.getMessage(),r);}
 @ExceptionHandler(MethodArgumentNotValidException.class)ResponseEntity<ApiError> validation(HttpServletRequest r){return out(HttpStatus.BAD_REQUEST,"Validation failed",r);}
 @ExceptionHandler(Exception.class)ResponseEntity<ApiError> other(HttpServletRequest r){return out(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r);}
 private ResponseEntity<ApiError> out(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),s.getReasonPhrase(),m,r.getRequestURI(),r.getHeader("X-Correlation-Id")));}
}
