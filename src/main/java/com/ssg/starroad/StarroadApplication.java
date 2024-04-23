package com.ssg.starroad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StarroadApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarroadApplication.class, args);
	}

}
