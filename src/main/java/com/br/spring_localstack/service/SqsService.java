package com.br.spring_localstack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Service
public class SqsService {

    @Value("${cloud.aws.services.sqs.queue-url}")
    private String queueUrl;

    private final SqsClient sqsClient;

    @Autowired
    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessage(String message, String queue) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl + queue)
                    .messageBody(message)
                    .build();

            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
            System.out.println("Message sent! Message ID: " + sendMessageResponse.messageId());
        } catch (SqsException e) {
            System.err.println("Error sending message to SQS: " + e.awsErrorDetails().errorMessage());
            e.printStackTrace();
        }
    }

    public String createQueue(String queueName) {
        try {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();

            CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
            return createQueueResponse.queueUrl();
        } catch (SqsException e) {
            e.printStackTrace();
            return "Erro ao criar a fila: " + e.getMessage();
        }
    }
}