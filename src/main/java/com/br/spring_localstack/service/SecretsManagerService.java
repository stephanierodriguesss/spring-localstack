package com.br.spring_localstack.service;

import com.br.spring_localstack.config.AwsConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Service
public class SecretsManagerService {

    private final SecretsManagerClient secretsManager;

    public SecretsManagerService() {
        this.secretsManager = AwsConfig.createSecretsManagerClient();
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
}
