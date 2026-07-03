package com.example.birthday.adminservice.exception;
import feign.FeignException;import jakarta.servlet.http.HttpServletRequest;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.time.Instant;
@RestControllerAdvice public class GlobalExceptionHandler{
 @ExceptionHandler(BusinessException.class)ResponseEntity<ApiError>business(BusinessException e,HttpServletRequest r){return response(e.getStatus(),e.getMessage(),r);}
 @ExceptionHandler(FeignException.NotFound.class)ResponseEntity<ApiError>notFound(HttpServletRequest r){return response(HttpStatus.NOT_FOUND,"User not found",r);}
 @ExceptionHandler(Exception.class)ResponseEntity<ApiError>unexpected(HttpServletRequest r){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r);}
 private ResponseEntity<ApiError>response(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),s.getReasonPhrase(),m,r.getRequestURI(),r.getHeader("X-Correlation-Id")));}
}
