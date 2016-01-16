package com.maven8919.lineupgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
@EnableOAuth2Sso
public class LineupGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LineupGeneratorApplication.class, args);
	}
}
