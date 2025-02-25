package com.br.spring_localstack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;

@Service
@RequiredArgsConstructor
public class SsmService {

    private final SsmClient ssmClient;

    public String createParameter(String name, String value) {
        PutParameterRequest request = PutParameterRequest.builder()
                .name(name)
                .value(value)
                .type(ParameterType.STRING)
                .overwrite(true)
                .build();

        ssmClient.putParameter(request);
        return "Parameter created: " + name;
    }

    public String getParameter(String name) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name(name)
                .withDecryption(true)
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }
}