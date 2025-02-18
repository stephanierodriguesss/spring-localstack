package com.br.spring_localstack;

import com.br.spring_localstack.service.SqsListenerService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLocalstackApplication {

    @Autowired
    private SqsListenerService sqsListenerService;

    public static void main(String[] args) {
        SpringApplication.run(SpringLocalstackApplication.class, args);
    }

    @PostConstruct
    public void startListening() {
        new Thread(() -> sqsListenerService.startListening()).start();
    }
}