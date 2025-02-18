package com.br.spring_localstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

@Service
public class SnsService {

    @Value("${cloud.aws.services.sns.topic-url}")
    private String url;

    private final SnsClient snsClient;

    @Autowired
    public SnsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String createTopic(String topicName) {
        try {
            CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();

            CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);
            return createTopicResponse.topicArn();
        } catch (SnsException e) {
            e.printStackTrace();
            return "Erro ao criar o tópico: " + e.getMessage();
        }
    }

    public String sendMessage(String topicName, String message) {
        try {
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(url+topicName)
                    .message(message)
                    .build();

            PublishResponse publishResponse = snsClient.publish(publishRequest);
            return "Mensagem publicada com ID: " + publishResponse.messageId();
        } catch (SnsException e) {
            e.printStackTrace();
            return "Erro ao publicar mensagem: " + e.getMessage();
        }
    }
}
