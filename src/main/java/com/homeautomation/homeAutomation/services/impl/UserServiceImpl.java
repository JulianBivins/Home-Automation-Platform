package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.UserRepository;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

//!
@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity saveUpdate(Long id, UserEntity userEntity) {
            userEntity.setUserId(id);
            return userRepository.save(userEntity);
    }

    @Override
    public Iterable<UserEntity> findAll() {
        return userRepository.findAll();
    }


    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserEntity partialUpdate(Long id, UserEntity userEntity) {
        userEntity.setUserId(id);
        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
            Optional.ofNullable(userEntity.getPassword()).ifPresent(existingUser::setPassword);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public UserEntity secondPartialUpdate(Long id, UserEntity userEntity) {
        Optional<UserEntity> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) throw new RuntimeException("User not found with id: " + id);

        userEntity.setUserId(id);
        UserEntity existingUser = existingUserOptional.get();

        if (userEntity.getUsername() != null) {
                existingUser.setUsername(userEntity.getUsername());
            }
            if (userEntity.getPassword() != null) {
                existingUser.setPassword(userEntity.getPassword());
            }
            if (userEntity.getRules() != null && !userEntity.getRules().isEmpty()) {
                existingUser.setRules(userEntity.getRules());
            }
            if (userEntity.getGroups() != null && !userEntity.getGroups().isEmpty()) {
                existingUser.setGroups(userEntity.getGroups());
            }
            return userRepository.save(existingUser);
    }


    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByIdCustom(Long userId) {
        userRepository.deleteByIdCustom(userId);
    }
}

