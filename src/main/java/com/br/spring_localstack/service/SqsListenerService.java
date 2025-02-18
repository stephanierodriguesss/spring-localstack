package com.br.spring_localstack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;

@Service
public class SqsListenerService {

    @Value("${cloud.aws.services.sqs.queue-url}")
    private String queueUrl;

    @Value("${cloud.aws.services.sqs.queue}")
    private String queue;

    private final SqsClient sqsClient;

    public SqsListenerService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void startListening() {
        while (true) {
            try {
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl + queue)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(20)
                        .build();

                ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);
                List<Message> messages = response.messages();

                if (!messages.isEmpty()) {
                    for (Message message : messages) {
                        processMessage(message);
                    }
                } else {
                    System.out.println("Nenhuma mensagem recebida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(Message message) {
        System.out.println("Mensagem recebida: " + message.body());
        deleteMessage(message);
    }

    private void deleteMessage(Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl + queue)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
        System.out.println("Mensagem deletada da fila.");
    }
}