package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    UserEntity save(UserEntity userEntity);

    UserEntity saveUpdate(Long id, UserEntity userEntity);

    Iterable<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);

    boolean isExists(Long id);

    UserEntity partialUpdate(Long id, UserEntity userEntity);
    UserEntity secondPartialUpdate(Long id, UserEntity userEntity);

    void delete(Long id);

    void deleteByIdCustom(Long userId);

    boolean existsByUsername(String username);

}
