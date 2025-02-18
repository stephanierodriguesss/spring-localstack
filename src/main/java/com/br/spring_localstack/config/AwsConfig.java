package com.br.spring_localstack.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;

public class AwsConfig {

    public static SecretsManagerClient createSecretsManagerClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("test", "test");

        return SecretsManagerClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }
}