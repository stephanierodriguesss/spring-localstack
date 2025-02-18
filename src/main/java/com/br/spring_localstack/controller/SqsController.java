package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sqs")
@RestController
public class SqsController {

    private final SqsService sqsService;

    @Autowired
    public SqsController(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam String message, @RequestParam String queue) {
        sqsService.sendMessage(message, queue);
    }

    @PostMapping("/createQueue")
    public String createQueue(@RequestParam String name) {
        return sqsService.createQueue(name);
    }
}