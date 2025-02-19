package com.br.spring_localstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient secretsManager;

    @Autowired
    public SecretsManagerService(SecretsManagerClient secretsManager) {
        this.secretsManager = secretsManager;
    }

    public String getSecret(String secretName) {
        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse secretValueResponse = secretsManager.getSecretValue(getSecretValueRequest);

            return secretValueResponse.secretString();
        } catch (SecretsManagerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createSecret(String secretName, String secretValue) {
        try {
            CreateSecretRequest createSecretRequest = CreateSecretRequest.builder()
                    .name(secretName)
                    .secretString(secretValue).build();

            CreateSecretResponse result = secretsManager.createSecret(createSecretRequest);
            return "Secret created successfully with ARN: " + result.arn();
        } catch (InvalidRequestException e) {
            return "Error creating secret: " + e.getMessage();
        }
    }
}