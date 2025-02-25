package com.br.spring_localstack.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.ssm.SsmClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    private static final String DEFAULT_ACCESS_KEY = "test";
    private static final String DEFAULT_SECRET_KEY = "test";

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.endpoint.url}")
    private String endpointUrl;

    @Value("${cloud.aws.credentials.name}")
    private String secretName;

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return buildAwsClient(SecretsManagerClient.builder());
    }

    @Bean
    public DynamoDbClient dynamoDbClient(SecretsManagerClient secretsManagerClient) {
        return buildAwsClient(DynamoDbClient.builder(), secretsManagerClient);
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public SsmClient ssmClient() {
        return buildAwsClient(SsmClient.builder());
    }

    @Bean
    public S3Client s3Client() {
        return buildAwsClient(S3Client.builder());
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient(SecretsManagerClient secretsManagerClient) {
        return buildAwsClient(SqsAsyncClient.builder(), secretsManagerClient);
    }

    @Bean
    public SqsClient sqsClient(SecretsManagerClient secretsManagerClient) {
        return buildAwsClient(SqsClient.builder(), secretsManagerClient);
    }

    @Bean
    public SnsClient snsClient(SecretsManagerClient secretsManagerClient) {
        return buildAwsClient(SnsClient.builder(), secretsManagerClient);
    }

    private <T extends AwsClientBuilder<T, C>, C> C buildAwsClient(T builder) {
        return builder
                .region(Region.of(region))
                .endpointOverride(URI.create(endpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(DEFAULT_ACCESS_KEY, DEFAULT_SECRET_KEY)))
                .build();
    }

    private <T extends AwsClientBuilder<T, C>, C> C buildAwsClient(T builder, SecretsManagerClient secretsManagerClient) {
        AwsBasicCredentials credentials = getAwsCredentials(secretsManagerClient);
        return builder
                .region(Region.of(region))
                .endpointOverride(URI.create(endpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private AwsBasicCredentials getAwsCredentials(SecretsManagerClient secretsManagerClient) {
        try {
            GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(
                    GetSecretValueRequest.builder().secretId(secretName).build()
            );

            String secretString = secretValueResponse.secretString();
            JsonNode jsonNode = new ObjectMapper().readTree(secretString);

            String accessKey = jsonNode.get("accessKey").asText(DEFAULT_ACCESS_KEY);
            String secretKey = jsonNode.get("secretKey").asText(DEFAULT_SECRET_KEY);

            return AwsBasicCredentials.create(accessKey, secretKey);
        } catch (Exception e) {
            return AwsBasicCredentials.create(DEFAULT_ACCESS_KEY, DEFAULT_SECRET_KEY);
        }
    }
}