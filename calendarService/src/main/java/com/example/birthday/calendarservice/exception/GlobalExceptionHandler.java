package com.example.birthday.calendarservice.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(BusinessException.class) ResponseEntity<ApiError> business(BusinessException ex,HttpServletRequest r){return response(ex.getStatus(),ex.getMessage(),r);}
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex,HttpServletRequest r){
        return response(HttpStatus.BAD_REQUEST,ex.getBindingResult().getFieldErrors().stream().map(e->e.getField()+": "+e.getDefaultMessage()).collect(Collectors.joining("; ")),r);
    }
    @ExceptionHandler(Exception.class) ResponseEntity<ApiError> unexpected(HttpServletRequest r){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r);}
    private ResponseEntity<ApiError> response(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),s.getReasonPhrase(),m,r.getRequestURI(),r.getHeader("X-Correlation-Id")));}
}
