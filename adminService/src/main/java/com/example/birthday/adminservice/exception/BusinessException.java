package com.example.birthday.adminservice.exception;
import lombok.Getter;import org.springframework.http.HttpStatus;
@Getter public class BusinessException extends RuntimeException{private final HttpStatus status;public BusinessException(HttpStatus s,String m){super(m);status=s;}}
