package com.br.spring_localstack.service;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.services.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(String fileName, InputStream inputStream) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
            return "Arquivo enviado com sucesso: " + fileName;
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Falha ao enviar arquivo para o S3", e);
        }
    }

    public ResponseInputStream<GetObjectResponse> getFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            throw new RuntimeException("Falha ao buscar arquivo do S3", e);
        }
    }

    public List<S3Object> listFiles(String prefix) {
        try {
            ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(listObjectsV2Request);
            return response.contents();
        } catch (S3Exception e) {
            throw new RuntimeException("Falha ao listar arquivos no S3", e);
        }
    }
}