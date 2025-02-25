package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String response = s3Service.uploadFile(file.getOriginalFilename(), inputStream);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            ResponseInputStream<GetObjectResponse> s3Object = s3Service.getFile(fileName);
            byte[] content = s3Object.readAllBytes();
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(@RequestParam(value = "prefix", defaultValue = "") String prefix) {
        var files = s3Service.listFiles(prefix);
        List<String> fileNames = files.stream().map(S3Object::key).toList();
        return ResponseEntity.ok(fileNames);
    }
}