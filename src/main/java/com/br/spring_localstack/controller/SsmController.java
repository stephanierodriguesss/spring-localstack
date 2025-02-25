package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.SsmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssm")
@RequiredArgsConstructor
public class SsmController {

    private final SsmService ssmService;

    @PostMapping
    public String createParameter(@RequestParam String name, @RequestParam String value) {
        return ssmService.createParameter(name, value);
    }

    @GetMapping
    public String getParameter(@RequestParam String name) {
        return ssmService.getParameter(name);
    }
}