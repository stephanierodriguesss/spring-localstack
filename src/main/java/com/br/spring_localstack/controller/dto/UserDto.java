package com.br.spring_localstack.controller.dto;

import com.br.spring_localstack.model.User;
import lombok.Getter;

import java.util.UUID;


@Getter
public class UserDto {
    private String name;
    private String email;

    public User userDtoToUser() {
        var user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}