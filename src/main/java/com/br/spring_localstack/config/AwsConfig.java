package com.br.spring_localstack.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.endpoint.url}")
    private String endpointUrl;

    @Value("${spring.cloud.aws.credentials.name}")
    private String secretName;

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public AwsBasicCredentials awsCredentials(SecretsManagerClient secretsManagerClient) throws JsonProcessingException {

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);

        String secretString = secretValueResponse.secretString();
        JsonNode jsonNode = new ObjectMapper().readTree(secretString);

        var accessKey = jsonNode.get("accessKey").asText();
        var secretKey = jsonNode.get("secretKey").asText();

        return AwsBasicCredentials.create(accessKey, secretKey);
    }
}