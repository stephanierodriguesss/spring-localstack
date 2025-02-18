package com.br.spring_localstack.controller;

import com.br.spring_localstack.service.SnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sns")
public class SnsController {

    private final SnsService snsService;

    @Autowired
    public SnsController(SnsService snsService) {
        this.snsService = snsService;
    }

    @PostMapping("/create")
    public String createTopic(@RequestParam String topicName) {
        return snsService.createTopic(topicName);
    }

    @PostMapping("/send")
    public String publishMessage(@RequestParam String topicName, @RequestParam String message) {
        return snsService.sendMessage(topicName, message);
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String topicName, @RequestParam String endpointUrl, @RequestParam String protocol) {
        return snsService.subscribeToTopic(topicName, endpointUrl, protocol);
    }
}