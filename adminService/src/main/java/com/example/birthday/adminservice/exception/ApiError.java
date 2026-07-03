package com.example.birthday.adminservice.exception;
import java.time.Instant;public record ApiError(Instant timestamp,int status,String error,String message,String path,String correlationId){}
