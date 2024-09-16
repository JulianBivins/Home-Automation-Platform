package com.homeautomation.homeAutomation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeAutomationApplication
		implements CommandLineRunner
{

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper; // Ensure this is injected
    private final ObjectMapper objectMapper;

    private UserEntity testUserEntityA;

    @Autowired
    public HomeAutomationApplication(UserRepository userRepository, Mapper<UserEntity, UserDto> userMapper, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper; // Injected via constructor
        this.objectMapper = objectMapper;
    }

    public static void main(String[] args) {
		SpringApplication.run(HomeAutomationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		userRepository.findAll().forEach(System.out::println);
        testUserEntityA = createTestUserEntityA();
        userRepository.save(testUserEntityA);
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        String userJson = objectMapper.writeValueAsString(testUserDtoA);
        System.out.println("THIS IS THE MAPPED USER = " + testUserDtoA);
        System.out.println("THIS IS USERJSON = " + userJson);
	}

    public static UserEntity createTestUserEntityA() {
        UserEntity userEntityA = new UserEntity();
        userEntityA.setUsername("testuserA");
        userEntityA.setPassword("1234");
        return userEntityA;
    }
}
