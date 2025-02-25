package com.br.spring_localstack.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.concurrent.CompletableFuture;

@Service
public class SqsListenerService {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${cloud.aws.services.sqs.queue-url}")
    private String queueUrl;

    @Value("${cloud.aws.services.sqs.queue}")
    private String queue;

    public SqsListenerService(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @PostConstruct
    public void startListening() {
        listen();
    }

    private void listen() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl + queue)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        CompletableFuture.runAsync(() -> {
            while (true) {
                sqsAsyncClient.receiveMessage(request)
                        .thenApply(response -> {
                            for (Message message : response.messages()) {
                                System.out.println("Received message: " + message.body());

                                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                                        .queueUrl(queueUrl + queue)
                                        .receiptHandle(message.receiptHandle())
                                        .build();

                                sqsAsyncClient.deleteMessage(deleteRequest)
                                        .thenRun(() -> System.out.println("Deleted message: " + message.messageId()))
                                        .exceptionally(throwable -> {
                                            System.err.println("Failed to delete message: " + message.messageId());
                                            throwable.printStackTrace();
                                            return null;
                                        });
                            }
                            return response;
                        })
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
}