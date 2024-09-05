package com.homeautomation.homeAutomation;

import com.homeautomation.homeAutomation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeAutomationApplication
//		implements CommandLineRunner
{

	private final UserRepository userRepository;

    public HomeAutomationApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(HomeAutomationApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		userRepository.findAll().forEach(System.out::println);
//	}
}
