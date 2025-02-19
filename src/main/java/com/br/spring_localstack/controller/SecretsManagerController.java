package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.SecretsManagerService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sm")
@RestController
public class SecretsManagerController {

    private final SecretsManagerService secretsManagerService;

    public SecretsManagerController(SecretsManagerService secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
    }

    @GetMapping
    public String getSecret(@RequestParam("name") String name) {
        return secretsManagerService.getSecret(name);
    }

    @PostMapping("/create")
    public String createSecret(@RequestParam String secretName, @RequestParam String secretValue) {
        return secretsManagerService.createSecret(secretName, secretValue);
    }
}
