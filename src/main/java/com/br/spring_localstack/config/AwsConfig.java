package com.br.spring_localstack.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.endpoint.url}")
    private String endpointUrl;

    @Value("${cloud.aws.credentials.name}")
    private String secretName;

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    @Bean
    public SqsClient sqsClient(SecretsManagerClient secretsManagerClient) throws Exception {
        String accessKey = getAwsCredentials(secretsManagerClient, "accessKey");
        String secretKey = getAwsCredentials(secretsManagerClient, "secretKey");

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

    private String getAwsCredentials(SecretsManagerClient secretsManagerClient, String key) throws Exception {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        String secretString = secretValueResponse.secretString();
        JsonNode jsonNode = new ObjectMapper().readTree(secretString);
        return jsonNode.get(key).asText();
    }
}