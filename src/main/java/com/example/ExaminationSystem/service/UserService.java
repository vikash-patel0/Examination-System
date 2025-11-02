package com.example.ExaminationSystem.service;

import com.example.ExaminationSystem.model.User;
import com.example.ExaminationSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public Optional<User> login(String usernameOrEmail, String password) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
