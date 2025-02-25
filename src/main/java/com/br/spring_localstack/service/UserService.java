package com.br.spring_localstack.service;


import com.br.spring_localstack.model.User;
import com.br.spring_localstack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public Optional<User> getUser(String id) {
        return repository.findById(id);
    }
}