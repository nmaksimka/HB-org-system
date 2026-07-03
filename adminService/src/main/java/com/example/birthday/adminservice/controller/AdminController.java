package com.example.birthday.adminservice.controller;
import com.example.birthday.adminservice.dto.*;import com.example.birthday.adminservice.service.AdminService;import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.data.web.PageableDefault;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import java.io.IOException;import java.nio.charset.StandardCharsets;import java.util.UUID;
@RestController @RequestMapping("/api/v1/admin") @RequiredArgsConstructor public class AdminController{
 private final AdminService service;
 @PatchMapping("/users/{id}/block")@ResponseStatus(HttpStatus.NO_CONTENT)void block(@PathVariable UUID id,@RequestHeader("X-User-Id")UUID admin,@RequestHeader(value="X-Correlation-Id",required=false)String c){service.block(admin,id,c);}
 @GetMapping("/audit-logs")Page<AuditLogResponse>audit(@PageableDefault(size=50)Pageable p){return service.audit(p);}
 @PostMapping(value="/imports",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)@ResponseStatus(HttpStatus.CREATED)AdminImportResponse upload(@RequestHeader("X-User-Id")UUID admin,@RequestPart("file")MultipartFile file)throws IOException{return service.importCsv(admin,file.getOriginalFilename(),new String(file.getBytes(),StandardCharsets.UTF_8));}
}
