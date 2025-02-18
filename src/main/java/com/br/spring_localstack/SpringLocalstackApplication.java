package com.br.spring_localstack;

import com.br.spring_localstack.service.SecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLocalstackApplication {

    @Autowired
    private SecretsManagerService secretsManagerService;

    public static void main(String[] args) {
        SpringApplication.run(SpringLocalstackApplication.class, args);
    }
}