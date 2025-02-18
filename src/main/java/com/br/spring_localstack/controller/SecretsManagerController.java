package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.SecretsManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sm")
@RestController
public class SecretsManagerController {

    private final SecretsManagerService secretsManagerService;

    public SecretsManagerController(SecretsManagerService secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
    }

    @GetMapping
    public String getSecret(@RequestParam("name") String name){
        return secretsManagerService.getSecret(name);
    }
}
