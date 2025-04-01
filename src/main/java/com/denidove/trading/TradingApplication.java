package com.denidove.trading;

import com.denidove.trading.entities.User;
import com.denidove.trading.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TradingApplication {

	private static final Logger log = LoggerFactory.getLogger(TradingApplication.class);

	private final UserService userService;

	public TradingApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TradingApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			log.info("Command line started");
			log.info("");

			/*
			User user = new User();
			user.setName("Ania");
			user.setAge(23);
			user.setPassword("nura");
			userService.save(user);
			*/
		};
	}

}
