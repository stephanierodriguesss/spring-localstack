package com.br.spring_localstack.controller;

import com.br.spring_localstack.controller.dto.UserDto;
import com.br.spring_localstack.model.User;
import com.br.spring_localstack.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody UserDto userDto) {
        var user = userDto.userDtoToUser();
        userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
}