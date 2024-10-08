package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity save(UserEntity userEntity);

    UserEntity saveUpdate(Long id, UserEntity userEntity);

    Iterable<UserEntity> findAll();

    Optional<UserEntity> findOne(Long id);

    Optional<UserEntity> findByUsername(String username);

    boolean isExists(Long id);

    UserEntity partialUpdate(Long id, UserEntity userEntity);

    void delete(Long id);
}
