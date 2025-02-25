package com.br.spring_localstack.model;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String email;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}