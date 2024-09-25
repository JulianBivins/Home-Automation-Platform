package com.homeautomation.homeAutomation.services.impl;

import com.homeautomation.homeAutomation.config.JwtServiceConfig;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.UserRepository;
import com.homeautomation.homeAutomation.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JwtServiceConfig jwtService;
//    @Autowired
//    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtServiceConfig jwtService;

    private final AuthenticationManager authenticationManager;

    //allows to create user, save it and generate token
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRoles(new HashSet<>(Set.of(UserEntity.Roles.USER)));
        userRepository.save(userEntity);
        String jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
         );
         //if we get to this point it means the user is authenticated
         UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("ERROR: Username not recognized"));
        String jwtToken = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
